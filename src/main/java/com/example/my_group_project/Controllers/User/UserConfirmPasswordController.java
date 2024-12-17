package com.example.my_group_project.Controllers.User;

import com.example.my_group_project.Controllers.BaseController;
import com.example.my_group_project.Database.DatabaseConnection;
import com.example.my_group_project.SoundPlay;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserConfirmPasswordController extends BaseController {

    @FXML
    private TextField passTextField;

    @FXML
    private TextField ConfirmPassField;

    @FXML
    private Label differPassword;

    @FXML
    private Label rightPassword;


    boolean fillIn() {
        String password = passTextField.getText();
        String confirmPass = ConfirmPassField.getText();
        rightPassword.setStyle("-fx-text-fill: red;");
        differPassword.setStyle("-fx-text-fill: red;");

        if (!password.isEmpty() && !confirmPass.isEmpty()) {
            if (!UserRegisterController.checkUserPassword(password).isEmpty()) {
                differPassword.setVisible(false);
                rightPassword.setVisible(true);
                rightPassword.setText(UserRegisterController.checkUserPassword(password));
            } else {
                if (!password.equals(confirmPass)) {
                    rightPassword.setVisible(false);
                    differPassword.setVisible(true);
                    differPassword.setText("*Mat khau khong trung nhau!");
                }else {
                    addPassToData(confirmPass);
                    return true;
                }
            }
        } else {
            if (confirmPass.isEmpty()) {
                differPassword.setText("* Khong duoc de trong!");
                differPassword.setVisible(true);
            } else if (password.isEmpty()) {
                rightPassword.setVisible(true);
                rightPassword.setText("*Khong duoc de trong!");
            }
        }
        return false;
    }

    void addPassToData(String password) {
        //String query = "INSERT INTO user (password) VALUES ? WHERE User_ID = ?";
        System.out.println(" update passsss");

        String query = "UPDATE user SET password = ? WHERE userID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, password);
            preparedStatement.setString(2, UserForgetPasswordController.getUserID());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void clickEnter(KeyEvent event) {
        SoundPlay.playSound("/soundEffects/SEFE_MouseClick.wav");
        if(event.getCode() == KeyCode.ENTER) {
            nextButtonAction(null);
        }
    }

    @FXML
    void clickEnter1(KeyEvent event) {
        SoundPlay.playSound("/soundEffects/SEFE_MouseClick.wav");
        if(event.getCode() == KeyCode.ENTER) {
            nextButtonAction(null);
        }
    }


    @FXML
    void backButtonAction(ActionEvent event) {
        if (!passTextField.getText().isEmpty() || !ConfirmPassField.getText().isEmpty()) {
            if (showAlert("Thong bao", "Ban chac chan muon roi di chu, khi ban chap nhan thong tin se khong luu lai")) {
                super.backButtonOnAction(event);
            } else {
                event.consume();
            }
        }

    }

    @FXML
    void nextButtonAction(ActionEvent event) {
        if (fillIn()) {
            UserProfileUserFormController.showAlert("Thanh cong", "Ban da doi mat khau thanh cong!");
            super.changeScene("logInUser.fxml", "Log In");
        }
    }

}
