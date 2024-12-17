package com.example.my_group_project.Controllers.Admin;
import com.example.my_group_project.Controllers.User.UserReportPaneController;
import com.example.my_group_project.SoundPlay;
import com.example.my_group_project.User.User;
import com.example.my_group_project.Database.DatabaseConnection;

import com.example.my_group_project.Report;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class AdminReportOneUserController extends AdminMenuController{
    @FXML
    private Button backButton;

    @FXML
    private Button bookBorrowButton;

    @FXML
    private Text contentText;

    @FXML
    private Label executionDateTextField;

    @FXML
    private Label reportIdTextField;

    @FXML
    private Button homeScene1Button;

    @FXML
    private Button logOutButton;

    @FXML
    private TextField noteTextField;

    @FXML
    private Button reportButton;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private ScrollPane scrollPane1;

    @FXML
    private TextField searchTextField;

    @FXML
    private CheckBox statusCheckBox;

    @FXML
    private Label titleTextField;

    @FXML
    private Label userEmailTextField;

    @FXML
    private Label userIdTextField;

    @FXML
    private ImageView userImage;

    @FXML
    private Button userManagementButton;

    @FXML
    private Label userNameTextField;

    @FXML
    private VBox vBox;

    @FXML
    private VBox vBox1;

    private boolean isChecked = false;

    public void initialize() {

        searchTextField.setOnKeyReleased(event -> filterSearch());
    }

    protected void checkStatus(String reportId) {
        String sql = "SELECT status FROM report WHERE reportID = ?";
        try(Connection connect = DatabaseConnection.getConnection();
            PreparedStatement pstm = connect.prepareStatement(sql)) {
            pstm.setString(1, reportId);
            try(ResultSet rs = pstm.executeQuery()) {
                while(rs.next()) {
                    String Status = rs.getString("status");
                    if(Status.equals("DONE")) {
                        statusCheckBox.setSelected(true);
                    } else {
                        statusCheckBox.setSelected(false);
                    }
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
        userId = userIdTextField.getText();
        List<Report> reportList = getReportFromDatabase(userId);
        int index = 0;
        if (reportList.isEmpty()) {
            vBox1.getChildren().add(new Label("No report found."));
            return;
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

                    stackPane.setOnMouseClicked(event -> {
                        loadReport(rp.getReportId());
                    });

                    stackPane.setOnMouseExited(event -> {
                        if (currentIndex % 2 == 0) {
                            reportHBox.setStyle("-fx-background-color: #f7efd8;");
                        } else {
                            reportHBox.setStyle("-fx-background-color: #ffffff;");
                        }
                    });

                    vBox1.getChildren().add(stackPane);
                    index++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //searchFilter
    private void filterSearch() {
        String search = searchTextField.getText();

        List<Report> filterReport = new ArrayList<>();
        for (Report report : getReportFromDatabase(userIdTextField.getText())) {
            if (report.getReportId() != null && report.getReportId().contains(search) ||
                    report.getExecutionDate() != null && report.getExecutionDate().contains(search) ||
                    report.getTitle() != null && report.getTitle().contains(search) ||
                    report.getStatus() != null && report.getStatus().contains(search)) {
                filterReport.add(report);
            }
        }
        displayFilterSearch(filterReport);
    }

    private void displayFilterSearch(List <Report> filterReport) {
        vBox1.getChildren().clear();

        if(filterReport.isEmpty()) {
            vBox1.getChildren().add(new Label("No report found"));
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

                vBox1.getChildren().add(stackPane);
                index++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void loadUserProfile ( String userId ) {
        String sql = "SELECT userID, name, email, profileImage FROM user WHERE userID = ?;";
        try(Connection connect = DatabaseConnection.getConnection();
            PreparedStatement pstmt = connect.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String userID = rs.getString("userID");
                String userName = rs.getString("name");
                String userEmail = rs.getString("email");

                userIdTextField.setText(userID);
                userNameTextField.setText(userName);
                userEmailTextField.setText(userEmail);

                if (rs.getBlob("profileImage") != null) {
                    byte[] imageBytes = rs.getBytes("profileImage");
                    Image image = new Image(new ByteArrayInputStream(imageBytes));
                    userImage.setImage(image);
                }

            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("User not found");
                alert.setHeaderText("No user found with the given user ID.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("An error occurred while retrieving user information.");
            alert.showAndWait();
        }
    }

    protected void loadReport(String reportId) {
        String sql = "SELECT reportID, executionDate, title, content, status FROM report WHERE reportID = ?";
        try (Connection connect = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connect.prepareStatement(sql)) {
            pstmt.setString(1, reportId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String reportID = rs.getString("reportID");
                String executionDate = rs.getString("executionDate");
                String title = rs.getString("title");
                String content = rs.getString("content");
                String status = rs.getString("status");

                reportIdTextField.setText(reportID);
                executionDateTextField.setText(executionDate);
                titleTextField.setText(title);
                contentText.setText(content);

                // Handle checkbox visibility and disable state based on the report status
                if (status.equals("DONE")) {
                    statusCheckBox.setSelected(true);
                    //statusCheckBox.setVisible(false);  // Hide checkbox when DONE
                    statusCheckBox.setDisable(true);   // Disable checkbox when DONE
                } else {
                    statusCheckBox.setSelected(false);
                    //statusCheckBox.setVisible(true);   // Show checkbox when WAITING
                    statusCheckBox.setDisable(false);  // Enable checkbox when WAITING
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("An error occurred while retrieving report information.");
            alert.showAndWait();
        }
    }


    @FXML
    private void handleCheckBox(ActionEvent event) {
        isChecked = statusCheckBox.isSelected();
        SoundPlay.playSound("/soundEffects/SEFE_MouseClick.wav");
        if(isChecked) {
            updateStatus(reportIdTextField.getText(), "DONE");
        } else {
            updateStatus(reportIdTextField.getText(), "WAITING");
        }
    }
    @FXML
    private void updateStatus(String reportId, String status) {
        String sql = "UPDATE report SET status = ? WHERE reportID = ?" ;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setString(2, reportId);
            pstmt.executeUpdate();

            if(status.equals("DONE")) {
                statusCheckBox.setManaged(false);
            } else {
                statusCheckBox.setManaged(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void backButtonOnAction(ActionEvent event) {
        SoundPlay.playSound("/soundEffects/SEFE_MouseClick.wav");
        super.changeScene("AdminHomeScene1.fxml","AdminHomeScene1" );
    }

}
