package com.example.pdf;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    private static Stage primaryStage; // Global reference for scene switching

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        // Load Login Scene First
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/pdf/Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        // Configure Stage
        stage.setTitle("LinkQr - Login");
        stage.setScene(scene);
        stage.setResizable(true); // Allow window resizing
        stage.show();
    }

    /**
     * Method to switch scenes dynamically.
     */
    public static void changeScene(String fxmlFile, String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/pdf/" + fxmlFile));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("LinkQr - " + title);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
