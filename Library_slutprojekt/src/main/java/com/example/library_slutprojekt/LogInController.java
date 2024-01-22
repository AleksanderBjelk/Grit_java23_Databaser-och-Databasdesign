package com.example.library_slutprojekt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.sql.*;

public class LogInController {

    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private Label logInMessageLabel;
    private Stage primaryStage;
    private int loggedInUserId;

    //Sätter primära scenen för denna kontrollern
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    //Knapp som stänger programmet när användaren vill avsluta
    public void cancelButtonOnAction(ActionEvent e) {
        primaryStage.close();
    }

    //Knapp för att öppna formuläret för att skapa en användare
    public void createUserButtonOnAction(ActionEvent e) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Register.fxml"));
            Parent root = fxmlLoader.load();

            RegisterController registerController = fxmlLoader.getController();
            registerController. setLogInController(this);

            Stage registerStage = new Stage();
            registerStage.setTitle("Registrera ny användare");
            registerStage.setScene(new Scene(root));
            registerStage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //Knapp för att logga in användaren om inloggningen är giltig
    public void loginButtonOnAction(ActionEvent e) {
        if (validateLogin()) {
            switchToMainView();
        } else {
            logInMessageLabel.setText("Ogiltlig inloggning, försök igen!");
        }
    }

    //Tar data från databasen och jämför om inloggningen är giltig
    public boolean validateLogin() {
        String username = usernameTextField.getText();
        String enteredPassword = passwordPasswordField.getText();

        String verifyLogin = "SELECT Users_Id, Password FROM Users WHERE name = ?";
        try (Connection connectDB = HelloApplication.connectToDatabase();
             PreparedStatement ps = connectDB.prepareStatement(verifyLogin)) {

            ps.setString(1, username);
            ResultSet queryResult = ps.executeQuery();

            if (queryResult.next()) {
                String storedPassword = queryResult.getString("Password");

                if (Hashing.Verify(enteredPassword, storedPassword)) {
                    loggedInUserId = queryResult.getInt("Users_Id");
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Byter till biblioteket om inloggningen är gilitg via validateLogin()
    private void switchToMainView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Hello-View.fxml"));
            Parent root = loader.load();

            HelloController helloController = loader.getController();
            helloController.setUserId(loggedInUserId);
            helloController.fetchUserReservations();

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
            primaryStage.centerOnScreen();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
