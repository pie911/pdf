module com.example.pdf {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires org.apache.commons.io;
    requires com.google.zxing;
    requires com.google.zxing.javase;


    opens com.example.pdf to javafx.fxml;
    exports com.example.pdf;
}