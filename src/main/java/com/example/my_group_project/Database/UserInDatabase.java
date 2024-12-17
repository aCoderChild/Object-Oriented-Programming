package com.example.my_group_project.Database;

import com.example.my_group_project.User.User;
import javafx.fxml.FXML;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserInDatabase {
    protected static List<User> getUserFromDatabase() {
        List<User> getUserFromDatabase = new ArrayList<>();
        String sql = "SELECT userID, name, email, phone, dateOfBirth, gender FROM user; ";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String userID = rs.getString ("userID");
                    String userName = rs.getString("name");
                    String email = rs.getString("email");
                    String phoneNumber = rs.getString("phone");
                    String dateOfBirth = rs.getString("dateOfBirth");
                    String gender = rs.getString("gender");
                    User user = new User(userID, userName, email, phoneNumber, dateOfBirth, gender);
                    getUserFromDatabase.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return getUserFromDatabase;
    }
}
