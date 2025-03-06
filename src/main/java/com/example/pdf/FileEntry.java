package com.example.pdf;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class FileEntry {
    private final StringProperty fileName;
    private final StringProperty fileSize;
    private final StringProperty fileType;
    private final StringProperty dateModified;
    private final StringProperty md5Checksum;

    /**
     * Constructs a FileEntry object with the given file.
     *
     * @param file The file to extract details from.
     */
    public FileEntry(File file) throws IOException, NoSuchAlgorithmException {
        this.fileName = new SimpleStringProperty(file.getName());
        this.fileSize = new SimpleStringProperty(formatFileSize(file.length()));
        this.fileType = new SimpleStringProperty(getFileExtension(file));
        this.dateModified = new SimpleStringProperty(getFileLastModifiedTime(file));
        this.md5Checksum = new SimpleStringProperty(generateMD5Checksum(file));
    }

    /**
     * Constructs a FileEntry object with only name and modified time.
     * This is useful when manually adding files without full details.
     *
     * @param name The file name.
     * @param modifiedTime The last modified time.
     */
    public FileEntry(String name, String modifiedTime) {
        this.fileName = new SimpleStringProperty(name);
        this.fileSize = new SimpleStringProperty("Unknown");
        this.fileType = new SimpleStringProperty("Unknown");
        this.dateModified = new SimpleStringProperty(modifiedTime);
        this.md5Checksum = new SimpleStringProperty("N/A");
    }

    /**
     * Retrieves the last modified time of a file.
     *
     * @param file The file whose last modified time is retrieved.
     * @return A formatted date string.
     */
    private String getFileLastModifiedTime(File file) {
        try {
            FileTime fileTime = Files.readAttributes(file.toPath(), BasicFileAttributes.class).lastModifiedTime();
            return fileTime.toInstant().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } catch (IOException e) {
            return "Unknown";
        }
    }

    /**
     * Detects the file extension/type.
     *
     * @param file The file to analyze.
     * @return The file extension as a string.
     */
    private String getFileExtension(File file) {
        String name = file.getName();
        int lastDot = name.lastIndexOf('.');
        return (lastDot > 0) ? name.substring(lastDot + 1).toUpperCase() : "Unknown";
    }

    /**
     * Generates an MD5 checksum for the given file.
     *
     * @param file The file to generate a checksum for.
     * @return The MD5 checksum as a Base64 string.
     */
    private String generateMD5Checksum(File file) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] fileBytes = Files.readAllBytes(file.toPath());
        byte[] digest = md.digest(fileBytes);
        return Base64.getEncoder().encodeToString(digest);
    }

    /**
     * Formats file size in human-readable format (KB, MB, GB).
     */
    private String formatFileSize(long size) {
        if (size < 1024) return size + " B";
        int exp = (int) (Math.log(size) / Math.log(1024));
        char pre = "KMGTPE".charAt(exp - 1);
        return String.format("%.1f %sB", size / Math.pow(1024, exp), pre);
    }

    /** Getters for JavaFX TableView Binding */
    public StringProperty fileNameProperty() { return fileName; }
    public StringProperty fileSizeProperty() { return fileSize; }
    public StringProperty fileTypeProperty() { return fileType; }
    public StringProperty dateModifiedProperty() { return dateModified; }
    public StringProperty md5ChecksumProperty() { return md5Checksum; }
}
