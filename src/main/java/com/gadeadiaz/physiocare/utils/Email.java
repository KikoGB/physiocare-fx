package com.gadeadiaz.physiocare.utils;


import com.gadeadiaz.physiocare.models.Patient;
import com.gadeadiaz.physiocare.models.Physio;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMultipart;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Properties;


public class Email {

    private static final String APPLICATION_NAME = "Physiocare Gmail";
    private static final JsonFactory JSON_FACTORY =
            GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final String CREDENTIALS_FILE_PATH = "src/main/resources/client_secret_199476945917-rka0sacuport1212vng3foe6gbdfij0n.apps.googleusercontent.com.json";

    /**
     * Autenticación con Gmail API (getCredentials)
     * 1. Usa OAuth 2.0 para autorizar el acceso a Gmail del usuario.
     * 2. Carga un archivo JSON con las credenciales (client_secret_...json) desde el proyecto.
     * 3. Guarda tokens en tokens/ para reutilizar el acceso sin pedir permiso cada vez.
     */


    public static void sendPatientMail(Patient patient) {

        File dest = Pdf.getPatientPdf(patient);
        System.out.println(dest.getName());
        try {

            // Build a new authorized API client service.
            final NetHttpTransport HTTP_TRANSPORT = new com.google.api.client.http.javanet.NetHttpTransport();
            Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                    getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            // Define the email parameters
            String user = "me";
            MimeMessage emailContent= createEmailWithAttachment(
                            "kikogadeabravo@gmail.com",
                            "kikogadeabravo@gmail.com", // Change to patient email
                            "Physiocare Notice",
                            "You are about to reach the limit of available appointments. See the attached document for more details.",
                    dest.getAbsolutePath());

            // Send the email
            sendMessage(service, user, emailContent);
        } catch (Exception e) {
            System.out.println("Error sending patient email");
        }
    }

    public static void sendPhysioMail(Physio physio) {

        File dest = Pdf.getPhysioPdf(physio);
        System.out.println(dest.getName());
        try {

            // Build a new authorized API client service.
            final NetHttpTransport HTTP_TRANSPORT = new com.google.api.client.http.javanet.NetHttpTransport();
            Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                    getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            // Define the email parameters
            String user = "me";
            MimeMessage emailContent= createEmailWithAttachment(
                    "kikogadeabravo@gmail.com",
                    "kikogadeabravo@gmail.com", //Change to physio email
                    "Salary",
                    "Hi " + physio.getFullName() + ", we have processed your last salary for last month.",
                    dest.getAbsolutePath());

            // Send the email
            sendMessage(service, user, emailContent);
        } catch (Exception e) {
            System.out.println("Error sending physio email");
        }
    }
    public static Credential getCredentials(
            final NetHttpTransport HTTP_TRANSPORT) throws Exception {
        // Load client secrets.
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY,
                        new InputStreamReader(
                                new FileInputStream(CREDENTIALS_FILE_PATH)));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets,
                        Collections.singletonList(GmailScopes.MAIL_GOOGLE_COM))
                        .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                        .setAccessType("offline")
                        .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
    public static MimeMessage createEmailWithAttachment(String to, String from, String subject, String bodyText, String fileDir) throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(from));
        email.addRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);

        // Crear el cuerpo del correo
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(bodyText, "utf-8");

        // Adjuntar archivo
        MimeBodyPart attachmentPart = new MimeBodyPart();
        attachmentPart.setDataHandler(new jakarta.activation.DataHandler(
                new jakarta.activation.FileDataSource(fileDir)));
        attachmentPart.setFileName(new java.io.File(fileDir).getName());

        // Crear estructura MIME
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(textPart);
        multipart.addBodyPart(attachmentPart);

        email.setContent(multipart, "multipart/mixed");

        return email;
    }
    /**
     * 5. Enviar el mensaje (sendMessage)
     * Send an email from the user's mailbox to its recipient.
     */
    public static void sendMessage(Gmail service, String userId, MimeMessage emailContent) throws MessagingException, java.io.IOException {
        Message message = createMessageWithEmail(emailContent);
        message = service.users().messages()
                .send(userId, message).execute();
        System.out.println("Message id: " + message.getId());
        System.out.println("Email sent successfully.");
    }

    /**
     * Create a message from an email.
     */
    public static Message createMessageWithEmail(MimeMessage email) throws MessagingException, java.io.IOException {
//        Codificar el correo para Gmail API (createMessageWithEmail)
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = java.util.Base64.getUrlEncoder()
                .encodeToString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }


}
