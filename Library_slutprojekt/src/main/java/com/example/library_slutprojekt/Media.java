package com.example.library_slutprojekt;

import javafx.beans.property.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Media {
    private final IntegerProperty mediaId;
    private final StringProperty Title;
    private final StringProperty Creator;
    private final ObjectProperty<LocalDate> published;
    private final StringProperty Genre;
    private final StringProperty Media;
    private final StringProperty availableCopies;
    private final StringProperty borrowDate;
    private final StringProperty returnDate;
    private final StringProperty dateReturned;
    private final StringProperty Status;
    private final IntegerProperty borrowedId;
    private final StringProperty reservationDate;
    private final StringProperty expirationDate;

    //Konstruktur som intierar
    public Media() {
        mediaId = new SimpleIntegerProperty(this, "Media_id");
        Title = new SimpleStringProperty(this, "Title");
        Creator = new SimpleStringProperty(this, "Creator");
        published = new SimpleObjectProperty<LocalDate>(this, "Published");
        Genre = new SimpleStringProperty(this, "Genre");
        Media = new SimpleStringProperty(this, "Media");
        availableCopies = new SimpleStringProperty(this, "Available_Copies");
        borrowDate = new SimpleStringProperty(this, "borrowDate", "");
        returnDate = new SimpleStringProperty(this,"returnDate","");
        dateReturned = new SimpleStringProperty(this, "dateReturned", "");
        Status = new SimpleStringProperty(this, "Status", "");
        borrowedId = new SimpleIntegerProperty(this, "borrowedId", 0);
        reservationDate = new SimpleStringProperty(this, "reservationDate", "");
        expirationDate = new SimpleStringProperty(this, "expirationDate", "");
    }


    public IntegerProperty mediaIdProperty() {
        return mediaId;
    }
    public int getMediaId() {
        return mediaId.get();
    }
    public void setMediaId(int newMediaId) {
        System.out.println("Setting Media ID: " + newMediaId);
        mediaId.set(newMediaId);
    }


    public StringProperty titleProperty() {
        return Title;
    }
    public String getTitle() {
        return Title.get();
    }
    public void setTitle(String newTitle) {
        Title.set(newTitle);
    }


    public StringProperty creatorProperty() {
        return Creator;
    }
    public String getCreator() {
        return Creator.get();
    }
    public void setCreator(String newCreator) {
        Creator.set(newCreator);
    }


    public ObjectProperty<LocalDate> publishedProperty() {
        return published;
    }

    public LocalDate getPublished() {
        return published.get();
    }

    public void setPublished(LocalDate newPublished) {
        published.set(newPublished);
    }


    public StringProperty genreProperty() {
        return Genre;
    }
    public String getGenre() {
        return Genre.get();
    }
    public void setGenre(String newGenre) {
        Genre.set(newGenre);
    }


    public StringProperty mediaProperty() {
        return Media;
    }
    public String getMedia() {
        return Media.get();
    }
    public void setMedia(String newMedia) {
        Media.set(newMedia);
    }


    public StringProperty availableCopiesProperty() {
        return availableCopies;
    }
    public String getAvailableCopies() {
        return availableCopies.get();
    }
    public void setAvailableCopies(String newAvailableCopies) {
        availableCopies.set(newAvailableCopies);
    }


    public String getBorrowDate() {
        return borrowDate.get();
    }

    public StringProperty borrowDateProperty() {
        return borrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate.set(borrowDate);
    }


    public String getStatus() {
        return Status.get();
    }

    public StringProperty statusProperty() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status.set(Status);
    }


    public int getBorrowedId() {
        return borrowedId.get();
    }

    public void setBorrowedId(int id) {
        borrowedId.set(id);
    }

    public IntegerProperty borrowedIdProperty() {
        return borrowedId;
    }


    public boolean isBook() {
        return "Book".equalsIgnoreCase(getMedia());
    }

    public String getReturnDate() {
        if (isBook()) {
            return calculateReturnDate(30);
        } else {
            return calculateReturnDate(10);
        }
    }

    public StringProperty returnDateProperty() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate.set(returnDate);
    }


    public StringProperty dateReturnedProperty() {
        return dateReturned;
    }

    public String getDateReturned() {
        return dateReturned.get();
    }

    public void setDateReturned(String newDateReturned) {
        dateReturned.set(newDateReturned);
    }


    public StringProperty reservationDateProperty() {
        return reservationDate;
    }

    public String getReservationDate() {
        return reservationDate.get();
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate.set(reservationDate);
    }


    public StringProperty expirationDateProperty() {
        return expirationDate;
    }

    public String getExpirationDate() {
        return expirationDate.get();
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate.set(expirationDate);
    }

    //Hjälpmetod för att beräkna återlämningsdatum
    public String calculateReturnDate(int daysToAdd) {
        LocalDate borrowDate = LocalDate.parse(getBorrowDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate returnDate = borrowDate.plusDays(daysToAdd);
        return returnDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
