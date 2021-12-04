package com.regisjesuit.purepursuitwidget.data;

import edu.wpi.first.shuffleboard.api.data.ComplexData;
import java.util.Map;
import lombok.Getter;

public class PurePursuitData extends ComplexData<PurePursuitData> {
    @Getter
    private final double[] xValues;
    @Getter
    private final double[] yValues;
    @Getter
    private final double robotX;
    @Getter
    private final double robotY;
    @Getter
    private final double lookaheadDistance;
    @Getter
    private final double lookaheadCurvature;

    public PurePursuitData(double[] xValues, double[] yValues, double robotX, double robotY, double lookaheadDistance, double lookaheadCurvature) {
        this.xValues = xValues;
        this.yValues = yValues;
        this.robotX = robotX;
        this.robotY = robotY;
        this.lookaheadDistance = lookaheadDistance;
        this.lookaheadCurvature = lookaheadCurvature;
    }

    @Override
    public Map<String, Object> asMap() {
        return Map.of("xValues", xValues, "yValues", yValues, "robotX", robotX, "robotY", robotY, "lookaheadDistance", lookaheadDistance, "lookaheadCurvature", lookaheadCurvature);
    }

}
