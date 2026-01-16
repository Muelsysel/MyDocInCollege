package POJO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Map;

@Data
@AllArgsConstructor
public class Shop {
    private String shopType;
    private String shopId;
    private String shopName;
    private String shopPassword;
    private double avgScore;
    private double avePrice;
    private String address;
    private String phone;
    private ArrayList<Food> foods;
    private Map<Integer, String> comments;

    public Shop() {
        this.foods = new ArrayList<>();
        this.comments = new java.util.HashMap<>();
    }

    @Override
    public String toString() {
        return "\n餐厅类型：'" + shopType + '\'' + "\n" +
                "餐厅id=：" + shopId + '\'' + "\n" +
                "餐厅名称：'" + shopName + '\'' + "\n" +
                "平均评分：" + avgScore + "\n" +
                "平均消费：" + avePrice + "\n" +
                "地址=：" + address + '\'' + "\n" +
                "电话=：" + phone + '\'' + "\n";
    }
}
