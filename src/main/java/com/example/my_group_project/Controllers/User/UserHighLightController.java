package com.example.my_group_project.Controllers.User;
import  com.example.my_group_project.Database.BookInDatabase;

import com.example.my_group_project.Book.Book;
import com.example.my_group_project.Controllers.BaseController;
import com.example.my_group_project.Controllers.PaginaTion;
import com.example.my_group_project.Database.BookInDatabase;
import com.example.my_group_project.User.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Pagination;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class UserHighLightController extends UserMenuController {
    @FXML
    private Pagination paginationHighLight;

    private User currentUser = User.getCurrentUser();

    private PaginaTion paginaTion = new PaginaTion();
    public static boolean HIGH_LIGHT;

    @FXML
    void initialize() {
        HIGH_LIGHT = true;
        paginaTion.pagination(BookInDatabase.getSavedBooks(), paginationHighLight);
    }

    @FXML
    public void backButtonOnAction (ActionEvent event) {
        int current = paginaTion.backButton();
        if(current < 0){
            super.backButtonOnAction(event);
        }else {
            paginationHighLight.setCurrentPageIndex(current);
        }
    }

}
