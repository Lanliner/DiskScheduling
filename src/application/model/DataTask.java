package application.model;

import javafx.concurrent.*;

import java.util.ArrayList;

public class DataTask extends ScheduledService<ArrayList<Integer>> {

    int index = 0;

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    protected Task<ArrayList<Integer>> createTask() {

        Task<ArrayList<Integer>> task = new Task<ArrayList<Integer>>() {
            @Override
            protected ArrayList<Integer> call() {
                ArrayList<Integer> list = new ArrayList<>();
                list.add(Tools.getFCFSData().getRoute().get(index));
                list.add(Tools.getSSTFData().getRoute().get(index));
                list.add(Tools.getLOOKData().getRoute().get(index));
                list.add(Tools.getCSCANPosData().getRoute().get(index));
                list.add(Tools.getCSCANNegData().getRoute().get(index));

                if(index == 399) {
                    this.cancel();
                }else {
                    index++;
                }
                return list;
            }
        };

        return task;

    }

}
