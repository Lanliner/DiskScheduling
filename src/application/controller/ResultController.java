package application.controller;

import application.model.ResultData;
import application.model.Tools;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class ResultController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createTable();
    }

    @FXML
    private TableView<ResultData> table;

    @FXML
    private TableColumn<ResultData, String> typeColumn;

    @FXML
    private TableColumn<ResultData, Integer> totalColumn;

    @FXML
    private TableColumn<ResultData, String> averageColumn;

    private void createTable() {
        table.getItems().add(new ResultData("FCFS", Tools.getFCFSData()));
        table.getItems().add(new ResultData("SSTF", Tools.getSSTFData()));
        table.getItems().add(new ResultData("电梯LOOK", Tools.getLOOKData()));
        table.getItems().add(new ResultData("C-SCAN（磁道号递增方向）", Tools.getCSCANPosData()));
        table.getItems().add(new ResultData("C-SCAN（磁道号递减方向）", Tools.getCSCANNegData()));

        typeColumn.setCellValueFactory(new PropertyValueFactory<ResultData,String>("type"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<ResultData,Integer>("total"));
        averageColumn.setCellValueFactory(new PropertyValueFactory<ResultData,String>("average"));
    }

}
