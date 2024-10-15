/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hoangnguyen.dev.Protocol;

import hoangnguyen.dev.App.Email;
import java.io.*;
import java.net.Socket;
import java.text.Normalizer;
import javax.net.ssl.SSLSocketFactory;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.internet.MimeUtility;

public class ImapClient {
    private Socket socket;
    private BufferedWriter writer;
    private BufferedReader reader;
    private int tag = 1;

    public void connect(String imapServer, int port) throws IOException {
        SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        socket = sslSocketFactory.createSocket(imapServer, port);
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

        String response = reader.readLine();
        System.out.println("Server: " + response);
        if (!response.startsWith("* OK")) {
            throw new IOException("Failed to connect to IMAP server: " + response);
        }
    }

    public boolean login(String username, String password) throws IOException {
        String command = String.format("LOGIN \"%s\" \"%s\"", username, password);
        String response = sendCommand(command);
        return response.contains("OK");
    }

    public boolean selectFolder(String folderName) throws IOException {
        String response = sendCommand("SELECT \"" + folderName + "\"");
        return response.contains("OK");
    }

    public List<Email> listEmails() throws IOException {
        List<Email> emails = new ArrayList<>();
        String response = sendCommand("SEARCH ALL");
        if (response.contains("* SEARCH")) {
            String[] lines = response.split("\r\n");
            for (String line : lines) {
                if (line.startsWith("* SEARCH")) {
                    String[] ids = line.substring(8).trim().split(" ");
                    for (String id : ids) {
                        Email email = fetchEmailInfo(id);
                        emails.add(email);
                    }
                    break;
                }
            }
        }
        return emails;
    }
    
//    public Email fetchEmailInfo(String id) throws IOException{
//        String response = sendCommand("FETCH " + id + " (BODY[HEADER] BODY[TEXT]");
//        Email email = new Email();
//        email.setId(id);
//        
//        String[] lines = response.split("\r\n");
//        boolean isBody = false;
//        StringBuilder body = new StringBuilder();
//        
//        for (String line : lines) {
//            if (line.startsWith("Subject: ")) {
//                email.setSubject(decodeSubject(line.substring(9).trim()));
//            } else if (line.startsWith("From: ")) {
//                email.setSender(line.substring(6).trim());
//            } else if (line.startsWith("To: ")) {
//                email.setRecipient(line.substring(4).trim());
//            } else if (line.startsWith("Date: ")) {
//                email.setDate(line.substring(6).trim());
//            } else if (line.startsWith("Content-Type: multipart/")) {
//                email.setHasAttachment(true);
//            } else if (line.equals("")) {
//                isBody = true;
//            } else if (isBody) {
//                body.append(line).append("\n");
//            }
//        }
//        email.setBody(body.toString().trim());
//        return email;
//    }
//    
//    private String decodeSubject(String encodedSubject) {
//        try {
//            return MimeUtility.decodeText(encodedSubject);
//        } catch (Exception e) {
//            return encodedSubject;
//        }
//    }
    private Email fetchEmailInfo(String id) throws IOException {
        Email email = new Email();
        email.setId(id);

        // Fetch email headers
        String headerResponse = sendCommand("FETCH " + id + " (BODY[HEADER.FIELDS (SUBJECT FROM TO DATE)])");
        if (headerResponse != null && headerResponse.contains("FETCH")) {
            parseHeaderResponse(headerResponse, email);
        } else {
            throw new IOException("Invalid header response: " + headerResponse);
        }

        // Fetch email structure
        String structureResponse = sendCommand("FETCH " + id + " BODYSTRUCTURE");
        if (structureResponse != null && structureResponse.contains("BODYSTRUCTURE")) {
            boolean hasAttachment = checkForAttachments(structureResponse);
            email.setHasAttachment(hasAttachment);

            if (hasAttachment) {
                String attachmentName = extractAttachmentName(structureResponse);
                email.setAttachmentName(attachmentName);
            }
        } else {
            throw new IOException("Invalid structure response: " + structureResponse);
        }

        // Fetch email body
        String bodyResponse = sendCommandAndReadFullResponse("FETCH " + id + " (BODY[TEXT])");
        if (bodyResponse != null && bodyResponse.contains("BODY")) {
            email.setBody(parseBodyResponse(bodyResponse));
        } else {
            throw new IOException("Invalid body response: " + bodyResponse);
        }

        return email;
    }

    public byte[] downloadAttachment(String emailId, String attachmentName) throws IOException {
        // Fetch the BODYSTRUCTURE to find the part number of the attachment
        String structureResponse = sendCommand("FETCH " + emailId + " BODYSTRUCTURE");
        String partNumber = findAttachmentPartNumber(structureResponse, attachmentName);
        
        if (partNumber == null) {
            throw new IOException("Attachment not found: " + attachmentName);
        }

        // Fetch the attachment data
        String fetchCommand = "FETCH " + emailId + " (BODY[" + partNumber + "])";
        String response = sendCommandAndReadFullResponse(fetchCommand);

        // Extract the attachment data from the response
        int startIndex = response.indexOf("{");
        int endIndex = response.lastIndexOf(")");
        if (startIndex == -1 || endIndex == -1 || startIndex >= endIndex) {
            throw new IOException("Invalid response format when fetching attachment");
        }
        
        // Extract the base64 encoded data
        String base64Data = extractBase64Data(response);
        return Base64.getDecoder().decode(base64Data);
    }
    
