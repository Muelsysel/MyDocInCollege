package ShopSystem;

import Mapper.ShopMapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

public class Login extends JFrame {

    public String id;

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

        setLocation(550, 250);
        // 将用户界面添加到窗口中
        this.add(panel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ShopMapper shopMapper = new ShopMapper();
                    String shopName = userTextField.getText();
                    String password = passwordField.getText();
                    if (shopName != null && password != null && shopMapper.hashCompare(shopName, password)) {
                        new ShopSystem(shopName);//传入用户信息
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "账号或密码错误");
                    }
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        this.pack();
        this.setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}