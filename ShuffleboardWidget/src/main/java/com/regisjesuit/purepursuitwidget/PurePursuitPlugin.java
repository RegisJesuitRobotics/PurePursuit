package com.regisjesuit.purepursuitwidget;

import com.regisjesuit.purepursuitwidget.data.type.PurePursuitType;
import com.regisjesuit.purepursuitwidget.widgets.PurePursuitWidget;
import edu.wpi.first.shuffleboard.api.data.DataType;
import edu.wpi.first.shuffleboard.api.plugin.Description;
import edu.wpi.first.shuffleboard.api.plugin.Plugin;
import edu.wpi.first.shuffleboard.api.widget.ComponentType;
import edu.wpi.first.shuffleboard.api.widget.WidgetType;
import java.util.List;
import java.util.Map;

@Description(group = "com.regisjesuit", name = "PurePursuit", version = "1.0.0", summary = "A plugin that allows preview of a pure pursuit path")
public class PurePursuitPlugin extends Plugin {

    @Override
    public List<DataType> getDataTypes() {
        return List.of(PurePursuitType.instance);
    }

    @Override
    public Map<DataType, ComponentType> getDefaultComponents() {
        return Map.of(PurePursuitType.instance, WidgetType.forAnnotatedWidget(PurePursuitWidget.class));
    }

    @Override
    public List<ComponentType> getComponents() {
        return List.of(WidgetType.forAnnotatedWidget(PurePursuitWidget.class));
    }
}
