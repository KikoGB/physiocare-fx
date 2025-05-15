package com.gadeadiaz.physiocare.utils;
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

public class Pdf {

    public static void medicalRecordPdfCreator(Record record){

        String dest = "./src/main/resources/records/" + record.getPatient().getInsuranceNumber() + "_" + record.getMedicalRecord() + ".pdf";
        Document document = null;
        try{
            PdfWriter writer = new PdfWriter(dest);
            PdfDocument pdf = new PdfDocument(writer);
            document = new Document(pdf);

            // Title
            Paragraph title = new Paragraph("Medical Record")
                    .setFontSize(20)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(title);

            // Tabla with patient info
            float[] colWidths = {2, 4};
            Table patientInfo = new Table(UnitValue.createPercentArray(colWidths));
            patientInfo.setWidth(UnitValue.createPercentValue(100));

            patientInfo.addCell(getHeaderCell("Record ID"));
            patientInfo.addCell(String.valueOf(record.getId()));

            patientInfo.addCell(getHeaderCell("Patient's Insurance Number"));
            patientInfo.addCell(String.valueOf(record.getPatient().getInsuranceNumber()));

            patientInfo.addCell(getHeaderCell("Patient Name"));
            patientInfo.addCell(record.getPatient().getName() + " " + record.getPatient().getSurname());

            patientInfo.addCell(getHeaderCell("Email"));
            patientInfo.addCell(record.getPatient().getEmail());

            patientInfo.addCell(getHeaderCell("Address"));
            patientInfo.addCell(record.getPatient().getAddress());

            document.add(patientInfo);

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
            e.printStackTrace();
        }
    }

    private static Cell getHeaderCell(String text) {
        return new Cell().add(new Paragraph(text))
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setBold()
                .setBorder(new SolidBorder(ColorConstants.GRAY, 1))
                .setTextAlignment(TextAlignment.LEFT);
    }
}
