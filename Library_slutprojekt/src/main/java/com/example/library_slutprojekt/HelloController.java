package com.example.library_slutprojekt;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

import java.sql.*;
import java.time.LocalDate;

public class HelloController {
    private int userId;
    private Connection connection;

    private ObservableList<Media> media;

    @FXML
    private TableColumn<Media, String> acColumn;

    @FXML
    private TableColumn<Media, String> creatorColumn;


    @FXML
    private TableColumn<Media, String> genreColumn;

    @FXML
    private TableView<Media> libraryTable;

    @FXML
    private TableView<Media> borrowedTable;

    @FXML
    private TableView<Media> reservedTable;

    @FXML
    private TableColumn<Media, String> dateReservedColumn;

    @FXML
    private TableColumn<Media, String> lastDateToLoanColumn;

    @FXML
    private TableColumn<Media, String> reservedTitlesColumn;

    @FXML
    private TableColumn<Media, String> dateLoanedBorrowedTable;

    @FXML
    private TableColumn<Media, String> returnDateBorrowedTable;

    @FXML
    private TableColumn<Media, String> dateReturnedColumn;

    @FXML
    private TableColumn<Media, String> titleBorrowedTable;

    @FXML
    private TableColumn<Media, String> statusDateBorrowedTable;

    @FXML
    private TableColumn<Media, String> mediaColumn;

    @FXML
    private TableColumn<Media, String> publishedColumn;

    @FXML
    private TableColumn<Media, String> titleColumn;

    @FXML
    private TextField newPasswordField;

    @FXML
    private TextField newUsernameField;

    @FXML
    private TextField searchField;

    public HelloController() {
        this.connection = HelloApplication.connectToDatabase();
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    //Initialiserar all UI och data
    public void initialize() {
        this.media = FXCollections.observableArrayList();

        titleColumn.setCellValueFactory(f -> f.getValue().titleProperty());
        creatorColumn.setCellValueFactory(f -> f.getValue().creatorProperty());
        publishedColumn.setCellValueFactory(f -> f.getValue().publishedProperty().asString());
        genreColumn.setCellValueFactory(f -> f.getValue().genreProperty());
        mediaColumn.setCellValueFactory(f -> f.getValue().mediaProperty());
        acColumn.setCellValueFactory(f -> f.getValue().availableCopiesProperty());

        titleBorrowedTable.setCellValueFactory(f -> f.getValue().titleProperty());
        dateLoanedBorrowedTable.setCellValueFactory(f -> f.getValue().borrowDateProperty());
        returnDateBorrowedTable.setCellValueFactory(f -> f.getValue().returnDateProperty());

        dateReservedColumn.setCellValueFactory(cellData -> cellData.getValue().reservationDateProperty());
        lastDateToLoanColumn.setCellValueFactory(cellData -> cellData.getValue().expirationDateProperty());
        reservedTitlesColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());

        libraryTable.setItems(media);
        borrowedTable.setItems(FXCollections.observableArrayList());
        updateReservedTable();
    }

    //Visar en dialogruta med information, används i flera olika metoder dör olika syften
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    //Knappen för att uppdatera användarens information i databasen (glöm inte att fixa hashing innan du lämnar in)
    @FXML
    void updateUser(ActionEvent event) {
        String newUsername = newUsernameField.getText();
        String newPassword = newPasswordField.getText();

        if (validateInput(newUsername, newPassword)) {
            String hashedPassword = Hashing.Encrypt(newPassword);
            if (hashedPassword != null) {
                performUserUpdate(userId, newUsername, hashedPassword);
                showAlert("Uppdatering genomförd", "Din information har uppdaterats.");
            } else {
                showAlert("Krypteringsfel", "Ett fel uppstod vid kryptering av lösenordet.");
            }
        } else {
            showAlert("Ogiltig input", "Fyll i både användarnamn och lösenord.");
        }
    }


    private boolean validateInput(String username, String password) {
        return !username.isEmpty() && !password.isEmpty();
    }

