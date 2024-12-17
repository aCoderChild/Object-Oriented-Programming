package com.example.my_group_project.Controllers.Admin;

import com.example.my_group_project.Report;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AdminReportPaneController {
    @FXML
    private Label executionDate;

    @FXML
    private Label reportId;

    @FXML
    private Label status;

    @FXML
    private Label title;

    @FXML
    private Label userId;

    void setReportDetails(Report report) {
        userId.setText(report.getUserId());
        reportId.setText(report.getReportId());
        executionDate.setText(report.getExecutionDate());
        title.setText(report.getTitle());
        status.setText(report.getStatus());
    }

    public String getReportId() {
        return reportId.getText();
    }
    public String getUserId() {
        return userId.getText();
    }
}
