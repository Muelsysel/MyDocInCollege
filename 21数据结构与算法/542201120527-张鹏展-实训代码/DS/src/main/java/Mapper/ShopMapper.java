package Mapper;

import POJO.Food;
import POJO.Shop;
import util.QuickSortForRecommendByAvgScore;

import java.io.*;
import java.util.*;

public class ShopMapper {
    static ArrayList<Shop> shops = new ArrayList<>();

    //初始化读取shop文件，将数据存入ArrayList shops中
    public ShopMapper() throws FileNotFoundException {
        try (BufferedReader reader = new BufferedReader(new FileReader(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("shop.txt")).getPath()))) {
            String line = null;
            Shop shop = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("shoptype: ")) {
                    if (shop != null) {
                        shops.add(shop);
                    }
                    shop = new Shop();
                    shop.setShopType(line.substring(10));
                } else if (line.startsWith("shopId: ")) {
                    shop.setShopId(line.substring((8)));
                } else if (line.startsWith("shopName: ")) {
                    shop.setShopName(line.substring(10));
                } else if (line.startsWith("shopPassword: ")) {
                    shop.setShopPassword(line.substring(14));
                } else if (line.startsWith("avgScore: ")) {
                    shop.setAvgScore(Double.parseDouble(line.substring(10)));
                } else if (line.startsWith("avePrice: ")) {
                    shop.setAvePrice(Double.parseDouble(line.substring(10)));
                } else if (line.startsWith("address: ")) {
                    shop.setAddress(line.substring(9));
                } else if (line.startsWith("phone: ")) {
                    shop.setPhone(line.substring(7));
                } else if (line.startsWith("food_id: ")) {
                    String[] foodInfo = line.split(", ");
                    String id = foodInfo[0].substring(9);
                    String name = foodInfo[1].substring(11);
                    double price = Double.parseDouble(foodInfo[2].substring(12));
                    Food food = new Food(id, name, price);
                    shop.getFoods().add(food);
                } else if (line.startsWith("Comment")) {
                    int index = Integer.parseInt(line.substring(7, 8));
                    String comment = line.substring(9);
                    shop.getComments().put(index, comment);
                }
            }
            if (shop != null) {
                shops.add(shop);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //哈希表判断账号密码是否对应
    public boolean hashCompare(String name, String password) {
        HashMap<String, Shop> shopHashMap = new HashMap<>();
        for (Shop shop : shops) {
            shopHashMap.put(shop.getShopName(), shop);
        }
        System.out.println("1");
        //判断是否存在并比较密码
        if(shopHashMap.get(name) == null){
            return false;
        }
        return shopHashMap.get(name).getShopPassword().equals(password);
    }

    //根据id查找餐厅，使用哈希表查找算法
    public Shop findShopById(String shopId) {
        HashMap<String, Shop> shopHashMap = new HashMap<>();

        for (Shop shop : shops) {
            shopHashMap.put(shop.getShopId(), shop);
        }
        return shopHashMap.get(shopId) == null ? null : shopHashMap.get(shopId);
    }

    //根据名字判断餐厅是否存在
    public boolean isExist(String shopName) {
        for (Shop shop : shops) {
            if (shop.getShopName().equals(shopName)) {
                return true;
            }
        }
        return false;
    }

    //根据店铺类型、食物、特色查找推荐店铺
    public List<Shop> getRecommend(String shopType, String food, String feature) {
        List<Shop> recommendShop = new ArrayList<>();
        for (Shop shop : shops) {
            boolean isAdded = false; // 添加一个标记变量
            if (shop.getShopType().contains(shopType) || shopType.equals("all")) {
                //匹配食物
                for (Food f : shop.getFoods()) {
                    if (f.getName().contains(food) || food.equals("all")) {
                        if (feature.equals("all")) {
                            recommendShop.add(shop);
                            // 找到符合条件的店铺后，跳出外层循环
                            break;
                        } else {
                            for (String c : shop.getComments().values()) {
                                if (c.contains(feature)) {
                                    recommendShop.add(shop);
                                    isAdded = true;
                                    break;

                                }
                            }
                        }
                    }
                    if (isAdded) {
                        break; // 如果已经添加过，则跳出外层循环
                    }
                }
            }
        }
        return recommendShop;
    }

    //    //使用冒泡排序
//    public List<Shop> sortByAvgScore(List<Shop> recommend) {
//        for (int i = 0; i < recommend.size(); i++) {
//            for (int j = i + 1; j < recommend.size(); j++) {
//                if (recommend.get(i).getAvgScore() < recommend.get(j).getAvgScore()) {
//                    Shop temp = recommend.get(i);
//                    recommend.set(i, recommend.get(j));
//                    recommend.set(j, temp);
//                }
//            }
//        }
//        return recommend;
//    }
    //这里使用快速排序来加快速度，具体实现在工具类QuickSortForRecommend中
    public List<Shop> sortByAvgScore(List<Shop> recommend) {
        QuickSortForRecommendByAvgScore quickSortForRecommendByAvgScore = new QuickSortForRecommendByAvgScore();
        quickSortForRecommendByAvgScore.quickSort(recommend, 0, recommend.size() - 1);
        return recommend;
    }


    // 通过给定的餐馆名称首字母在英文字母表中的序号作为哈希键，在一个哈希表中查找对应的餐馆对象，并返回该对象, 使用链地址法解决冲突问题
    public Shop findShopByName(String shopName) {
        HashMap<Integer, LinkedList<Shop>> shopHashMap = new HashMap<>();

        for (Shop shop : shops) {
            int hashKey = Character.toUpperCase(shop.getShopName().charAt(0)) - 'A' + 1;// 计算餐馆名称首字母在英文字母表中的序号
            LinkedList<Shop> shopList = shopHashMap.getOrDefault(hashKey, new LinkedList<>());// 获取该哈希键对应的链表
            shopList.add(shop); // 将餐馆对象添加到链表中
            shopHashMap.put(hashKey, shopList);// 将该链表添加到哈希表中
        }

        int hashKey = Character.toUpperCase(shopName.charAt(0)) - 'A' + 1;
        LinkedList<Shop> shopList = shopHashMap.get(hashKey);// 获取该哈希键对应的链表
        if (shopList != null) {
            for (Shop shop : shopList) {
                // 如果找到匹配的餐馆名称，则返回该餐馆对象
                if (shop.getShopName().equals(shopName)) {
                    return shop;
                }
            }
        }
        return null;// 如果未找到匹配的餐馆名称，则返回null
    }

    //更新餐馆信息
    public void updateShopInfoFile(Shop shop) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("shop.txt")).getPath()))) {
            for (Shop s : shops) {
                if (s.getShopId().equals(shop.getShopId())) {
                    // 更新原有的 shop
                    s.setShopType(shop.getShopType());
                    s.setShopName(shop.getShopName());
                    s.setShopPassword(shop.getShopPassword());
                    s.setAvgScore(shop.getAvgScore());
                    s.setAvePrice(shop.getAvePrice());
                    s.setAddress(shop.getAddress());
                    s.setPhone(shop.getPhone());
                    s.setFoods(shop.getFoods());
                    s.setComments(shop.getComments());
                }

                // 将修改后的 shop 写入到文件
                writer.write("shoptype: " + s.getShopType() + "\n");
                writer.write("shopId: " + s.getShopId() + "\n");
                writer.write("shopName: " + s.getShopName() + "\n");
                writer.write("shopPassword: " + s.getShopPassword() + "\n");
                writer.write("avgScore: " + s.getAvgScore() + "\n");
                writer.write("avePrice: " + s.getAvePrice() + "\n");
                writer.write("address: " + s.getAddress() + "\n");
                writer.write("phone: " + s.getPhone() + "\n");

                for (Food food : s.getFoods()) {
                    writer.write("food_id: " + food.getId() + ", " + "food_name: " + food.getName() + ", " + "food_price: " + food.getPrice() + "\n");
                }

                for (Integer index : s.getComments().keySet()) {
                    writer.write("Comment" + index + ":" + s.getComments().get(index) + "\n");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //删除餐馆信息
    public void deleteShopInfoFile(Shop shop) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("shop.txt")).getPath()))) {
            for (Shop s : shops) {
                if (s.getShopId().equals(shop.getShopId())) {//将目标 shop 从文件中删除
                    continue;
                }

                // 将修改后的 shop 写入到文件
                writer.write("shoptype: " + s.getShopType() + "\n");
                writer.write("shopId: " + s.getShopId() + "\n");
                writer.write("shopName: " + s.getShopName() + "\n");
                writer.write("shopPassword: " + s.getShopPassword() + "\n");
                writer.write("avgScore: " + s.getAvgScore() + "\n");
                writer.write("avePrice: " + s.getAvePrice() + "\n");
                writer.write("address: " + s.getAddress() + "\n");
                writer.write("phone: " + s.getPhone() + "\n");

                for (Food food : s.getFoods()) {
                    writer.write("food_id: " + food.getId() + ", " + "food_name: " + food.getName() + ", " + "food_price: " + food.getPrice() + "\n");
                }

                for (Integer index : s.getComments().keySet()) {
                    writer.write("Comment" + index + ":" + s.getComments().get(index) + "\n");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}