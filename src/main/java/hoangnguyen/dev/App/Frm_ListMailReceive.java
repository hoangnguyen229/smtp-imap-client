/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package hoangnguyen.dev.App;

import hoangnguyen.dev.Protocol.ImapClient;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Dell
 */
public class Frm_ListMailReceive extends javax.swing.JFrame {

    private ImapClient imapClient;
    private DefaultListModel<String> listModel;
    private String imapHost;
    private int imapPort;
    private String userEmail;
    private String password;
    private List<Email> emails;
    /**
     * Creates new form frmHome
     */
    public Frm_ListMailReceive() {
        initComponents();
    }

    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listMailReceived = new javax.swing.JList<>();
        jPanel3 = new javax.swing.JPanel();
        lb_subject = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txt_recipientName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txt_sender = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txt_receivingDate = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txt_attachmentFile = new javax.swing.JTextField();
        btn_attachment = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txt_message = new javax.swing.JTextArea();
        txt_search = new javax.swing.JTextField();
        btnDelete = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        btn_home = new javax.swing.JMenu();
        jmenu_exit = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Received Mail List");

        listMailReceived.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        listMailReceived.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listMailReceivedValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(listMailReceived);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 83, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        lb_subject.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lb_subject.setText("Subject");

        jLabel1.setText("Recipient name:");

        jLabel4.setText("Sender:");

        jLabel3.setText("Receiving date:");

        jLabel5.setText("Attached file:");

        txt_attachmentFile.setText("No attachment");

        btn_attachment.setText("Save As");
        btn_attachment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_attachmentActionPerformed(evt);
            }
        });

        txt_message.setColumns(20);
        txt_message.setRows(5);
        jScrollPane2.setViewportView(txt_message);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lb_subject, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txt_receivingDate, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt_recipientName))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_attachmentFile)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_attachment))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txt_sender, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lb_subject)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txt_recipientName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_sender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txt_receivingDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_attachmentFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_attachment)
                    .addComponent(jLabel5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        txt_search.setText("Search");

        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btn_home.setText("Home");
        btn_home.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_homeActionPerformed(evt);
            }
        });

        jmenu_exit.setText("Exit");
        jmenu_exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenu_exitActionPerformed(evt);
            }
        });
        btn_home.add(jmenu_exit);

        jMenuBar1.add(btn_home);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnDelete)
                .addContainerGap(10, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txt_search, javax.swing.GroupLayout.PREFERRED_SIZE, 439, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(155, 155, 155))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txt_search, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    public Frm_ListMailReceive(String imapHost, int imapPort, String userEmail, String password) {
        this.userEmail = userEmail;
        this.password = password;
        this.imapHost = imapHost;
        this.imapPort = imapPort;
        initComponents();
        setupImapClient();
        btn_attachment.setEnabled(false);
    }
    
    private void setupImapClient() {
        imapClient = new ImapClient();
        listModel = new DefaultListModel<>(); // Khởi tạo mô hình danh sách
        listMailReceived.setModel(listModel); // Gán mô hình vào JList

        try {
            imapClient.connect(imapHost, imapPort); // Kết nối với server IMAP
            if (imapClient.login(userEmail, password)) {
                List<String> folders = imapClient.listFolders(); // Lấy danh sách thư mục
                for (String folder : folders) {
                    System.out.println(folder); // In ra danh sách thư mục
                }

                // Chọn thư mục "INBOX"
                if (imapClient.selectFolder("INBOX")) { // Chọn "INBOX" thay vì "[Gmail]/INBOX"
                    emails = imapClient.listEmails(); // Lấy danh sách email
                    for (Email email : emails) {
                        listModel.addElement(email.getSubject()); // Thêm tiêu đề email vào mô hình
                    }
                } else {
                    System.out.println("Failed to select folder");
                }
            } else {
                System.out.println("Login failed");
            }
        } catch (IOException ex) {
            ex.printStackTrace(); // Xử lý lỗi
        }
    }

    
    private void displayEmailDetails(int index) {
        Email email = emails.get(index);
        lb_subject.setText(email.getSubject());
        txt_recipientName.setText(email.getDecodedRecipient()); 
        txt_receivingDate.setText(email.getDate());
        txt_sender.setText(email.getDecodedSender()); 
        txt_message.setText(email.getBody());

    }
    private void btn_homeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_homeActionPerformed
        
    }//GEN-LAST:event_btn_homeActionPerformed

    private void jmenu_exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenu_exitActionPerformed
        Frm_Home frm_Home = new Frm_Home(userEmail,password);
        frm_Home.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jmenu_exitActionPerformed

    private void btn_attachmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_attachmentActionPerformed

    }//GEN-LAST:event_btn_attachmentActionPerformed

    private void listMailReceivedValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listMailReceivedValueChanged
        if(!evt.getValueIsAdjusting()){
            int selectedIndex = listMailReceived.getSelectedIndex();
            if(selectedIndex != -1){
                displayEmailDetails(selectedIndex);
            }
        }
    }//GEN-LAST:event_listMailReceivedValueChanged

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed

    int selectedIndex = listMailReceived.getSelectedIndex();
    if (selectedIndex != -1) {
        Email emailToDelete = emails.get(selectedIndex);
        try {
            boolean success = imapClient.moveToTrash(emailToDelete.getId());

            if (success) {
                // Update the display: Remove the email from the listModel and emails list
                listModel.remove(selectedIndex);
                emails.remove(selectedIndex);

                // Clear the details of the deleted email
                lb_subject.setText(""); 
                txt_recipientName.setText("");
                txt_receivingDate.setText("");
                txt_sender.setText("");
                txt_message.setText("");
                
                // Show a success message
                JOptionPane.showMessageDialog(this, "Email has been successfully moved to trash!");
            } else {
                // Show an error message if the email could not be moved to trash
                JOptionPane.showMessageDialog(this, "Failed to move email to trash.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            // Show an error message if an exception occurs
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while moving the email to trash: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        // Show a warning message if no email is selected
        JOptionPane.showMessageDialog(this, "No email selected.", "Warning", JOptionPane.WARNING_MESSAGE);
    }
    }//GEN-LAST:event_btnDeleteActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
//        try {
//            for (javax.swing.UIManager.LookAndFeeltxt_recipientNamevax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(Frm_ListMailReceive.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(Frm_ListMailReceive.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(Frm_ListMailReceive.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(Frm_ListMailReceive.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Frm_ListMailReceive().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btn_attachment;
    private javax.swing.JMenu btn_home;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JMenuItem jmenu_exit;
    private javax.swing.JLabel lb_subject;
    private javax.swing.JList<String> listMailReceived;
    private javax.swing.JTextField txt_attachmentFile;
    private javax.swing.JTextArea txt_message;
    private javax.swing.JTextField txt_receivingDate;
    private javax.swing.JTextField txt_recipientName;
    private javax.swing.JTextField txt_search;
    private javax.swing.JTextField txt_sender;
    // End of variables declaration//GEN-END:variables
}
