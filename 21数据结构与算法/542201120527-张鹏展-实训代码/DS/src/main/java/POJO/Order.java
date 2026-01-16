package POJO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    String account;
    String shopName;
    String time;

    @Override
    public String toString() {
        return "\n用户：" + account + "餐厅名:" + shopName + "，时间：" + time;
    }
}
