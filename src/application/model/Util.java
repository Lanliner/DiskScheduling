package application.model;

import javafx.animation.FadeTransition;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.util.Optional;

public class Util {

    /**
     * 设置节点锚定
     * @param node 节点
     * @param top 顶间距
     * @param bottom 底间距
     * @param left 左间距
     * @param right 右间距
     */
    public static void setAnchor(Node node, Double top, Double bottom, Double left, Double right) {
        if(top != null) {
            AnchorPane.setTopAnchor(node, top);
        }
        if(bottom != null) {
            AnchorPane.setBottomAnchor(node, bottom);
        }
        if(left != null) {
            AnchorPane.setLeftAnchor(node, left);
        }
        if(right != null) {
            AnchorPane.setRightAnchor(node, right);
        }
    }

    /**
     * 弹出警报窗口
     * @param alertType 警报类型
     * @param title 警报标题
     * @param content 警报内容
     * @param beep 是否发声
     * @return 用户选择（只有警报类型为CONFIRMATION时有意义）
     */
    public static boolean showMessage(Alert.AlertType alertType, String title, String content, boolean beep) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        if(beep) {
            java.awt.Toolkit.getDefaultToolkit().beep();
        }
        Optional<ButtonType> opt = alert.showAndWait();
        if(opt.isPresent() && opt.get().equals(ButtonType.CANCEL)) {
            return false;
        }
        return true;
    }

    public static void playClickAnime(AnchorPane root, MouseEvent event) {
        Circle outerCircle = new Circle(25);
        Circle innerCircle = new Circle(22);
        Shape ring = Shape.subtract(outerCircle, innerCircle);
        ring.setFill(Paint.valueOf("DodgerBlue"));
        root.getChildren().add(ring);
        ring.setLayoutX(event.getSceneX());
        ring.setLayoutY(event.getSceneY());

        ScaleTransition st = new ScaleTransition(Duration.millis(500), ring);
        st.setFromX(0);
        st.setToX(1);
        st.setFromY(0);
        st.setToY(1);
        st.setNode(ring);

        FadeTransition ft = new FadeTransition(Duration.millis(500), ring);
        ft.setFromValue(1);
        ft.setToValue(0);
        ft.setNode(ring);

        ft.setOnFinished(finish -> {
            root.getChildren().remove(ring);
        });

        ft.play();
        st.play();
    }
}
