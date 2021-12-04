package com.regisjesuit.purepursuitwidget.widgets;

import com.regisjesuit.purepursuitwidget.data.PurePursuitData;
import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.ParametrizedController;
import edu.wpi.first.shuffleboard.api.widget.SimpleAnnotatedWidget;
import java.util.Arrays;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

@Description(name = "PurePursuitWidget", dataTypes = PurePursuitData.class)
@ParametrizedController("PurePursuitWidget.fxml")
public class PurePursuitWidget extends SimpleAnnotatedWidget<PurePursuitData> {

    private Canvas pathLayer;
    private Canvas robotLayer;
    @FXML
    private StackPane root;

    @FXML
    private void initialize() {
        pathLayer = new Canvas(651 + 10, 315 + 10);
        robotLayer = new Canvas(651 + 10, 315 + 10);
        Pane canvasStacker = new Pane(pathLayer, robotLayer);
        root.getChildren().add(canvasStacker);
        dataOrDefault.addListener((observable, oldValue, newValue) -> {
            if (!Arrays.equals(oldValue.getXValues(), newValue.getXValues()) || !Arrays.equals(
                oldValue.getYValues(), newValue.getYValues())) {
                drawPoints();
            }
            drawRobot();
        });
    }

    private void drawRobot() {
        GraphicsContext gc = robotLayer.getGraphicsContext2D();
        gc.clearRect(0, 0, robotLayer.getWidth(), robotLayer.getHeight());

        double robotXTranslated = transX(dataOrDefault.get().getRobotX());
        double robotYTranslated = transY(dataOrDefault.get().getRobotY());
        double lookaheadDistanceTranslated = translatePathPoint(dataOrDefault.get().getLookaheadDistance());
        gc.setStroke(Color.BLUE);
        gc.fillOval(robotXTranslated - 5, robotYTranslated - 5, 10, 10);

        gc.setStroke(Color.RED);
        gc.strokeOval(robotXTranslated - lookaheadDistanceTranslated / 2,
            robotYTranslated - lookaheadDistanceTranslated / 2,
            lookaheadDistanceTranslated,
            lookaheadDistanceTranslated);
    }

    private void drawPoints() {
        double[] xPoints = dataOrDefault.get().getXValues();
        double[] yPoints = dataOrDefault.get().getYValues();

        GraphicsContext gc = pathLayer.getGraphicsContext2D();
        gc.clearRect(0, 0, pathLayer.getWidth(), pathLayer.getHeight());

        gc.setLineWidth(2.0);
        if (xPoints.length == 0 || xPoints.length != yPoints.length) {
            return;
        }
        for (int i = 0; i < xPoints.length - 1; i++) {
            gc.moveTo(transX(xPoints[i]), transY(yPoints[i]));
            gc.lineTo(transX(xPoints[i + 1]), transY(yPoints[i + 1]));
            gc.stroke();
        }
    }

    @Override
    public Pane getView() {
        return root;
    }

    private double transX(double x) {
        return translatePathPoint(x) + 10;
    }

    private double transY(double y) {
        return 315 - translatePathPoint(y);
    }

    private double translatePathPoint(double point) {
        return point * 39.37008;
    }


}
