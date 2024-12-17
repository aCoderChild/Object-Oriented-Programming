package com.example.my_group_project.Controllers.Admin;

import com.example.my_group_project.Book.Book;
import com.example.my_group_project.Book.BorrowInfo;
import com.example.my_group_project.Book.BorrowedBook;
import com.example.my_group_project.Database.BookInDatabase;
import com.example.my_group_project.Database.DatabaseConnection;
import com.example.my_group_project.SoundPlay;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminBookInformationController extends AdminHomeScene {

    @FXML
    private TextField author;

    @FXML
    private ImageView bookImage;

    @FXML
    private TextField category;

    @FXML
    private TextArea content;

    @FXML
    private TextField publicYear;

    @FXML
    private TextField searchTextField;

    @FXML
    private TextField bookName;

    @FXML
    private VBox vBox2;

    @FXML
    private TextField borrowCountTextField;

    @FXML
    private TextField viewCountTextField;


    @FXML
    private Button uploadButton;

    private Book currentBook;

    private String imageURL;



    @FXML
    public void initialize() {
        setFieldsEditable(false);
        searchTextField.setOnKeyReleased(event -> filterSearch());
    }


    @FXML
    void getClear() {
        bookName.setText("");
        author.setText("");
        category.setText("");
        content.setText("");
        publicYear.setText("");
        viewCountTextField.setText("");
        borrowCountTextField.setText("");
    }

    @FXML
    void editButtonOnAction(ActionEvent event) {
        SoundPlay.playSound("/soundEffects/SEFE_MouseClick.wav");
        setFieldsEditable(true);
    }

    @FXML
    void saveButtonOnAction(ActionEvent event) {
        if (currentBook != null) {
            String query = "UPDATE book SET author = ?, yearPublic = ?, genre = ?, description = ? , viewCount = ?, borrowCount =? WHERE bookID = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                // Kiểm tra giá trị cho borrowCount và viewCount
                int viewCount = 0;
                try {
                    viewCount = Integer.parseInt(viewCountTextField.getText());
                } catch (NumberFormatException e) {
                    System.err.println("Invalid number format for viewCount: " + viewCountTextField.getText());
                }

                int borrowCount = 0;
                try {
                    borrowCount = Integer.parseInt(borrowCountTextField.getText());
                } catch (NumberFormatException e) {
                    System.err.println("Invalid number format for borrowCount: " + borrowCountTextField.getText());
                }

                pstmt.setString(1, author.getText());
                pstmt.setInt(2, Integer.parseInt(publicYear.getText()));  // Sử dụng publicYear trong khi lưu
                pstmt.setString(3, category.getText());
                pstmt.setString(4, content.getText());
                pstmt.setInt(5, Integer.parseInt(viewCountTextField.getText()));
                pstmt.setInt(6, Integer.parseInt(borrowCountTextField.getText()));
                pstmt.setString(7, currentBook.getId());  // ID sách hiện tại
                pstmt.executeUpdate();

                System.out.println("Book information updated successfully.");
                SoundPlay.playSound("/soundEffects/SEFE_Notification_Bell.wav");
                setFieldsEditable(false);

            } catch (SQLException e) {
                System.err.println("Error saving book information: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.err.println("Invalid number format: " + e.getMessage());
            }
        } else {
            String query = "INSERT INTO book (bookID, title, author, yearPublic, genre, description, imageURL, viewCount, borrowCount) VALUES (generateRandomID(10), ?, ?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setString(1, bookName.getText());
                pstmt.setString(2, author.getText());
                pstmt.setInt(3, Integer.parseInt(publicYear.getText()));  // publicYear khi thêm mới sách
                pstmt.setString(4, category.getText());
                pstmt.setString(5, content.getText());
                pstmt.setString(6, getImageURL());  // Đường dẫn ảnh sách
                pstmt.setInt(7, Integer.parseInt(viewCountTextField.getText()));
                pstmt.setInt(8, Integer.parseInt(borrowCountTextField.getText()));
                pstmt.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Book has been successfully added.");
                SoundPlay.playSound("/soundEffects/SEFE_Notification_Bell.wav");
                alert.showAndWait();
                changeScene("AdminHomeScene1.fxml", "AdminHomeScene1");

            } catch (SQLException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("An error occurred while saving the book.");
                SoundPlay.playSound("/soundEffects/SEFE_Pop.wav");
                alert.showAndWait();
            }
        }
    }


    @FXML
    void deleteButtonOnAction (ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Are you sure you want to delete this user?");
        SoundPlay.playSound("/soundEffects/SEFE_Notification_Bell.wav");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                deleteBookFromDatabase();
            }
        });
    }

    private void deleteBookFromDatabase() {
        String sql = "DELETE FROM book WHERE bookID = ?";

        try (Connection connect = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connect.prepareStatement(sql)) {
            pstmt.setString(1, currentBook.getId());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Book has been successfully deleted.");
                SoundPlay.playSound("/soundEffects/SEFE_SadMusic.wav");
                alert.showAndWait();

                changeScene("AdminHomeScene1.fxml", "AdminHomeScene1");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error occurred while deleting the user.");
            SoundPlay.playSound("/soundEffects/SEFE_Pop.wav");
            alert.showAndWait();
        }
    }

    @FXML
    private void uploadButtonOnAction (ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            imageURL = selectedFile.toURI().toString();
            bookImage.setImage(new Image(imageURL));
        }
    }

    private String getImageURL() {
        return imageURL;
    }


    protected void setFieldsEditable(boolean editable) {
        bookName.setEditable(editable);
        author.setEditable(editable);
        publicYear.setEditable(editable);
        category.setEditable(editable);
        content.setEditable(editable);
        viewCountTextField.setEditable(editable);
        borrowCountTextField.setEditable(editable);
        uploadButton.setVisible(editable);
        uploadButton.setDisable(!editable);
    }

    public void loadBookInformation(String bookId) {
        String query = "SELECT * FROM book WHERE bookID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, bookId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                bookName.setText(rs.getString("title"));
                author.setText(rs.getString("author"));
                int yearPublished = rs.getInt("yearPublic");
                if (yearPublished == 0) {
                    publicYear.setText("Unknown");
                } else {
                    publicYear.setText(String.valueOf(yearPublished));
                }
                category.setText(rs.getString("genre"));
                content.setText(rs.getString("description"));

                String imageUrl = rs.getString("imageURL");
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    Image bookImageView = new Image(imageUrl);
                    bookImage.setImage(bookImageView);
                } else {
                    bookImage.setImage(null);
                }

                currentBook = new Book(bookId, rs.getString("title"), rs.getString("author"),
                        yearPublished, rs.getString("genre"),
                        rs.getString("description"), rs.getString("imageURL"));
            }
        } catch (SQLException e) {
            System.err.println("Error loading book information: " + e.getMessage());
        }
    }



    public void loadBorrowInformation(String bookId) {
        String borrowCountQuery = "SELECT borrowCount FROM book WHERE bookID = ?";
        String viewCountQuery = "SELECT viewCount FROM book WHERE bookID = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            int borrowCount = 0;
            try (PreparedStatement pstmt = conn.prepareStatement(borrowCountQuery)) {
                pstmt.setString(1, bookId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    borrowCount = rs.getInt("borrowCount");
                }
            }

            int viewCount = 0;
            try (PreparedStatement viewStmt = conn.prepareStatement(viewCountQuery)) {
                viewStmt.setString(1, bookId);
                ResultSet viewRs = viewStmt.executeQuery();
                if (viewRs.next()) {
                    viewCount = viewRs.getInt("viewCount");
                }
            }

            // Set the text fields with the fetched counts
            if (viewCountTextField != null) {
                viewCountTextField.setText(String.valueOf(viewCount));
            } else {
                System.err.println("viewCountTextField is null.");
            }

            if (borrowCountTextField != null) {
                borrowCountTextField.setText(String.valueOf(borrowCount));
            } else {
                System.err.println("borrowCountTextField is null.");
            }

        } catch (SQLException e) {
            System.err.println("Error loading borrow information: " + e.getMessage());
        }
    }


    @FXML
    protected static List<BorrowedBook> getUserBorrowFromDatabase(String bookID) {
        List<BorrowedBook> getUserBorrowFromDatabase = new ArrayList<>();
        String sql = "SELECT userID, bookID, borrowDate, returnDate, status FROM borrow WHERE bookID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bookID); // Set the userId safely
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String userID = rs.getString("userID");
                    String bookId = rs.getString("bookID");
                    String borrowDate = rs.getString("borrowDate");
                    String returnDate = rs.getString("returnDate");
                    String status = rs.getString("status");
                    BorrowedBook brbook = new BorrowedBook(userID, bookId, borrowDate, returnDate, status);
                    getUserBorrowFromDatabase.add(brbook);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return getUserBorrowFromDatabase;
    }

    protected void displayBookBorrow(String bookID) {
        List<BorrowedBook> bookList = getUserBorrowFromDatabase(bookID);
        int index = 0;
        if (bookList.isEmpty()) {
            vBox2.getChildren().add(new Label("No book found."));
            return;
        } else {
            for (BorrowedBook book : bookList) {
                if (book == null) {
                    continue;
                }
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/my_group_project/AdminBookInformationPane.fxml"));
                    HBox bookHBox = loader.load();
                    AdminBookInformationPaneController adminBookInformationPaneController = loader.getController();
                    adminBookInformationPaneController.setBorrowBookDetail(book);
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
                        showBook(adminBookInformationPaneController.getBookId(), adminBookInformationPaneController.getUserId());
                    });
                    vBox2.getChildren().add(stackPane);
                    index++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void filterSearch() {
        String search = searchTextField.getText();

        List<BorrowedBook> filterBook = new ArrayList<>();
        for (BorrowedBook book : BookInDatabase.getBorrowedBookFromDatabase()) {
            if (book.getId().toLowerCase() != null && book.getId().toLowerCase().contains(search) ||
                    book.getUserID().toLowerCase() != null && book.getUserID().toLowerCase().contains(search) ||
                    book.getUsername().toLowerCase() != null && book.getUsername().toLowerCase().contains(search) ||
                    book.getBorrowDate().toLowerCase() != null && book.getBorrowDate().toLowerCase().contains(search) ||
                    book.getReturnDate().toLowerCase() != null && book.getReturnDate().toLowerCase().contains(search) ||
                    book.getStatus().toLowerCase() != null && book.getStatus().toLowerCase().contains(search)) {
                filterBook.add(book);
            }
        }
        displayFilterSearch(filterBook);
    }

    private void displayFilterSearch(List<BorrowedBook> filterBook) {
        vBox2.getChildren().clear();

        if (filterBook.isEmpty()) {
            vBox2.getChildren().add(new Label("No book found"));
            return;
        }

        int index = 0;
        for (BorrowedBook book : filterBook) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/my_group_project/AdminBookInformationPane.fxml"));
                HBox bookHBox = loader.load();

                AdminBookInformationPaneController bookInformationPaneController = loader.getController();
                bookInformationPaneController.setBorrowBookDetail(book);  // Cập nhật chi tiết người dùng
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
                    showBook(bookInformationPaneController.getBookId(), bookInformationPaneController.getUserId());
                });
                vBox2.getChildren().add(stackPane);
                index++;
            } catch (IOException e) {
                e.printStackTrace();
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

            Stage stage = (Stage) vBox2.getScene().getWindow();
            stage.setScene(new Scene(userProfilePane));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void backButtonOnAction(ActionEvent event) {
        SoundPlay.playSound("/soundEffects/SEFE_MouseClick.wav");
        super.changeScene("AdminHomeScene1.fxml","AdminHomeScene1" );
    }

}