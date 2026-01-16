package util;

import POJO.Shop;

import java.util.List;

//实现对推荐店铺的快速排序，根据平均分进行排序
public class QuickSortForRecommendByAvgScore {
    public void quickSort(List<Shop> recommend, int low, int high) {
        if (low < high) {
            // 将数组划分为两部分，并获取划分点的索引
            int pivotIndex = partition(recommend, low, high);

            // 递归地对划分后的两部分进行快速排序
            quickSort(recommend, low, pivotIndex - 1);
            quickSort(recommend, pivotIndex + 1, high);
        }
    }

    private int partition(List<Shop> recommend, int low, int high) {
        // 选择最后一个元素作为基准值
        double pivot = recommend.get(high).getAvgScore();

        // 初始化指针 i，指向小于等于基准值的区域的最后一个元素
        int i = low - 1;

        // 遍历数组，将小于等于基准值的元素交换到区域的前部分
        for (int j = low; j < high; j++) {
            if (recommend.get(j).getAvgScore() >= pivot) {
                i++;
                swap(recommend, i, j);
            }
        }

        // 将基准值放置到正确的位置上
        swap(recommend, i + 1, high);

        // 返回基准值的索引
        return i + 1;
    }

    private void swap(List<Shop> recommend, int i, int j) {
        // 交换两个元素的位置
        Shop temp = recommend.get(i);
        recommend.set(i, recommend.get(j));
        recommend.set(j, temp);
    }
}