    private String extractAttachmentName(String structureResponse) {
        // Use a regular expression to find the filename
        Pattern pattern = Pattern.compile("\"filename\"\\s+\"([^\"]+)\"", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(structureResponse);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
    
    private String findAttachmentPartNumber(String structureResponse, String attachmentName) {
        // Tìm phần BODYSTRUCTURE chứa thông tin về các phần MIME
        String[] parts = structureResponse.split("\\(\\(");

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i].toUpperCase(); // Chuyển về uppercase để so sánh không phân biệt hoa thường

            // Kiểm tra xem phần này có chứa "ATTACHMENT" và tên tệp không
            if (part.contains("ATTACHMENT") && part.contains(attachmentName.toUpperCase())) {
                // Part number bắt đầu từ 1 (IMAP sử dụng indexing từ 1)
                return String.valueOf(i + 1);
            }
        }

        return null; // Nếu không tìm thấy tệp đính kèm
    }
    
    /**
     * Tách chính xác dữ liệu base64 từ phản hồi và loại bỏ các ký tự không hợp lệ.
     */
    private String extractBase64Data(String response) {
    // Dùng regex để tìm dữ liệu base64 nhưng không loại bỏ các ký tự
        Pattern BASE64_PATTERN = Pattern.compile("\\{(\\d+)}\\s*(.*)");
        Matcher matcher = BASE64_PATTERN.matcher(response);
        if (matcher.find()) {
            String base64Data = matcher.group(2).trim();
            // Kiểm tra xem dữ liệu có đủ các ký tự base64 hợp lệ không
            if (base64Data.length() % 4 == 0) {
                return base64Data;
            } else {
                // Bổ sung padding nếu thiếu
                while (base64Data.length() % 4 != 0) {
                    base64Data += "=";
                }
                return base64Data;
            }
        } else {
            throw new IllegalArgumentException("Invalid response format for base64 data");
        }
    }

    private String sendCommandAndReadFullResponse(String command) throws IOException {
     writer.write(tag + " " + command + "\r\n");
     writer.flush();
     System.out.println("Client: " + tag + " " + command);

     StringBuilder response = new StringBuilder();
     String line;
     int contentLength = -1;

     while ((line = reader.readLine()) != null) {
         System.out.println("Server: " + line);
         response.append(line).append("\r\n");

         if (line.contains("{")) {
             int startIndex = line.lastIndexOf("{") + 1;
             int endIndex = line.lastIndexOf("}");

             if (startIndex < endIndex) {
                 String lengthString = line.substring(startIndex, endIndex).trim();
                 try {
                     contentLength = Integer.parseInt(lengthString);
                 } catch (NumberFormatException e) {
                     System.out.println("Lỗi: Đầu vào không phải là một số hợp lệ: " + lengthString);
                     contentLength = -1; // Đặt lại contentLength để không đọc dữ liệu không hợp lệ
                 }
             }
         }

         if (contentLength > 0) {
             char[] buffer = new char[contentLength];
             reader.read(buffer, 0, contentLength);
             response.append(buffer);
             contentLength = -1; // Đặt lại để tránh đọc lại dữ liệu không cần thiết
         }

         if (line.startsWith(tag + " OK") || line.startsWith(tag + " NO") || line.startsWith(tag + " BAD")) {
             break;
         }
     }
     tag++;
     return response.toString();
 }

    private void parseHeaderResponse(String response, Email email) throws IOException {
        String[] lines = response.split("\r\n");
        for (String line : lines) {
            if (line.startsWith("Subject: ")) {
                email.setSubject(decodeSubject(line.substring(9).trim()));
            } else if (line.startsWith("From: ")) {
                email.setSender(line.substring(6).trim());
            } else if (line.startsWith("To: ")) {
                email.setRecipient(line.substring(4).trim());
            } else if (line.startsWith("Date: ")) {
                email.setDate(line.substring(6).trim());
            }
        }
    }

    private String parseBodyResponse(String response) {
    int startIndex = response.indexOf("\r\n\r\n");
    if (startIndex != -1) {
        // Extract the body part after the header part
        String body = response.substring(startIndex + 4).trim();
        
        // If the body is multipart, find the first text part
        int textPartStart = body.indexOf("\r\n--");
        if (textPartStart != -1) {
            body = body.substring(0, textPartStart).trim();
        }

        // Remove any MIME headers and trim whitespace
        int contentStart = body.indexOf("\r\n\r\n");
        if (contentStart != -1) {
            body = body.substring(contentStart + 4).trim();
        }

        return body;
    }
    return "";
}

