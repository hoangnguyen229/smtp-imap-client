/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hoangnguyen.dev.App;

import hoangnguyen.dev.Protocol.ImapClient;
import hoangnguyen.dev.Protocol.SmtpClient;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

/**
 *
 * @author vanho
 */
public class EmailAction {
    private SmtpClient smtpClient;
    private ImapClient imapClient;
    private String username;
    private String passsword;
    
    public EmailAction(){
        this.smtpClient = new SmtpClient();
        this.imapClient = new ImapClient();
    }
    
    public boolean authenticate(String username, String passowrd){
        this.username = username;
        this.passsword = passowrd;
        try{
            imapClient.connect("imap.gmail.com", 993);
            return imapClient.login(username, passsword);
        }
        catch(IOException ex){
            ex.printStackTrace();
            return false;
        } finally {
            try{
                imapClient.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    
    public boolean sendMail(String[] toEmails, String[] ccEmails, String[] bccEmails, String subject, String body, String attachmentPath) {
        try {
            smtpClient.connect(MailConfig.smtpHost, MailConfig.SMTP_START_TLS_PORT);
            smtpClient.sendHelo("localhost");
            smtpClient.sendStartTls();
            smtpClient.authenticate(username, passsword);
            smtpClient.sendMailFrom(username);

            // Send RCPT TO for all recipients
            for (String email : toEmails) {
                smtpClient.sendRcptTo(email);
            }
            for (String email : ccEmails) {
                smtpClient.sendRcptTo(email);
            }
            for (String email : bccEmails) {
                smtpClient.sendRcptTo(email);
            }

            // Generate a boundary for multipart message
            String boundary = "===" + System.currentTimeMillis() + "===";

            // Construct the email headers
            StringBuilder headers = new StringBuilder();
            headers.append("MIME-Version: 1.0\r\n");
            headers.append("Content-Type: multipart/mixed; boundary=\"").append(boundary).append("\"\r\n");
            headers.append("Subject: ").append(subject).append("\r\n");
            headers.append("To: ").append(String.join(", ", toEmails)).append("\r\n");
            if (ccEmails.length > 0) {
                headers.append("CC: ").append(String.join(", ", ccEmails)).append("\r\n");
            }
            headers.append("\r\n");

            // Construct the multipart message body
            StringBuilder messageBody = new StringBuilder();
            messageBody.append("--").append(boundary).append("\r\n");
            messageBody.append("Content-Type: text/plain; charset=utf-8\r\n\r\n");
            messageBody.append(body).append("\r\n\r\n");

            // Add attachment if provided
            if (attachmentPath != null && !attachmentPath.isEmpty()) {
                File file = new File(attachmentPath);
                String fileName = file.getName();
                String mimeType = Files.probeContentType(file.toPath());
                
                messageBody.append("--").append(boundary).append("\r\n");
                messageBody.append("Content-Type: ").append(mimeType).append("; name=\"").append(fileName).append("\"\r\n");
                messageBody.append("Content-Transfer-Encoding: base64\r\n");
                messageBody.append("Content-Disposition: attachment; filename=\"").append(fileName).append("\"\r\n\r\n");

                // Read and encode the file
                byte[] fileContent = Files.readAllBytes(file.toPath());
                String encodedFile = Base64.getEncoder().encodeToString(fileContent);
                
                // Split the base64 string into lines
                for (int i = 0; i < encodedFile.length(); i += 76) {
                    messageBody.append(encodedFile.substring(i, Math.min(encodedFile.length(), i + 76))).append("\r\n");
                }
                messageBody.append("\r\n");
            }

            messageBody.append("--").append(boundary).append("--\r\n");

            // Send the email data
            smtpClient.sendData(headers.toString(), messageBody.toString());
            smtpClient.sendQuit();
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            try {
                smtpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private String getDomainFromEmail(String email) {
        return email.substring(email.indexOf("@") + 1);
    }

    private String encodeNonAscii(String text) {
        if (text == null) {
            return "";
        }
        try {
            return "=?UTF-8?B?" + Base64.getEncoder().encodeToString(text.getBytes("UTF-8")) + "?=";
        } catch (java.io.UnsupportedEncodingException e) {
            return text;
        }
    }
    
    // Helper method to split comma-separated email addresses
    public static String[] splitEmails(String emails) {
        if (emails == null || emails.trim().isEmpty()) {
            return new String[0];
        }
        return Arrays.stream(emails.split(","))
                     .map(String::trim)
                     .filter(s -> !s.isEmpty())
                     .toArray(String[]::new);
    }
    
    public String getUserName(){
        return this.username;
    }
    
//    public List<String> listSentEmails(){
//        List<String> emails = new ArrayList<>();
//        try{
//            imapClient.connect(MailConfig.imapHost, MailConfig.TLS_PORT);
//            if(imapClient.login(username, passsword)){
//                if(imapClient.selectFolder("[GMAIL]/Sent Mail")){
//                    emails = imapClient.listEmails();
//                }
//            }
//        }
//        catch(IOException ex){
//            ex.printStackTrace();
//        } finally {
//            try{
//                imapClient.logout();
//                imapClient.close();
//            }
//            catch(IOException e){
//                e.printStackTrace();
//            }
//        }
//        return emails;
//    } 
}
