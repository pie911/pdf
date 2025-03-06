package com.example.pdf;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;

public class HelloApplication extends Application {

    private static Stage primaryStage; // Global reference for scene switching
    private static double width = 600; // Preserve window width
    private static double height = 400; // Preserve window height

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        // Load Login Scene First
        changeScene("Login.fxml", "Login");

        // Configure Stage
        stage.setResizable(true); // Allow window resizing
        stage.show();
    }

    /**
     * Method to switch scenes dynamically while preserving window size.
     */
    public static void changeScene(String fxmlFile, String title) {
        if (primaryStage == null) {
            System.err.println("⚠️ Error: Primary stage is not initialized!");
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/pdf/" + fxmlFile));
            Scene scene = new Scene(fxmlLoader.load(), width, height);

            // Preserve window size when switching scenes
            width = primaryStage.getWidth();
            height = primaryStage.getHeight();

            primaryStage.setScene(scene);
            primaryStage.setTitle("LinkQr - " + title);
            primaryStage.show();
        } catch (IOException e) {
            showErrorAlert("⚠️ Error loading " + fxmlFile + "! File might be missing or corrupted.");
            e.printStackTrace();
        }
    }

    /**
     * Displays an error alert when scene loading fails.
     */
    private static void showErrorAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Application Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}