    //Själva logiken för att kunna uppdatera användarens info
    private void performUserUpdate(int userId, String newUsername, String newPassword) {
        try {
            String updateQuery = "UPDATE Users SET Name = ?, Password = ? WHERE Users_id = ?";
            try (PreparedStatement ps = connection.prepareStatement(updateQuery)) {
                ps.setString(1, newUsername);
                ps.setString(2, newPassword);
                ps.setInt(3, userId);

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Uppdatering genomförd. Antal rader påverkade: " + rowsAffected);
                } else {
                    System.out.println("Uppdatering misslyckades. Ingen rad påverkad.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //Knappen för att låna media
    @FXML
    void borrowMedia(ActionEvent event) {

        Media selectedMedia = libraryTable.getSelectionModel().getSelectedItem();

        if (selectedMedia != null && "YES".equals(selectedMedia.getAvailableCopies())) {
            performBorrow(selectedMedia);

            borrowedTable.getItems().add(selectedMedia);
        } else {
            showAlert("Ingen tillgänglig kopia", "Tyvärr, det finns ingen tillgänglig kopia av " + (selectedMedia != null ? selectedMedia.getTitle() : "") + " att låna.");
        }
    }

    //Knappen för att retunera media
    @FXML
    void returnMedia(ActionEvent event) {
        Media selectedBorrowedMedia = borrowedTable.getSelectionModel().getSelectedItem();
        if (selectedBorrowedMedia != null) {
            performReturn(selectedBorrowedMedia);
        } else {
            showAlert("Ingen vald media för returnering", "Vänligen välj media att returnera!");
        }
    }

    //Koden för att utföra låning av media
    private void performBorrow(Media media) {
        try {
            String borrowQuery = "INSERT INTO BorrowedMedia (Media_id, Users_id, Borrow_Date, Return_Date) VALUES (?, ?, ?, ?)";

            try (PreparedStatement ps = connection.prepareStatement(borrowQuery)) {
                ps.setInt(1, media.getMediaId());
                ps.setInt(2, userId);
                ps.setDate(3, java.sql.Date.valueOf(LocalDate.now()));

                media.setBorrowDate(java.sql.Date.valueOf(LocalDate.now()).toString());

                if (media.isBook()) {
                    ps.setDate(4, java.sql.Date.valueOf(LocalDate.now().plusDays(30)));
                    media.setReturnDate(media.calculateReturnDate(30));
                } else {
                    ps.setDate(4, java.sql.Date.valueOf(LocalDate.now().plusDays(10)));
                    media.setReturnDate(media.calculateReturnDate(10));
                }
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    updateAvailableCopies(media.getMediaId(), "NO");
                    updateBorrowedTableColumns();

                    showAlert("Lån genomfört", "Lån av " + media.getTitle() + " genomfört.");
                } else {
                    showAlert("Lån misslyckades", "Det gick inte att genomföra lånet.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Databasfel", "Ett fel uppstod vid kommunikation med databasen.");
        }
    }

    //Koden för att lämna tillbaka medain som är lånad
    private void performReturn(Media borrowedMedia) {

        if (borrowedMedia.getMediaId() == 0) {
            showAlert("Fel", "Ogiltigt mediaID för återlämning.");
            return;
        }
        try {
            String returnQuery = "UPDATE BorrowedMedia SET Return_Date = ?, Status = 'Returned' WHERE Borrowed_id = ?";
            try (PreparedStatement ps = connection.prepareStatement(returnQuery)) {
                ps.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
                ps.setInt(2, borrowedMedia.getBorrowedId());

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Returning Media ID: " + borrowedMedia.getMediaId());
                    updateAvailableCopies(borrowedMedia.getMediaId(), "YES");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //Uppdaterar medians tillgänlighet
    private void updateAvailableCopies(int mediaId, String newAvailability) {
        try {
            String updateQuery = "UPDATE Media SET Available_Copies = ? WHERE Media_id = ?";
            try (PreparedStatement ps = connection.prepareStatement(updateQuery)) {
                ps.setString(1, newAvailability);
                ps.setInt(2, mediaId);
                int rowsAffected = ps.executeUpdate();
                System.out.println("Rows affected in Media: " + rowsAffected);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //Knappen för att hantera reservation
    @FXML
    void reserveMedia(ActionEvent event) {
        Media selectedMedia = libraryTable.getSelectionModel().getSelectedItem();
        if (selectedMedia != null) {
            if ("NO".equals(selectedMedia.getAvailableCopies()) && !isReserved(selectedMedia.getMediaId())) {
                performReservation(selectedMedia);
            } else {
                showAlert("Reservation fel", "Media är inte tillgängligt för reservation.");
            }
        }
    }

    //Kollar om media är reserverad
    private boolean isReserved(int mediaId) {
        String query = "SELECT COUNT(*) FROM Reservations WHERE Media_id = ? AND Expiration_Date > CURRENT_DATE()";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, mediaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    private void performReservation(Media media) {
        if (media == null) {
            showAlert("Fel", "Ingen media vald för reservation.");
            return;
        }

        int mediaId = media.getMediaId();
        if (isReserved(mediaId)) {
            showAlert("Reservation fel", "Media är redan reserverad.");
            return;
        }

        makeReservation(userId, mediaId);
        updateReservedTable();
    }

    //Visar reservationerna för användaren
    private void updateReservedTable() {

        ObservableList<Media> reservedMediaList = FXCollections.observableArrayList();

        String query = "SELECT m.Media_id, m.Title, r.Reservation_Date, r.Expiration_Date "
                + "FROM Reservations r "
                + "JOIN Media m ON r.Media_id = m.Media_id "
                + "WHERE r.Users_id = ? AND r.Expiration_Date > CURRENT_DATE()";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Media media = new Media();
                    media.setMediaId(rs.getInt("Media_id"));
                    media.setTitle(rs.getString("Title"));
                    media.setReservationDate(rs.getDate("Reservation_Date").toString());
                    media.setExpirationDate(rs.getDate("Expiration_Date").toString());

                    reservedMediaList.add(media);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Databasfel", "Ett fel uppstod vid hämtning av reservationer.");
        }
        reservedTable.setItems(reservedMediaList);
    }

    //skapar reserveringar med rätt reservationstid
    public void makeReservation(int userId, int mediaId) {
        LocalDate reservationDate = LocalDate.now();
        LocalDate expirationDate;
        LocalDate returnDate = getReturnDateForMedia(mediaId);

        if (returnDate != null) {
            expirationDate = returnDate.plusDays(30);
        } else {
            expirationDate = reservationDate.plusDays(30);
        }
        addReservationToDatabase(userId, mediaId, reservationDate, expirationDate);
    }


    private LocalDate getReturnDateForMedia(int mediaId) {
        String query = "SELECT Return_Date FROM BorrowedMedia WHERE Media_id = ? AND Status = 'Active'";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, mediaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Date returnDateSql = rs.getDate(1);
                    if (returnDateSql != null) {
                        return returnDateSql.toLocalDate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Något är del i getReturnDateForMedia");
        }
        return null;
    }

    //Logiken för at lägga till reservationer i databasen
    private void addReservationToDatabase(int userId, int mediaId, LocalDate reservationDate, LocalDate expirationDate) {
        String insertQuery = "INSERT INTO Reservations (Media_id, Users_id, Reservation_Date, Expiration_Date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insertQuery)) {
            ps.setInt(1, mediaId);
            ps.setInt(2, userId);
            ps.setDate(3, java.sql.Date.valueOf(reservationDate));
            ps.setDate(4, java.sql.Date.valueOf(expirationDate));

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Reservation genomförd", "Du har nu reserverat media.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Databasfel", "Ett fel uppstod vid skapandet av reservationen.");
        }
    }

    //Visar lånehistorik för användaren, genom att trycka på en knapp och uppdaterar borrowedTable
    @FXML
    void performHistory(ActionEvent event) {
        ObservableList<Media> borrowedMediaList = FXCollections.observableArrayList();
        String query = "SELECT bm.Borrowed_id, bm.Media_id, m.Title, bm.Borrow_Date, bm.Return_Date, bm.Status FROM BorrowedMedia bm " +
                "JOIN Media m ON bm.Media_id = m.Media_id " +
                "WHERE bm.Users_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Media media = new Media();
                    media.setMediaId(rs.getInt("Media_id"));
                    media.setBorrowedId(rs.getInt("Borrowed_id"));
                    media.setTitle(rs.getString("Title"));
                    media.setBorrowDate(rs.getDate("Borrow_Date").toString());

                    Date returnDateSql = rs.getDate("Return_Date");
                    if (returnDateSql != null) {
                        media.setReturnDate(returnDateSql.toString());
                        media.setDateReturned(returnDateSql.toString());
                    } else {
                        media.setReturnDate("Ej återlämnad");
                        media.setDateReturned("");
                    }

                    String status = rs.getString("Status").equals("Active") ? "Utlånad" : "Returnerad";
                    media.setStatus(status);

                    borrowedMediaList.add(media);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Databasfel", "Ett fel uppstod vid hämtning av lånehistorik.");
        }

        borrowedTable.setItems(borrowedMediaList);
        updateBorrowedTableColumns();
    }


    public void fetchUserReservations() {
        updateReservedTable();
    }

    //sökfunktionen i programmet
    @FXML
    void Search(ActionEvent event) {
        String searchTerm = searchField.getText().trim();

        ObservableList<Media> searchResult = FXCollections.observableArrayList();

        try {
            String query = "SELECT * FROM Media WHERE Title LIKE ? OR Creator LIKE ? OR Genre LIKE ? OR Published LIKE ? OR Media LIKE ? OR Available_Copies LIKE ?"; //testa med tex Homer Simpson eller The not so great Gatsby

            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, "%" + searchTerm + "%");
                ps.setString(2, "%" + searchTerm + "%");
                ps.setString(3, "%" + searchTerm + "%");
                ps.setString(4, "%" + searchTerm + "%");
                ps.setString(5, "%" + searchTerm + "%");
                ps.setString(6, "%" + searchTerm + "%");

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Media media = new Media();
                        media.setMediaId(rs.getInt("Media_id"));
                        media.setTitle(rs.getString("Title"));
                        media.setCreator(rs.getString("Creator"));
                        media.setPublished(rs.getDate("Published").toLocalDate());
                        media.setGenre(rs.getString("Genre"));
                        media.setMedia(rs.getString("Media"));
                        media.setAvailableCopies(rs.getString("Available_Copies"));
                        searchResult.add(media);
                    }
                }
            }

            libraryTable.setItems(searchResult);
        } catch (SQLException ex) {
            ex.printStackTrace();

        }
    }

    //Uppdaterar kolumnerna i tabellen för utlånad media
    private void updateBorrowedTableColumns() {
        titleBorrowedTable.setCellValueFactory(f -> f.getValue().titleProperty());
        dateLoanedBorrowedTable.setCellValueFactory(f -> f.getValue().borrowDateProperty());
        returnDateBorrowedTable.setCellValueFactory(f -> f.getValue().returnDateProperty());
        dateReturnedColumn.setCellValueFactory(f -> f.getValue().dateReturnedProperty());
        statusDateBorrowedTable.setCellValueFactory(f -> f.getValue().statusProperty());
    }
}
