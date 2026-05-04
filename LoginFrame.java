import java.awt.event.*;
import javax.swing.*;

public class LoginFrame {
    
    public static void main(String[] args) {
        UserManager userManager = new UserManager();
        userManager.loadFromFile(); //從檔案載入使用者資料

        JFrame frame = new JFrame("登入系統");
        frame.setSize(300, 200);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //使用者名稱標籤和輸入框
        JLabel userLabel = new JLabel("帳號:");
        userLabel.setBounds(30, 30, 80, 25);
        frame.add(userLabel);

        JTextField userField = new JTextField();
        userField.setBounds(100, 30, 150, 25);
        frame.add(userField);

        //密碼標籤和輸入框
        JLabel passLabel = new JLabel("密碼:");
        passLabel.setBounds(30, 70, 80, 25);
        frame.add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(100, 70, 150, 25);
        frame.add(passField);

        //登入按鈕
        JButton loginButton = new JButton("登入");
        loginButton.setBounds(100, 110, 80, 30);
        frame.add(loginButton);

        frame.setVisible(true);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String username = userField.getText();
                String password = new String(passField.getPassword());

                if (userManager.login(username, password)) {
                    JOptionPane.showMessageDialog(frame, "登入成功");
                } else {
                    JOptionPane.showMessageDialog(frame, "登入失敗");
                }
            }
        });

    }
}
