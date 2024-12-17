package com.example.my_group_project.Database;


import com.example.my_group_project.Book.Book;
import com.example.my_group_project.Book.BorrowedBook;
import com.example.my_group_project.Book.RecentBook;
import com.example.my_group_project.User.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BookInDatabase {
    private static User currentUser = User.getCurrentUser();
    private static List<Book> listOfBooks =  returnBookMostView("SELECT * FROM book ORDER BY viewCount desc LIMIT 10");
    private static List<Book> hightlightBook =  getSavedBooks();

    public static List<Book> getHightlightBook() {
        return hightlightBook;
    }

    public static List<Book> getListOfBooks(){
        return listOfBooks;
    }

    // Create - Method to insert a book into the database
    public static void insertBook(Book book) throws SQLException {
        if (!isBookInDatabase(book)) {  // Check if the book already exists in the database
            String sql = "INSERT INTO book (bookID, title, author, imageURL, description, genre, viewCount, borrowCount, yearPublic) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, book.getId());
                pstmt.setString(2, book.getTitle());
                pstmt.setString(3, book.getAuthor());
                pstmt.setString(4, book.getImageURL());
                pstmt.setString(5, book.getDescription());
                pstmt.setString(6, book.getGenre());
                pstmt.setInt(7, book.getViewCount());
                pstmt.setInt(8, book.getBorrowCount());
                pstmt.setInt(9, book.getYearPublic());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new SQLException("Error while inserting book", e);
            }
        } else {
            System.out.println("Book with ID " + book.getId() + " already exists in the database.");
        }
    }

    public static boolean isBookInDatabase(Book book) throws SQLException {
        String query = "SELECT * FROM book WHERE bookID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, book.getId());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next() ) {
                book.setId(resultSet.getString("bookID"));
                book.setViewCount(resultSet.getInt("viewCount"));
                book.setBorrowCount(resultSet.getInt("borrowCount"));
                book.setImageURL(resultSet.getString("imageURL"));
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error while checking if book exists in database", e);
        }
        return false;
    }

    public static List<Book> getSavedBooks(){
        List<Book> books = new ArrayList<>();
        if(currentUser != null) {
            System.out.println("Hallooo");
            String query = "SELECT b.* FROM book b " +
                    "JOIN highlightbook h ON h.bookID = b.bookID " +
                    "WHERE h.userID = ? ";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1,currentUser.getId());
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    Book book = new Book();
                    book.setId(resultSet.getString("bookID"));
                    book.setTitle(resultSet.getString("title"));
                    book.setAuthor(resultSet.getString("author"));
                    book.setDescription(resultSet.getString("description"));
                    book.setImageURL(resultSet.getString("imageURL"));
                    book.setViewCount(resultSet.getInt("viewCount"));
                    book.setGenre(resultSet.getString("genre"));
                    book.setBorrowCount(resultSet.getInt("borrowCount"));
                    books.add(book);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return books;
    }
    public static void savedBooks(Book book, User user) throws SQLException {
        System.out.println("Ua sao no khong luu vay");

        String query = "INSERT INTO highlightbook (userID, bookID) VALUES (?, ?)";
        String checkQuery = "SELECT * FROM highlightbook WHERE userID = ? AND bookID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setString(1, user.getId());
            checkStmt.setString(2, book.getId());
            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next()) {

                System.out.println("Hong hong hong ne ne ne ");
                try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                    preparedStatement.setString(1, user.getId());
                    preparedStatement.setString(2, book.getId());
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error while saving book", e);
        }

    }

    // Read - Method to get the list of recent book for a user
    public static List<Book> getBorrowedBooks(String User_ID) {
        List<Book> books = new ArrayList<>();
        String query = "SELECT bk.* FROM book bk JOIN borrow b ON b.bookID = bk.bookID WHERE b.userID = ? AND b.status = 'borrowed'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, User_ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getString("bookID"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setDescription(resultSet.getString("description"));
                book.setImageURL(resultSet.getString("imageURL"));
                book.setViewCount(resultSet.getInt("viewCount"));

                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public static List<Book> getRecentBooks(String User_ID) {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM book bk JOIN recentbook r ON r.bookID = bk.bookID WHERE r.userID = ? order by r.time DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, User_ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getString("bookID"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setDescription(resultSet.getString("description"));
                book.setImageURL(resultSet.getString("imageURL"));
                book.setViewCount(resultSet.getInt("viewCount"));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public static void updateBook(Book book) throws SQLException {
        String sql = "UPDATE book SET title = ?, author = ?, description = ?, genre = ?, viewCount = ?, addDate = ?, imageURL = ? WHERE bookID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getDescription());
            pstmt.setString(4, book.getGenre());
            pstmt.setInt(5, book.getViewCount());
            LocalDateTime dateTime = book.getTime();
            pstmt.setTimestamp(6, dateTime != null ? Timestamp.valueOf(dateTime) : Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(7, book.getImageUrl());
            pstmt.setString(8, book.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error while updating book", e);
        }
    }

    // Delete - Method to remove a book from the highlight book list
    /*public static void deleteBook(String bookId, String userId) throws SQLException {
        String query = "DELETE FROM highlightbook WHERE User_ID = ? AND bookID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, userId);
            preparedStatement.setString(2, bookId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error while deleting book", e);
        }
    }

     */

    public static void saveRecentBooks(RecentBook book) throws SQLException {
        if (book != null && User.getCurrentUser() != null && Book.getMainBook() != null) {  // Added null checks
            book.setCurrentTime();
            String query = "INSERT INTO recentbook (userID, bookID, time) VALUES (?, ?, ?)";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, User.getCurrentUser().getId());
                stmt.setString(2, Book.getMainBook().getId());
                stmt.setString(3,String.valueOf(book.getTime()));

                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new SQLException("Error while saving recent book", e);
            }
        } else {
            System.out.println("Error: User or Book information is missing.");
        }
    }

    public static void deleteBook(Book book, String message) throws SQLException {
        String query = message;
        System.out.println("Hihi");
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, User.getCurrentUser().getId());
            stmt.setString(2, book.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error while deleting recent book", e);
        }
    }

    public static void updateViewCount(String bookID, String message) throws SQLException {
        String query = message;
        //"UPDATE book SET viewCount = viewCount + 1 WHERE bookID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, bookID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error while updating view count", e);
        }
    }

    /*public static void updateViewCount(String bookID) throws SQLException {
        String query = "UPDATE book SET viewCount = viewCount + 1 WHERE bookID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, bookID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error while updating view count", e);
        }
    }

     */

