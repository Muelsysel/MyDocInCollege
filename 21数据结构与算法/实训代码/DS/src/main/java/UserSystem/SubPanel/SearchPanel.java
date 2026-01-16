package UserSystem.SubPanel;

import Mapper.DistanceMapper;
import Mapper.ShopMapper;
import POJO.Shop;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SearchPanel extends JPanel {
    ShopMapper shopMapper = new ShopMapper();
    private JLabel searchLabel;
    private JTextField searchField;
    private JButton searchButton;
    DistanceMapper distanceMapper = new DistanceMapper();

    public SearchPanel() throws IOException {
        // 设置面板的布局为BorderLayout
        setLayout(new BorderLayout());

        // 创建搜索框和查询按钮
        searchLabel = new JLabel("根据餐厅id查询:");
        searchField = new JTextField(20);
        searchButton = new JButton("查询");

        // 创建一个面板用于水平居中搜索框和查询按钮
        JPanel centerPanel = new JPanel();
        centerPanel.add(searchLabel);
        centerPanel.add(searchField);
        centerPanel.add(searchButton);

        // 将centerPanel添加到面板的中间位置
        add(centerPanel, BorderLayout.CENTER);

        // 添加查询按钮的点击事件监听
        searchButton.addActionListener(e -> {
            String id = searchField.getText();
            if (id.isEmpty()) {// 输入为空
                JOptionPane.showMessageDialog(this, "请输入餐厅id",
                        "查询餐厅", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            try {
                // 获取id
                Shop shop = shopMapper.findShopById(id);

                if (shop != null) {

                    double distance = distanceMapper.findShortestDistance(id);
                    JOptionPane.showMessageDialog(this, "餐厅信息：" + shop + "\n最短距离是：" + distance,
                            "查询餐厅", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "没有找到该餐厅",
                            "查询餐厅", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}
