package application.controller;

import application.Menu;
import application.model.DataTask;
import application.model.Tools;
import application.model.Util;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private Stage stage;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        slider.setValue(1);

        initChart();

        doneLabel.setVisible(false);
        checkSeriesButton.setDisable(true);
        resultButton.setDisable(true);
        startButton.setDisable(true);

        root.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            Util.playClickAnime(root, event);
        });
    }

    @FXML
    private AnchorPane root;

    /************************
     * 操作区                *
     ***********************/
    @FXML
    private Button generateSeriesButton;

    @FXML
    private Label doneLabel;

    @FXML
    private Button checkSeriesButton;

    @FXML
    private Button resultButton;

    @FXML
    private void generateSeriesAction(ActionEvent event) {
        series = Tools.generateSeries();

        new Thread(() -> {
            Platform.runLater(() -> {
                doneLabel.setVisible(true);
            });
            try {
                Thread.sleep(1500);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                doneLabel.setVisible(false);
            });
        }).start();

        checkSeriesButton.setDisable(false);
        resultButton.setDisable(true);
        startButton.setDisable(false);
    }

    @FXML
    private void checkSeriesAction(ActionEvent event) throws Exception {
        Menu.initSeriesPage(new Stage(), series);
    }

    @FXML
    private void resultAction(ActionEvent event) throws Exception {
        Menu.initResultPage(new Stage());
    }

    @FXML
    private TextField startText;

    @FXML
    private Button startButton;

    @FXML
    private void startAction(ActionEvent event) {
        String text = startText.getText().trim();
        int start = 0;
        boolean isValid = false;
        if(!text.isEmpty()) {
            try {
                start = Integer.parseInt(text);
                if(start >= 0 && start < 1500) {
                    isValid = true;
                }
            }catch (NumberFormatException ignored) {
            }

        }

        if(isValid) {
            if(Tools.getTask() != null && Tools.getTask().isRunning()) {
                Tools.getTask().cancel();
            }
            Tools.setTask(new DataTask());

            Tools.draw(lineChart, series, start, (int) (slider.getValue() * 50));

            updateVision();

            resultButton.setDisable(false);
        }else {
            Util.showMessage(Alert.AlertType.WARNING, "警告", "请输入0-1499内的有效值", true);
        }
    }


    /************************
     * 显示按钮区             *
     ***********************/
    @FXML
    private Slider slider;

    @FXML
    private RadioButton FCFSButton;

    @FXML
    private RadioButton SSTFButton;

    @FXML
    private RadioButton LOOKButton;

    @FXML
    private RadioButton CSCANPosButton;

    @FXML
    private RadioButton CSCANNegButton;

    @FXML
    private void FCFSAction(ActionEvent event) {
        updateVision();
    }

    @FXML
    private void SSTFAction(ActionEvent event) {
        updateVision();
    }

    @FXML
    private void LOOKAction(ActionEvent event) {
        updateVision();
    }

    @FXML
    private void CSCANPosAction(ActionEvent event) {
        updateVision();
    }

    @FXML
    private void CSCANNegAction(ActionEvent event) {
        updateVision();
    }

    private void updateVision() {
        lineChart.setStyle(
                "CHART_COLOR_1: " + (FCFSButton.isSelected() ? "orange" : "transparent") + ";" +
                "CHART_COLOR_2: " + (SSTFButton.isSelected() ? "green" : "transparent") + ";" +
                "CHART_COLOR_3: " + (LOOKButton.isSelected() ? "purple" : "transparent") + ";" +
                "CHART_COLOR_4: " + (CSCANPosButton.isSelected() ? "blue" : "transparent") + ";" +
                "CHART_COLOR_5: " + (CSCANNegButton.isSelected() ? "red" : "transparent")
        );
    }

    /************************
     * 图表区                *
     ***********************/
    @FXML
    private AnchorPane chartPane;

    private LineChart<Number, Number> lineChart;

    private ArrayList<Integer> series;

    private void initChart() {
        NumberAxis XAxis = new NumberAxis("请求", 0, 410 , 10);
        NumberAxis YAxis = new NumberAxis("磁道", 0, 1500 , 100);

        lineChart = new LineChart<Number, Number>(XAxis, YAxis);
        chartPane.getChildren().add(lineChart);
        Util.setAnchor(lineChart, 10.0, 0.0, 0.0, 10.0);
    }
}
