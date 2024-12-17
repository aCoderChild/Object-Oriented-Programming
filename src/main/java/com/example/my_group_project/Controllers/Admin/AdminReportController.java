package com.example.my_group_project.Controllers.Admin;
import com.example.my_group_project.Database.DatabaseConnection;

import com.example.my_group_project.Report;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminReportController extends AdminMenuController {
    @FXML
    private Button backButton;

    @FXML
    private Button bookBorrowButton;

    @FXML
    private Button homeScene1Button;

    @FXML
    private Button logOutButton;

    @FXML
    private Button reportButton;

    @FXML
    private TextField searchTextField;

    @FXML
    private VBox vBox;

    @FXML
    private Button userManagementButton;

    @FXML
    public void backButtonOnAction(ActionEvent event) {
        super.changeScene("AdminHomeScene1.fxml","AdminHomeScene1" );
    }
    @FXML
    public void initialize() {
        displayReport();

        searchTextField.setOnKeyReleased(event -> filterSearch());
    }

    @FXML
    protected static List<Report> getReportFromDatabase() {
        List<Report> getReportFromDatabase = new ArrayList<>();
        String sql = "SELECT userID, reportID,  executionDate, title, status FROM report ORDER BY executionDate DESC; ";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String userId = rs.getString ("userID");
                    String reportId = rs.getString("reportID");
                    String executionDate = rs.getString("executionDate");
                    String title = rs.getString("title");
                    String status = rs.getString("status");
                    Report report = new Report (reportId, executionDate, title,userId,status);
                    getReportFromDatabase.add(report);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return getReportFromDatabase;
    }


    private void displayReport() {
        List<Report> reportList = getReportFromDatabase();
        int index = 0;
        if (reportList.isEmpty()) {
            vBox.getChildren().add(new Label("No report found."));
            return;
        } else {
            for (Report report : reportList) {
                if (report == null) {
                    continue;
                }
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/my_group_project/AdminReportPane.fxml"));
                    HBox reportHBox = loader.load();
                    AdminReportPaneController reportController = loader.getController();
                    reportController.setReportDetails(report);
                    final int currentIndex = index;
                    if (currentIndex % 2 == 0) {
                        reportHBox.setStyle("-fx-background-color: #f7efd8;");
                    } else {
                        reportHBox.setStyle("-fx-background-color: #ffffff;");
                    }

                    StackPane stackPane = new StackPane();
                    stackPane.getChildren().add(reportHBox);

                    stackPane.setOnMouseEntered(event -> {
                        reportHBox.setStyle("-fx-background-color: #ffc100; -fx-cursor: hand;");
                    });

                    stackPane.setOnMouseExited(event -> {
                        if (currentIndex % 2 == 0) {
                            reportHBox.setStyle("-fx-background-color: #f7efd8;");
                        } else {
                            reportHBox.setStyle("-fx-background-color: #ffffff;");
                        }
                    });

                    reportHBox.setOnMouseClicked(event -> {
                        showReport(reportController.getReportId(), reportController.getUserId());
                    });

                    vBox.getChildren().add(stackPane);
                    index++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /*private void showUser(String userId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminUserManagement.fxml"));
            Pane userProfilePane = loader.load();

            AdminOneUserProfileController userProfileController = loader.getController();

            userProfileController.loadUserProfile(userId);
            userProfileController.displayUserBorrow(userId);

            Stage stage = (Stage) vBox.getScene().getWindow();
            stage.setScene(new Scene(userProfilePane));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    private void filterSearch() {
        String search = searchTextField.getText();

        List<Report> filterReport = new ArrayList<>();
        for (Report report : getReportFromDatabase()) {
            if (report.getUserId() != null && report.getUserId().contains(search) ||
                    report.getReportId() != null && report.getReportId().contains(search) ||
                    report.getExecutionDate() != null && report.getExecutionDate().contains(search) ||
                    report.getTitle()!= null && report.getTitle().contains(search) ||
                    report.getStatus() != null && report.getStatus().contains(search)) {
                filterReport.add(report);
            }
        }
        displayFilterSearch(filterReport);
    }

    private void displayFilterSearch(List <Report> filterReport) {
        vBox.getChildren().clear();

        if(filterReport.isEmpty()) {
            vBox.getChildren().add(new Label("No report found"));
            return;
        }

        int index = 0;
        for (Report report : filterReport) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/my_group_project/AdminReportPane.fxml"));
                HBox reportHBox = loader.load();

                AdminReportPaneController reportController = loader.getController();
                reportController.setReportDetails(report);  // Cập nhật chi tiết người dùng
                final int currentIndex = index;
                if (currentIndex % 2 == 0) {
                    reportHBox.setStyle("-fx-background-color: #f7efd8;");  // Màu nền cho dòng chẵn
                } else {
                    reportHBox.setStyle("-fx-background-color: #ffffff;");  // Màu nền cho dòng lẻ
                }

                StackPane stackPane = new StackPane();
                stackPane.getChildren().add(reportHBox);

                stackPane.setOnMouseEntered(event -> {
                    reportHBox.setStyle("-fx-background-color: #ffc100; -fx-cursor: hand;");
                });

                stackPane.setOnMouseExited(event -> {
                    if (currentIndex % 2 == 0) {
                        reportHBox.setStyle("-fx-background-color: #f7efd8;");
                    } else {
                        reportHBox.setStyle("-fx-background-color: #ffffff;");
                    }
                });
                reportHBox.setOnMouseClicked(event -> {
                    showReport(reportController.getReportId(), reportController.getUserId());
                });
                vBox.getChildren().add(stackPane);
                index++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showReport(String reportId, String userId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/my_group_project/AdminReportOneUser.fxml"));
            Pane reportPane = loader.load();

            AdminReportOneUserController reportController = loader.getController();

            reportController.loadUserProfile(userId);
            reportController.loadReport(reportId);
            reportController.displayReport(userId);
            reportController.checkStatus(reportId);

            Stage stage = (Stage) vBox.getScene().getWindow();
            stage.setScene(new Scene(reportPane));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
