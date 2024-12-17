package com.example.my_group_project.Controllers.User;

import com.example.my_group_project.Book.Book;
import com.example.my_group_project.Controllers.BaseController;
import com.example.my_group_project.Database.BookInDatabase;
import com.example.my_group_project.User.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.sql.SQLException;

public class UserBookProfileHighlightController {

    @FXML
    private Label authorTextField;

    @FXML
    private Text contentTextField;

    @FXML
    private Label nameOfBookTextField;

    @FXML
    private ImageView bookImageView;

    @FXML
    private Button highlightButton;


    public void setBookDetails(Book book) {
        if (book == null) {
            System.err.println("Book is null in setBookDetails.");
            return;
        }

        nameOfBookTextField.setText(book.getTitle());
        authorTextField.setText(book.getAuthor());
        contentTextField.setText(book.getDescription());
        UserBookProfileController.setNoImage(bookImageView,book);

        if (UserHighLightController.HIGH_LIGHT) {
            highlightButton.setOnMouseClicked(mouseEvent -> {
                System.out.println("He lo me ne con !!!");
                if (BaseController.showAlert("Hoi lai", "Ban co muon huy luu sach khong")) {
                    try {
                        User currentUser = User.getCurrentUser();
                        if (book != null && currentUser != null) {
                            System.out.println("he looooo");
                            BookInDatabase.deleteBook(book, "DELETE FROM highlightbook WHERE userID = ? AND bookID = ?" );
                            BaseController.showIntro("Ban da huy luu sach thanh cong!", BaseController.getMainStage());
                        } else {
                            System.err.println("Main book or current user is null.");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    mouseEvent.consume();
                }
            });
        }


    }
}