/*
    public static void updateBorrowCoun(String bookID) throws SQLException {
        String query = "UPDATE book SET borrowCount = borrowCount + 1 WHERE bookID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, bookID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error while updating borrow count", e);
        }
    }

 */



    public static List<Book> returnBookMostView(String message){
        String query = message;
        List<Book> popularBook = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                System.out.println("Co gi ma troi!");
                popularBook.add(new Book(resultSet.getString("bookID"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("imageURL"),
                        resultSet.getString("description"),
                        resultSet.getString("genre"),
                        resultSet.getInt("viewCount"),
                        resultSet.getInt("yearPublic")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        listOfBooks = popularBook;
        return popularBook;
    }

    //get book from the database
    public static Book getBookById(String bookID) {
        String sql = "SELECT * FROM book WHERE bookID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bookID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Book(
                            rs.getString("bookID"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("imageURL"),
                            rs.getString("description"),
                            rs.getString("genre"),
                            rs.getInt("viewCount"),
                            rs.getInt("borrowCount")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Book> getBookFromDatabase() {
        List<Book> bookList = new ArrayList<>();
        String sql = "SELECT bookID, title, author, genre, yearPublic FROM book";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String bookID = rs.getString("bookID");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String genre = rs.getString("genre");
                int yearPublic = rs.getInt("yearPublic");
                Book book = new Book(bookID, title, author, genre, yearPublic);
                bookList.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookList;
    }

    public static List<BorrowedBook> getBorrowedBookFromDatabase() {
        List<BorrowedBook> bookList = new ArrayList<>();
        String sql = "SELECT b.bookID, bk.title, b.userID, b.borrowDate, b.returnDate, b.status FROM borrow b JOIN book bk ON b.bookID = bk.bookID";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String userID = rs.getString("b.userID");
                String bookID = rs.getString("b.bookID");
                String title = rs.getString("bk.title");
                String borrowDate = rs.getString("b.borrowDate");
                String returnDate = rs.getString("b.returnDate");
                String status = rs.getString("b.status");
                BorrowedBook book = new BorrowedBook(bookID, title, userID, borrowDate, returnDate, status);
                bookList.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookList;
    }


    // Static method to fetch recent book from the database
    private static List<Book> fetchRecentBooks() {
        String query = "SELECT b.*, re.time FROM book b JOIN recentbooks re ON re.bookID = b.bookID ORDER BY re.time DESC LIMIT 10";
        List<Book> latestBooks = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Book newBook = new Book(
                        resultSet.getString("bookID"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("imageURL"),
                        resultSet.getString("description"),
                        resultSet.getString("genre"),
                        resultSet.getInt("viewCount"),
                        resultSet.getInt("borrowCount")
                );
                String dateTimeStr = resultSet.getString("time");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                newBook.setTime(LocalDateTime.parse(dateTimeStr, formatter));
                latestBooks.add(newBook);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return latestBooks;
    }

    // Helper method to save a book to the database
    public static void saveBookToDatabase(Book book) throws SQLException {
        String query = "INSERT INTO recentbook (userID, bookID, time) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, User.getCurrentUser().getId());
            statement.setString(2, book.getId());
            statement.setString(3, String.valueOf(book.getTime()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Helper method to delete a book from the database
    public static void deleteBookFromDatabase(Book book) {
        String query = "DELETE FROM recentbook WHERE userID = ? AND bookID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, User.getCurrentUser().getId());
            statement.setString(2, book.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
