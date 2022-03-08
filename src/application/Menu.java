package application;

import application.controller.MainController;

import application.model.Util;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;

public class Menu {

    public static void initMainPage(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Menu.class.getResource("/application/view/MainPage.fxml"));
        Parent root = loader.load();

        stage.initStyle(StageStyle.DECORATED);
        stage.setMinWidth(1280);
        stage.setMinHeight(800);
        stage.setTitle("磁盘调度算法模拟程序");
        stage.setScene(new Scene(root));

        MainController controller = loader.getController();
        controller.setStage(stage);

        stage.show();
    }

    public static void initSeriesPage(Stage stage, ArrayList<Integer> series) throws Exception {
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("请求序列");
        stage.setWidth(440);
        stage.setHeight(800);
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);

        AnchorPane ap = new AnchorPane();
        stage.setScene(new Scene(ap));

        TextArea ta = new TextArea();
        ap.getChildren().add(ta);
        Util.setAnchor(ta, 10.0, 10.0, 10.0, 10.0);
        StringBuilder text = new StringBuilder();
        for(int i = 0; i < series.size(); i++) {
            text.append(String.format("%4d\t\t", series.get(i)));
            if((i + 1) % 5 == 0) {
                text.append("\n");
            }
        }
        ta.setText(text.toString());
        ta.setEditable(false);

        stage.show();
    }

    public static void initResultPage(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Menu.class.getResource("/application/view/ResultPage.fxml"));
        Parent root = loader.load();

        stage.initStyle(StageStyle.UNIFIED);
        stage.setResizable(false);
        stage.setTitle("结果分析");
        stage.setScene(new Scene(root));

        stage.show();
    }

}
