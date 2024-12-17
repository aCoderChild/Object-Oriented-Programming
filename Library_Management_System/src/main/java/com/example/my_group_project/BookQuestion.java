package com.example.my_group_project;

public class BookQuestion {
    private String question;
    private String correctAnswer;

    public BookQuestion(String question, String correctAnswer) {
        this.question = question;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
