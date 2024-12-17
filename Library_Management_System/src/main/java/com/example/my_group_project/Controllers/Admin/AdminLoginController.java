package com.example.my_group_project.Controllers.Admin;

import com.example.my_group_project.Controllers.BaseController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class AdminLoginController extends BaseController {
    @FXML
    private TextField passwordTextField;

    @FXML
    void adminLogInButtonOnAction(ActionEvent event) {
        if(passwordTextField.getText().equals("amen")) {
            changeScene("AdminHomeScene1.fxml", "AdminHomeScene1");
        } else {
            showAlert("Error", "Sai roi nguoi anh chi em. Thu lai de");
        }
    }

    @FXML
    void clickEnter(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER){
            adminLogInButtonOnAction(null);
        }
    }
}
