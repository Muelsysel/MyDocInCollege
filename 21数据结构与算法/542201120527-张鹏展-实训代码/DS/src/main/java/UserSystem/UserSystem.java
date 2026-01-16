package UserSystem;

import POJO.UserInfo;
import UserSystem.SubPanel.SearchPanel;
import UserSystem.SubPanel.UserBookPanel;
import UserSystem.SubPanel.UserInfoPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

public class UserSystem extends JFrame {
    private JPanel mainPanel;
    private JButton personalInfoButton;
    private JButton restaurantSearchButton;
    private JButton restaurantBookingButton;
    private JPanel contentPanel;

    public UserSystem(UserInfo userInfo) {
        setTitle("用户子系统");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel(new BorderLayout());
        contentPanel = new JPanel(new BorderLayout());

        // 设置背景色和边距
        mainPanel.setBackground(new Color(0xF9F9F9));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        personalInfoButton = new JButton();
        // 设置按钮图标
        //personalInfoButton.setIcon(new ImageIcon(getClass().getResource("/icons/user.png")));
        personalInfoButton.setText("个人信息");
        // 设置按钮样式
        personalInfoButton.setForeground(new Color(0x444444));
        personalInfoButton.setBackground(new Color(0xFFFFFF));
        personalInfoButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xDDDDDD)),
                BorderFactory.createEmptyBorder(10, 30, 10, 30)
        ));
        personalInfoButton.setFont(new Font("微软雅黑", Font.PLAIN, 16));

        restaurantSearchButton = new JButton();
        // 设置按钮图标
        //restaurantSearchButton.setIcon(new ImageIcon(getClass().getResource("/icons/search.png")));
        restaurantSearchButton.setText("餐馆查询");
        // 设置按钮样式
        restaurantSearchButton.setForeground(new Color(0x444444));
        restaurantSearchButton.setBackground(new Color(0xFFFFFF));
        restaurantSearchButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xDDDDDD)),
                BorderFactory.createEmptyBorder(10, 30, 10, 30)
        ));
        restaurantSearchButton.setFont(new Font("微软雅黑", Font.PLAIN, 16));

        restaurantBookingButton = new JButton();
        // 设置按钮图标
        //restaurantBookingButton.setIcon(new ImageIcon(getClass().getResource("/icons/booking.png")));
        restaurantBookingButton.setText("餐馆预定");
        // 设置按钮样式
        restaurantBookingButton.setForeground(new Color(0x444444));
        restaurantBookingButton.setBackground(new Color(0xFFFFFF));
        restaurantBookingButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xDDDDDD)),
                BorderFactory.createEmptyBorder(10, 30, 10, 30)
        ));
        restaurantBookingButton.setFont(new Font("微软雅黑", Font.PLAIN, 16));

        JPanel functionPanel = new JPanel();
        functionPanel.setBackground(new Color(0xFFFFFF));
        functionPanel.setBorder(BorderFactory.createLineBorder(new Color(0xDDDDDD)));
        functionPanel.setLayout(new BoxLayout(functionPanel, BoxLayout.Y_AXIS));
        functionPanel.add(Box.createVerticalStrut(40)); // 垂直间距
        functionPanel.add(personalInfoButton);
        functionPanel.add(Box.createVerticalStrut(20)); // 垂直间距
        functionPanel.add(restaurantSearchButton);
        functionPanel.add(Box.createVerticalStrut(20)); // 垂直间距
        functionPanel.add(restaurantBookingButton);
        functionPanel.add(Box.createVerticalGlue()); // 垂直扩展

        JLabel titleLabel = new JLabel("<html><font size='6'><b>用户子系统</b></font></html>");
        titleLabel.setForeground(new Color(0x444444));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(functionPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        personalInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 切换到个人信息管理界面
                showPersonalInfoPanel(userInfo);
            }
        });

        restaurantSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 切换到餐馆查询界面
                try {
                    showRestaurantSearchPanel(userInfo);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        restaurantBookingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 切换到餐馆预定界面
                try {
                    showRestaurantBookingPanel(userInfo);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        add(mainPanel);
    }

    private void showPersonalInfoPanel(UserInfo userInfo) {
        // 显示个人信息界面的代码
        contentPanel.removeAll();
        contentPanel.add(new UserInfoPanel(userInfo), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showRestaurantSearchPanel(UserInfo userInfo) throws IOException {
        // 显示餐馆查询界面的代码
        contentPanel.removeAll();
        contentPanel.add(new SearchPanel(), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showRestaurantBookingPanel(UserInfo userInfo) throws IOException {
        // 显示餐馆预定界面的代码
        contentPanel.removeAll();
        contentPanel.add(new UserBookPanel(userInfo), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }


}
