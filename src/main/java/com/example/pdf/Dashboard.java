package com.example.pdf;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.scene.input.DragEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Dashboard {

    @FXML private ImageView profileImage;
    @FXML private Label usernameLabel;
    @FXML private Button btnUpload, btnViewFiles, btnEditPDF, btnLogout;
    @FXML private BorderPane dragDropArea;
    @FXML private TableView<FileEntry> recentFilesTable;
    @FXML private TableColumn<FileEntry, String> fileNameColumn;
    @FXML private TableColumn<FileEntry, String> dateModifiedColumn;

    private String currentUser;
    private static final String FILE_STORAGE_PATH = "C:\\Users\\Yashv\\IdeaProjects\\pdf\\UserFiles\\";
    private ObservableList<FileEntry> fileList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        fileNameColumn.setCellValueFactory(cellData -> cellData.getValue().fileNameProperty());
        dateModifiedColumn.setCellValueFactory(cellData -> cellData.getValue().dateModifiedProperty());
        recentFilesTable.setItems(fileList);

        setupDragAndDrop();
    }

    public void setUser(String username) {
        this.currentUser = username;
        usernameLabel.setText(username);

        File userProfile = new File(FILE_STORAGE_PATH + username + "\\profile.png");
        if (userProfile.exists()) {
            profileImage.setImage(new Image(userProfile.toURI().toString()));
        }

        loadUserFiles(username);
    }

    private void setupDragAndDrop() {
        dragDropArea.setOnDragOver(event -> {
            if (event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        dragDropArea.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            if (dragboard.hasFiles()) {
                dragboard.getFiles().forEach(this::saveFileToUserFolder);
                recentFilesTable.refresh();
            }
            event.setDropCompleted(true);
            event.consume();
        });
    }

    private void saveFileToUserFolder(File file) {
        if (currentUser == null) return;
        File userDir = new File(FILE_STORAGE_PATH + currentUser);
        if (!userDir.exists()) userDir.mkdirs();

        File destinationFile = new File(userDir, file.getName());
        try {
            Files.copy(file.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            String modifiedTime = getFileLastModifiedTime(destinationFile);
            fileList.add(new FileEntry(file.getName(), modifiedTime));
            recentFilesTable.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadUserFiles(String username) {
        File userDir = new File(FILE_STORAGE_PATH + username);
        if (!userDir.exists()) return;

        File[] files = userDir.listFiles();
        if (files != null) {
            fileList.clear();
            for (File file : files) {
                String modifiedTime = getFileLastModifiedTime(file);
                fileList.add(new FileEntry(file.getName(), modifiedTime));
            }
        }
    }

    private String getFileLastModifiedTime(File file) {
        try {
            FileTime fileTime = Files.readAttributes(file.toPath(), BasicFileAttributes.class).lastModifiedTime();
            return fileTime.toInstant().atZone(java.time.ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } catch (IOException e) {
            return "Unknown";
        }
    }

    @FXML
    private void onUploadClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File to Upload");
        File selectedFile = fileChooser.showOpenDialog(btnUpload.getScene().getWindow());
        if (selectedFile != null) {
            saveFileToUserFolder(selectedFile);
        }
    }

    @FXML
    private void onViewFilesClick() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Feature Coming Soon!", ButtonType.OK);
        alert.show();
    }

    @FXML
    private void onEditPDFClick() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Feature Coming Soon!", ButtonType.OK);
        alert.show();
    }

    @FXML
    private void onLogoutClick() {
        SceneSwitcher.switchTo("Login.fxml"); // Ensure SceneSwitcher class exists
    }
}
