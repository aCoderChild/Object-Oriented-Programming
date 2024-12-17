package com.example.my_group_project;

import com.example.my_group_project.Book.RecentBook;
import com.example.my_group_project.Controllers.BaseController;
import com.example.my_group_project.Database.BookInDatabase;
import com.example.my_group_project.User.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        initializeMainStage(stage);
        stage.setOnCloseRequest(this::handleWindowCloseRequest);
    }

    private void initializeMainStage(Stage stage) throws IOException {
        BaseController.setMainStage(stage);
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("welcomeToWebsite.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 600);
        stage.setTitle("Xin văn chào!");
        stage.setScene(scene);
        stage.show();
    }

    private void handleWindowCloseRequest(WindowEvent event) {
        System.out.println("Close request received"); // Debugging statement
        Alert alert = createConfirmationAlert();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get().getButtonData() == ButtonBar.ButtonData.YES) {
                try {
                    saveRecentBooksToDatabase();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                event.consume();
            }
        }
    }

    private Alert createConfirmationAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("bye bye");
        alert.setHeaderText("BẠN ĐỊNH BỎ TUI THIỆC HẢ T.T");
        alert.setContentText(null);
        alert.getDialogPane().setStyle("-fx-background-color: #f7efd8; -fx-border-radius: 20px;");
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/custom.css").toExternalForm());

        ButtonType sureButton = new ButtonType("TẠM BIỆT", ButtonBar.ButtonData.YES);
        ButtonType cancelButton = new ButtonType("TÔI Ở LẠI NÈ", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(sureButton, cancelButton);
        return alert;
    }

    public static void saveRecentBooksToDatabase() throws SQLException {
        User currentUser = User.getCurrentUser();
        if (currentUser != null) {
            RecentBook recentBook = currentUser.getRecentBook();
            if (recentBook != null) {  // Added null check for recentBook
                BookInDatabase.saveRecentBooks(recentBook);
            } else {
                System.out.println("Error: RecentBook is null.");
            }
        } else {
            System.out.println("Error: CurrentUser is null.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}