package Mapper;

import POJO.Order;
import util.OrderComparator;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class OrderMapper {
    List<Order> orders = new ArrayList<>();//存储订单列表

    public OrderMapper(){
        try (BufferedReader reader = new BufferedReader(new FileReader(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("order.txt")).getPath()))) {
            String line;
            //跳过首行
            reader.readLine();
            Pattern pattern = Pattern.compile("\\s+");
            //逐行读取
            while ((line = reader.readLine()) != null) {
                String[] info = pattern.split(line);
                if (info.length == 3) {
                    //将每行数据封装成Order对象
                    Order order = new Order();
                    order.setAccount(info[0]);
                    order.setShopName(info[1]);
                    order.setTime(info[2]);
                    orders.add(order);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //查询用户所有订单
    public List<Order> getUserOrder(String account) {
        List<Order> userOrders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getAccount().equals(account)) {
                userOrders.add(order);

            }
        }
        return userOrders;
    }

    //查询用户在某个商户的订单
    public List<Order> getUserOrderInShop(String account, String shopName) {
        List<Order> userOrderInShop = new ArrayList<>();
        for (Order order : orders) {
            if (order.getAccount().equals(account) && order.getShopName().equals(shopName)) {//判断订单是否属于该商户
                userOrderInShop.add(order);
            }
        }
        return userOrderInShop;
    }

    //添加订单
    public void addOrder(String account, String shopName, String time) {
        orders.add(new Order(account, shopName, time));
        writeToFile(orders);
    }

    //将订单信息写入文件
    private void writeToFile(List<Order> orders) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("order.txt")).getPath()))) {
            writer.write("用户账号 \t    商家名称\t\t           预定时间");
            writer.newLine();
            for (Order order : orders) {
                writer.write(order.getAccount() + "\t" + order.getShopName() + "\t" + order.getTime());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //查询餐厅的订单
    public List<Order> showOrder(String shopName) {
        List<Order> shopOrders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getShopName().equals(shopName)) {
                shopOrders.add(order);
            }
        }
        return shopOrders;
    }

    //查询用户在某个商户的订单
    public List<Order> showUserOrder(String account, String shopName) {
        List<Order> shopOrders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getShopName().equals(shopName) && order.getAccount().equals(account)) {
                shopOrders.add(order);
            }
        }
        return shopOrders;

    }

    //根据时间顺序排列订单
    public List<Order> showOrderByTime(String shopName) {
        List<Order> shopOrders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getShopName().equals(shopName)) {
                shopOrders.add(order);
            }
        }
        //使用自己实现的工具类OrderComparator排序
        shopOrders.sort(new OrderComparator());
        return shopOrders;
    }

    //删除对应数量订单
    public void deleteOrder(String number, String shopName) {
        int n = Integer.parseInt(number);
        List<Order> shopOrders = showOrderByTime(shopName);//获取订单
        if (shopOrders.size() < n) {//如果删除的数量大于订单的数量，则删除全部
            n = shopOrders.size();
        }
        //这里使用迭代器进行删除，否则会产生ConcurrentModificationException异常
        Iterator<Order> iterator = orders.iterator(); // 获取迭代器
        int i = 0;
        while (iterator.hasNext()) { // 遍历集合
            Order order = iterator.next(); // 获取当前元素
            if (i < n && order.getShopName().equals(shopName)) {
                iterator.remove(); // 使用迭代器的remove方法删除元素
                i++;
            }
            if (i >= n) {
                break; // 删除完成后退出循环
            }
        }
        writeToFile(orders);//将删除后的订单写入文件
    }

}
