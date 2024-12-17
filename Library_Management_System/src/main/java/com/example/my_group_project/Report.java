package com.example.my_group_project;

public class Report {
    private String reportId;
    private String userId;
    private String executionDate;
    private String title;
    private String content;
    private String status;

    public Report(String reportId, String executionDate, String title, String userId, String status) {
        this.reportId = reportId;
        this.executionDate = executionDate;
        this.title = title;
        this.userId = userId;
        this.status = status;
    }

    public Report(String reportId, String executionDate, String title, String status) {
        this.reportId = reportId;
        this.executionDate = executionDate;
        this.title = title;
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(String executionDate) {
        this.executionDate = executionDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}