package com.example.pdf;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class HelloController {

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
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("⚠️ Please enter both username and password!");
            return;
        }

        // Simulated authentication (Replace with database authentication later)
        if (username.equals("admin") && password.equals("1234")) {
            errorLabel.setText("✅ Login Successful! Redirecting...");
            // TODO: Load Dashboard scene
        } else {
            errorLabel.setText("❌ Invalid username or password!");
        }
    }

    /**
     * Handles register link click (Redirect to Register Page).
     */
    @FXML
    protected void onRegisterLinkClick(MouseEvent event) {
        try {
            HelloApplication.changeScene("Register.fxml", "Register");
        } catch (IOException e) {
            errorLabel.setText("⚠️ Error loading Register page!");
        }
    }


    /**
     * Clears error message when typing.
     */
    @FXML
    protected void onFieldTyped() {
        errorLabel.setText("");
    }
}
