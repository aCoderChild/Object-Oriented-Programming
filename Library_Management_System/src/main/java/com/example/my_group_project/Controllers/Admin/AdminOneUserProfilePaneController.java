package com.example.my_group_project.Controllers.Admin;
import com.example.my_group_project.Book.BorrowedBook;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class AdminOneUserProfilePaneController {
    @FXML
    private Label bookId;

    @FXML
    private HBox hBox;

    @FXML
    private Label bookName;

    @FXML
    private Label dueDate;

    @FXML
    private Label endDate;

    @FXML
    private Label status;

    @FXML
    private Label userId;

    @FXML
    public void setUserBorrowDetail(BorrowedBook brBook) {
        if (brBook == null) {
            return;
        }
        userId.setText(brBook.getUserID());
        status.setText(brBook.getStatus());
        endDate.setText(brBook.getBorrowDate());
        dueDate.setText(brBook.getReturnDate());
        bookId.setText(brBook.getId());
        bookName.setText(brBook.getTitle());
    }

    public String getUserId() {
        return userId.getText();
    }

    public String getBookId() {
        return bookId.getText();
    }

}
