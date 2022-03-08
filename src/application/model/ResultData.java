package application.model;

import javafx.beans.property.*;

public class ResultData {

    private final SimpleStringProperty type = new SimpleStringProperty();
    private final SimpleIntegerProperty total = new SimpleIntegerProperty();
    private final SimpleStringProperty average = new SimpleStringProperty();

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public int getTotal() {
        return total.get();
    }

    public void setTotal(int total) {
        this.total.set(total);
    }

    public String getAverage() {
        return average.get();
    }

    public void setAverage(String average) {
        this.average.set(average);
    }

    public ResultData(String type, SeekData data) {
        setType(type);
        setTotal(data.getCount());
        setAverage(String.format("%.2f", (data.getCount() / 400.0)));
    }
}
