package com.example.my_group_project.Controllers.Admin;
import com.example.my_group_project.Book.BorrowedBook;
import com.example.my_group_project.Database.DatabaseConnection;
import com.example.my_group_project.SoundPlay;
import com.example.my_group_project.User.User;


import com.example.my_group_project.Database.TestInformation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

public class AdminOneUserProfileController extends AdminMenuController {
    @FXML
    private ComboBox<String> genderComboBox;

    @FXML
    private Button saveButton;

    @FXML
    private TextField searchTextField;

    @FXML
    private Button uploadButton;
    @FXML
    private DatePicker userDatePicker;
    @FXML
    private TextField userEmail;
    @FXML
    private TextField userFullNameTextField;
    @FXML
    private TextField userIdTextField;
    @FXML
    private ImageView userImage;
    @FXML
    private Button userManagementButton;
    @FXML
    private TextField userNameTextField;
    @FXML
    private TextField userPhoneNumber;
    @FXML
    private TextField passwordTextField;

    private String userId;
    private boolean isInsert = false;
    private boolean isEdited = false;
    private File selectedImageFile;

    @FXML
    private VBox vBox;

    @FXML
    private void initialize() {
        genderComboBox.getItems().addAll("Male", "Female", "Other");
        searchTextField.setOnKeyReleased(event -> filterBook());
        if (userId != null && !userId.isEmpty()) {
            loadUserProfile(userId);
            searchTextField.setOnKeyReleased(event -> filterBook());
        } else {
            getClear();
            setEditable(true);
            isInsert = true;
        }
        setEditable(false);
    }

    // set trang thai
    @FXML
    void onEdit() {
        SoundPlay.playSound("/soundEffects/SEFE_MouseClick.wav");
        isEdited = true;
        setEditable(isEdited);
        saveButton.setDisable(!isEdited);
    }

    @FXML
    void getClear() {
        //userIdTextField.setText("");
        userFullNameTextField.setText("");
        userNameTextField.setText("");
        userPhoneNumber.setText("");
        userEmail.setText("");
        userDatePicker.setValue(null);
        genderComboBox.setValue(null);
        passwordTextField.setText("");
    }

    //set trang thai
    @FXML
    void setEditable(boolean edit) {
        userIdTextField.setEditable(false);
        userFullNameTextField.setEditable(edit);
        userNameTextField.setEditable(edit);
        userPhoneNumber.setEditable(edit);
        userEmail.setEditable(edit);
        userDatePicker.setEditable(edit);
        genderComboBox.setDisable(!edit);
        passwordTextField.setEditable(edit);
        //set trang thai cho uploadButton truoc va sau khi an editButton
        uploadButton.setVisible(edit);
        uploadButton.setDisable(!edit);
    }


