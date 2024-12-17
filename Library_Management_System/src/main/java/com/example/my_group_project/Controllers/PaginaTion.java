package com.example.my_group_project.Controllers;

import com.example.my_group_project.Book.Book;
import com.example.my_group_project.Controllers.User.UserBookProfileHighlightController;
import com.example.my_group_project.Controllers.User.UserHistoryController;
import com.example.my_group_project.Controllers.User.UserHomeController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class PaginaTion {

    private int currentPage;
    private static final int MAX_PAGE = 10;


    private Node displayHistory(List<Book> clickedBooks) {
        System.out.println("Display history called.");

        VBox pageVBox = new VBox();
        pageVBox.setSpacing(10);
        pageVBox.setPrefSize(920, 450);
        pageVBox.setLayoutX(56);
        pageVBox.setLayoutY(187);
        pageVBox.setStyle("-fx-background-color: #ffffff;");

        if (clickedBooks.isEmpty()) {
            pageVBox.getChildren().add(new Label("No books clicked yet."));
            return pageVBox;
        }

        for (Book book : clickedBooks) {
            if (book == null) continue;

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/my_group_project/bookProfileHighLight.fxml"));
                Pane bookCard = loader.load();

                UserBookProfileHighlightController controller = loader.getController();
                controller.setBookDetails(book);

                bookCard.setOnMouseClicked(event -> {
                    System.out.println("Book clicked: " + book.getTitle());
                    try {
                        UserHomeController.bookProfile(bookCard, book);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

                pageVBox.getChildren().add(bookCard);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return pageVBox;
    }

    /*public void showBookProfile(Book book, Pane pane) {
        try {
            Book.setMainBook(book);
            FXMLLoader loader = new FXMLLoader(UserHistoryController.class.getResource("/com/example/my_group_project/bookProfile.fxml"));
            Pane root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) pane.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(book.getTitle());
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

     */

    public void pagination(List<Book> recentBooks, Pagination paginationHistory) {
        System.out.println("Ec ec chay di");

        int booksPerPage = MAX_PAGE;
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(920, 450);
        scrollPane.setLayoutX(56);
        scrollPane.setLayoutY(187);
        scrollPane.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        int pageCount = (int) Math.ceil((double) recentBooks.size() / booksPerPage);
        pageCount = Math.max(pageCount, 1);

        paginationHistory.setPageCount(pageCount);
        paginationHistory.setPageFactory((Integer indexPage) -> {
            System.out.println("Page index: " + indexPage);

            int start = indexPage * booksPerPage;
            int end = Math.min(start + booksPerPage, recentBooks.size());

            List<Book> pageBooks = recentBooks.subList(start, end);
            currentPage = indexPage;

            Node content = displayHistory(pageBooks);
            scrollPane.setContent(content);
            scrollPane.setFitToWidth(true);

            return scrollPane;
        });
    }

    public int backButton() {
        return --currentPage;
    }
}