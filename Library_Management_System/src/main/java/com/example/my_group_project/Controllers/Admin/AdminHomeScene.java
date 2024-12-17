package com.example.my_group_project.Controllers.Admin;

import com.example.my_group_project.Book.Book;
import com.example.my_group_project.Database.BookInDatabase;
import com.example.my_group_project.Database.DatabaseConnection;
import com.example.my_group_project.User.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.example.my_group_project.Database.BookInDatabase.getBookFromDatabase;

public class AdminHomeScene extends AdminMenuController {
    @FXML
    private VBox vBox;

    @FXML
    private TextField searchTextField;

    @FXML
    private Text numberOfBookText;

    @FXML
    public void initialize() {
        loadViewCount();
        displayBook();
        searchTextField.setOnKeyReleased(event -> filterSearch());
    }

    public void loadViewCount() {
        int x1 = 0, x2 = 0;
        String sql = "SELECT Count(*) as total FROM book";
        String sql2 = "SELECT SUM(viewCount) as total2 FROM book";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                        x1 = resultSet.getInt("total");
                    //String viewCount= String.valueOf(resultSet.getInt("total"));
                    //numberOfBookText.setText(viewCount);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (Connection connect = DatabaseConnection.getConnection();
             PreparedStatement stm = connect.prepareStatement(sql2)) {
            try (ResultSet result = stm.executeQuery()) {
                if (result.next()) {
                    x2 = result.getInt("total2");
                    //String viewCount= String.valueOf(resultSet.getInt("total"));
                    //numberOfBookText.setText(viewCount);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int tt = x1 + x2;
        String viewCount = String.valueOf(tt);
        numberOfBookText.setText(viewCount);
    }

    private void showBook(String bookID) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/my_group_project/AdminBookInformation.fxml"));
            Pane adminHomePane = loader.load();

            AdminBookInformationController adminBookInformationController = loader.getController();

            adminBookInformationController.loadBookInformation(bookID);;
            adminBookInformationController.displayBookBorrow(bookID);
            adminBookInformationController.loadBorrowInformation(bookID);

            Stage stage = (Stage) vBox.getScene().getWindow();
            stage.setScene(new Scene(adminHomePane));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addBookButtonOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/my_group_project/AdminBookInformation.fxml"));
            Pane adminBookInformationPane = loader.load();

            AdminBookInformationController controller = loader.getController();
            controller.setFieldsEditable(true);
            controller.getClear();

            Stage stage = (Stage) vBox.getScene().getWindow();
            stage.setScene(new Scene(adminBookInformationPane));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void displayBook() {
        List<Book> bookList = BookInDatabase.getBookFromDatabase();
        int index = 0;
        if (bookList.isEmpty()) {
            vBox.getChildren().add(new Label("No book found."));
            return;
        } else {
            for (Book book : bookList) {
                if (book == null) {
                    continue;
                }
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/my_group_project/AdminHomePane.fxml"));
                    HBox bookHBox = loader.load();
                    AdminHomePaneController homePaneController = loader.getController();
                    homePaneController.setBookDetail(book);
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
                        showBook(homePaneController.getCurrentBookID());
                    });
                    vBox.getChildren().add(stackPane);
                    index++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void filterSearch() {
        String search= searchTextField.getText();

        List<Book> filterBook = new ArrayList<>();
        for (Book book : BookInDatabase.getBookFromDatabase()) {
            if (book.getId().toLowerCase() != null && book.getId().toLowerCase().contains(search) ||
                    book.getTitle().toLowerCase() != null && book.getTitle().toLowerCase().contains(search) ||
                    book.getAuthor().toLowerCase() != null && book.getAuthor().toLowerCase().contains(search) ||
                    book.getGenre().toLowerCase() != null && book.getGenre().toLowerCase().contains(search) ||
                    book.getYearPublic() != -1 && String.valueOf(book.getYearPublic()).contains(search)){

                filterBook.add(book);
            }
        }
        displayFilterSearch(filterBook);
    }

    private void displayFilterSearch(List <Book> filterBook) {
        vBox.getChildren().clear();

        if(filterBook.isEmpty()) {
            vBox.getChildren().add(new Label("No book found"));
            return;
        }

        int index = 0;
        for (Book book : filterBook) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/my_group_project/AdminHomePane.fxml"));
                HBox bookHBox = loader.load();

                AdminHomePaneController homePane = loader.getController();
                homePane.setBookDetail(book);  // Cập nhật chi tiết người dùng
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

                bookHBox.setOnMouseClicked(event -> {
                    showBook(homePane.getCurrentBookID());
                });
                vBox.getChildren().add(stackPane);
                index++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}