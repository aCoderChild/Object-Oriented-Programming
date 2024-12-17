package com.example.my_group_project.Controllers.Admin;
import com.example.my_group_project.Database.DatabaseConnection;


import com.example.my_group_project.SoundPlay;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.image.Image;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.TextField;
import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.io.ByteArrayInputStream;

public class AdminBookUserBorrowController extends AdminMenuController{
    @FXML
    private Label authorNameLabel;

    @FXML
    private Label bookIdLabel;

    @FXML
    private ImageView bookImage;

    @FXML
    private Label bookNameLabel;

    @FXML
    private Label categoryLabel;

    @FXML
    private TextField dateBorrow;

    @FXML
    private TextField dateReturn;


    @FXML
    private Label dateOfBirthLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Label userEmailLabel;

    @FXML
    private Label userFullNameLabel;

    @FXML
    private Label userIdLabel;

    @FXML
    private ImageView userImage;

    @FXML
    private Label userNameLabel;

    //private boolean editable = false;

    public void initialize() {
        setEditable(false);
    }

    private void setEditable(boolean editable) {
        //dateReturn.setEditable(editable);
        dateBorrow.setEditable(editable);
    }

    @FXML
    private Label userPhoneNumberLabel;


    protected void displayUser(String userId) {
        String query = "SELECT userID, name, dateOfBirth, fullName,email, phone, profileImage FROM user WHERE userID = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, userId);  // Set userID cho câu truy vấn
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                userIdLabel.setText(resultSet.getString("userID"));
                userNameLabel.setText(resultSet.getString("name"));
                dateOfBirthLabel.setText(resultSet.getString("dateOfBirth"));
                userFullNameLabel.setText(resultSet.getString("fullName"));
                userEmailLabel.setText(resultSet.getString("email"));
                userPhoneNumberLabel.setText(resultSet.getString("phone"));

                if (resultSet.getBlob("profileImage") != null) {
                    byte[] imageBytes = resultSet.getBytes("profileImage");
                    Image image = new Image(new ByteArrayInputStream(imageBytes));
                    userImage.setImage(image);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    protected void displayBook(String bookId) {
        String query = "select b.bookID, b.author, b.genre, b.title, br.status, br.borrowDate," +
                " br.returnDate , b.imageURL FROM book b JOIN borrow br " +
                "ON br.bookID = ? AND b.bookID = br.bookID";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, bookId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                bookIdLabel.setText(resultSet.getString("b.bookID"));
                bookNameLabel.setText(resultSet.getString("b.title"));
                statusLabel.setText(resultSet.getString("br.status"));
                dateBorrow.setText(resultSet.getString("br.borrowDate"));
                dateReturn.setText(resultSet.getString("br.returnDate"));
                authorNameLabel.setText(resultSet.getString("b.author"));
                categoryLabel.setText(resultSet.getString("b.genre"));


                String imageUrl = resultSet.getString("imageURL");
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    Image bookImageView = new Image(imageUrl);
                    bookImage.setImage(bookImageView);
                } else {
                    bookImage.setImage(null);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editButtonOnAction(ActionEvent event) {
        setEditable(true);
    }

    @FXML
    private void saveButtonOnAction(ActionEvent event) {
        // Lấy dữ liệu từ TextField
        String borrowDateString = dateBorrow.getText();
       // String returnDateString = dateReturn.getText();

        // Chuyển đổi String thành LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime borrowDateTime = LocalDateTime.parse(borrowDateString, formatter);
       // LocalDateTime returnDateTime = LocalDateTime.parse(returnDateString, formatter);

        // Chuyển đổi LocalDateTime thành java.sql.Timestamp
        Timestamp sqlBorrowDate = Timestamp.valueOf(borrowDateTime);
        //java.sql.Timestamp sqlReturnDate = java.sql.Timestamp.valueOf(returnDateTime);

        String bookId = bookIdLabel.getText();

        // Cập nhật dữ liệu vào cơ sở dữ liệu
        String query = "UPDATE borrow SET borrowDate = ? WHERE bookID = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setTimestamp(1, sqlBorrowDate);
            //preparedStatement.setTimestamp(2, sqlReturnDate);
            preparedStatement.setString(2, bookId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Book borrow info updated successfully.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void backButtonOnAction(ActionEvent event) {
        SoundPlay.playSound("/soundEffects/SEFE_MouseClick.wav");
        super.changeScene("AdminHomeScene1.fxml","AdminHomeScene1" );
    }
}
