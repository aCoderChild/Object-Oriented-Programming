package com.example.my_group_project.Controllers.User;

import com.example.my_group_project.Book.Book;
import com.example.my_group_project.Book.Searching.BookAPI;
import com.example.my_group_project.Book.Searching.BookRecommendation;
import com.example.my_group_project.Book.Searching.SearchCache;
import com.example.my_group_project.Controllers.BaseController;
import com.example.my_group_project.Database.BookInDatabase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.List;

public class UserBookSearch extends BaseController {
    @FXML
    private TextField searchTextField;

    @FXML
    private VBox resultsVBox;

    @FXML
    private Button searchButton;

    // Create an instance of SearchCache
    private SearchCache searchCache = new SearchCache();

    // Create an instance of BookRecommendation
    private BookRecommendation bookRecommendation = new BookRecommendation();

    @FXML
    void searchButtonAction(ActionEvent event) {
        searchBooks();
    }

    @FXML
    void searchTextFieldOnAction(ActionEvent event) {
        long startTime = System.currentTimeMillis(); // Start time measurement
        searchBooks();
        long endTime = System.currentTimeMillis(); // End time measurement
        long executionTime = endTime - startTime;
        System.out.println("Execution time: " + executionTime + " ms");
    }

    private void searchBooks() {
        String query = searchTextField.getText().trim();
        // Check if the query is empty
        if (query.isEmpty()) {
            resultsVBox.getChildren().clear();
            return;
        }

        // Check cache first
        if (searchCache.contains(query)) {
            System.out.println("Fetching books from cache for query: " + query);
            List<Book> cachedBooks = searchCache.get(query);
            displaySearchResults(cachedBooks);
            return;
        }

        // Search using the API
        List<Book> books;
        try {
            books = BookAPI.searchBooks(query);
            // Cache the results
            searchCache.put(query, books);
            System.out.println("Books fetched from API and cached for query: " + query);
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
            return;
        }

        // Enhance search results using AI-based recommendation
        List<Book> recommendedBooks = bookRecommendation.recommendBooks(query, books);

        // Display search results
        displaySearchResults(recommendedBooks);
    }

    private void displaySearchResults(List<Book> books) {
        resultsVBox.getChildren().clear();
        for (Book book : books) {
            HBox bookInfoBox = new HBox(10);

            Text bookInfo = new Text(book.getTitle() + " by " + book.getAuthor());
            bookInfo.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14px; -fx-fill: #333333;");

            if (book.getImageUrl() != null) {
                try {
                    Image image = new Image(book.getImageUrl());
                    ImageView bookImage = new ImageView(image);
                    bookImage.setFitHeight(50);
                    bookImage.setFitWidth(50);
                    bookInfoBox.getChildren().add(bookImage);
                } catch (Exception e) {
                    System.out.println("Failed to load image: " + e.getMessage());
                }
            }

            try {
                BookInDatabase.insertBook(book);
            } catch (SQLException e) {
                e.printStackTrace();  // You can handle the exception as needed
            }


            bookInfoBox.getChildren().add(bookInfo);

            bookInfoBox.setOnMouseClicked(event -> {
                try {
                    UserHomeController.bookProfile(bookInfoBox,book);
                    /*FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/my_group_project/bookProfile.fxml"));
                    Parent root = loader.load();

                    try {
                        UserBookProfileController controller = loader.getController();
                        controller.setBookDetails(book);
                    } catch (SQLException e) {
                        e.printStackTrace();  // Handle the exception as needed
                    }

                    Stage stage = (Stage) resultsVBox.getScene().getWindow();
                    stage.setTitle("Book Profile");
                    stage.setScene(new Scene(root));

                     */
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

            resultsVBox.getChildren().add(bookInfoBox);
        }
    }
}