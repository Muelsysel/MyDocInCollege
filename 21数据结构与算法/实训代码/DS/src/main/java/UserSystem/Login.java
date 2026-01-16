package UserSystem;

import Mapper.UserMapper;
import POJO.UserInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Login extends JFrame {
    public String id;
    private UserMapper userMapper = new UserMapper();

    public Login() {
        super("用户登陆界面");
        userLogin();
        setSize(600, 400);
    }

    public void userLogin() {

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 20));
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
        setLocation(550, 250);
        // 将用户界面添加到窗口中
        this.add(panel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String account = userTextField.getText();
                String password = passwordField.getText();
                if (userMapper.compare(account, password)) {
                    UserInfo userInfo = userMapper.getUser(account);
                    new UserSystem(userInfo);//传入用户信息
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "账号或密码错误");
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
    JTextField phoneTextField;

    private UserMapper userMapper = new UserMapper();

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

        // 创建账号密码手机号输入面板
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        JLabel userLabel = new JLabel("账号：");
        JLabel passLabel = new JLabel("密码：");
        JLabel confirmLabel = new JLabel("确认密码：");
        JLabel phoneLabel = new JLabel("手机号:");

        userLabel.setHorizontalAlignment(JLabel.CENTER);
        passLabel.setHorizontalAlignment(JLabel.CENTER);
        confirmLabel.setHorizontalAlignment(JLabel.CENTER);
        phoneLabel.setHorizontalAlignment(JLabel.CENTER);

        userTextField = new JTextField();
        passwordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();
        phoneTextField = new JTextField();
        inputPanel.add(userLabel);
        inputPanel.add(userTextField);
        inputPanel.add(passLabel);
        inputPanel.add(passwordField);
        inputPanel.add(confirmLabel);
        inputPanel.add(confirmPasswordField);
        inputPanel.add(phoneLabel);
        inputPanel.add(phoneTextField);
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
        String account = userTextField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String phone = phoneTextField.getText();

        if (account.isBlank() || password.isBlank() || confirmPassword.isBlank() || phone.isBlank()) {
            JOptionPane.showMessageDialog(this, "请输入账号,密码和手机号！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "两次输入的密码不相同！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (userMapper.compare(account, password)) {
            JOptionPane.showMessageDialog(this, "该账号已存在！", "错误", JOptionPane.ERROR_MESSAGE);
        } else {
            userMapper.register(account, password, phone);
            JOptionPane.showMessageDialog(this, "注册成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }
}
