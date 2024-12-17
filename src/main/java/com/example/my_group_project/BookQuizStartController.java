package com.example.my_group_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import java.io.IOException;

public class BookQuizStartController {

    @FXML
    private Button startButton;

    @FXML
    private TextField numQuestionsTextField;

    @FXML
    public void handleStartQuizButtonAction(ActionEvent event) {
        String numQuestionsStr = numQuestionsTextField.getText().trim();
        try {
            int numQuestions = Integer.parseInt(numQuestionsStr);
            if (numQuestions > 0) {
                // Pass the number of questions to the next scene
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/my_group_project/BookQuizGame.fxml"));
                Parent root = loader.load();

                // Get the controller of the new scene and set the number of questions
                BookQuizGameController controller = loader.getController();
                controller.setNumQuestions(numQuestions);

                Stage stage = (Stage) numQuestionsTextField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } else {
                // Show error message for invalid input
                numQuestionsTextField.setPromptText("Enter a valid number");
            }
        } catch (NumberFormatException | IOException e) {
            // Show error message for invalid input or loading error
            numQuestionsTextField.setPromptText("Enter a valid number");
        }
    }
}
