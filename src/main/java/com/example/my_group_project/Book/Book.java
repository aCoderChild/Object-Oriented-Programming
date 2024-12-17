package com.example.my_group_project.Book;

import java.time.LocalDateTime;
import java.util.Objects;

public class Book {
    private String id;
    private String title;
    private String author;
    private String imageURL;
    private String description;
    private String genre;
    private int viewCount;
    private int yearPublic = -1;
    protected LocalDateTime time;
    private int borrowCount;
    private static Book mainBook;

    // Constructors
    public Book() {}

    public Book(String bookID) {
        this.id = bookID;
    }

    public Book(String bookID, String title, String author, String imageURL, String description, String genre, int viewCount, int borrowCount, int yearPublic) {
        this.id = bookID;
        this.title = title;
        this.author = author;
        this.imageURL = imageURL;
        this.description = description;
        this.genre = genre;
        this.viewCount = viewCount;
        this.borrowCount = borrowCount;
        this.yearPublic = yearPublic;
    }

    public Book(String bookID, String title, String author, String imageURL, String description, String genre, int viewCount, int yearPublic) {
        this.id = bookID;
        this.title = title;
        this.author = author;
        this.imageURL = imageURL;
        this.description = description;
        this.genre = genre;
        this.viewCount = viewCount;
        this.yearPublic = yearPublic;
    }

    public Book(String bookID, String title, String author, int yearPublic, String genre, String description, String imageURL) {
        this.id = bookID;
        this.title = title;
        this.author = author;
        this.yearPublic = yearPublic;
        this.genre = genre;
        this.description = description;
        this.imageURL = imageURL;
    }

    public Book(String bookID, String title, String author, String imageURL, String description, String genre) {
        this.id = bookID;
        this.title = title;
        this.author = author;
        this.imageURL = imageURL;
        this.description = description;
        this.genre = genre;
    }

    public Book(String bookID, String title, String author, String genre, int yearPublic) {
        this.id = bookID;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.yearPublic = yearPublic;
    }

    public Book(String id, String title) {
        this.id = id;
        this.title = title;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String authors) {
        this.author = authors;
    }

    public String getImageUrl() {
        return imageURL;
    }

    public void setImageURL(String imageUrl) {
        this.imageURL = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getBorrowCount() {
        return borrowCount;
    }

    public void setBorrowCount(int borrowCount) {
        this.borrowCount = borrowCount;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getImageURL() {
        return imageURL;
    }

    public int getYearPublic() {
        return yearPublic;
    }

    public void setYearPublic(int yearPublic) {
        this.yearPublic = yearPublic;
    }

    public static Book getMainBook() {
        return mainBook;
    }

    public static void setMainBook(Book mainBook) {
        Book.mainBook = mainBook;
    }

    // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return id.equals(book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}