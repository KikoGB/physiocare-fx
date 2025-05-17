package com.gadeadiaz.physiocare.utils;
import com.gadeadiaz.physiocare.models.Appointment;
import com.gadeadiaz.physiocare.models.Patient;
import com.gadeadiaz.physiocare.models.Physio;
import com.gadeadiaz.physiocare.models.Record;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class Pdf {
    // Header
    private static final Paragraph header = new Paragraph("David's Clinic - C/ Mayor 123, Madrid")
            .setFontSize(12)
            .setItalic()
            .setTextAlignment(TextAlignment.RIGHT)
            .setMarginBottom(20);
    public static void medicalRecordPdfCreator(Record record){

        String dest = "./src/main/resources/records/" + record.getPatient().getInsuranceNumber() + ".pdf";
        Document document;
        try{
            PdfWriter writer = new PdfWriter(dest);
            PdfDocument pdf = new PdfDocument(writer);
            document = new Document(pdf);

            // Header
            document.add(header);
            // Title
            Paragraph title = new Paragraph("Medical Record")
                    .setFontSize(16)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(title);

            // Tabla with patient info
            float[] colWidths = {2, 4};
            Table recordInfo = new Table(UnitValue.createPercentArray(colWidths));
            recordInfo.setWidth(UnitValue.createPercentValue(100));

            recordInfo.addCell(getHeaderCell("Record ID"));
            recordInfo.addCell(String.valueOf(record.getId()));

            recordInfo.addCell(getHeaderCell("Patient's Insurance Number"));
            recordInfo.addCell(String.valueOf(record.getPatient().getInsuranceNumber()));

            recordInfo.addCell(getHeaderCell("Patient Name"));
            recordInfo.addCell(record.getPatient().getFullName());

            recordInfo.addCell(getHeaderCell("Email"));
            recordInfo.addCell(record.getPatient().getEmail());

            recordInfo.addCell(getHeaderCell("Address"));
            recordInfo.addCell(record.getPatient().getAddress());

            document.add(recordInfo);

            // Space
            document.add(new Paragraph("\n"));

            // Medical record
            Paragraph medTitle = new Paragraph("Description:")
                    .setFontSize(16)
                    .setBold()
                    .setTextAlignment(TextAlignment.LEFT)
                    .setMarginBottom(10);
            document.add(medTitle);

            Paragraph medRecord = new Paragraph(record.getMedicalRecord())
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.JUSTIFIED);

            document.add(medRecord);

            document.close();
            System.out.println("Medical Record PDF created successfully.");

             File pdfFile = new File(dest);
             if (pdfFile.exists()) {
                 Sftp.savePDF(pdfFile.getAbsolutePath(), pdfFile.getName());
             }


        }catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }

    public static File getPatientPdf(Patient patient){
        String dest = "./src/main/resources/patients/" + patient.getInsuranceNumber() + ".pdf";
        File newPdf = null;
        Document document;
        try{
            PdfWriter writer = new PdfWriter(dest);
            PdfDocument pdf = new PdfDocument(writer);
            document = new Document(pdf);

            document.add(header);

            // Title
            Paragraph title = new Paragraph(patient.getFullName())
                    .setFontSize(20)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(title);


            // Available Appointments
            Paragraph availableAppointments = new Paragraph("Available Appointments: " + (10 - patient.getAppointments().size()))
                    .setFontSize(14)
                    .setItalic()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(availableAppointments);

            // Table with patient info
            float[] colWidths = {1, 1, 1, 2, 1};
            Table appointmentsTable = new Table(UnitValue.createPercentArray(colWidths));
            appointmentsTable.setWidth(UnitValue.createPercentValue(100));
            appointmentsTable.addHeaderCell(getHeaderCell("Date"));
            appointmentsTable.addHeaderCell(getHeaderCell("Diagnosis"));
            appointmentsTable.addHeaderCell(getHeaderCell("Treatment"));
            appointmentsTable.addHeaderCell(getHeaderCell("Observations"));
            appointmentsTable.addHeaderCell(getHeaderCell("Physio"));

            System.out.println(patient.getAppointments().size());
            for (Appointment a: patient.getAppointments()) {
                if (a.getConfirmed()) {
                    appointmentsTable.addCell(new Cell().add(new Paragraph(!a.getDate().isBlank() ? a.getDate() : "Empty Date")).setFontSize(9));
                    appointmentsTable.addCell(new Cell().add(new Paragraph(!a.getDiagnosis().isBlank() ? a.getDiagnosis() : "Empty Diagnosis")).setFontSize(9));
                    appointmentsTable.addCell(new Cell().add(new Paragraph(!a.getTreatment().isBlank() ? a.getTreatment() : "Empty Treatment")).setFontSize(9));
                    appointmentsTable.addCell(new Cell().add(new Paragraph(!a.getObservations().isBlank() ? a.getObservations() : "Empty Observations")).setFontSize(9));
                    appointmentsTable.addCell(new Cell().add(new Paragraph(a.getPhysio().getFullName())).setFontSize(9));
                }
            }

            document.add(appointmentsTable);

            document.close();
            System.out.println("Patient appointments PDF created successfully.");

            newPdf = new File(dest);

        }catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
        return newPdf;
    }

    public static File getPhysioPdf(Physio physio){
        LocalDate now = LocalDate.now();
        YearMonth currentMonth = YearMonth.from(now);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);

        String dest = "./src/main/resources/physios/" + physio.getLicenseNumber() + "_" + currentMonth + ".pdf";
        File newPdf = null;
        Document document;
        try{
            PdfWriter writer = new PdfWriter(dest);
            PdfDocument pdf = new PdfDocument(writer);
            document = new Document(pdf);

            document.add(header);

            // Title
            Paragraph title = new Paragraph("- PAYROLL -")
                    .setFontSize(20)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(title);

            // Count last month confirmed appointments
            List<Appointment> confirmed = physio.getAppointments()
                    .stream()
                    .filter(a -> {
                       if(a.getConfirmed() && !a.getDate().isBlank()) {
                           LocalDate appointmentDate = LocalDate.parse(a.getDate(), formatter);
                           YearMonth appointmentYM = YearMonth.from(appointmentDate);
                           return appointmentYM.equals(currentMonth);
                       }
                       return false;
                    }).toList();

            // Calcular salario total
            double totalSalary = (confirmed.size() * 100);

            // Crear tabla resumen
            float[] summaryColWidths = {3, 3};
            Table salarySummary = new Table(UnitValue.createPercentArray(summaryColWidths));
            salarySummary.setWidth(UnitValue.createPercentValue(50));
            salarySummary.addCell(new Cell().add(new Paragraph("Physiotherapist")).setFontSize(12).setBold());
            salarySummary.addCell(new Cell().add(new Paragraph(physio.getFullName())).setFontSize(12));
            salarySummary.addCell(new Cell().add(new Paragraph("Month")).setFontSize(12).setBold());
            salarySummary.addCell(new Cell().add(new Paragraph(currentMonth.toString())).setFontSize(10));
            salarySummary.addCell(new Cell().add(new Paragraph("Licence Number")).setFontSize(10).setBold());
            salarySummary.addCell(new Cell().add(new Paragraph(physio.getLicenseNumber())).setFontSize(10));
            salarySummary.addCell(new Cell().add(new Paragraph("Confirmed Appointments")).setFontSize(10).setBold());
            salarySummary.addCell(new Cell().add(new Paragraph(String.valueOf(confirmed.size())).setFontSize(10)));
            salarySummary.addCell(new Cell().add(new Paragraph("Total Salary")).setFontSize(12).setBold());
            salarySummary.addCell(new Cell().add(new Paragraph(String.format("$%.2f", totalSalary))).setFontSize(12).setBold());

            document.add(salarySummary);
            document.add(new Paragraph("\n"));

            // Confimed Appointments
            Paragraph confirmedAppointments = new Paragraph("Confirmed Appointments")
                    .setFontSize(14)
                    .setItalic()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(confirmedAppointments);



            if(!confirmed.isEmpty()){
                // Table with patient info
                float[] colWidths = {1, 1, 4, 1};
                Table appointmentsTable = new Table(UnitValue.createPercentArray(colWidths));
                appointmentsTable.setWidth(UnitValue.createPercentValue(100));
                appointmentsTable.addHeaderCell(getHeaderCell("Date"));
                appointmentsTable.addHeaderCell(getHeaderCell("Patient"));
                appointmentsTable.addHeaderCell(getHeaderCell("Treatment"));
                appointmentsTable.addHeaderCell(getHeaderCell("Price"));

                for (Appointment a: confirmed) {
                    if (a.getConfirmed()) {
                        appointmentsTable.addCell(new Cell().add(new Paragraph(!a.getDate().isBlank() ? a.getDate() : "Empty Date")).setFontSize(9));
                        appointmentsTable.addCell(new Cell().add(new Paragraph(a.getPatient().getFullName())).setFontSize(9));
                        appointmentsTable.addCell(new Cell().add(new Paragraph(!a.getTreatment().isBlank() ? a.getTreatment() : "Empty Treatment")).setFontSize(9));
                        appointmentsTable.addCell(new Cell().add(new Paragraph("$100").setFontSize(9)));
                    }
                }
                document.add(appointmentsTable);
            }else{
                Paragraph noAppointmentsMessage = new Paragraph("You need work hard!")
                        .setFontSize(9)
                        .setItalic()
                        .setTextAlignment(TextAlignment.CENTER)
                        .setMarginBottom(20);
                document.add(confirmedAppointments);

                document.add(noAppointmentsMessage);
            }


            document.close();
            System.out.println("Physio salary PDF created successfully.");

            newPdf = new File(dest);

        }catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
        return newPdf;
    }

    private static Cell getHeaderCell(String text) {
        return new Cell().add(new Paragraph(text))
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setBold()
                .setBorder(new SolidBorder(ColorConstants.GRAY, 1))
                .setTextAlignment(TextAlignment.LEFT);
    }
}
