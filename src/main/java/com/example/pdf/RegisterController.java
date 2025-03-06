package com.example.pdf;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RegisterController {

    private static final String AUTH_FILE_PATH = "C:\\Users\\Yashv\\IdeaProjects\\pdf\\LinkQrData\\Data\\auth.json";
    private static final String USERS_FOLDER_PATH = "C:\\Users\\Yashv\\IdeaProjects\\pdf\\LinkQrData\\Users";

    @FXML
    private TextField usernameField, emailField;
    @FXML
    private PasswordField passwordField, confirmPasswordField;
    @FXML
    private Button registerButton, uploadImageButton;
    @FXML
    private Hyperlink loginLink;
    @FXML
    private Label errorLabel;

    private File selectedProfileImage = null;

    /**
     * Handles register button click - Stores user data in auth.json.
     */
    @FXML
    protected void onRegisterButtonClick(ActionEvent event) {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (!validateInput(username, email, password, confirmPassword)) return;

        if (userExists(username)) {
            showError("⚠️ Username already exists!");
            return;
        }

        if (saveUser(username, email, password)) {
            createUserFolder(username);
            saveProfileImage(username);
            showError("✅ Registration Successful! Redirecting...");
            redirectToLogin();
        } else {
            showError("⚠️ Error saving user!");
        }
    }

    /**
     * Validates user input fields.
     */
    private boolean validateInput(String username, String email, String password, String confirmPassword) {
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("⚠️ All fields are required!");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            showError("❌ Passwords do not match!");
            return false;
        }
        if (!isValidEmail(email)) {
            showError("⚠️ Invalid email format!");
            return false;
        }
        if (!isValidPassword(password)) {
            showError("⚠️ Password must be at least 6 characters!");
            return false;
        }
        return true;
    }

    /**
     * Checks if user exists in auth.json.
     */
    private boolean userExists(String username) {
        try {
            File file = new File(AUTH_FILE_PATH);
            if (!file.exists()) return false;

            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, String>> users = objectMapper.readValue(file, new TypeReference<>() {});
            return users.stream().anyMatch(user -> user.get("username").equals(username));
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Saves user details to auth.json.
     */
    private boolean saveUser(String username, String email, String password) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(AUTH_FILE_PATH);

            List<Map<String, String>> users = file.exists()
                    ? objectMapper.readValue(file, new TypeReference<>() {})
                    : new ArrayList<>();

            users.add(Map.of("username", username, "email", email, "password", password));
            objectMapper.writeValue(file, users);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Creates a new folder for user files.
     */
    private void createUserFolder(String username) {
        File userDir = new File(USERS_FOLDER_PATH, username);
        if (!userDir.exists() && userDir.mkdirs()) {
            showError("✅ User folder created!");
        }
    }

    /**
     * Handles file upload for user profile picture.
     */
    @FXML
    protected void onUploadImageClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        Stage stage = (Stage) registerButton.getScene().getWindow();
        selectedProfileImage = fileChooser.showOpenDialog(stage);

        if (selectedProfileImage != null) {
            showError("✅ Profile image selected!");
        }
    }

    /**
     * Saves the uploaded profile image to the user's folder.
     */
    private void saveProfileImage(String username) {
        if (selectedProfileImage != null) {
            File userProfileFolder = new File(USERS_FOLDER_PATH, username);
            File destination = new File(userProfileFolder, "profile.jpg");
            try {
                Files.copy(selectedProfileImage.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                showError("⚠️ Failed to save profile image!");
            }
        }
    }

    /**
     * Displays error messages and auto-hides after 2 seconds.
     */
    private void showError(String message) {
        errorLabel.setText(message);
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                errorLabel.setText("");
            } catch (InterruptedException ignored) {}
        }).start();
    }

    /**
     * Redirects to the login page after successful registration.
     */
    private void redirectToLogin() {
        new Thread(() -> {
            try {
                Thread.sleep(1500); // Delay before switching scene
                Platform.runLater(() -> {
                    HelloApplication.changeScene("Login.fxml", "Login");
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    /**
     * Handles login link click (Redirect to Login Page).
     */
    @FXML
    protected void onLoginLinkClick(MouseEvent event) throws IOException {
        HelloApplication.changeScene("Login.fxml", "Login");
    }

    /**
     * Clears error message when typing.
     */
    @FXML
    protected void onFieldTyped() {
        errorLabel.setText("");
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

    private void run() throws IOException, InterruptedException {
        Thread.sleep(1500);
        HelloApplication.changeScene("Login.fxml", "Login");
    }
}
