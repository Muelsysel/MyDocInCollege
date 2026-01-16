package ShopSystem.SubPanel;

import Mapper.DistanceMapper;
import Mapper.OrderMapper;
import Mapper.ShopMapper;
import POJO.Order;
import POJO.Shop;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class BookInfoPanel extends JPanel {
    OrderMapper orderMapper = new OrderMapper();
    ShopMapper shopMapper = new ShopMapper();
    DistanceMapper distanceMapper = new DistanceMapper();
    private JButton shopSearchButton;
    private JButton userSearchButton;
    private JButton orderHandleButton;
    private Shop shop;

    public BookInfoPanel(String shopName) throws IOException {
        this.shop = getShop(shopName);
        setLayout(new GridLayout(2, 2));
        //加入按钮
        shopSearchButton = new JButton("查询餐馆预订");
        userSearchButton = new JButton("查询用户预订");
        orderHandleButton = new JButton("处理用户预订");

        add(shopSearchButton);
        add(userSearchButton);
        add(orderHandleButton);

        shopSearchButton.addActionListener(e -> {
            //查询餐馆所有预订
            if (showOrder(shop.getShopName()) == null) {
                JOptionPane.showMessageDialog(null, "该餐馆暂无预订");
            } else {
                showAllOrder();
            }

        });

        userSearchButton.addActionListener(e -> {
            //查询某用户在餐厅的预订
            String userAccount = JOptionPane.showInputDialog("请输入要查询的用户账号");
            if (userAccount == null) {
                JOptionPane.showMessageDialog(null, "不能为空！");
                return;
            }
            List<Order> orders = showUserOrder(userAccount);
            JOptionPane.showMessageDialog(null, Objects.requireNonNullElse(orders, "该用户暂无预订或该用户不存在"));
        });
        orderHandleButton.addActionListener(e -> {
            //处理预订信息

            if (showOrder(shop.getShopName()) == null) {
                JOptionPane.showMessageDialog(null, "该餐馆暂无预订");
            } else {
                showAllOrder();
                String number = JOptionPane.showInputDialog("请输入要处理的订单数量");
                if (number == null || Integer.parseInt(number) <= 0) {
                    JOptionPane.showMessageDialog(null, "不能为空或小于0！");
                    return;
                }
                orderHandle(number);
                JOptionPane.showMessageDialog(null, "处理成功！");
            }

        });


    }

    //新建界面展示信息
    //提取出来单独为方法方便调用
    private void showAllOrder() {
        List<Order> orders = showOrderByTime(shop.getShopName());
        StringBuilder sb = new StringBuilder();
        for (Order order : orders) {
            sb.append(order.toString()).append("\n");
        }

        JTextArea textArea = new JTextArea(sb.toString());
        JScrollPane scrollPane = new JScrollPane(textArea);

        JFrame frame = new JFrame("餐厅订单");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(scrollPane);

        frame.pack();
        frame.setVisible(true);
        frame.setSize(400, 800);
        frame.setLocationRelativeTo(null);
    }

    //根据餐厅名查询餐馆信息
    private Shop getShop(String shopName) {
        return shopMapper.findShopByName(shopName);
    }

    //查询餐馆所有预订
    private List<Order> showOrder(String shopName) {
        return orderMapper.showOrder(shopName);
    }

    //查询某用户在餐厅的预订
    private List<Order> showUserOrder(String account) {
        return orderMapper.showUserOrder(account, shop.getShopName());
    }

    //根据时间排序查询餐馆订单
    private List<Order> showOrderByTime(String shopName) {
        return orderMapper.showOrderByTime(shopName);
    }

    //处理预订信息
    private void orderHandle(String number) {
        orderMapper.deleteOrder(number, shop.getShopName());
    }
}
