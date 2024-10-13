/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hoangnguyen.dev.Protocol;

import hoangnguyen.dev.App.Email;
import java.io.*;
import java.net.Socket;
import javax.net.ssl.SSLSocketFactory;
import java.util.ArrayList;
import java.util.List;
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

        // Lấy tiêu đề
        String headerResponse = sendCommand("FETCH " + id + " (BODY[HEADER.FIELDS (SUBJECT FROM TO DATE)])");
        parseHeaderResponse(headerResponse, email);

        // Lấy nội dung
        String bodyResponse = sendCommandAndReadFullResponse("FETCH " + id + " (BODY[TEXT])");
        email.setBody(parseBodyResponse(bodyResponse));

        // Kiểm tra tệp đính kèm
//        String structureResponse = sendCommand("FETCH " + id + " (BODYSTRUCTURE)");
//        email.setHasAttachment(checkForAttachments(structureResponse));

        return email;
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
                    contentLength = Integer.parseInt(line.substring(startIndex, endIndex));
                }
            }

            if (contentLength > 0) {
                char[] buffer = new char[contentLength];
                reader.read(buffer, 0, contentLength);
                response.append(buffer);
                contentLength = -1;
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
        int endIndex = response.lastIndexOf(")");
        if (startIndex != -1 && endIndex != -1) {
            return response.substring(startIndex + 4, endIndex).trim();
        }
        return "";
    }

    private boolean checkForAttachments(String response) {
        // Kiểm tra xem có phần nào trong cấu trúc email là attachment không
        return response.toLowerCase().contains("attachment");
    }

    private String decodeSubject(String encodedSubject) {
        try {
            return MimeUtility.decodeText(encodedSubject);
        } catch (Exception e) {
            return encodedSubject;
        }
    }

    private String fetchEmailSubject(String id) throws IOException {
        String response = sendCommand("FETCH " + id + " (BODY[HEADER.FIELDS (SUBJECT)])");
        String subject = "Unknown Subject";
        String[] lines = response.split("\r\n");
        for (String line : lines) {
            if (line.startsWith("Subject:")) {
                subject = line.substring(9).trim();
                break;
            }
        }
        return id + ": " + subject;
    }

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

}