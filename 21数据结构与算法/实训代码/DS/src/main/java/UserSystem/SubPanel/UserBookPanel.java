package UserSystem.SubPanel;

import Mapper.DistanceMapper;
import Mapper.OrderMapper;
import Mapper.ShopMapper;
import POJO.Order;
import POJO.Shop;
import POJO.UserInfo;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class UserBookPanel extends JPanel {
    OrderMapper orderMapper = new OrderMapper();
    ShopMapper shopMapper = new ShopMapper();
    DistanceMapper distanceMapper = new DistanceMapper();
    private JButton userButton;
    private JButton shopButton;
    private JButton addButton;
    private JButton recommendButton;
    private UserInfo userInfo;

    public UserBookPanel(UserInfo userInfo) throws IOException {
        this.userInfo = userInfo;

        setLayout(new GridLayout(2, 2));

        userButton = new JButton("用户预订");
        shopButton = new JButton("商家预订");
        addButton = new JButton("添加预订");
        recommendButton = new JButton("餐厅推荐");


        add(userButton);
        add(shopButton);
        add(addButton);
        add(recommendButton);

        userButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "您预订的餐馆和时间有：" + getUserBooking());
        });
        shopButton.addActionListener(e -> {
            //用户输入餐厅名字进行查询
            String shopName = JOptionPane.showInputDialog("请输入餐厅名字");
            if (shopName == null) {
                JOptionPane.showMessageDialog(null, "输入为空");
            } else {
                JOptionPane.showMessageDialog(this, "您在该餐馆预定的时间有：" + getUserOrderInShop(shopName));
            }

        });
        addButton.addActionListener(e -> {
            String shopName = JOptionPane.showInputDialog("请输入餐厅名字");
            if (isExist(shopName)) {
                String time = JOptionPane.showInputDialog("请输入预订时间（xx月xx日x餐）");
                if (time == null) {
                    JOptionPane.showMessageDialog(null, "输入为空");
                } else {
                    addOrder(shopName, time);
                    JOptionPane.showMessageDialog(null, "添加预订成功");
                }
            } else {
                JOptionPane.showMessageDialog(null, "该餐馆不存在");
            }
        });
        recommendButton.addActionListener(e -> {
            String shopType = JOptionPane.showInputDialog("请输入餐馆类型(可以为空)");
            String food = JOptionPane.showInputDialog("请输入食物类型(可以为空)");
            String features = JOptionPane.showInputDialog("请输入餐馆特点(可以为空)");
            String sort = JOptionPane.showInputDialog("请选择排序方式(1为按评分排序/2为按距离排序，默认为按评分排序)");

            if (shopType.isEmpty()) shopType = "all";
            if (food.isEmpty()) food = "all";
            if (features.isEmpty()) features = "all";
            if (sort.isEmpty()) sort = "1";


            try {
                List<Shop> shopList = getRecommend(shopType, food, features, sort);
                StringBuilder sb = new StringBuilder();
                for (Shop shop : shopList) {
                    sb.append(shop.toString()).append("\n");
                }

                JTextArea textArea = new JTextArea(sb.toString());
                JScrollPane scrollPane = new JScrollPane(textArea);

                JFrame frame = new JFrame("推荐餐馆");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.getContentPane().add(scrollPane);

                frame.pack();
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        });


    }

    //查询用户的订单
    private List<Order> getUserBooking() {
        return orderMapper.getUserOrder(userInfo.getAccount());
    }

    //查询用户在某餐厅的订单
    private List<Order> getUserOrderInShop(String shopName) {
        return orderMapper.getUserOrderInShop(userInfo.getAccount(), shopName);
    }

    //查询餐厅是否存在
    private boolean isExist(String shopName) {
        return shopMapper.isExist(shopName);
    }

    private void addOrder(String shopName, String time) {
        orderMapper.addOrder(userInfo.getAccount(), shopName, time);
    }

    private List<Shop> getRecommend(String shopType, String food, String features, String sort) throws IOException {
        //得到根据平均分排序餐馆

        List<Shop> shopList = shopMapper.getRecommend(shopType, food, features);
        if (sort.equals("1")) {
            return shopMapper.sortByAvgScore(shopList);
        } else {
            return distanceMapper.sortByDistance(shopList);
        }
    }


}
