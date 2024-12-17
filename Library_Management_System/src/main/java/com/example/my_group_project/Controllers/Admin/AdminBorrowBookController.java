package com.example.my_group_project.Controllers.Admin;
import com.example.my_group_project.Database.BookInDatabase;
import static com.example.my_group_project.Database.BookInDatabase.getBorrowedBookFromDatabase;
import com.example.my_group_project.Book.BorrowedBook;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;



public class AdminBorrowBookController extends AdminMenuController {


   @FXML
    private Button backButton;

    @FXML
    private Button bookBorrowButton;

    @FXML
    private Button homeScene1Button;

    @FXML
    private Button logOutButton;

    @FXML
    private Button reportButton;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField searchTextField;

    @FXML
    private Button userManagementButton;

    @FXML
    private VBox vBox;

    @FXML
    public void initialize() {
        displayBook();

        searchTextField.setOnKeyReleased(event -> filterSearch());
    }

    private void displayBook() {
        List<BorrowedBook> bookList = BookInDatabase.getBorrowedBookFromDatabase();
        int index = 0;
        if (bookList.isEmpty()) {
            vBox.getChildren().add(new Label("No book found"));
            return;
        } else {
            for (BorrowedBook book : bookList) {
                if (book == null) {
                    continue;
                }
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/my_group_project/AdminBorrowBookPane.fxml"));
                    HBox bookHBox = loader.load();
                    AdminBorrowBookPaneController borrowBookPane = loader.getController();
                    borrowBookPane.setBookDetail(book);
                    final int currentIndex = index;
                    if (currentIndex % 2 == 0) {
                        bookHBox.setStyle("-fx-background-color: #f7efd8;");
                    } else {
                        bookHBox.setStyle("-fx-background-color: #ffffff;");
                    }

                    StackPane stackPane = new StackPane();
                    stackPane.getChildren().add(bookHBox);

                    stackPane.setOnMouseEntered(event -> {
                        bookHBox.setStyle("-fx-background-color: #ffc100; -fx-cursor: hand;");
                    });

                    stackPane.setOnMouseExited(event -> {
                        if (currentIndex % 2 == 0) {
                            bookHBox.setStyle("-fx-background-color: #f7efd8;");
                        } else {
                            bookHBox.setStyle("-fx-background-color: #ffffff;");
                        }
                    });
                    bookHBox.setOnMouseClicked(event -> {
                        showBook(borrowBookPane.getBookId(), borrowBookPane.getUserId());
                    });
                    vBox.getChildren().add(stackPane);
                    index++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void showBook(String bookId, String userId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/my_group_project/AdminBookUserBorrow.fxml"));
            Pane userProfilePane = loader.load();

            AdminBookUserBorrowController bookUserBorrowController = loader.getController();
            bookUserBorrowController.displayUser(userId);
            bookUserBorrowController.displayBook(bookId);

            Stage stage = (Stage) vBox.getScene().getWindow();
            stage.setScene(new Scene(userProfilePane));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void filterSearch() {
        String search = searchTextField.getText();

        List<BorrowedBook> filterBook = new ArrayList<>();
        for (BorrowedBook book : BookInDatabase.getBorrowedBookFromDatabase()) {
            if (book.getId().toLowerCase() != null && book.getId().toLowerCase().contains(search) ||
                    book.getTitle().toLowerCase() != null && book.getTitle().toLowerCase().contains(search) ||
                    book.getUserID().toLowerCase() != null && book.getUserID().toLowerCase().contains(search) ||
                    book.getBorrowDate().toLowerCase() != null && book.getBorrowDate().toLowerCase().contains(search) ||
                    book.getReturnDate().toLowerCase() != null && book.getReturnDate().toLowerCase().contains(search) ||
                    book.getStatus().toLowerCase() != null && book.getStatus().toLowerCase().contains(search)) {
                filterBook.add(book);
            }
        }
        displayFilterSearch(filterBook);
    }

    private void displayFilterSearch(List <BorrowedBook> filterBook) {
        vBox.getChildren().clear();

        if(filterBook.isEmpty()) {
            vBox.getChildren().add(new Label("No book found"));
            return;
        }

        int index = 0;
        for (BorrowedBook book : filterBook) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/my_group_project/AdminBorrowBookPane.fxml"));
                HBox bookHBox = loader.load();

                AdminBorrowBookPaneController borrowBookPane = loader.getController();
                borrowBookPane.setBookDetail(book);  // Cập nhật chi tiết người dùng
                final int currentIndex = index;
                if (currentIndex % 2 == 0) {
                    bookHBox.setStyle("-fx-background-color: #f7efd8;");  // Màu nền cho dòng chẵn
                } else {
                    bookHBox.setStyle("-fx-background-color: #ffffff;");  // Màu nền cho dòng lẻ
                }

                StackPane stackPane = new StackPane();
                stackPane.getChildren().add(bookHBox);

                stackPane.setOnMouseEntered(event -> {
                    bookHBox.setStyle("-fx-background-color: #ffc100; -fx-cursor: hand;");
                });

                stackPane.setOnMouseExited(event -> {
                    if (currentIndex % 2 == 0) {
                        bookHBox.setStyle("-fx-background-color: #f7efd8;");
                    } else {
                        bookHBox.setStyle("-fx-background-color: #ffffff;");
                    }
                });
                /*bookHBox.setOnMouseClicked(event -> {
                    showUser(userManagementPane.getUserId());
                });*/
                vBox.getChildren().add(stackPane);
                index++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void showBorrow(String bookId, String userId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/my_group_project/AdminBookUserBorrow.fxml"));
            Pane userProfilePane = loader.load();

            AdminBookUserBorrowController bookUserBorrowController = loader.getController();



            Stage stage = (Stage) vBox.getScene().getWindow();
            stage.setScene(new Scene(userProfilePane));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void backButtonOnAction(ActionEvent event) {
        super.changeScene("AdminHomeScene1.fxml","AdminHomeScene1" );
    }


    // load pane san co
    //thuc hien tim kiem nhanh cho thanh search
    // add them sach theo pane( da noi o home scene1 )

}
