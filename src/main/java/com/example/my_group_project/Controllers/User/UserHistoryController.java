package com.example.my_group_project.Controllers.User;

import com.example.my_group_project.Book.Book;
import com.example.my_group_project.Controllers.PaginaTion;
import com.example.my_group_project.Database.BookInDatabase;
import com.example.my_group_project.Database.DatabaseConnection;
import com.example.my_group_project.SoundPlay;
import com.example.my_group_project.User.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserHistoryController extends UserMenuController {


    @FXML
    private Pagination paginationHistory;

    private PaginaTion paginaTion = new PaginaTion();

    private String currentUserId = UserLoginController.userIDMain;// Replace with actual user ID handling

    //rivate static List<Book> clickedBooks = new ArrayList<>(getClickedBooksFromDatabase());

    @FXML
    public void initialize() {
        UserHighLightController.HIGH_LIGHT = false;
        //getClickedBooksFromDatabase();
        pagination(getClickedBooksFromDatabase());
    }

    /*public static void addBookToHistory(Book book){
        clickedBooks.add(book);
    }

     */

    /*private Node displayHistory() {
        System.out.println("Goi co chay honggg!!!!");
        VBox pageVBox = new VBox();
        pageVBox.setSpacing(10);
        pageVBox.setPrefWidth(920);
        pageVBox.setPrefHeight(450);
        pageVBox.setLayoutX(56);
        pageVBox.setLayoutY(187);
        pageVBox.setStyle("-fx-background-color: #ffffff;");

        if (clickedBooks.isEmpty()) {
            pageVBox.getChildren().add(new Label("No books clicked yet."));
            return pageVBox;
        }

        for (Book book : clickedBooks) {
            if (book == null) {
                continue;
            }

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/my_group_project/bookProfileHighLight.fxml"));
                Pane bookCard = loader.load();

                UserBookProfileHighlightController controller = loader.getController();
                controller.setBookDetails(book);

                bookCard.setOnMouseClicked(event -> {
                    System.out.println("Book clicked: " + book.getTitle());

                    showBookProfile(book, bookCard);
                });

                pageVBox.getChildren().add(bookCard);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return pageVBox;
    }

     */

    //clicked books: the book borrowed
    public static List<Book> getClickedBooksFromDatabase() {
        List<Book> books = new ArrayList<>();
        System.out.println("Helooo");
        String sql = "SELECT bookID FROM borrow WHERE userID = ? ORDER BY borrowDate DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, User.getCurrentUser().getId());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String bookId = rs.getString("bookID");
                    Book book = BookInDatabase.getBookById(bookId);
                    books.add(book);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }



    void pagination(List<Book> borrowedBooks) {
        System.out.println("Toi đã chạy!!!!");
        paginaTion.pagination(borrowedBooks,paginationHistory);
    }

    @FXML
    public void backButtonOnAction(ActionEvent event) {
        SoundPlay.playSound("/soundEffects/SEFE_MouseClick.wav");
        int current = paginaTion.backButton();
        if(current < 0){
            super.backButtonOnAction(event);
        }else {
            paginationHistory.setCurrentPageIndex(current);
        }
    }


    /*@FXML
   public void logOutOnAction(ActionEvent event) {
        super.logOutButtonOnAction(event);
    }

     */


    /*void pagination(List<Book> clickedBooks) {
        System.out.println("Toi đã chạy!!!!");
        int booksPerPage = 10;
        int pageCount = (int) Math.ceil(clickedBooks.size() / (double) booksPerPage);
        pageCount = Math.max(pageCount, 1);

        paginationHistory.setPageCount(pageCount);
        paginationHistory.setPageFactory((Integer indexPage) -> {
            System.out.println("Hello!!!");
            System.out.println(indexPage);
            int start = indexPage * booksPerPage;
            int end = Math.min(start + booksPerPage, clickedBooks.size());
            List<Book> pageBooks = clickedBooks.subList(start, end);

            Node content = displayHistory();
            return content;
        });
    }

     */
}
