package com.example.my_group_project.Controllers.User;

import com.example.my_group_project.Database.DatabaseConnection;
import com.example.my_group_project.Report;
import com.example.my_group_project.SoundPlay;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserMoreInformationController extends UserMenuController {
    @FXML
    private TextField titleTextField;

    @FXML
    private TextField contentTextField;

    @FXML
    private TextField searchTextField;

    @FXML
    private VBox vBox;
    private String userId;
    private boolean isEditable = false;

    public void initialize() {
        this.userId = UserLoginController.userIDMain;
        displayReport(this.userId);
        searchTextField.setOnKeyReleased(event -> filterSearch(this.userId));
        setEditable(false);
    }

    private void clearTextField() {
        titleTextField.clear();
        contentTextField.clear();
    }

    private void setEditable(boolean edit) {
        titleTextField.setEditable(edit);
        contentTextField.setEditable(edit);
    }

    //editButton
    @FXML
    private void editButtonOnAction(ActionEvent event) {
        if(!isEditable) {
            titleTextField.setEditable(true);
            contentTextField.setEditable(true);

            isEditable = true;
        }
    }

    //saveButton
    @FXML
    private void saveButtonOnAction(ActionEvent event) {
        String title = titleTextField.getText();
        String content = contentTextField.getText();

        String reportId = UUID.randomUUID().toString();
        String executionDate = LocalDate.now().toString();
        String status = "chua duoc thuc hien";


        if(title.isEmpty() || content.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please fill in all the fields.");
            SoundPlay.playSound("/soundEffects/SEFE_Pop.wav");
            alert.getDialogPane().setStyle("-fx-background-color: #f7efd8; -fx-border-radius: 20px;");
            String cssPath = UserProfileUserFormController.class.getResource("/custom.css").toExternalForm();
            alert.getDialogPane().getStylesheets().add(cssPath);
            alert.showAndWait();
            return;
        }

        String sql = "INSERT INTO report (reportID, executionDate, title, content, status, userID) VALUES (?,?,?,?,?,?)";


        try (Connection connect = DatabaseConnection.getConnection();
             PreparedStatement pstm = connect.prepareStatement(sql)) {

            pstm.setString(1, reportId);
            pstm.setString(2, executionDate);
            pstm.setString(3, title);
            pstm.setString(4, content);
            pstm.setString(5, status);
            pstm.setString(6, this.userId);

            int rowsAffected = pstm.executeUpdate();

            if (rowsAffected > 0) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Report has been successfully saved.");
                SoundPlay.playSound("/soundEffects/SEFE_Awwww.wav");
                alert.getDialogPane().setStyle("-fx-background-color: #f7efd8; -fx-border-radius: 20px;");
                String cssPath = UserProfileUserFormController.class.getResource("/custom.css").toExternalForm();
                alert.getDialogPane().getStylesheets().add(cssPath);
                alert.showAndWait();

                clearTextField();
                setEditable(false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("An error occurred while saving the report.");
            alert.getDialogPane().setStyle("-fx-background-color: #f7efd8; -fx-border-radius: 20px;");
            String cssPath = UserProfileUserFormController.class.getResource("/custom.css").toExternalForm();
            alert.getDialogPane().getStylesheets().add(cssPath);
            alert.showAndWait();
        }

        Report report = new Report(reportId,executionDate,title,status);
        try {
            addReportNow(report);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //thuc hien load P*aneHistory
    @FXML
    private List<Report> getReportFromDatabase(String userId) {
        List<Report> getReportFromDatabase = new ArrayList<>();
        String sql = "SELECT reportID, executionDate, title, status FROM report WHERE userID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String reportId = rs.getString("reportID");
                    String executionDate= rs.getString("executionDate");
                    String title = rs.getString("title");
                    String status = rs.getString("status");

                    Report rp = new Report(reportId,executionDate, title, status);
                    getReportFromDatabase.add(rp);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return getReportFromDatabase;
    }
    // show history
    protected void displayReport(String userId) {
        List<Report> reportList = getReportFromDatabase(userId);
        int index = 0;
        if (reportList.isEmpty()) {
            vBox.getChildren().add(new Label("No report found."));

        } else {
            for (Report rp : reportList) {
                if (rp == null) {
                    continue;
                }
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/my_group_project/reportPane.fxml"));
                    HBox reportHBox = loader.load();
                    UserReportPaneController reportPane = loader.getController();
                    reportPane.setReportDetail(rp);
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

                    vBox.getChildren().add(stackPane);
                    index++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //searchFilter
    private void filterSearch(String userId) {
        String search = searchTextField.getText();

        List<Report> filterReport = new ArrayList<>();
        for (Report report : getReportFromDatabase(userId)) {
            if (report.getReportId() != null && report.getReportId().contains(search) ||
                    report.getExecutionDate() != null && report.getExecutionDate().contains(search) ||
                    report.getTitle() != null && report.getTitle().contains(search) ||
                    report.getStatus() != null && report.getStatus().contains(search)) {
                filterReport.add(report);
            }
        }
        displayFilterSearch(filterReport);
    }

    private void addReportNow(Report report) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/my_group_project/reportPane.fxml"));
        HBox reportHBox = loader.load();
        UserReportPaneController reportPane = new UserReportPaneController();
        reportPane = loader.getController();
        reportPane.setReportDetail(report);
        int index = 0;
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

        vBox.getChildren().add(stackPane);

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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/my_group_project/reportPane.fxml"));
                HBox reportHBox = loader.load();
                UserReportPaneController reportPane = new UserReportPaneController();
                reportPane = loader.getController();
                reportPane.setReportDetail(report);
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

                vBox.getChildren().add(stackPane);
                index++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
