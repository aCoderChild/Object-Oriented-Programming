package com.example.my_group_project.Controllers.User;

import com.example.my_group_project.Book.Book;
import com.example.my_group_project.Book.Searching.BookAPI;
import com.example.my_group_project.Controllers.BaseController;
import com.example.my_group_project.Database.BookInDatabase;
import com.example.my_group_project.Database.DatabaseConnection;
import com.example.my_group_project.SoundPlay;
import com.example.my_group_project.User.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Stack;

public class UserBookProfileController extends UserMenuController {
    @FXML
    private Text content;

    @FXML
    private Label nameOfAuthor;

    @FXML
    private Label nameOfBook;

    @FXML
    private Text numberOfBorrow;

    @FXML
    private Text pageViews;

    @FXML
    private ImageView bookImageView;

    @FXML
    private ImageView highlightImage;

    @FXML
    private VBox bookTableVbox;



    private Book currentBook = Book.getMainBook(); // Hold the current book being viewed
    private User currentUser = User.getCurrentUser();

    private static Stack<Book> bookHistory = new Stack<>();


    public void addBookHistory(Book book) {
        if (!bookHistory.isEmpty() && bookHistory.peek().equals(book)) {
            return;
        }
        bookHistory.push(book);
    }

    public Book getPreviousBook() {
        if (!bookHistory.isEmpty()) {
            return bookHistory.pop();
        }
        return null;
    }

