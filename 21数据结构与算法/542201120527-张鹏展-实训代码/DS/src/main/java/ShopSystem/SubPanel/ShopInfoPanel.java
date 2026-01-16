package ShopSystem.SubPanel;

import Mapper.ShopMapper;
import POJO.Food;
import POJO.Shop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import static java.lang.System.exit;

public class ShopInfoPanel extends JPanel {
    ShopMapper shopMapper = new ShopMapper();
    private JTextField TypeField;
    private JTextField NameField;
    private JTextField AvePriceField;
    private JTextField AvgScoreField;
    private JTextField AddressField;
    private JTextField PhoneField;
    private JTextField foodsField;
    private JButton viewButton;
    private JButton modifyButton;
    private JButton deleteButton;
    private Shop shop;

    public ShopInfoPanel(String shopName) throws FileNotFoundException {
        //初始化对象
        this.shop = getShop(shopName);
        initUI();
    }


    //初始化ui
    private void initUI() {
        setLayout(new BorderLayout());
        //生成面板
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(8, 2, 10, 5)); // 调整行间距
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        //生成标签
        JLabel IdLabel = new JLabel("id:");
        JLabel idLabel = new JLabel(shop.getShopId());
        JLabel TypeLabel = new JLabel("餐厅类型：");
        TypeField = new JTextField(shop.getShopType());
        JLabel NameLabel = new JLabel("餐厅名称：");
        NameField = new JTextField(shop.getShopName());
        JLabel AvePriceLabel = new JLabel("平均消费：");
        AvePriceField = new JTextField(String.valueOf(shop.getAvePrice()));
        JLabel AvgScoreLabel = new JLabel("平均评分：");
        AvgScoreField = new JTextField(String.valueOf(shop.getAvgScore()));
        JLabel AddressLabel = new JLabel("地址：");
        AddressField = new JTextField(shop.getAddress());
        JLabel PhoneLabel = new JLabel("联系方式：");
        PhoneField = new JTextField(shop.getPhone());
        JLabel foodsLabel = new JLabel("菜品：");
        foodsField = new JTextField(shop.getFoods().toString());
        //加入水平居中
        idLabel.setHorizontalAlignment(SwingConstants.CENTER);
        TypeField.setHorizontalAlignment(SwingConstants.CENTER);
        NameField.setHorizontalAlignment(SwingConstants.CENTER);
        AvePriceField.setHorizontalAlignment(SwingConstants.CENTER);
        AvgScoreField.setHorizontalAlignment(SwingConstants.CENTER);
        AddressField.setHorizontalAlignment(SwingConstants.CENTER);
        PhoneField.setHorizontalAlignment(SwingConstants.CENTER);
        foodsField.setHorizontalAlignment(SwingConstants.CENTER);
        //加入面板
        infoPanel.add(IdLabel);
        infoPanel.add(idLabel);
        infoPanel.add(TypeLabel);
        infoPanel.add(TypeField);
        infoPanel.add(NameLabel);
        infoPanel.add(NameField);
        infoPanel.add(AvePriceLabel);
        infoPanel.add(AvePriceField);
        infoPanel.add(AvgScoreLabel);
        infoPanel.add(AvgScoreField);
        infoPanel.add(AddressLabel);
        infoPanel.add(AddressField);
        infoPanel.add(PhoneLabel);
        infoPanel.add(PhoneField);
        infoPanel.add(foodsLabel);
        infoPanel.add(foodsField);

        //生成按钮
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        viewButton = new JButton("查询");
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
                // 查看
                viewShopInfo();
            }
        });

        modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 修改
                try {
                    modifyShopInfo();
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 注销
                deleteShopInfo();
                exit(0);//注销后关闭程序
            }
        });
    }

    private void viewShopInfo() {
        // 显示个人信息
        JOptionPane.showMessageDialog(this, "餐厅信息：" + shop.toString(), "提示", JOptionPane.INFORMATION_MESSAGE);
    }

    //修改商户信息
    private void modifyShopInfo() throws FileNotFoundException {
        shop.setShopName(NameField.getText());
        shop.setPhone(PhoneField.getText());

        //shop.setFoods(foodsField.getText());
        //需要将字符串转化为数组
        //更新食物信息
        String foodsStr = foodsField.getText();
        ArrayList<Food> newFoods = new ArrayList<>();
        String[] foodStrings = foodsStr.split(",");//分割
        int i = 0;
        String id = "";
        String name = "";
        String price = "";

        for (String foodStr : foodStrings) {
            int type = i % 3; // 0, 1, 2

            // 获取id, name, price
            switch (type) {
                case 0:
                    id = foodStr.substring(foodStr.indexOf("id=") + 3);
                    break;
                case 1:
                    name = foodStr.substring(foodStr.indexOf("name=") + 5);
                    break;
                case 2:
                    price = foodStr.substring(foodStr.indexOf("price=") + 6, foodStr.indexOf(")"));
                    // 在这里创建Food对象并添加到newFoods列表
                    newFoods.add(new Food(id, name, Double.parseDouble(price)));
                    break;
            }

            i++;
        }
        shop.setFoods(newFoods);


        shop.setAvePrice(Double.parseDouble(AvePriceField.getText()));
        shop.setAvgScore(Double.parseDouble(AvgScoreField.getText()));
        shop.setShopType(TypeField.getText());
        shop.setAddress(AddressField.getText());
        // 更新商户信息到文件
        shopMapper.updateShopInfoFile(shop);
        JOptionPane.showMessageDialog(this, "信息修改成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteShopInfo() {
        // 注销个人信息
        shopMapper.deleteShopInfoFile(shop);
        JOptionPane.showMessageDialog(this, "注销成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
    }


    //根据名字查找商户
    private Shop getShop(String shopName) {
        return shopMapper.findShopByName(shopName);
    }

}
