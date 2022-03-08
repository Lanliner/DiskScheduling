package application.model;

import java.util.ArrayList;

public class SeekData {

    private final ArrayList<Integer> route = new ArrayList<>();   //磁头移动路径
    private int count = 0;  //磁头移动道数

    public ArrayList<Integer> getRoute() {
        return route;
    }

    public int getCount() {
        return count;
    }

    public void addCount(int count) {
        this.count += count;
    }

}
