package com.example.my_group_project.Controllers.Admin;

import com.example.my_group_project.Book.BorrowedBook;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class AdminBookInformationPaneController {
    @FXML
    private Label userID;

    @FXML
    private Label bookID;


    @FXML
    private Label borrowDate;

    @FXML
    private Label returnDate;

    @FXML
    private Label status;

    @FXML
    public void setBorrowBookDetail(BorrowedBook brBook) {
        if (brBook == null) {
            return;
        }
        userID.setText(brBook.getUserID());
        bookID.setText(brBook.getId());
        borrowDate.setText(brBook.getBorrowDate());
        returnDate.setText(brBook.getReturnDate());
        status.setText(brBook.getStatus());
    }

    public String getUserId() {
        return userID.getText();
    }
    public String getBookId() { return bookID.getText();}
}
