package com.example.my_group_project.Controllers.Admin;
import com.example.my_group_project.Book.Book;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import com.example.my_group_project.Book.BorrowedBook;

public class AdminBorrowBookPaneController {

    @FXML
    private Label bookId;

    @FXML
    private Label bookName;

    @FXML
    private Label borrowDate;

    @FXML
    private Label returnDate;

    @FXML
    private Label status;

    @FXML
    private Label userId;

    @FXML
    public void setBookDetail(BorrowedBook book) {
        if (book == null) {
            System.out.println("Book is null.");
            return;
        }
        bookId.setText(book.getId());
        bookName.setText(book.getTitle());
        userId.setText(book.getUserID());
        borrowDate.setText(book.getBorrowDate());
        returnDate.setText(book.getReturnDate());
        status.setText(book.getStatus());
    }

    public String getUserId() {
        return userId.getText();
    }

    public String getBookId() {
        return bookId.getText();
    }
}
