package application.model;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.util.Duration;

import java.util.*;

public class Tools {

    private static SeekData FCFSData;
    private static SeekData SSTFData;
    private static SeekData LOOKData;
    private static SeekData CSCANPosData;
    private static SeekData CSCANNegData;

    public static SeekData getFCFSData() {
        return FCFSData;
    }

    public static SeekData getSSTFData() {
        return SSTFData;
    }

    public static SeekData getLOOKData() {
        return LOOKData;
    }

    public static SeekData getCSCANPosData() {
        return CSCANPosData;
    }

    public static SeekData getCSCANNegData() {
        return CSCANNegData;
    }

    public static DataTask task;

    public static void setTask(DataTask task) {
        Tools.task = task;
    }

    public static DataTask getTask() {
        return task;
    }

    /**
     * 在折线表绘制折线
     * @param chart 折线表
     * @param series 请求序列
     * @param delay 间隔
     */
    public static void draw(LineChart<Number, Number> chart, ArrayList<Integer> series, int start, int delay) {
        chart.getData().clear();

        XYChart.Series<Number, Number> FCFS = new XYChart.Series<>();
        FCFS.setName("FCFS");
        XYChart.Series<Number, Number> SSTF = new XYChart.Series<>();
        SSTF.setName("SSTF");
        XYChart.Series<Number, Number> LOOK = new XYChart.Series<>();
        LOOK.setName("电梯LOOK");
        XYChart.Series<Number, Number> CSCANPos = new XYChart.Series<>();
        CSCANPos.setName("C-SCAN正向");
        XYChart.Series<Number, Number> CSCANNeg = new XYChart.Series<>();
        CSCANNeg.setName("C-SCAN逆向");

        chart.getData().add(FCFS);
        chart.getData().add(SSTF);
        chart.getData().add(LOOK);
        chart.getData().add(CSCANPos);
        chart.getData().add(CSCANNeg);

        FCFSData = generateSeekData(Type.FCFS, start, series);
        SSTFData = generateSeekData(Type.SSTF, start,series);
        LOOKData = generateSeekData(Type.LOOK, start,series);
        CSCANPosData = generateSeekData(Type.CSCAN_POS, start,series);
        CSCANNegData = generateSeekData(Type.CSCAN_NEG, start,series);

        //初始磁道
        FCFS.getData().add(new XYChart.Data<Number, Number>(0, start));
        SSTF.getData().add(new XYChart.Data<Number, Number>(0, start));
        LOOK.getData().add(new XYChart.Data<Number, Number>(0, start));
        CSCANPos.getData().add(new XYChart.Data<Number, Number>(0, start));
        CSCANNeg.getData().add(new XYChart.Data<Number, Number>(0, start));

        task.setDelay(Duration.seconds(1));
        task.setPeriod(Duration.millis(delay));

        task.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                //各项新值
                int valueFCFS = newValue.get(0);
                int valueSSTF = newValue.get(1);
                int valueLOOK = newValue.get(2);
                int valueCSCANPos = newValue.get(3);
                int valueCSCANNeg = newValue.get(4);

                int index = task.getIndex();    //当前插入路径索引

                FCFS.getData().add(new XYChart.Data<Number, Number>(index, valueFCFS));
                SSTF.getData().add(new XYChart.Data<Number, Number>(index, valueSSTF));
                LOOK.getData().add(new XYChart.Data<Number, Number>(index, valueLOOK));
                CSCANPos.getData().add(new XYChart.Data<Number, Number>(index, valueCSCANPos));
                CSCANNeg.getData().add(new XYChart.Data<Number, Number>(index, valueCSCANNeg));
            }
        });

        task.start();

    }

    /**
     * 生成请求序列
     * @return 请求序列的ArrayList
     */
    public static ArrayList<Integer> generateSeries() {
        ArrayList<Integer> series = new ArrayList<>(400);

        Random rand = new Random();

        for(int i = 0; i < 200; i++) {
            int next;
            do {
                next = rand.nextInt(500);
            }while(!series.isEmpty() && next == series.get(series.size() - 1));     //避免产生两个连续的同磁道请求
            series.add(next);
        }
        for(int i = 0; i < 100; i++) {
            int next;
            do {
                next = 500 + rand.nextInt(500);
            }while(!series.isEmpty() && next == series.get(series.size() - 1));
            series.add(next);
        }
        for(int i = 0; i < 100; i++) {
            int next;
            do {
                next = 1000 + rand.nextInt(500);
            }while(!series.isEmpty() && next == series.get(series.size() - 1));
            series.add(next);
        }

        Collections.shuffle(series);
        return series;
    }

    /**
     * 根据给定的寻道算法生成寻道结果
     * @param series 请求序列
     * @param start 起始编号
     * @param type 寻道算法
     * @return 寻道结果
     */
    private static SeekData generateSeekData(Type type, int start, ArrayList<Integer> series) {
        SeekData data = new SeekData();

        switch (type) {
            case FCFS:
                data.getRoute().add(series.get(0));
                for (int target : series) {
                    data.getRoute().add(target);
                    data.addCount(Math.abs(target - start));
                    start = target;
                }
                break;

            case SSTF:
                ArrayList<Integer> rec = new ArrayList<>(series);
                for(int i = 0; i < series.size(); i++) {
                    //在rec中寻找下个请求
                    int next = 0;   //下一次磁道下标
                    int min = Integer.MAX_VALUE;     //最短路程
                    for(int j = 0; j < rec.size(); j++) {
                        int length = Math.abs(rec.get(j) - start);
                        if(length < min) {
                            next = j;
                            min = length;
                        }
                    }
                    //移动至下一磁道
                    int target = rec.remove(next);
                    data.getRoute().add(target);
                    data.addCount(Math.abs(target - start));
                    start = target;
                }
                break;

            case LOOK:
                ArrayList<Integer> temp = new ArrayList<>(series);
                temp.sort(Comparator.naturalOrder());   //排序
                //起始移动方向设为距离起始磁道最近磁道的方向
                int next = 0;   //下一次磁道下标
                int min = Integer.MAX_VALUE;
                for(int i = 0; i < temp.size(); i++) {
                    int length = Math.abs(temp.get(i) - start);
                    if(length < min) {
                        next = i;
                        min = length;
                    }
                }
                boolean isPositive = next - start > 0;
                //移动至下一磁道
                int target = temp.get(next);
                data.getRoute().add(target);
                data.addCount(Math.abs(target - start));
                start = target;

                //以next为界（不含）分为两部分
                List<Integer> negList = null;
                if(next > 0) {
                    negList = temp.subList(0, next);
                }
                List<Integer> posList = null;
                if(next < temp.size() - 1) {
                    posList = temp.subList(next + 1, temp.size());
                }

                //先遍历起始方向部分，再遍历反方向部分
                if(isPositive) {
                    //后段遍历
                    if(posList != null) {
                        for(int tar : posList) {
                            data.getRoute().add(tar);
                            data.addCount(tar - start);
                            start = tar;
                        }
                    }
                    //前段遍历
                    if(negList != null) {
                        Collections.reverse(negList);
                        for(int tar : negList) {
                            data.getRoute().add(tar);
                            data.addCount(Math.abs(tar - start));
                            start = tar;
                        }
                    }
                }else {
                    //前段遍历
                    if(negList != null) {
                        Collections.reverse(negList);
                        for(int tar : negList) {
                            data.getRoute().add(tar);
                            data.addCount(Math.abs(tar - start));
                            start = tar;
                        }
                    }
                    //后段遍历
                    if(posList != null) {
                        for(int tar : posList) {
                            data.getRoute().add(tar);
                            data.addCount(tar - start);
                            start = tar;
                        }
                    }
                }

                break;

            case CSCAN_POS:
            case CSCAN_NEG:
                ArrayList<Integer> tempCS = new ArrayList<>(series);
                tempCS.sort(Comparator.naturalOrder());   //排序
                int nextCS;   //下一次磁道下标
                if(type == Type.CSCAN_POS) {
                    nextCS = 0;
                    for (int i = 0; i < tempCS.size(); i++) {
                        if (tempCS.get(i) >= start) {
                            nextCS = i;
                            break;
                        }
                    }
                } else {
                    nextCS = tempCS.size() - 1;
                    for (int i = tempCS.size() - 1; i >= 0; i--) {
                        if (tempCS.get(i) <= start) {
                            nextCS = i;
                            break;
                        }
                    }
                }
                //移动至下一磁道
                int targetCS = tempCS.get(nextCS);
                data.getRoute().add(targetCS);
                data.addCount(Math.abs(targetCS - start));
                start = targetCS;

                //以nextCS为界（不含）分为两部分
                List<Integer> negListCS = null;
                if(nextCS > 0) {
                    negListCS = tempCS.subList(0, nextCS);
                }
                List<Integer> posListCS = null;
                if(nextCS < tempCS.size() - 1) {
                    posListCS = tempCS.subList(nextCS + 1, tempCS.size());
                }

                //先遍历起始方向部分，再遍历反方向部分
                if(type == Type.CSCAN_POS) {
                    //后段遍历
                    if(posListCS != null) {
                        for(int tar : posListCS) {
                            data.getRoute().add(tar);
                            data.addCount(Math.abs(tar - start));
                            start = tar;
                        }
                    }
                    //前段遍历
                    if(negListCS != null) {
                        for(int tar : negListCS) {
                            data.getRoute().add(tar);
                            data.addCount(Math.abs(tar - start));
                            start = tar;
                        }
                    }
                }else {
                    //前段遍历
                    if(negListCS != null) {
                        Collections.reverse(negListCS);
                        for(int tar : negListCS) {
                            data.getRoute().add(tar);
                            data.addCount(Math.abs(tar - start));
                            start = tar;
                        }
                    }
                    //后段遍历
                    if(posListCS != null) {
                        Collections.reverse(posListCS);
                        for(int tar : posListCS) {
                            data.getRoute().add(tar);
                            data.addCount(Math.abs(tar - start));
                            start = tar;
                        }
                    }
                }

        }

        return data;
    }

}
