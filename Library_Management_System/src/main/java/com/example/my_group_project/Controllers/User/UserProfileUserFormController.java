package com.example.my_group_project.Controllers.User;
import com.example.my_group_project.SoundPlay;

import com.example.my_group_project.Database.DatabaseConnection;
import com.example.my_group_project.Database.TestInformation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.*;

public class UserProfileUserFormController extends UserMenuController {
    @FXML
    private Label name;

    @FXML
    private TextField fullnameField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField emailField;

    @FXML
    private DatePicker dateOfBirthPicker;

    @FXML
    private ComboBox<String> genderBox;

    @FXML
    private Button saveButton;

    @FXML
    private Button uploadButton;

    @FXML
    private ImageView imageProfile;

    private String userID;

    private boolean isEdited = false;

    private File selectedImageFile;

    @FXML
    public void initialize() {
        this.userID = UserLoginController.userIDMain;
        genderBox.getItems().addAll("Male", "Female", "Other");
        if (userID != null && !userID.isEmpty()) {
            loadUserProfile(userID);
        }
        setEditable(false);
        uploadButton.setDisable(true);
    }

    @FXML
    void onEdit() {
        isEdited = true;
        setEditable(isEdited);
        uploadButton.setDisable(!isEdited);
        saveButton.setDisable(!isEdited);
    }

    @FXML
    void setEditable(boolean edit) {
        fullnameField.setEditable(edit);
        usernameField.setEditable(edit);
        phoneNumberField.setEditable(edit);
        emailField.setEditable(edit);
        dateOfBirthPicker.setEditable(edit);
        genderBox.setDisable(!edit);

        //uploadButton.setVisible(edit);
        //uploadButton.setDisable(!edit);
    }

    @FXML
    private void loadUserProfile(String userID) {
        String query = "SELECT * FROM user WHERE userID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                fullnameField.setText(resultSet.getString("fullName"));
                usernameField.setText(resultSet.getString("name"));
                phoneNumberField.setText(resultSet.getString("phone"));
                emailField.setText(resultSet.getString("email"));

                // Check if dateOfBirth is not null
                Date dateOfBirth = resultSet.getDate("dateOfBirth");
                if (dateOfBirth != null) {
                    dateOfBirthPicker.setValue(dateOfBirth.toLocalDate());
                } else {
                    dateOfBirthPicker.setValue(null);
                }

                String gender = resultSet.getString("gender");
                name.setText(resultSet.getString("name"));

                if (gender != null) {
                    genderBox.setValue(gender);
                }

                if (resultSet.getBlob("profileImage") != null) {
                    byte[] imageBytes = resultSet.getBytes("profileImage");
                    Image image = new Image(new ByteArrayInputStream(imageBytes));
                    imageProfile.setImage(image);
                }
            } else {
                System.out.println("No user found with UserId: " + userID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void updateUserProfile(String userId) {
        String query = "UPDATE user SET fullName = ?, name = ?, phone = ?, email = ?, dateOfBirth = ?, gender = ?, profileImage = ? WHERE userID = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, fullnameField.getText());
            preparedStatement.setString(2, usernameField.getText());
            preparedStatement.setString(3, phoneNumberField.getText());
            preparedStatement.setString(4, emailField.getText());
            preparedStatement.setDate(5, Date.valueOf(dateOfBirthPicker.getValue()));
            preparedStatement.setString(6, genderBox.getValue());
            System.out.print("3sdnajsncdc");
            if (selectedImageFile != null) {
                try (FileInputStream imageStream = new FileInputStream(selectedImageFile)) {
                    System.out.print("2sdnajsncdc");
                    byte[] imageBytes = Files.readAllBytes(selectedImageFile.toPath());
                    preparedStatement.setBinaryStream(7, new ByteArrayInputStream(imageBytes), imageBytes.length);
                    System.out.println("Selected file: " + selectedImageFile.getAbsolutePath());
                    System.out.print("5sdnajsncdc");
                } catch (IOException e) {
                    System.out.print("1sdnajsncdc");
                    e.printStackTrace();
                    showAlert("Error", "An error occurred while reading the image.");
                    return;
                }
            } else {
                System.out.print("sdnajsncdc");
                preparedStatement.setNull(7, Types.BLOB);
            }
            preparedStatement.setString(8, userId);

            preparedStatement.executeUpdate();
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Profile updated successfully!");
            } else {
                System.out.println("Failed to update profile.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onSave() {
        SoundPlay.playSound("/soundEffects/SEFE_CartoonAccent.wav");
        if (isEdited) {
            String fullname = fullnameField.getText();
            String username = usernameField.getText();
            String phoneNumber = phoneNumberField.getText();
            String email = emailField.getText();
            String gender = genderBox.getValue();
            String dateOfBirth = null;

            if (dateOfBirthPicker.getValue() != null) {
                dateOfBirth = dateOfBirthPicker.getValue().toString();
            }

            if (phoneNumber.length() < 10 || !phoneNumber.matches("\\d+")) {
                showAlert1("Lỗi", "PhoneNumber không hợp lệ!");
                return;
            }

            if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                showAlert1("Lỗi", "Email không hợp lệ!");
                return;
            }

            if (fullname.isEmpty() || username.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || gender == null || dateOfBirth == null) {
                showAlert("Loi", "Vui long dien day du thong tin");
                return;
            } else {
                if (!TestInformation.checkUserName(username) || !TestInformation.checkPhone(phoneNumber) || !TestInformation.checkEmail(email)) {
                    System.out.println(TestInformation.checkUserName(username));
                    System.out.println(TestInformation.checkEmail(email));

                    if (!TestInformation.checkPhone(phoneNumber)) {
                        showAlert1("Không hợp lệ", "Đã có số điện thoại này!");
                        return;
                    } else if (!TestInformation.checkEmail(email)) {
                        showAlert1("Không hợp lệ", "Đã có email này!");
                        return;
                    } else if (!TestInformation.checkUserName(username)) {
                        showAlert1("Không hợp lệ", "Đã có user name này!");
                        return;
                    }

                } else {
                    if (!userID.isEmpty() && userID != null) {
                        showAlert1("Thanh cong", "Thay doi thanh cong!");
                        isEdited = false;
                        uploadButton.setDisable(!isEdited);
                        updateUserProfile(userID);
                    }
                }
            }
        }
    }


    @FXML
    void onUploadPicture() {
        SoundPlay.playSound("/soundEffects/SEFE_CartoonAccent.wav");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        selectedImageFile = fileChooser.showOpenDialog(null);

        if (selectedImageFile != null) {
            Image image = new Image(selectedImageFile.toURI().toString());
            imageProfile.setImage(image);
        }
    }
}