package com.example.my_group_project.Controllers.User;

import com.example.my_group_project.Book.Book;
import com.example.my_group_project.Controllers.BaseController;
import com.example.my_group_project.Database.BookInDatabase;
import com.example.my_group_project.Database.DatabaseConnection;
import com.example.my_group_project.User.User;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class UserHomeController extends UserMenuController {

    @FXML
    private Label nameOfPopular;

    @FXML
    private Text descriptionHere;

    @FXML
    private Label authorBook;

    @FXML
    private HBox booksHere;

    @FXML
    private VBox bookTableVbox;

    @FXML
    private HBox hBoxBooks;

    @FXML
    private ImageView imageBooks;


    //popularBook
    void suggestListBooks(List<Book> books) throws IOException, SQLException {
        if (books == null || books.isEmpty()) {
            System.out.println("No suggested books available.");
            return; // Exit the method if there are no books
        }

        int numberOfBooks = Math.min(books.size(), 10); // Ensure we don't access more than the number of available books

        for (int i = 0; i < numberOfBooks; i++) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/my_group_project/bookRequest.fxml"));
            Pane book = fxmlLoader.load();
            Book newBook = books.get(i);
            ImageView image = (ImageView) book.lookup("#setImageBook");
            Label description = (Label) book.lookup("#setContentBook");
            Label title = (Label) book.lookup("#setPageView");

            description.setText(newBook.getDescription());
            title.setText(newBook.getTitle());
            UserBookProfileController.setNoImage(image,newBook);

            book.setOnMouseClicked(mouseEvent -> {
                try {
                    bookProfile(book, newBook);
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            });

            booksHere.getChildren().add(book);
        }
    }

    //latestbooks
    void latestBooks(List<Book> books) throws IOException, SQLException {
        if (books == null || books.isEmpty()) {
            System.out.println("No latest books available.");
            return; // Exit the method if there are no books
        }

        int numberOfBooks = Math.min(books.size(), 10); // Ensure we don't access more than the number of available books

        for (int i = 0; i < numberOfBooks; i++) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/my_group_project/bookRequest.fxml"));
            Pane book = fxmlLoader.load();
            Book newBook = books.get(i);
            ImageView image = (ImageView) book.lookup("#setImageBook");
            Label description = (Label) book.lookup("#setContentBook");
            Label title = (Label) book.lookup("#setPageView");

            description.setText(newBook.getDescription());
            title.setText(newBook.getTitle());
            UserBookProfileController.setNoImage(image,newBook);

            book.setOnMouseClicked(mouseEvent -> {
                try {
                    bookProfile(book, newBook);
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            });

            hBoxBooks.getChildren().add(book);
        }
    }

    //recentBooks
    void recentBooks(List<Book> books) throws IOException {
        //System.out.println(books);
        if(books.isEmpty()){
            bookTableVbox.getChildren().add(new Text("Chưa có sách truy cập gần đây"));
        }else {
            for (int i = 0; i < books.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/my_group_project/bookRelative.fxml"));
                Pane book = fxmlLoader.load();
                Book newbook = books.get(i);

                ImageView image = (ImageView) book.lookup("#setImageBook");
                Label author = (Label) book.lookup("#setAuthor");
                Label title = (Label) book.lookup("#nameOfBook");
                Label category = (Label) book.lookup("#setCategory");
                Label borrow = (Label) book.lookup("#numberOfBorrow");
                Label read = (Label) book.lookup("#numberOfRead");

                UserBookProfileController.setNoImage(image,newbook);
                author.setText(newbook.getAuthor());
                title.setText(newbook.getTitle());
                category.setText(newbook.getGenre());
                borrow.setText(String.valueOf(newbook.getBorrowCount()));
                read.setText(String.valueOf(newbook.getViewCount()));

                book.setOnMouseClicked(mouseEvent -> {
                    try {
                        bookProfile(book, newbook);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
                bookTableVbox.getChildren().add(book);
            }
        }
    }

    //book popular
    void getPopularBook() throws SQLException {
        List<Book> books = BookInDatabase.getListOfBooks();
        if (books.isEmpty()) {
            // Handle the case where there are no books
            nameOfPopular.setText("No popular book available");
            descriptionHere.setText("");
            authorBook.setText("");
            imageBooks.setImage(null);
        } else {
            Book popularBook = books.get(0);
            nameOfPopular.setText(popularBook.getTitle());
            descriptionHere.setText(popularBook.getDescription());
            authorBook.setText(popularBook.getAuthor());
            UserBookProfileController.setNoImage(imageBooks,popularBook);
            imageBooks.setOnMouseClicked(mouseEvent -> {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/my_group_project/bookProfile.fxml"));
                    Parent root = fxmlLoader.load();
                    Stage stage = (Stage) imageBooks.getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setTitle("Books");
                    stage.setScene(scene);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    //hien ca
    @FXML
    void initialize() {
        try {
            showIntro("Chao mung ban!", getMainStage());
            getPopularBook();
            suggestListBooks(BookInDatabase.getListOfBooks());
            latestBooks(BookInDatabase.getListOfBooks());
            //chu y cho nay
            recentBooks(User.getCurrentUser().getRecentBook().getRecentBooks());
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }


    //hien ra main books
    @FXML
    public static void bookProfile(Pane pane, Book book) throws IOException, SQLException {
        //book = checkBook(book);

        if (!BookInDatabase.isBookInDatabase(book)) {
            System.out.println("D co id ne"); //amen
            BookInDatabase.insertBook(book);
        } else {
            book.setViewCount(book.getViewCount() + 1);
            BookInDatabase.updateViewCount(book.getId(),
                    "UPDATE book SET viewCount = viewCount + 1 WHERE bookID = ?");
        }
        Book.setMainBook(book);
        changeScene("bookProfile.fxml",book.getTitle());
        /*FXMLLoader fxmlLoader = new FXMLLoader(UserHomeController.class.getResource("/com/example/my_group_project/bookProfile.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) pane.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Book");
        stage.setScene(scene);

         */
    }


    //hien "Chao mung ban " sau khi dang nhap vao
    //gap loi luc nao vao home la cu hien TT
    public static void showIntro(String message, Stage stage){
        Popup popup = new Popup();
        popup.setAutoHide(true);

        Label popupContent = new Label(message);
        popupContent.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-padding: 10px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        popupContent.setFont(new Font("Arial", 14));
        popupContent.setTextFill(Color.WHITE);

        Pane pane = new Pane(popupContent);
        pane.setStyle("-fx-background-color: white ");
        popup.getContent().add(pane);
        popup.show(stage);
        popup.setX(stage.getX() + stage.getWidth() / 2 );
        popup.setY(stage.getY() + stage.getHeight() / 2);
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(10), popupContent);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(event -> popup.hide());

        fadeOut.play();
    }
}
