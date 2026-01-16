package util;

import POJO.Order;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

//继承Comparator，实现自定义的比较器
public class OrderComparator implements Comparator<Order> {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM月d日");

    @Override
    public int compare(Order order1, Order order2) {
        try {
            Date date1 = dateFormat.parse(order1.getTime());
            Date date2 = dateFormat.parse(order2.getTime());
            int result = date1.compareTo(date2);
            if (result == 0) {//日期相同对比餐次
                int meal1 = getMealNumber(order1.getTime());
                int meal2 = getMealNumber(order2.getTime());
                return Integer.compare(meal1, meal2);
            } else {
                return result;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int getMealNumber(String time) {
        String meal = time.substring(time.length() - 1);
        switch (meal) {
            case "早":
                return 0;
            case "午":
                return 1;
            case "晚":
                return 2;
            default:
                return -1;
        }
    }
}
