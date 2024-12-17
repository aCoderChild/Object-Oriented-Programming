package com.example.my_group_project.Controllers.User;

import com.example.my_group_project.Report;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class UserReportPaneController {

    @FXML
    private Label executionDate;

    @FXML
    private Label reportId;

    @FXML
    private Label status;

    @FXML
    private Label title;

    @FXML
    public void setReportDetail(Report report) {
        if (report == null) {
            System.out.println("User is null.");
            return;
        }
        reportId.setText(report.getReportId());
        executionDate.setText(report.getExecutionDate());
        title.setText(report.getTitle());
        status.setText(report.getStatus());
    }
}
