package hoangnguyen.dev.App;

import javax.mail.internet.MimeUtility;

public class Email {
    private String id;
    private String subject;
    private String sender;
    private String recipient;
    private String date;
    private String body;
    private boolean hasAttachment;
    private String attachmentName;

    // Getters và Setters cho các thuộc tính
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isHasAttachment() {
        return hasAttachment;
    }

    public void setHasAttachment(boolean hasAttachment) {
        this.hasAttachment = hasAttachment;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

//    // Phương thức trong lớp Email
//public String getRawSenderEmail() {
//    // Giả sử sender có định dạng: "Tên người gửi" <email>
//    String[] parts = sender.split("<");
//    if (parts.length > 1) {
//        String email = parts[1].replace(">", "").trim(); // Tách lấy địa chỉ email và bỏ dấu >
//        return email; // Trả về chỉ địa chỉ email
//    }
//    return sender; // Nếu không có định dạng, trả về nguyên bản
//}

//public String getDecodedSender() {
//    try {
//        // Giả sử sender có định dạng: "Tên người gửi" <email>
//        String[] parts = sender.split("<");
//        if (parts.length > 1) {
//            // Giải mã phần tên
//            String name = MimeUtility.decodeText(parts[0].trim());
//            return name; // Trả về tên người gửi đã được giải mã
//        } else {
//            return MimeUtility.decodeText(sender); // Trả về địa chỉ email đã được giải mã
//        }
//    } catch (Exception e) {
//        e.printStackTrace();
//        return sender; // Trả về sender gốc nếu có lỗi
//    }
//}


    // Phương thức giải mã tiêu đề người gửi
    public String getDecodedSender() {
        return decodeHeader(sender);
    }

    // Phương thức giải mã tiêu đề người nhận
    public String getDecodedRecipient() {
        return decodeHeader(recipient);
    }

    // Phương thức giải mã tiêu đề (Tên người gửi hoặc người nhận)
    private String decodeHeader(String header) {
        StringBuilder decodedHeader = new StringBuilder();
        try {
            // Giả sử tiêu đề có định dạng: "Tên người gửi" <email>
            String[] parts = header.split("<");
            if (parts.length > 1) {
                // Giải mã phần tên, bỏ dấu ngoặc kép nếu có
                String name = parts[0].trim().replace("\"", "");
                decodedHeader.append(MimeUtility.decodeText(name));
            } else {
                // Nếu chỉ có địa chỉ email mà không có tên
                decodedHeader.append(MimeUtility.decodeText(header.replace("\"", ""))); // Bỏ dấu ngoặc kép
            }
        } catch (Exception e) {
            e.printStackTrace();
            return header; // Trả về header gốc nếu có lỗi
        }
        return decodedHeader.toString();
    }
}
