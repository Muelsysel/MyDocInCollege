import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class login extends JFrame {
    public String id;
    public login(){
        super("用户登陆界面");
        UUI();
        setSize(600,400);
    }
    public void UUI(){

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3,2,10,20));
        panel.setBackground(new Color(233, 233, 233)); // 设置背景色

        JLabel userLabel = new JLabel("账号：");
        userLabel.setFont(new Font("楷体", Font.PLAIN, 18)); // 设置字体

        panel.add(userLabel);

        JTextField userTextField = new JTextField();
        userTextField.setFont(new Font("楷体", Font.PLAIN, 18));
        panel.add(userTextField);

        JLabel passLabel = new JLabel("密码：");
        passLabel.setFont(new Font("楷体", Font.PLAIN, 18));
        panel.add(passLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("宋体", Font.PLAIN, 18));
        panel.add(passwordField);

        JButton loginButton = new JButton("登录");
        loginButton.setFont(new Font("楷体", Font.PLAIN, 18));
        loginButton.setBackground(new Color(0, 191, 255)); // 设置按钮颜色
        loginButton.setForeground(Color.WHITE); // 设置字体颜色
        panel.add(loginButton);

        JButton registerButton = new JButton("注册");
        registerButton.setFont(new Font("楷体", Font.PLAIN, 18));
        registerButton.setBackground(new Color(0, 191, 255));
        registerButton.setForeground(Color.WHITE);
        panel.add(registerButton);
        setLocation(550,250);
        // 将用户界面添加到窗口中
        this.add(panel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (
                        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/training", "root", "242648");
                        PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE id=? AND password=?")
                ) {
                    ps.setString(1, userTextField.getText());
                    ps.setString(2, new String(passwordField.getPassword()));
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, "登录成功！");
                        // 登录成功后进入主界面
                        id = userTextField.getText();
                        new Exam(id);
                        dispose(); // 销毁登录窗口
                    } else {
                        JOptionPane.showMessageDialog(null, "用户名或密码错误！");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 显示注册界面
                new Register();
            }
        });

        this.pack();
        this.setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}
class Register extends JFrame {
    JTextField userTextField;
    JPasswordField passwordField;
    JPasswordField confirmPasswordField;

    public Register() {
        super("用户注册");

        // 设置窗口大小
        setSize(400, 250);

        // 创建标题面板
        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("欢迎注册");
        titleLabel.setFont(new Font("宋体", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 创建账号密码输入面板
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        JLabel userLabel = new JLabel("账号：");
        JLabel passLabel = new JLabel("密码：");
        JLabel confirmLabel = new JLabel("确认密码：");
        userLabel.setHorizontalAlignment(JLabel.CENTER);
        passLabel.setHorizontalAlignment(JLabel.CENTER);
        confirmLabel.setHorizontalAlignment(JLabel.CENTER);
        userTextField = new JTextField();
        passwordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();
        inputPanel.add(userLabel);
        inputPanel.add(userTextField);
        inputPanel.add(passLabel);
        inputPanel.add(passwordField);
        inputPanel.add(confirmLabel);
        inputPanel.add(confirmPasswordField);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 创建提交按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton submitButton = new JButton("提交");
        submitButton.setFont(new Font("宋体", Font.PLAIN, 16));
        submitButton.setBackground(Color.WHITE);
        submitButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        submitButton.setPreferredSize(new Dimension(80, 30));
        submitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                submitButton.setBackground(new Color(240, 240, 240));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                submitButton.setBackground(Color.WHITE);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                submitButton.setBackground(new Color(220, 220, 220));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                submitButton.setBackground(new Color(240, 240, 240));
            }
        });
        submitButton.addActionListener(this::submit);
        buttonPanel.add(submitButton);

        // 将面板添加到窗口中
        add(titlePanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // 设置窗口居中显示
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void submit(ActionEvent actionEvent) {
        String username = userTextField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (username.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            JOptionPane.showMessageDialog(this, "请输入账号和密码！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "两次输入的密码不相同！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/training", "root", "242648");
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE id=?");
                PreparedStatement ps2 = conn.prepareStatement("INSERT INTO users(id, password) VALUES(?, ?)")
        ) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "该用户名已存在，请重新输入！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ps2.setString(1, username);
            ps2.setString(2, password);
            int result = ps2.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "注册成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
                dispose(); // 销毁注册窗口
            } else {
                JOptionPane.showMessageDialog(this, "注册失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
