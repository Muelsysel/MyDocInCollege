package UserSystem.SubPanel;

import Mapper.UserMapper;
import POJO.UserInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import static java.lang.System.exit;

public class UserInfoPanel extends JPanel {
    private JLabel accountLabel;
    private JTextField phoneField;
    private JPasswordField passwordField;
    private JButton viewButton;
    private JButton modifyButton;
    private JButton deleteButton;
    private UserInfo userInfo;

    private UserMapper userMapper = new UserMapper();

    public UserInfoPanel(UserInfo userInfo) {
        this.userInfo = userInfo;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(3, 2, 10, 5)); // 调整行间距
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel accountTitleLabel = new JLabel("账号:");
        accountLabel = new JLabel(userInfo.getAccount());
        JLabel passwordTitleLabel = new JLabel("密码:");
        passwordField = new JPasswordField(userInfo.getPassword());
        JLabel contactTitleLabel = new JLabel("联系方式:");
        phoneField = new JTextField(userInfo.getPhone());

        accountTitleLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        passwordTitleLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        contactTitleLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        infoPanel.add(accountTitleLabel);
        infoPanel.add(accountLabel);
        infoPanel.add(passwordTitleLabel);
        infoPanel.add(passwordField);
        infoPanel.add(contactTitleLabel);
        infoPanel.add(phoneField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));

        viewButton = new JButton("查看");
        modifyButton = new JButton("修改");
        deleteButton = new JButton("注销");

        buttonPanel.add(viewButton);
        buttonPanel.add(modifyButton);
        buttonPanel.add(deleteButton);

        add(infoPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 查看个人信息
                viewPersonalInfo();
            }
        });

        modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 修改个人信息
                try {
                    modifyPersonalInfo();
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 注销个人信息
                deletePersonalInfo();
                exit(0);//注销后关闭程序
            }
        });
    }

    // 显示个人信息
    private void viewPersonalInfo() {

        JOptionPane.showMessageDialog(this, "账号：" + userInfo.getAccount() + "\n密码：" + userInfo.getPassword() + "\n联系方式：" + userInfo.getPhone(),
                "个人信息", JOptionPane.INFORMATION_MESSAGE);
    }

    // 修改个人信息
    private void modifyPersonalInfo() throws FileNotFoundException {

        String password = new String(passwordField.getPassword());
        String contact = phoneField.getText();
        userInfo.setPassword(password);
        userInfo.setPhone(contact);

        // 更新用户信息到文件
        userMapper.updateUserInfoFile(userInfo);
        JOptionPane.showMessageDialog(this, "个人信息修改成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
    }

    // 注销个人信息
    private void deletePersonalInfo() {

        int result = JOptionPane.showConfirmDialog(this, "确定要注销个人信息吗？", "提示", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            try {
                // 从文件中删除用户信息
                userMapper.deleteUserInfoFile(userInfo);
                JOptionPane.showMessageDialog(this, "个人信息注销成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "个人信息注销失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
