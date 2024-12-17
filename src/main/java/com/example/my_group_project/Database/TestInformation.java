package com.example.my_group_project.Database;

import com.example.my_group_project.User.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestInformation {

    private static boolean checkInformation(String infor, String message) {
        try (Connection connectDB = DatabaseConnection.getConnection();
             PreparedStatement checkStatement = connectDB.prepareStatement(message)) {
            checkStatement.setString(1, infor);
            checkStatement.setString(2, User.getCurrentUser().getId());
            ResultSet resultSet1 = checkStatement.executeQuery();
            if (resultSet1.next() && resultSet1.getInt(1) > 0) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean checkUserName(String userName) {
        return checkInformation(userName, "SELECT COUNT(name) FROM user WHERE name = ? AND userID != ?");
        //return checkInformation(userName, "SELECT COUNT(name) FROM user WHERE name = ? AND User_ID != ?");
    }

    public static boolean checkEmail(String email) {
        return checkInformation(email, "SELECT COUNT(email) FROM user WHERE email = ? AND userID != ?");
    }

    public static boolean checkPhone(String phoneNumber) {
        return checkInformation(phoneNumber, "SELECT COUNT(phone) FROM user WHERE phone = ? AND userID != ?");
    }


}