    @FXML
    private void insertNewUser(String fullname, String username, String phoneNumber, String email, String dateOfBirth, String gender, String password) {
        String sql = "INSERT INTO user (userID, fullName, name, phone, email, dateOfBirth,gender, password) VALUES (generateRandomID(10),?,?,?,?,?,?,?);";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, fullname);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, phoneNumber);
            preparedStatement.setString(4, email);

            if (dateOfBirth != null && !dateOfBirth.isEmpty()) {
                preparedStatement.setString(5, dateOfBirth);
            } else {
                preparedStatement.setNull(5, java.sql.Types.DATE);
            }

            preparedStatement.setString(6, gender);
            preparedStatement.setString(7, password);

            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // lay thong tin nguoi dung va sach muon
    @FXML
    protected static List<BorrowedBook> getUserBorrowFromDatabase(String userId) {
        List<BorrowedBook> getUserBorrowFromDatabase = new ArrayList<>();
        String sql = "SELECT br.userID, br.bookID, b.title, br.borrowDate, br.returnDate, br.status \n" +
                "FROM book b JOIN borrow br ON b.bookID = br.bookID AND br.userID = ? ;";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String userID = rs.getString("br.userID");
                    String bookId = rs.getString("br.bookID");
                    String bookName = rs.getString("b.title");
                    String endDate = rs.getString("br.borrowDate");
                    String dueDate = rs.getString("br.returnDate");
                    String status = rs.getString("br.status");
                    BorrowedBook brbook = new BorrowedBook(bookId, bookName, userID, endDate, dueDate, status);
                    getUserBorrowFromDatabase.add(brbook);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return getUserBorrowFromDatabase;
    }

    //show nguoi dung + sach muon tren vbox
    protected void displayUserBorrow(String userId) {
        System.out.print("bdw");
        List<BorrowedBook> borrowList = getUserBorrowFromDatabase(userId);
        int index = 0;
        if (borrowList.isEmpty()) {
            vBox.getChildren().add(new Label("No book found."));
            return;
        } else {
            for (BorrowedBook brbook : borrowList) {
                if (brbook == null) {
                    continue;
                }
                try {
                    System.out.print("bdw");
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/my_group_project/AdminOneUserProfilePane.fxml"));
                    HBox borrowHBox = loader.load();
                    AdminOneUserProfilePaneController oneUserProfilePane = loader.getController();
                    oneUserProfilePane.setUserBorrowDetail(brbook);
                    System.out.print("bdw");
                    final int currentIndex = index;
                    if (currentIndex % 2 == 0) {
                        borrowHBox.setStyle("-fx-background-color: #f7efd8;");
                    } else {
                        borrowHBox.setStyle("-fx-background-color: #ffffff;");
                    }

                    StackPane stackPane = new StackPane();
                    stackPane.getChildren().add(borrowHBox);

                    stackPane.setOnMouseEntered(event -> {
                        borrowHBox.setStyle("-fx-background-color: #ffc100; -fx-cursor: hand;");
                    });

                    stackPane.setOnMouseExited(event -> {
                        if (currentIndex % 2 == 0) {
                            borrowHBox.setStyle("-fx-background-color: #f7efd8;");
                        } else {
                            borrowHBox.setStyle("-fx-background-color: #ffffff;");
                        }
                    });

                    vBox.getChildren().add(stackPane);
                    index++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //filter search
    protected void filterBook() {
        String search = searchTextField.getText();
        System.out.print("sear");

        List<BorrowedBook> filterBook = new ArrayList<>();
        for (BorrowedBook brbook : getUserBorrowFromDatabase(userIdTextField.getText())) {
            if ((brbook.getId() != null && brbook.getId().contains(search)) ||
                    (brbook.getTitle() != null && brbook.getTitle().contains(search)) ||
                    (brbook.getBorrowDate() != null && brbook.getBorrowDate().contains(search)) ||
                    (brbook.getReturnDate() != null && brbook.getReturnDate().contains(search)) ||
                    (brbook.getStatus() != null && brbook.getStatus().contains(search))) {
                filterBook.add(brbook);
            }
        }
        displayFilterSearch(filterBook);
    }

    private void displayFilterSearch(List<BorrowedBook> filterBook) {
        vBox.getChildren().clear();

        if (filterBook.isEmpty()) {
            vBox.getChildren().add(new Label("No book found"));
            return;
        }

        int index = 0;
        for (BorrowedBook brbook : filterBook) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/my_group_project/AdminOneUserProfilePane.fxml"));
                HBox bookHBox = loader.load();

                AdminOneUserProfilePaneController oneUserProfilePane = loader.getController();
                oneUserProfilePane.setUserBorrowDetail(brbook);  // Cập nhật chi tiết người dùng
                final int currentIndex = index;
                if (currentIndex % 2 == 0) {
                    bookHBox.setStyle("-fx-background-color: #f7efd8;");  // Màu nền cho dòng chẵn
                } else {
                    bookHBox.setStyle("-fx-background-color: #ffffff;");  // Màu nền cho dòng lẻ
                }

                StackPane stackPane = new StackPane();
                stackPane.getChildren().add(bookHBox);

                stackPane.setOnMouseEntered(event -> {
                    bookHBox.setStyle("-fx-background-color: #ffc100; -fx-cursor: hand;");
                });

                stackPane.setOnMouseExited(event -> {
                    if (currentIndex % 2 == 0) {
                        bookHBox.setStyle("-fx-background-color: #f7efd8;");
                    } else {
                        bookHBox.setStyle("-fx-background-color: #ffffff;");
                    }
                });

                vBox.getChildren().add(stackPane);
                index++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //load thong tin nguoi dung
    @FXML
    public void loadUserProfile(String userId) {
        String query = "SELECT * FROM user WHERE userID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                userIdTextField.setText(resultSet.getString("userID"));
                userFullNameTextField.setText(resultSet.getString("fullName"));
                userNameTextField.setText(resultSet.getString("name"));
                userPhoneNumber.setText(resultSet.getString("phone"));
                userEmail.setText(resultSet.getString("email"));
                Date dateOfBirth = resultSet.getDate("dateOfBirth");
                if (dateOfBirth != null) {
                    userDatePicker.setValue(dateOfBirth.toLocalDate());
                } else {
                    userDatePicker.setValue(null);
                }

                passwordTextField.setText(resultSet.getString("password"));
                String gender = resultSet.getString("gender");
                if (gender != null) {
                    genderComboBox.setValue(gender);
                }
                if (resultSet.getBlob("profileImage") != null) {
                    byte[] imageBytes = resultSet.getBytes("profileImage");
                    Image image = new Image(new ByteArrayInputStream(imageBytes));
                    userImage.setImage(image);
                }
            } else {
                System.out.println("No user found with UserId: " + userId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //update sau khi an saveButton
    void updateUserProfile(String userId) {
        String query = "UPDATE user SET userID = ?, fullName = ?, name = ?, phone = ?, email = ?, dateOfBirth = ?, gender = ?, profileImage = ?, password = ? WHERE userID = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, userIdTextField.getText());
            preparedStatement.setString(2, userFullNameTextField.getText());
            preparedStatement.setString(3, userNameTextField.getText());
            preparedStatement.setString(4, userPhoneNumber.getText());
            preparedStatement.setString(5, userEmail.getText());
            preparedStatement.setDate(6, Date.valueOf(userDatePicker.getValue()));
            preparedStatement.setString(7, genderComboBox.getValue());

            if (selectedImageFile != null) {
                try (FileInputStream imageStream = new FileInputStream(selectedImageFile)) {
                    byte[] imageBytes = Files.readAllBytes(selectedImageFile.toPath());
                    preparedStatement.setBinaryStream(8, new ByteArrayInputStream(imageBytes), imageBytes.length);
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert1("Error", "An error occurred while reading the image.");
                    return;
                }
            } else {
                String getImageQuery = "SELECT profileImage FROM user WHERE userID = ?";
                try (PreparedStatement getImageStmt = connection.prepareStatement(getImageQuery)) {
                    getImageStmt.setString(1, userId);
                    ResultSet rs = getImageStmt.executeQuery();
                    if (rs.next()) {
                        byte[] existingImage = rs.getBytes("profileImage");
                        if (existingImage != null) {
                            preparedStatement.setBinaryStream(8, new ByteArrayInputStream(existingImage), existingImage.length);
                        } else {
                            preparedStatement.setNull(8, java.sql.Types.BLOB);
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            preparedStatement.setString(9, passwordTextField.getText());
            preparedStatement.setString(10, userId);

            preparedStatement.executeUpdate();
            System.out.println("Profile updated successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void deleteButtonOnAction(ActionEvent event) {
        boolean isConfirmed = showAlert("Confirm Delete", "Are you sure you want to delete this user?");

        if (isConfirmed) {
            deleteUserFromDatabase();
        }
    }

    private void deleteUserFromDatabase() {
        // String sql1 = "DELETE FROM borrow WHERE userID = ?";
        //String sql = "DELETE FROM borrow WHERE userID = ?";
        String sql1 = "DELETE FROM user WHERE userID = ?";

        try (Connection connect = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connect.prepareStatement(sql1)) {
            pstmt.setString(1, userIdTextField.getText());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                showAlert1("Success", "User has been successfully deleted.");
                changeScene("AdminUserManagement.fxml", "AdminUserManagement");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error occurred while deleting the user.");
            alert.showAndWait();
        }
    }


    @FXML
    void onSave() {
        SoundPlay.playSound("/soundEffects/SEFE_MouseClick.wav");
        String userID = userIdTextField.getText();
        String fullname = userFullNameTextField.getText();
        String username = userNameTextField.getText();
        String phoneNumber = userPhoneNumber.getText();
        String email = userEmail.getText();
        String gender = genderComboBox.getValue();
        String dateOfBirth = null;
        String password = passwordTextField.getText();
        userId = userID;//wtf cai gi day ma :))
        User.setCurrentUser(new User(userID, username));

        if (userDatePicker.getValue() != null) {
            dateOfBirth = userDatePicker.getValue().toString();
        }
        if (phoneNumber.length() < 10 || !phoneNumber.matches("\\d+")) {
            showAlert1("Lỗi", "PhoneNumber không hợp lệ!");
        } else if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            showAlert1("Lỗi", "Email không hợp lệ!");
        } else if (fullname.isEmpty() || username.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || gender == null || dateOfBirth == null) {
            showAlert1("Loi", "Vui lòng điền đầy đủ thông tin");
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
                    updateUserProfile(userID);
                } else {
                    insertNewUser(fullname, username, phoneNumber, email, dateOfBirth, gender, password);
                    SoundPlay.playSound("/soundEffects/SEFE_Notitfication_Bell.wav");
                }
            }
            setEditable(false);
        }
    }


    //upload anh
    @FXML
    void onUploadPicture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        selectedImageFile = fileChooser.showOpenDialog(null);

        if (selectedImageFile != null) {
            Image image = new Image(selectedImageFile.toURI().toString());
            userImage.setImage(image);
        }
    }

    @FXML
    public void backButtonOnAction(ActionEvent event) {
        SoundPlay.playSound("/soundEffects/SEFE_MouseClick.wav");
        super.changeScene("AdminHomeScene1.fxml","AdminHomeScene1" );
    }
}
