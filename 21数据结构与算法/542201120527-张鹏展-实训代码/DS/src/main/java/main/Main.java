package main;

import UserSystem.Login;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    public Main() {
        super("系统");
        ChoiceUI();
    }

    public static void main(String[] args) {
        new Main();
    }

    public void ChoiceUI() {
        JButton ManagerButton = new JButton("商家");
        JButton LoginButton = new JButton("用户");

        ManagerButton.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        LoginButton.setFont(new Font("微软雅黑", Font.PLAIN, 18));

        ManagerButton.addActionListener(e -> {
            new ShopSystem.Login();
            dispose();
        });
        LoginButton.addActionListener(e -> {
            new UserSystem.Login();
            dispose();
        });

        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("请选择您需要的功能");
        titleLabel.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 28));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel cont = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 50, 15, 50);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        cont.add(ManagerButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        cont.add(LoginButton, gbc);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(cont, BorderLayout.CENTER);

        add(titlePanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        setSize(600, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}