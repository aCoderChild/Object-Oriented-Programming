package com.example.my_group_project.Controllers;

import com.almasb.fxgl.audio.Sound;
import com.example.my_group_project.Controllers.User.UserProfileUserFormController;
import com.example.my_group_project.HelloApplication;
import com.example.my_group_project.SoundPlay;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Stack;

public abstract class BaseController {
    private static Stage mainStage;
    private static Stack<Scene> sceneStack = new Stack<>();
    private static Stack<FXMLLoader> fxmlLoaderStack = new Stack<>();

    // Method to set the main stage
    public static void setMainStage(Stage stage) {
        mainStage = stage;
    }

    // Method to get the main stage (optional convenience method)
    public static Stage getMainStage() {
        return mainStage;
    }

    // Method to change the scene and push the current scene to the stack
   /* protected void changeScene(String fxmlFile, String title) {
        if (mainStage == null) {
            throw new IllegalStateException("Main stage is not set. Ensure setMainStage is called.");
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/my_group_project/"+fxmlFile));
            Parent root = loader.load();
            Scene newScene = new Scene(root);

            sceneStack.push(newScene);

            mainStage.setTitle(title);
            mainStage.setScene(newScene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    */

    public static void changeScene(String fxml, String title) {
        if (mainStage == null) {
            throw new IllegalStateException("Main stage is not set. Ensure setMainStage is called.");
        }
        try {
            FXMLLoader loader = new FXMLLoader(BaseController.class.getResource("/com/example/my_group_project/"+fxml));
            if(loader.getRoot() != null) {
                loader.setRoot(null);
            }
            loader.setController(null);
            Parent root = loader.load();
            Scene newScene = new Scene(root);
            fxmlLoaderStack.push(loader);
            // Push the new scene to the stack
            //sceneStack.push(newScene);
            mainStage.setTitle(title);
            mainStage.setScene(newScene);

            // Print the scene stack for debugging
            // printSceneStack();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    // Method to handle the back button action
    public void backButtonOnAction(ActionEvent event) {
        SoundPlay.playSound("/soundEffects/SEFE_MouseClick.wav");
        if (mainStage == null) {
            throw new IllegalStateException("Main stage is not set. Ensure setMainStage is called.");
        }
        if (!fxmlLoaderStack.isEmpty()) {
            fxmlLoaderStack.pop(); // Bỏ giao diện hiện tại

            if (!fxmlLoaderStack.isEmpty()) {
                try {
                    FXMLLoader previousLoader = fxmlLoaderStack.peek();
                    previousLoader.setRoot(null); // Xóa root hiện tại nếu có
                    previousLoader.setController(null);// Lấy giao diện trước đó
                    Parent root = previousLoader.load(); // Tải giao diện

                    // Thiết lập lại giao diện chính
                    Scene scene = new Scene(root);
                    mainStage.setScene(scene);
                    mainStage.setTitle(mainStage.getTitle());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("No previous screen to go back to.");
            }
        }
    }


    public static boolean showAlert(String title, String message){
        SoundPlay.playSound("/soundEffects/SEFE_Notification_Bell.wav");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.setContentText(null);
        alert.getDialogPane().setStyle("-fx-background-color: #f7efd8; -fx-border-radius: 20px;");
        alert.getDialogPane().getStylesheets().add(BaseController.class.getResource("/custom.css").toExternalForm());
        ButtonType sureButton = new ButtonType("Đồng ý");
        ButtonType cancelButton = new ButtonType("Hủy bỏ", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(sureButton, cancelButton);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == sureButton) {
                return true;
            } else if (result.get() == cancelButton) {
                return false;
            }
        }
        return false;
    }

    @FXML
    public static void showAlert1(String title, String message) {
        SoundPlay.playSound("/soundEffects/SEFE_Notification_Bell.wav");
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().setStyle("-fx-background-color: #f7efd8; -fx-border-radius: 20px;");
        String cssPath = UserProfileUserFormController.class.getResource("/custom.css").toExternalForm();
        alert.getDialogPane().getStylesheets().add(cssPath);
        alert.showAndWait();
    }

    //hien "Chao mung ban " sau khi dang nhap vao
    //gap loi luc nao vao home la cu hien TT
    public static void showIntro(String message, Stage stage){
        Popup popup = new Popup();
        popup.setAutoHide(true);

        Label popupContent = new Label(message);
        popupContent.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-padding: 10px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        popupContent.setFont(new Font("Arial", 14));
        popupContent.setTextFill(Color.WHITE);

        Pane pane = new Pane(popupContent);
        pane.setStyle("-fx-background-color: white ");
        popup.getContent().add(pane);
        popup.show(stage);
        popup.setX(stage.getX() + stage.getWidth() / 2 );
        popup.setY(stage.getY() + stage.getHeight() / 2);
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(10), popupContent);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(event -> popup.hide());

        fadeOut.play();
    }



}