    @FXML
    void clickToSaveBook(MouseEvent event) {
        System.out.println("Luu ne !!!!"); //kho lam moi luu dc a :(
        Image currentImage = highlightImage.getImage();
        Image previousImage = new Image(getClass().getResource("/picture/remember.png").toExternalForm());
        Image lateImage = new Image(getClass().getResource("/picture/remembered.png").toExternalForm());
        if (currentImage != null && currentImage.getUrl() != null) {
            if (currentImage.getUrl().contains("remembered.png")) {
                //hien la ban co chac muon huy luu sach khong
                if (showAlert("Hoi lai", "Ban co muon huy luu sach khong")) {
                    try {
                        BookInDatabase.deleteBook(currentBook, "DELETE FROM highlightbook WHERE userID = ? AND bookID = ?");
                        SoundPlay.playSound("/soundEffects/SEFE_Painful_Scream.wav");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    highlightImage.setImage(previousImage);
                    highlightImage.setFitWidth(21);
                    highlightImage.setFitHeight(23);
                    highlightImage.setPreserveRatio(true);
                    showIntro("Ban da huy luu sach thanh cong!", getMainStage());
                } else {
                    event.consume();
                }
            } else if (currentImage.getUrl().contains("remember.png")) {
                highlightImage.setImage(lateImage);
                highlightBook();
                UserHomeController.showIntro("Ban da luu sach thanh cong!", getMainStage());
                SoundPlay.playSound("/soundEffects/SEFE_Bell.wav");
            }
        } else {
            System.out.println("Không có ảnh hiển thị");
        }
    }

    void highlightBook() {
        try {
            //Code chỗ này
            BookInDatabase.savedBooks(currentBook, currentUser);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setNoImage(ImageView imageView, Book thisbook) {

        String imageUrl = thisbook.getImageUrl();

        if (imageUrl == null || imageUrl.isEmpty()) {
            URL defaultImageUrl = UserBookProfileController.class.getResource("/picture/no_image.png");
            if (defaultImageUrl != null) {
                imageView.setImage(new Image(defaultImageUrl.toExternalForm()));
                // return defaultImageUrl.toExternalForm();
            } else {
                System.out.println("Khong tim thay");
            }
        } else {
            try {
                imageView.setImage(new Image(imageUrl));
            } catch (IllegalArgumentException e) {
                URL defaultImageUrl = UserBookProfileController.class.getResource("/picture/no_image.png");
                if (defaultImageUrl != null) {
                    System.out.println("Dmdmdmdmdmdmdmdmdm"); //em xin loi bon em stress qua aj :((
                    imageView.setImage(new Image(defaultImageUrl.toExternalForm()));
                }
            }
        }
        //return imageUrl;
    }


    public void get() {
        try {

            nameOfAuthor.setText(currentBook.getAuthor());
            nameOfBook.setText(currentBook.getTitle());
            setNoImage(bookImageView, currentBook);
            pageViews.setText(String.valueOf(currentBook.getViewCount()));
            numberOfBorrow.setText(String.valueOf(currentBook.getBorrowCount()));
            content.setText(currentBook.getDescription());

        } catch (NullPointerException e) {
            System.out.println("Null pointer here!");
        }
    }

    @FXML
    void initialize() {
        try {
            get();
            // ca cho nay nha
            currentUser.getRecentBook().addBook(Book.getMainBook());
            currentBook.setTime(LocalDateTime.now());
            if (BookInDatabase.getHightlightBook().contains(Book.getMainBook())) {
                URL image = getClass().getResource("/picture/remembered.png");
                highlightImage.setImage(new Image(image.toExternalForm()));
                highlightImage.setFitWidth(21);
                highlightImage.setFitHeight(23);
                highlightImage.setPreserveRatio(true);
            } else {
                System.out.println("Ua cai gi vayyyy"); //kho
            }
            //genre bi null
            if (currentBook.getGenre() != null && !currentBook.getGenre().isEmpty()) {
                List<Book> recommendedBooks = BookAPI.getRecommendedBooks(currentBook.getGenre());
                displayRecommendedBooks(recommendedBooks);
            } else {
                List<Book> recommendedBooks = BookAPI.getRecommendedBooks("");
                displayRecommendedBooks(recommendedBooks);
            }

        } catch (IOException | SQLException | GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    //them sach muon vao database
    @FXML
    void borrowButtonOnAction(ActionEvent event) throws SQLException {
        if (currentBook == null) {
            System.out.println("No book selected to borrow.");
            return;
        }
        if (UserHistoryController.getClickedBooksFromDatabase().contains(currentBook)) {
            showIntro("Ban da muon sach nay", getMainStage());
            SoundPlay.playSound("/soundEffects/SEFE_Pop.wav");
            return;
        }
        if (BaseController.showAlert("Thong bao", "Ban co muon muon sach nay khong")) {
            //UserHistoryController.addBookToHistory(currentBook);
            try (Connection connection = DatabaseConnection.getConnection()) {
                String sql = "INSERT INTO borrow (bookID, borrowDate, userID, status) VALUES (?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, currentBook.getId());
                    preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                    preparedStatement.setString(3, UserLoginController.userIDMain); // Replace with actual user ID
                    preparedStatement.setString(4, "borrowed"); // Example status
                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Book successfully borrowed: " + currentBook.getTitle());
                    } else {
                        System.out.println("Failed to borrow book: " + currentBook.getTitle());
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            UserHomeController.showIntro("Ban da muon sach thanh cong", getMainStage());
            SoundPlay.playSound("/soundEffects/SEFE_Bell.wav");
            currentBook.setBorrowCount(currentBook.getBorrowCount() + 1);
            BookInDatabase.updateViewCount(currentBook.getId(), "UPDATE book SET borrowCount = borrowCount + 1 WHERE bookID = ?");
        } else {
            event.consume();
        }
    }

    public void setBookDetails(Book book) throws SQLException {
        if (book == null) {
            System.err.println("Book is null.");
            return;
        }
        currentBook = book;
        BookInDatabase.updateViewCount(currentBook.getId(),
                "UPDATE books SET viewCount = viewCount + 1 WHERE book_ID = ?");

        javafx.application.Platform.runLater(() -> {
            nameOfBook.setText(book.getTitle());
            nameOfAuthor.setText(book.getAuthor());
            content.setText(book.getDescription());
            setNoImage(bookImageView, book);


            try {
                List<Book> recommendedBooks = BookAPI.getRecommendedBooks(book.getGenre());
                displayRecommendedBooks(recommendedBooks);
            } catch (IOException | GeneralSecurityException e) {
                e.printStackTrace();
            }
            // Fetch and display views, borrows, and readings
            fetchAndDisplayBookMetrics(currentBook.getId());
        });
    }

    private void fetchAndDisplayBookMetrics(String bookId) {
        String fetchMetricsSql = "SELECT viewCount, viewCount, borrowCount FROM books WHERE bookID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement fetchStmt = connection.prepareStatement(fetchMetricsSql)) {

            fetchStmt.setString(1, bookId);
            try (ResultSet resultSet = fetchStmt.executeQuery()) {
                if (resultSet.next()) {
                    int views = resultSet.getInt("viewCount");
                    //int readings = resultSet.getInt("viewCount");
                    int borrows = resultSet.getInt("number_of_borrows");

                    javafx.application.Platform.runLater(() -> {
                        pageViews.setText(String.valueOf(views));
                        numberOfBorrow.setText(String.valueOf(borrows));
                        //numberOfRead.setText(String.valueOf(readings));
                    });
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL error while fetching book metrics: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void displayRecommendedBooks(List<Book> books) {
        if (books == null || books.isEmpty()) {
            Text noBooksText = new Text("No recommended books available.");
            noBooksText.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14px; -fx-fill: #333333;");
            bookTableVbox.getChildren().clear(); // Clear previous entries
            bookTableVbox.getChildren().add(noBooksText); // Show message if no books are available
            return;
        }

        bookTableVbox.getChildren().clear(); // Clear previous entries

        for (Book book : books) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/my_group_project/bookCard.fxml"));
                Pane bookCard = loader.load();

                ImageView image = (ImageView) bookCard.lookup("#setImageBook");
                Label author = (Label) bookCard.lookup("#setAuthor");
                Label title = (Label) bookCard.lookup("#nameOfBook");
                Label category = (Label) bookCard.lookup("#setCategory");
                Label borrow = (Label) bookCard.lookup("#numberOfBorrow");
                Label read = (Label) bookCard.lookup("#numberOfRead");
                setNoImage(image, book);


                // chu y
                if (BookInDatabase.isBookInDatabase(book)) {
                    System.out.println("Wtf????");
                    read.setText(String.valueOf(book.getViewCount()));
                    borrow.setText(String.valueOf(book.getBorrowCount()));
                } else {
                    borrow.setText(String.valueOf('0'));
                    read.setText(String.valueOf('0'));
                }
                //Book newBook = books.get(i);
                author.setText(book.getAuthor());

                title.setText(book.getTitle());
                category.setText(book.getGenre());


                bookCard.setOnMouseClicked(event -> {
                    addBookHistory(currentBook);
                    System.out.println("Book clicked: " + book.getTitle());
                    try {
                        UserHomeController.bookProfile(bookCard, book);
                    } catch (IOException | SQLException e) {
                        throw new RuntimeException(e);
                    }
                });


                bookTableVbox.getChildren().add(bookCard); // Add bookCard to bookTableVbox
            } catch (IOException e) {
                System.err.println("Failed to load book card: " + e.getMessage());
                e.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @FXML
    public void backButtonOnAction(ActionEvent event) {
        Book previousBook = getPreviousBook();
        if (previousBook != null) {
            Book.setMainBook(previousBook);
            System.out.println(previousBook.getTitle());
        }
        super.backButtonOnAction(event);
    }
}