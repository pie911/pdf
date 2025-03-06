package com.example.pdf;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class HelloController {

    private static final String AUTH_FILE_PATH = "C:\\Users\\Yashv\\IdeaProjects\\pdf\\LinkQrData\\Data\\auth.json";

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Hyperlink registerLink;

    @FXML
    private Label errorLabel;

    /**
     * Handles login button click.
     */
    @FXML
    protected void onLoginButtonClick(ActionEvent event) {
        String input = usernameField.getText().trim(); // Can be either username or email
        String password = passwordField.getText();

        if (input.isEmpty() || password.isEmpty()) {
            errorLabel.setText("⚠️ Please enter your username/email and password!");
            return;
        }

        if (authenticateUser(input, password)) {
            errorLabel.setText("✅ Login Successful! Redirecting...");
            try {
                Thread.sleep(1000);
                HelloApplication.changeScene("Dashboard.fxml", "Dashboard");
            } catch (InterruptedException e) {
                errorLabel.setText("⚠️ Error loading Dashboard!");
            }
        } else {
            errorLabel.setText("❌ Invalid username/email or password!");
        }
    }

    /**
     * Reads auth.json and checks if username/email & password match.
     */
    private boolean authenticateUser(String input, String password) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(AUTH_FILE_PATH);

            if (!file.exists()) {
                errorLabel.setText("⚠️ User database not found!");
                return false;
            }

            List<Map<String, String>> users = objectMapper.readValue(file, new TypeReference<List<Map<String, String>>>() {});

            for (Map<String, String> user : users) {
                String storedUsername = user.get("username");
                String storedEmail = user.get("email");
                String storedPassword = user.get("password");

                if ((input.equals(storedUsername) || input.equals(storedEmail)) && password.equals(storedPassword)) {
                    return true; // Successful login
                }
            }

            return false; // No match found
        } catch (IOException e) {
            errorLabel.setText("⚠️ Error reading user database!");
            return false;
        }
    }

    /**
     * Handles register link click (Redirect to Register Page).
     */
    @FXML
    protected void onRegisterLinkClick(MouseEvent event) {
        HelloApplication.changeScene("Register.fxml", "Register");
    }

    /**
     * Clears error message when typing.
     */
    @FXML
    protected void onFieldTyped() {
        errorLabel.setText("");
    }
}
