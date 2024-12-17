package com.example.my_group_project.User;

import com.example.my_group_project.Book.RecentBook;

public class User {
    private String id;
    private String username;
    private String fullName;
    private String password;
    private String gender;
    private String email;
    private String phone;
    private String dateOfBirth;

    private static User currentUser;
    private RecentBook recentBook = new RecentBook();

    // Constructor
    public User(String id, String username) {
        this.id = id;
        this.username = username;
    }

    public User(String userID, String username, String email, String phoneNumber, String dateOfBirth, String gender) {
        this.id = userID;
        this.username = username;
        this.email = email;
        this.phone = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
    }

    // Getters and Setters
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RecentBook getRecentBook() {
        return recentBook;
    }

    public void setRecentBook(RecentBook recentBook) {
        this.recentBook = recentBook;
    }
}