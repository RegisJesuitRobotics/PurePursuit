package com.regisjesuit.purepursuitwidget.data.type;

import com.regisjesuit.purepursuitwidget.data.PurePursuitData;
import edu.wpi.first.shuffleboard.api.data.ComplexDataType;
import java.util.Map;
import java.util.function.Function;

public class PurePursuitType extends ComplexDataType<PurePursuitData> {

    public static final PurePursuitType instance = new PurePursuitType();

    protected PurePursuitType() {
        super("PurePursuit", PurePursuitData.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Function<Map<String, Object>, PurePursuitData> fromMap() {
        return map -> new PurePursuitData((double[]) map.getOrDefault("xValues", new double[0]),
                (double[]) map.getOrDefault("yValues", new double[0]), (double) map.getOrDefault("robotX", 0.0),
                (double) map.getOrDefault("robotY", 0.0), (double) map.getOrDefault("lookaheadDistance", 1.0),
                (double) map.getOrDefault("lookaheadCurvature", 0.0), (double) map.getOrDefault("lookaheadX", 0.0),
                (double) map.getOrDefault("lookaheadY", 0.0));
    }

    @Override
    public PurePursuitData getDefaultValue() {
        return new PurePursuitData(new double[0], new double[0], 0, 0, 1, 0, 0, 0);
    }
}
