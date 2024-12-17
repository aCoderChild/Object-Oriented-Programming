package com.example.my_group_project.Book;

import com.example.my_group_project.Controllers.User.UserLoginController;
import com.example.my_group_project.Database.BookInDatabase;
import com.example.my_group_project.Database.DatabaseConnection;
import com.example.my_group_project.User.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class RecentBook extends Book {
    private static final int MAX_BOOKS = 10;
    private Queue<Book> recentBooks = new LinkedList<>(fetchRecentBooks());

    public void setCurrentTime() {
        this.time = LocalDateTime.now();
    }

    public void addBook(Book book) throws SQLException {
        if (recentBooks.contains(book)) {
            recentBooks.remove(book);
            BookInDatabase.deleteBookFromDatabase(book);
            System.out.println("hello");
            //BookInDatabase.deleteBook(book,
            // "DELETE FROM recentbooks WHERE user_ID = ? AND book_ID = ?");
        }
        if (recentBooks.size() >= MAX_BOOKS) {
            recentBooks.poll();
        }
        recentBooks.offer(book);
    }

    // Method to get the list of recent books
    public List<Book> getRecentBooks() {
        List<Book> books = new ArrayList<>(recentBooks);
        books.sort(Comparator.comparing(Book::getTime, Comparator.nullsLast(LocalDateTime::compareTo)).reversed());
        return books;
    }

    public static List<Book> fetchRecentBooks() {

        String query = "SELECT b.*, time FROM book b JOIN recentbook re ON re.bookID = b.bookID WHERE userID = ? ORDER BY time DESC LIMIT 10";
        List<Book> latestBooks = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, UserLoginController.userIDMain);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Book newBook = new Book(resultSet.getString("bookID"), resultSet.getString("title"),
                            resultSet.getString("author"), resultSet.getString("imageURL"),
                            resultSet.getString("description"), resultSet.getString("genre"),
                            resultSet.getInt("viewCount"), resultSet.getInt("borrowCount"));
                    String dateTimeStr = resultSet.getString("time");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    newBook.setTime(LocalDateTime.parse(dateTimeStr, formatter));
                    latestBooks.add(newBook);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return latestBooks;

    }

    public void changeBooksToDate() throws SQLException {
        for (Book book : recentBooks) {
            if(BookInDatabase.isBookInDatabase(book)){
                BookInDatabase.deleteBookFromDatabase(book);
            }
            BookInDatabase.saveBookToDatabase(book);
        }
    }
}