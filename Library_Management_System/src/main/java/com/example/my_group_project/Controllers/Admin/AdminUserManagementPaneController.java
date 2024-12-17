package com.example.my_group_project.Controllers.Admin;
import com.example.my_group_project.User.User;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class AdminUserManagementPaneController {
    @FXML
    private Label dateOfBirth;

    @FXML
    private Label email;

    @FXML
    private Label gender;

    @FXML
    private HBox hbox;

    @FXML
    private Button moreButton;

    @FXML
    private Label phoneNumber;

    @FXML
    private Label userId;

    @FXML
    private Label userName;

    public static String currentIdUser ;

    @FXML
    public void setUserDetail(User user) {
        if (user == null) {
            System.out.println("User is null.");
            return;
        }
        userId.setText(user.getId());
        userName.setText(user.getUsername());
        email.setText(user.getEmail());
        phoneNumber.setText(user.getPhone());
        dateOfBirth.setText(user.getDateOfBirth());
        gender.setText(user.getGender());
    }

    public String getUserId() {
        currentIdUser = userId.getText();
        return currentIdUser;
    }
}
