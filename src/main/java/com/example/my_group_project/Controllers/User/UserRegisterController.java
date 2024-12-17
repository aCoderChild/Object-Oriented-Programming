package com.example.my_group_project.Controllers.User;
import com.example.my_group_project.SoundPlay;

import com.example.my_group_project.Database.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.IOException;
import java.sql.*;


public class UserRegisterController {

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField setPasswordTextField;

    @FXML
    private TextField setEmailTextField;

    @FXML
    private TextField setPhoneTextField;

    @FXML
    private Button registerButton;

    @FXML
    private Button loginButton;

    @FXML
    private Label registrationMessageLabel;

    @FXML
    private Label userNameLabel;

    @FXML
    private Button backButton;

    @FXML
    private Label passwordLabel;

    @FXML
    private Label checkEmail;

    @FXML
    private Label checkPhone;

    @FXML
    void loginButtonOnAction(ActionEvent event) throws IOException {
        System.out.print("nn");
        System.out.print("nn");
        Stage stage = (Stage) loginButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/my_group_project/loginUser.fxml"));
        stage.setTitle("Login");
        stage.setScene(new Scene(root));
    }

    @FXML
    void registerButtonOnAction(ActionEvent event) throws SQLException {
        boolean isValidEmail = setEmailTextField.getText().contains("@gmail.com");
        boolean isValidPhone = setPhoneTextField.getText().matches("\\d+");

        if (!usernameTextField.getText().isBlank() &&
                !setPasswordTextField.getText().isBlank() &&
                isValidEmail &&
                isValidPhone) {
            registerUser();
        } else {
            registrationMessageLabel.setText("Invalid input! Please check your details.");
        }
    }

    public void registerUser() throws SQLException {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String userName = usernameTextField.getText();
        String password = setPasswordTextField.getText();
        String email = setEmailTextField.getText();
        String phone = setPhoneTextField.getText();

        String randomIDQuery = "SELECT generateRandomID(10) AS randomID";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet resultSet = statement.executeQuery(randomIDQuery);
            resultSet.next();
            String userID = resultSet.getString("randomID");

            if (!checkUserName(userName) || !checkUserPassword(password).isEmpty() || !checkEmail(email) ||
                    !checkPhoneNumber(phone)) {
                if (!checkUserName(userName)) {
                    userNameLabel.setText("* Name user already exists!");
                    userNameLabel.setVisible(true);

                } else userNameLabel.setVisible(false);

                if (!checkUserPassword(password).isEmpty()) {
                    passwordLabel.setText(checkUserPassword(password));
                    passwordLabel.setVisible(true);

                } else passwordLabel.setVisible(false);

                if (!checkEmail(email)) {
                    checkEmail.setText("* Email already exists!");
                    checkEmail.setVisible(true);

                } else checkEmail.setVisible(false);

                if (!checkPhoneNumber(phone)) {
                    checkPhone.setText("* Phone number is not valid!");
                    checkPhone.setVisible(true);

                } else checkPhone.setVisible(false);

            } else {

                String insertQuery = "INSERT INTO user (userID, name, password, email, phone) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connectDB.prepareStatement(insertQuery);
                preparedStatement.setString(1, userID);
                preparedStatement.setString(2, userName);

                preparedStatement.setString(3, password);
                preparedStatement.setString(4, email);
                preparedStatement.setString(5, phone);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    registrationMessageLabel.setText("User has been registered successfully!");
                    toLogInScene();

                } else {
                    registrationMessageLabel.setText("Registration failed. Please try again.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Kiem tra xem sdt co hop le khong
    public boolean checkPhoneNumber(String phone) {
        if (phone.length() < 10 || !phone.matches("\\d+")) {
            return false;
        }
        return true;
    }

    //Kiểm tra xem email tồn tại hay khong
    public boolean checkEmail(String email) throws SQLException {
        String query = "SELECT COUNT(email) FROM user WHERE email = ?";
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, email);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            showAlert("Lỗi", "Email không hợp lệ!");
            return false;
        }
        if (resultSet.next() && resultSet.getInt(1) > 0) {
            return false;
        }
        return true;
    }

    @FXML
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().setStyle("-fx-background-color: #f7efd8; -fx-border-radius: 20px;");
        String cssPath = UserProfileUserFormController.class.getResource("/custom.css").toExternalForm();
        alert.getDialogPane().getStylesheets().add(cssPath);
        alert.showAndWait();
    }

    //kiem tra password co hop le khong
    public static String checkUserPassword(String password) {
        String index = "?!@%^$&*";
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String number = "0123456789";

        if (password.length() < 6) {
            return "* Password cannot be shorter than 6 characters.";
        }

        boolean hasSpecialChar = false;
        boolean hasLowerCase = false;
        boolean hasUpperCase = false;
        boolean hasNumber = false;

        for (int i = 0; i < password.length(); i++) {
            char currentChar = password.charAt(i);

            if (index.contains(String.valueOf(currentChar))) {
                hasSpecialChar = true;

            } else if (lowerCase.contains(String.valueOf(currentChar))) {
                hasLowerCase = true;

            } else if (upperCase.contains(String.valueOf(currentChar))) {
                hasUpperCase = true;
            } else if (number.contains(String.valueOf(currentChar))) {
                hasNumber = true;

            }
        }

        if (!hasSpecialChar) {
            return "* Password must have at least 1 special character!";
        }
        if (!hasLowerCase) {
            return "* Password must contain lowercase characters!";
        }
        if (!hasUpperCase) {
            return "* Password must contain uppercase characters!";
        }
        if (!hasNumber) {
            return "* Password must contain number!";
        }

        return "";
    }

    //kiem tra xem ten da ton tai hay chua
    public boolean checkUserName(String userName) throws SQLException {
        //boolean nameIsCheck = false;
        Connection connectDB = DatabaseConnection.getConnection();
        String nameQuery = "SELECT COUNT(name) FROM user WHERE name = ?";
        PreparedStatement checkStatement = connectDB.prepareStatement(nameQuery);
        checkStatement.setString(1, userName);
        ResultSet resultSet1 = checkStatement.executeQuery();

        if (resultSet1.next() && resultSet1.getInt(1) > 0) {
            return false;
        }
        return true;
    }

    public void toLogInScene() throws IOException {
        Stage stage = (Stage) registerButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/my_group_project/loginUser.fxml"));
        stage.setTitle("Login Again");
        stage.setScene(new Scene(root));
    }

    public void backButtonOnAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/my_group_project/welcomeToWebsite.fxml"));
        stage.setTitle("Hello!");
        stage.setScene(new Scene(root));
    }


    @FXML
    void clickEnter(KeyEvent event) throws SQLException {
        if(event.getCode() == KeyCode.ENTER){
            registerUser();
        }
    }
}

