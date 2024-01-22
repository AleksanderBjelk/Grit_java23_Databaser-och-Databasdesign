package com.example.library_slutprojekt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class HelloApplication extends Application {
    public static final String url = "jdbc:mysql://localhost:3306/Lib";
    public static final String user = "root";
    public static final String password = "åååå";

    //Sätter min startsida till min login
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent root = loader.load();

        LogInController loginController = loader.getController();
        loginController.setPrimaryStage(primaryStage);

        primaryStage.setScene(new Scene(root));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    //Skapar anslutning
    public static Connection connectToDatabase() {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
