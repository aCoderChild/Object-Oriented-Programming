package com.example.my_group_project;

import com.example.my_group_project.Controllers.BaseController;
import com.example.my_group_project.Controllers.User.UserMenuController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class BookQuizGameController extends UserMenuController {
    @FXML
    private Label questionLabel;
    @FXML
    private TextField answerTextField;
    @FXML
    private TextArea questionTextArea;
    @FXML
    private Button checkButton;
    @FXML
    private Button nextButton;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label feedbackLabel;

    private List<BookQuestion> questions = new ArrayList<>();
    private List<BookQuestion> selectedQuestions = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int score = 0;
    private int numQuestions = 10; // Default value

    @FXML
    public void initialize() {
        loadQuestionsFromFile("src/main/resources/questionsAndAnswers/questions.rtf", "src/main/resources/questionsAndAnswers/answers.rtf");
        checkButton.setDisable(true); // Initially disable the check button
        answerTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            checkButton.setDisable(newValue.trim().isEmpty());
        });
    }

    public void setNumQuestions(int numQuestions) {
        this.numQuestions = numQuestions;
        startQuiz();
    }

    private void loadQuestionsFromFile(String questionsFilePath, String answersFilePath) {
        try (BufferedReader questionReader = new BufferedReader(new FileReader(questionsFilePath));
             BufferedReader answerReader = new BufferedReader(new FileReader(answersFilePath))) {

            String questionLine;
            String answerLine;
            while ((questionLine = questionReader.readLine()) != null && (answerLine = answerReader.readLine()) != null) {
                questions.add(new BookQuestion(questionLine, answerLine));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startQuiz() {
        selectedQuestions.clear();
        Collections.shuffle(questions);
        for (int i = 0; i < Math.min(numQuestions, questions.size()); i++) {
            selectedQuestions.add(questions.get(i));
        }
        currentQuestionIndex = 0;
        score = 0;
        scoreLabel.setText(String.valueOf(score));
        loadQuestion();
    }

    @FXML
    private void handleCheckButtonAction(ActionEvent event) {
        checkAnswer();
        checkButton.setDisable(true); // Disable check button after checking the answer
        answerTextField.setEditable(false); // Make the text field non-editable
    }

    @FXML
    private void handleNextButtonAction(ActionEvent event) {
        currentQuestionIndex++;
        if (currentQuestionIndex < selectedQuestions.size()) {
            loadQuestion();
        } else {
            endQuiz();
        }
    }

    private void loadQuestion() {
        BookQuestion currentQuestion = selectedQuestions.get(currentQuestionIndex);
        questionLabel.setText(String.valueOf(currentQuestionIndex + 1));
        questionTextArea.setText(currentQuestion.getQuestion());
        answerTextField.clear();
        answerTextField.setEditable(true); // Make the text field editable again for the next question
        checkButton.setDisable(true); // Disable check button until the user enters text
        feedbackLabel.setText(""); // Clear feedback label
    }

    private void checkAnswer() {
        BookQuestion currentQuestion = selectedQuestions.get(currentQuestionIndex);
        String playerAnswer = answerTextField.getText().trim();
        if (playerAnswer.equalsIgnoreCase(currentQuestion.getCorrectAnswer())) {
            SoundPlay.playSound("/soundEffects/SEFE_CrowdClapping.wav");
            score++;
            feedbackLabel.setText("Correct!");
        } else {
            SoundPlay.playSound("/soundEffects/SEFE_Wrong_Answer.wav");
            feedbackLabel.setText("Incorrect. The correct answer is: " + currentQuestion.getCorrectAnswer());
        }
        scoreLabel.setText(String.valueOf(score));
    }

    private void showConfirmationAndChangeScene(String fxmlFilePath, String title) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Action");
        alert.setHeaderText("Are you sure you want to proceed?");
        alert.setContentText("This will end the current quiz.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Change scene if user confirms
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFilePath));
                Parent root = loader.load();
                Stage stage = (Stage) questionLabel.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle(title);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void homeOnAction(ActionEvent event) {
        SoundPlay.playSound("/soundEffects/SEFE_MouseClick.wav");
        showConfirmationAndChangeScene("home.fxml", "Home");
    }

    @FXML
    public void moreInforOnAction(ActionEvent event) {
        SoundPlay.playSound("/soundEffects/SEFE_MouseClick.wav");
        showConfirmationAndChangeScene("moreInformation.fxml", "More Information");
    }

    @FXML
    public void gameButtonOnAction(ActionEvent event) {
        SoundPlay.playSound("/soundEffects/SEFE_MouseClick.wav");
        showConfirmationAndChangeScene("bookQuizGameStart.fxml", "bookQuizGameStart");
    }

    @FXML
    public void profileOnAction(ActionEvent event) {
        SoundPlay.playSound("/soundEffects/SEFE_MouseClick.wav");
        showConfirmationAndChangeScene("profileUser.fxml", "Profile");
    }

    @FXML
    public void searchOnAction(ActionEvent event) {
        SoundPlay.playSound("/soundEffects/SEFE_MouseClick.wav");
        showConfirmationAndChangeScene("searchBook.fxml", "Searching");
    }

    private void endQuiz() {
        questionTextArea.setText("Quiz Over! Your final score is " + score + ".");
        answerTextField.setVisible(false);
        checkButton.setVisible(false);
        feedbackLabel.setText("");
        showEndQuizDialog();
    }

    private void showEndQuizDialog() {
        SoundPlay.playSound("/soundEffects/SEFE_Bell.wav");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Quiz Over!");
        alert.setHeaderText("Congratulations!");

        if (score == numQuestions) {
            alert.setContentText("Perfect score! You've answered all questions correctly.");
        } else {
            alert.setContentText("Your final score is " + score + ". Better luck next time!");
        }

        alert.getDialogPane().setStyle("-fx-background-color: #f7efd8; -fx-border-radius: 20px;");
        alert.getDialogPane().getStylesheets().add(BaseController.class.getResource("/custom.css").toExternalForm());

        ButtonType continueButton = new ButtonType("Continue Playing");
        ButtonType exitButton = new ButtonType("Exit to Main Menu");
        alert.getButtonTypes().setAll(continueButton, exitButton);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == continueButton) {
            changeScene("bookQuizGameStart.fxml", "bookQuizGameStart");
        } else if (result.isPresent() && result.get() == exitButton) {
            changeScene("home.fxml", "home");
        }
    }
}