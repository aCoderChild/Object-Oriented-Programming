package com.example.my_group_project.Database;

public class LoginDetail {
    private String username;
    private String password;

    public LoginDetail(String username, String password) {
        this.username = username;
        this.password = password;
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

    @Override
    public String toString() {
        return username; // This is what will be displayed in the ComboBox
    }
}
