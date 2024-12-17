package com.example.my_group_project.Book;

public class BorrowedBook extends Book{
    private String userID;
    private String username;
    private String borrowDate;
    private String returnDate;
    private String status;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public BorrowedBook(String userID, String bookID, String borrowDate, String returnDate, String status) {
        super(bookID);
        this.userID = userID;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public BorrowedBook( String bookID,String title, String userID, String borrowDate, String returnDate, String status) {
        super(bookID, title);
        this.userID = userID;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.status = status;
    }
}
