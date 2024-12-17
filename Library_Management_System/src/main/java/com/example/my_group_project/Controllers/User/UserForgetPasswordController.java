package com.example.my_group_project.Controllers.User;

import com.example.my_group_project.Controllers.BaseController;
import com.example.my_group_project.Database.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserForgetPasswordController extends BaseController {
    @FXML
    private TextField nameTextField;

    private static String userID;

    private static String userEmail;

    public static String getUserID() {
        return userID;
    }

    public static String getUserEmail() {
        return userEmail;
    }

    @FXML
    private boolean getUserName() throws SQLException {
        String userName = nameTextField.getText();

        if (checkInformation(userName, "SELECT COUNT(name) FROM user WHERE name = ?")) {
            UserProfileUserFormController.showAlert1("Khong hop le", "Khong ton tai user name nay");
            return false;
        } else {
            Connection connection = DatabaseConnection.getConnection();
            getUserID(connection,userName);
            return true;
        }

    }

    private boolean checkInformation(String infor, String message) {
        try (Connection connectDB = DatabaseConnection.getConnection();
             PreparedStatement checkStatement = connectDB.prepareStatement(message)) {
            checkStatement.setString(1, infor);
            ResultSet resultSet1 = checkStatement.executeQuery();
            if (resultSet1.next() && resultSet1.getInt(1) > 0) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @FXML
    void nextButtonOnAction(ActionEvent event) throws IOException, SQLException {
        if (getUserName()) {
            super.changeScene("emailSend.fxml", "Test");
        } else {
            UserProfileUserFormController.showAlert1("Khong hop le", "Khong ton tai user name nay");
        }
    }

    public static void getUserID(Connection connectDB, String username) {
        String query = "SELECT userID,email FROM user WHERE name = ? ";
        try (PreparedStatement statement = connectDB.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                userID = resultSet.getString("userID");
                userEmail = resultSet.getString("email");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void clickEnter(KeyEvent event) throws IOException, SQLException {
        if (event.getCode() == KeyCode.ENTER) {
            nextButtonOnAction(null);
        }
    }
}
