module com.example.pdf {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.pdf to javafx.fxml;
    exports com.example.pdf;
}