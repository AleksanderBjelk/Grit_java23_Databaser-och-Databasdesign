package com.example.library_slutprojekt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;

import java.sql.*;

public class RegisterController {

    @FXML
    private TextField newUserUsernameTextField;

    @FXML
    private PasswordField newUserPasswordField;

    @FXML
    private TextField newUserEmailField;

    @FXML
    private Label registrationMessageLabel;

    public void setLogInController(LogInController logInController) {
    }

    //En knapp för att hantera skapandet av en ny användare
    @FXML
    void createNewUserButtonOnAction(ActionEvent event) {
        String username = newUserUsernameTextField.getText();
        String password = newUserPasswordField.getText();
        String email = newUserEmailField.getText();

        //Kontrollerar så alla fällt är ifyllda
        if (!username.isEmpty() && !password.isEmpty() && !email.isEmpty()) {
            registerUser(username, password, email);
        } else {
            registrationMessageLabel.setText("Vänligen fyll i alla fält.");
        }
    }

    //Metoden för att registrera den nya användaren i databasen
    public void registerUser(String username, String password, String email) {
        String hashedPassword = Hashing.Encrypt(password);
        Date currentDate = new Date(System.currentTimeMillis()); // Nuvarande datum

        String insertUser = "INSERT INTO Users (name, Password, Email, Created) VALUES (?, ?, ?, ?)";
        try (Connection connectDB = HelloApplication.connectToDatabase();
             PreparedStatement ps = connectDB.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, username);
            ps.setString(2, hashedPassword);
            ps.setString(3, email);
            ps.setDate(4, currentDate);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int userId = rs.getInt(1);
                    }
                }
            } else {
                System.out.println("Användarregistrering misslyckades.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
