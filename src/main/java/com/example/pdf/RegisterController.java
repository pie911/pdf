package com.example.pdf;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class RegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button registerButton;

    @FXML
    private Hyperlink loginLink;

    @FXML
    private Label errorLabel;

    /**
     * Handles register button click.
     */
    @FXML
    protected void onRegisterButtonClick(ActionEvent event) {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Input Validation
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorLabel.setText("⚠️ All fields are required!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            errorLabel.setText("❌ Passwords do not match!");
            return;
        }

        if (!isValidEmail(email)) {
            errorLabel.setText("⚠️ Invalid email format!");
            return;
        }

        if (!isValidPassword(password)) {
            errorLabel.setText("⚠️ Password must be at least 6 characters!");
            return;
        }

        // Simulated Registration Success (Replace with Database Code Later)
        errorLabel.setText("✅ Registration Successful! Redirecting...");

        // TODO: Store user data in database

        // Redirect to Login Scene
        try {
            HelloApplication.changeScene("Login.fxml", "Login");
        } catch (IOException e) {
            errorLabel.setText("⚠️ Error loading login page!");
        }
    }

    /**
     * Validates email format.
     */
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    /**
     * Validates password strength.
     */
    private boolean isValidPassword(String password) {
        return password.length() >= 6;
    }

    /**
     * Handles login link click (Redirect to Login Page).
     */
    @FXML
    protected void onLoginLinkClick(MouseEvent event) {
        try {
            HelloApplication.changeScene("Login.fxml", "Login");
        } catch (IOException e) {
            errorLabel.setText("⚠️ Error loading login page!");
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
