module com.example.library_slutprojekt {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.naming;


    opens com.example.library_slutprojekt to javafx.fxml;
    exports com.example.library_slutprojekt;
}