package com.regisjesuit.simulator.controllers;

import com.regisjesuit.purepursuit.path.PurePursuitPath;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class Controller {

    private PurePursuitPath path;

    public Canvas canvas;

    public void onMouseMove(MouseEvent mouseEvent) {
    }

    public void onCanvasClick(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            // Add point
        } else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            // Remove closest point
        }
    }

    public void runButtonClick(MouseEvent mouseEvent) {
    }
}