    private boolean checkForAttachments(String structureResponse) {
        // Check if the response contains "ATTACHMENT" (case-insensitive)
        return structureResponse.toLowerCase().contains("attachment");
    }

    private String decodeSubject(String encodedSubject) {
        try {
            return MimeUtility.decodeText(encodedSubject);
        } catch (Exception e) {
            return encodedSubject;
        }
    }

//    private String fetchEmailSubject(String id) throws IOException {
//        String response = sendCommand("FETCH " + id + " (BODY[HEADER.FIELDS (SUBJECT)])");
//        String subject = "Unknown Subject";
//        String[] lines = response.split("\r\n");
//        for (String line : lines) {
//            if (line.startsWith("Subject:")) {
//                subject = line.substring(9).trim();
//                break;
//            }
//        }
//        return id + ": " + subject;
//    }
    
    public void logout() throws IOException {
        sendCommand("LOGOUT");
    }

    private String sendCommand(String command) throws IOException {
        String taggedCommand = tag + " " + command;
        writer.write(taggedCommand + "\r\n");
        writer.flush();
        System.out.println("Client: " + taggedCommand);

        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println("Server: " + line);
            response.append(line).append("\r\n");
            if (line.startsWith(tag + " OK") || line.startsWith(tag + " NO") || line.startsWith(tag + " BAD")) {
                break;
            }
        }
        tag++;
        return response.toString();
    }

    public void close() throws IOException {
        if (writer != null) writer.close();
        if (reader != null) reader.close();
        if (socket != null) socket.close();
    }

    public List<String> listFolders() throws IOException {
        List<String> folders = new ArrayList<>();
        String response = sendCommand("LIST \"\" \"*\"");

        String[] lines = response.split("\r\n");
        for (String line : lines) {
            if (line.startsWith("* LIST")) {
                // Lấy tên thư mục từ dòng trả về
                String[] parts = line.split(" ");
                String folderName = parts[parts.length - 1].replace("\"", "");
                folders.add(folderName);
            }
        }
        return folders;
    }
    
    public boolean deleteEmail(String emailId) throws IOException {
        // Đánh dấu email để xóa
        String response = sendCommand("STORE " + emailId + " +FLAGS (\\Deleted)");
        if (!response.contains("OK")) {
            return false; // Không thể đánh dấu để xóa
        }

        // Xóa các email đã đánh dấu
        response = sendCommand("EXPUNGE");
        return response.contains("OK");
    }
    
    public boolean moveToTrash(String emailId) throws IOException {
        // Tên thư mục thùng rác với mã hóa UTF-7
        String trashFolder = "[Gmail]/Th&APk-ng r&AOE-c"; 

        // Sao chép email vào thư mục thùng rác
        String response = sendCommand("COPY " + emailId + " \"" + trashFolder + "\"");
        System.out.println("COPY response: " + response); // In phản hồi từ lệnh COPY
        if (!response.contains("OK")) {
            System.err.println("Failed to copy email to trash.");
            return false; // Không thể sao chép email vào thùng rác
        }

        // Đánh dấu email là đã xóa bằng cách thêm cờ \\Deleted
        response = sendCommand("STORE " + emailId + " +FLAGS (\\Deleted)");
        System.out.println("STORE response: " + response); // In phản hồi từ lệnh STORE
        if (!response.contains("OK")) {
            System.err.println("Failed to mark email as deleted.");
            return false; // Không thể đánh dấu email là đã xóa
        }

        // Gọi lệnh EXPUNGE để thực hiện việc xóa email đã đánh dấu
        response = sendCommand("EXPUNGE");
        System.out.println("EXPUNGE response: " + response); // In phản hồi từ lệnh EXPUNGE
        if (!response.contains("OK")) {
            System.err.println("Failed to expunge deleted email.");
            return false; // Không thể xóa email đã đánh dấu
        }

        return true; // Nếu hoàn tất, trả về true
    }

    // Phương thức di chuyển email vào hộp thư đến
    public boolean moveToInbox(String emailId) throws IOException {
        // Đặt tên thư mục đến
        String inboxFolder = "INBOX"; 

        // Sao chép email vào thư mục đến
        String response = sendCommand("COPY " + emailId + " \"" + inboxFolder + "\"");
        System.out.println("COPY response: " + response); // In phản hồi từ lệnh COPY
        if (!response.contains("OK")) {
            return false; // Không thể sao chép email
        }

        // Đánh dấu email để xóa
        response = sendCommand("STORE " + emailId + " +FLAGS (\\Deleted)");
        System.out.println("STORE response: " + response); // In phản hồi từ lệnh STORE
        if (!response.contains("OK")) {
            return false; // Không thể đánh dấu email
        }

        // Gọi lệnh EXPUNGE để thực hiện xóa
        response = sendCommand("EXPUNGE");
        System.out.println("EXPUNGE response: " + response); // In phản hồi từ lệnh EXPUNGE
        if (!response.contains("OK")) {
            return false; // Không thể xóa email
        }

        return true; // Nếu đã hoàn tất, trả về true
    }
    
 
}