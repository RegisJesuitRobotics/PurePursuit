package com.regisjesuit.simulator;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/com.regisjesuit.simulator/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        primaryStage.setTitle("Pure Pursuit Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
