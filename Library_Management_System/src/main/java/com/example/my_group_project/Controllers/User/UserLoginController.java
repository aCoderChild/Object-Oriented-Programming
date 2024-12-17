package com.example.my_group_project.Controllers.User;

import com.example.my_group_project.Controllers.BaseController;
import com.example.my_group_project.Database.DatabaseConnection;
import com.example.my_group_project.Database.LoginDetail;
import com.example.my_group_project.SoundPlay;
import com.example.my_group_project.User.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class UserLoginController extends BaseController {
    @FXML
    private Label loginMessageLabel;
    @FXML
    private Rectangle rectangle;
    @FXML
    private Button loginButton, signupButton;
    @FXML
    private ComboBox<LoginDetail> usernameComboBox;
    @FXML
    private PasswordField enterPasswordField;
    @FXML
    private CheckBox rememberMeCheckBox;

    private String userID;
    public static String userIDMain;

    @FXML
    void adminButtonOnAction(ActionEvent event) {
        SoundPlay.playSound("/soundEffects/SEFE_MouseClick.wav");
        super.changeScene("loginAdmin.fxml", "Admin Login");
    }

    @FXML
    void loginButtonOnAction(ActionEvent event) {
        SoundPlay.playSound("/soundEffects/SEFE_MouseClick.wav");
        super.changeScene("loginUser.fxml", "User Login");
    }

    @FXML
    void signupButtonOnAction(ActionEvent event) {
        SoundPlay.playSound("/soundEffects/SEFE_MouseClick.wav");
        super.changeScene("signUpUser.fxml", "Sign up");
    }

    @FXML
    void forgetButtonOnAction(ActionEvent event) {
        SoundPlay.playSound("/soundEffects/SEFE_MouseClick.wav");
        super.changeScene("forgetPassStage1.fxml", "forget Password");
    }

    @Override
    public void backButtonOnAction(ActionEvent event) {
        SoundPlay.playSound("/soundEffects/SEFE_MouseClick.wav");
        super.changeScene("welcomeToWebsite.fxml", "Hello!");
    }

    @FXML
    void userButtonOnAction(ActionEvent event) {
        loginButton.setVisible(true);
        signupButton.setVisible(true);
        rectangle.setVisible(true);
    }

    @FXML
    private void initialize() {
        // Ensure usernameComboBox is not null before loading details
        if (usernameComboBox != null) {
            loadLoginDetails();
        } else {
            System.out.println("usernameComboBox is null during initialization");
        }
    }

    @FXML
    private void rememberMeCheckBoxOnAction(ActionEvent event) {
        SoundPlay.playSound("/soundEffects/SEFE_MouseClick.wav");
        if (rememberMeCheckBox.isSelected()) {
            String username = usernameComboBox.getEditor().getText();
            String password = enterPasswordField.getText();
            saveLoginDetails(username, password);
        }
    }

    @FXML
    void checkLoginButtonOnAction(ActionEvent event) throws SQLException {
        String username = usernameComboBox.getEditor().getText();
        String password = enterPasswordField.getText();
        if (!username.isBlank() && !password.isBlank()) {
            validateLogin(username, password);
        } else {
            loginMessageLabel.setText("Please enter your username and password");
        }
    }

    public void validateLogin(String username, String password) throws SQLException {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        if (connectDB == null) {
            loginMessageLabel.setText("Failed to connect to the database");
            System.out.println("Connection is null");
        } else {
            String verifyLogin = "SELECT COUNT(1) FROM user WHERE name = ? AND password = ?";
            try (PreparedStatement statement = connectDB.prepareStatement(verifyLogin)) {
                statement.setString(1, username);
                statement.setString(2, password);
                try (ResultSet queryResult = statement.executeQuery()) {
                    if (queryResult.next() && queryResult.getInt(1) == 0) {
                        SoundPlay.playSound("/soundEffects/SEFE_Wrong_Answer.wav");
                        loginMessageLabel.setText("Invalid username or password");
                    } else {
                        loginMessageLabel.setText("Login successful");
                        SoundPlay.playSound("/soundEffects/SEFE_KidsCheering.wav");
                        userID = getUserId(connectDB, username, password);
                        System.out.println("User ID after login: " + userID);
                        userIDMain = userID;
                        try {
                            System.out.println("Bắt đầu gọi constructor User");
                            User.setCurrentUser(new User(userID, username));
                            System.out.println("Kết thúc gọi User constructor");

                        } catch (Exception e) {
                            System.out.println("Lỗi xảy ra: " + e.getMessage());
                            e.printStackTrace();
                        }

                        if (rememberMeCheckBox.isSelected()) {
                            saveLoginDetails(username, password);
                        }
                        toHomeScene();

                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void toHomeScene() {
        super.changeScene("home.fxml", "Home");
    }

    public String getUserId(Connection connectDB, String username, String password) {
        String query = "SELECT userID FROM user WHERE name = ? AND password = ?";
        try (PreparedStatement statement = connectDB.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("userID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void saveLoginDetails(String username, String password) {
        Preferences prefs = Preferences.userNodeForPackage(UserLoginController.class);
        List<LoginDetail> loginDetails = new ArrayList<>();
        try {
            for (String key : prefs.keys()) {
                if (key.startsWith("loginDetail")) {
                    String[] details = prefs.get(key, "").split(":");
                    if (details.length == 2) {
                        loginDetails.add(new LoginDetail(details[0], details[1]));
                    }
                }
            }
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
        boolean exists = false;
        for (LoginDetail detail : loginDetails) {
            if (detail.getUsername().equals(username)) {
                if(!detail.getPassword().equals(password)){
                    prefs.remove(detail.getUsername());
                    System.out.println("Do biet mat khau la gi ne");
                    detail.setPassword(password);
                    prefs.put("loginDetail" + loginDetails.indexOf(detail), detail.getUsername() + ":" + detail.getPassword());
                }
                exists = true;
                break;
            }
        } if (!exists) {
            loginDetails.add(new LoginDetail(username, password));
            for (int i = 0; i < loginDetails.size(); i++) {
                prefs.put("loginDetail" + i, loginDetails.get(i).getUsername() + ":" + loginDetails.get(i).getPassword());
            }
        }
    }

    private void loadLoginDetails() {
        Preferences prefs = Preferences.userNodeForPackage(UserLoginController.class);
        List<LoginDetail> loginDetails = new ArrayList<>();
        try {
            for (String key : prefs.keys()) {
                if (key.startsWith("loginDetail")) {
                    String[] details = prefs.get(key, "").split(":");
                    if (details.length == 2) {
                        loginDetails.add(new LoginDetail(details[0], details[1]));
                    }
                }
            }
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
        ObservableList<LoginDetail> loginDetailList = FXCollections.observableArrayList(loginDetails);
        System.out.println("Loaded login details: " + loginDetails); //debug
        if (usernameComboBox != null) {
            usernameComboBox.setItems(loginDetailList);
            System.out.println("usernameComboBox initialized");
            usernameComboBox.setOnAction(event -> {
                Object selectedItem = usernameComboBox.getSelectionModel().getSelectedItem();
                if (selectedItem instanceof LoginDetail) {
                    LoginDetail selectedDetail = (LoginDetail) selectedItem;
                    enterPasswordField.setText(selectedDetail.getPassword());
                }
            });
            usernameComboBox.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
                // Only clear the password field if the newValue does not match any LoginDetail username
                boolean matchFound = false;
                for (LoginDetail detail : usernameComboBox.getItems()) {
                    if (detail.getUsername().equals(newValue)) {
                        matchFound = true;
                        break;
                    }
                }
                if (!matchFound) {
                    enterPasswordField.clear();
                }
            });
        } else {
            System.out.println("usernameComboBox is null");
        }
    }

    @FXML
    void clickEnter(KeyEvent event) throws SQLException {
        if(event.getCode() == KeyCode.ENTER){
            checkLoginButtonOnAction(null);
        }
    }

    @FXML
    void clickEnter1(KeyEvent event) throws SQLException {
        if(event.getCode() == KeyCode.ENTER){
            checkLoginButtonOnAction(null);
        }
    }
}

