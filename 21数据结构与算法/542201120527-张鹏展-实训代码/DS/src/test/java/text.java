import POJO.Shop;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;


public class text {
    private List<Connection> connections;

    public text() throws IOException {
        connections = readFile();
    }

    public static void main(String[] args) {


    }

    // 读取文件
    private List<Connection> readFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("distance.txt")).getPath()))) {
            List<Connection> newConnection = new ArrayList<>();
            String line;
            Pattern pattern = Pattern.compile("\\s+");
            reader.readLine();//跳过首行
            while ((line = reader.readLine()) != null) {

                String[] info = pattern.split(line);

                String fromId = info[0];
                String fromName = info[1];
                String toId = info[2];
                String toName = info[3];
                String distanceStr = info[4];

                try {
                    double distance = Double.parseDouble(distanceStr);
                    newConnection.add(new Connection(fromId, toId, distance));
                } catch (NumberFormatException e) {
                    // 跳过错误数据
                }

            }
            return newConnection;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public double findShortestDistance(String endId) throws IOException {
        String startId = "512324"; // 起点ID
        Map<String, Double> shortestDistances = new HashMap<>(); // 存储每个节点到起点的最短距离
        PriorityQueue<String> queue = new PriorityQueue<>(Comparator.comparingDouble(shortestDistances::get)); // 优先队列，按照节点到起点的最短距离排序
        shortestDistances.put(startId, 0.0); // 起点到自身的距离为0
        queue.add(startId); // 将起点加入队列

        while (!queue.isEmpty()) {
            String currentId = queue.poll(); // 取出距离起点最近的节点

            if (currentId.equals(endId)) { // 搜索到终点
                System.out.println("距离" + endId + "为" + shortestDistances.get(endId));
                return shortestDistances.get(endId); // 返回终点到起点的最短距离
            }

            for (Connection connection : connections) { // 遍历当前节点的所有连接
                if (connection.from.equals(currentId)) {
                    double newDistance = shortestDistances.get(currentId) + connection.distance; // 计算通过当前节点到达相邻节点的距离
                    if (!shortestDistances.containsKey(connection.to) || newDistance < shortestDistances.get(connection.to)) { // 如果该相邻节点未被访问过，或者通过当前节点到达该节点的距离更短
                        shortestDistances.put(connection.to, newDistance); // 更新该相邻节点到起点的最短距离
                        queue.add(connection.to); // 将该节点加入队列
                    }
                }
            }
        }
        return 100; // 如果找不到最短距离，返回100
    }

    //根据距离排序，使用冒泡排序
    public List<Shop> sortByDistance(List<Shop> recommend) throws IOException {
        //新建一个哈希表来存储推荐列表里各个商家的距离
        HashMap<String, Double> distanceMap = new HashMap<>();
        for (Shop shop : recommend) {
            distanceMap.put(shop.getShopId(), findShortestDistance(shop.getShopId()));
        }
        //冒泡排序
        for (int i = 0; i < recommend.size(); i++) {
            for (int j = 0; j < recommend.size() - 1; j++) {
                if (distanceMap.get(recommend.get(j).getShopId()) > distanceMap.get(recommend.get(j + 1).getShopId())) {
                    Shop temp = recommend.get(j);
                    recommend.set(j, recommend.get(j + 1));
                    recommend.set(j + 1, temp);
                }
            }
        }
        return recommend;
    }

    @Data
    @AllArgsConstructor
    static
    class Connection {
        private String from;
        private String to;
        private double distance;
    }

}


