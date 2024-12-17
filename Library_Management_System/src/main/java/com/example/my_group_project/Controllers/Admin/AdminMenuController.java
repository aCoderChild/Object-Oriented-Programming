package com.example.my_group_project.Controllers.Admin;
import com.example.my_group_project.Controllers.Admin.AdminMenuController;
import com.example.my_group_project.HelloApplication;

import com.example.my_group_project.Controllers.BaseController;
import com.example.my_group_project.SoundPlay;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javax.swing.*;//ua sao co swing
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public abstract class AdminMenuController extends BaseController {
    @FXML
    void homeScene1ButtonOnAction(ActionEvent event) {
        SoundPlay.playSound("/soundEffects/SEFE_MouseClick.wav");
        super.changeScene("AdminHomeScene1.fxml","AdminHomeScene1");
    }

    @FXML
    void bookBorrowButtonOnAction(ActionEvent event) throws IOException {
        SoundPlay.playSound("/soundEffects/SEFE_MouseClick.wav");
        super.changeScene("AdminBorrowBook.fxml", "AdminBorrowBook");
    }

    @FXML
    void userManagementButtonOnAction(ActionEvent event) {
        SoundPlay.playSound("/soundEffects/SEFE_MouseClick.wav");
        super.changeScene("AdminUserManagement.fxml", "AdminUserManagement");
    }

    @FXML
    void reportButtonOnAction(ActionEvent event) throws IOException {
        SoundPlay.playSound("/soundEffects/SEFE_MouseClick.wav");
        super.changeScene("AdminReport.fxml", "AdminReport");
    }

    @FXML
    void logOutButtonOnAction(ActionEvent event) {
        if (showAlert("Dang xuat", "Ban muon dang xuat chu???")) {
            try {
                HelloApplication.saveRecentBooksToDatabase();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            changeScene("welcomeToWebsite.fxml", "HelloView");
        } else {
            event.consume();
        }
    }
}