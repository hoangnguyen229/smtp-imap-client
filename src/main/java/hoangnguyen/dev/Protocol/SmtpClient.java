package hoangnguyen.dev.Protocol;

import java.io.*;
import java.net.Socket;
import java.util.Base64;
import javax.net.ssl.SSLSocketFactory;

public class SmtpClient {

    private Socket socket;
    private BufferedWriter writer;
    private BufferedReader reader;

    // Kết nối tới máy chủ SMTP
    public void connect(String smtpServer, int port) throws IOException {
        socket = new Socket(smtpServer, port);
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

        // Đọc phản hồi ban đầu từ máy chủ
        String response = reader.readLine();
        System.out.println("Server: " + response);
        handleResponse(response);
    }

    // Thêm lệnh AUTH LOGIN
    public void authenticate(String username, String password) throws IOException {
        // Gửi lệnh AUTH LOGIN
        sendCommand("AUTH LOGIN");

        // Mã hóa username và password bằng Base64
        String encodedUsername = Base64.getEncoder().encodeToString(username.getBytes());
        String encodedPassword = Base64.getEncoder().encodeToString(password.getBytes());

        // Gửi username đã mã hóa Base64
        sendCommand(encodedUsername);

        // Gửi password đã mã hóa Base64
        sendCommand(encodedPassword);
    }

    // Gửi lệnh STARTTLS
    public void sendStartTls() throws IOException {
        sendCommand("STARTTLS");

        // Sau khi gửi lệnh STARTTLS, chuyển sang SSL
        SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        socket = sslSocketFactory.createSocket(socket, socket.getInetAddress().getHostAddress(), socket.getPort(), true);

        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

        System.out.println("TLS connection established");
    }

    // Gửi lệnh HELO
    public void sendHelo(String domain) throws IOException {
        sendCommand("HELO " + domain);
    }

    // Gửi lệnh MAIL FROM
    public void sendMailFrom(String fromEmail) throws IOException {
        sendCommand("MAIL FROM:<" + fromEmail + ">");
    }

    // Gửi lệnh RCPT TO
    public void sendRcptTo(String toEmail) throws IOException {
        sendCommand("RCPT TO:<" + toEmail + ">");
    }

    // Gửi lệnh DATA
    public void sendData(String headers, String body) throws IOException {
        sendCommand("DATA");
        writer.write(headers);
        writer.write("\r\n");
        writer.write(body + "\r\n");
        writer.write(".\r\n"); // Kết thúc phần nội dung thư với dấu chấm
        writer.flush();
        String response = reader.readLine();
        System.out.println("Server: " + response);
        handleResponse(response);
    }

    // Gửi lệnh QUIT
    public void sendQuit() throws IOException {
        sendCommand("QUIT");
    }

    // Gửi lệnh tới máy chủ và đọc phản hồi
    private void sendCommand(String command) throws IOException {
        writer.write(command + "\r\n");
        writer.flush();
        System.out.println("Client: " + command);

        // Đọc phản hồi từ máy chủ
        String response = reader.readLine();
        System.out.println("Server: " + response);
        
        // Xử lý phản hồi từ máy chủ
//        handleResponse(response);
    }

    // Phân tích phản hồi của máy chủ
    private void handleResponse(String response) {
        // Lấy mã phản hồi (là 3 ký tự đầu tiên của chuỗi phản hồi)
        int responseCode = Integer.parseInt(response.substring(0, 3));

        switch (responseCode) {
            case 250:
                System.out.println("250 OK - Command successful.");
                break;
            case 550:
                System.out.println("550 User unknown - The recipient address is not recognized.");
                break;
            case 221:
                System.out.println("221 Service closing transmission channel.");
                break;
            case 354:
                System.out.println("354 Start mail input - End with <CRLF>.<CRLF>");
                break;
            case 235:
                System.out.println("235 Authentication successful.");
                break;
            case 530:
                System.out.println("530 Authentication required.");
                break;
            case 503:
                System.out.println("503 Bad sequence of commands.");
                break;
            case 535:
                System.out.println("535 Authentication failed.");
                break;
            default:
                System.out.println("Unrecognized response code: " + responseCode);
                break;
        }
    }

    // Đóng kết nối
    public void close() throws IOException {
        writer.close();
        reader.close();
        socket.close();
    }

    public static void main(String[] args) {
        SmtpClient smtpClient = new SmtpClient();
        try {
            smtpClient.connect("smtp.gmail.com", 587); // Kết nối tới máy chủ Gmail

            smtpClient.sendHelo("hoangnguyen.dev");

            // Bước mới: Gửi lệnh STARTTLS
            smtpClient.sendStartTls();

            // Bước mới: Xác thực với tên người dùng và mật khẩu Gmail
            smtpClient.authenticate("chauduonghuetch@gmail.com", "tctj pslj vnmp xnha");

            smtpClient.sendMailFrom("chauduonghuetch@gmail.com");
            smtpClient.sendRcptTo("vanhoang1231234@gmail.com");
            smtpClient.sendData("Tiêu đề thư", "Nội dung của email này.");
            smtpClient.sendQuit();

            // Đóng kết nối
            smtpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
