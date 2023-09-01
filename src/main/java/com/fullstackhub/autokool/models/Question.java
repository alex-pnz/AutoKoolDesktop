package com.fullstackhub.autokool.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Question {
    private int id;
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String answer1;
    private String answer2;
    private String answer3;
    private String image;
    private boolean result;

    public Question(int id, String question, String option1, String option2, String option3, String answer1, String answer2, String answer3, String image) {
        this.id = id;
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.image = image;
    }

    public String getQuestion() {
        return question;
    }

    public String getOption1() {
        return option1;
    }

    public String getOption2() {
        return option2;
    }

    public String getOption3() {
        return option3;
    }

    public String getAnswer1() {
        return answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public String getImage() {
        return image;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "Question{" +
                "question='" + question + '\'' +
                ", option1='" + option1 + '\'' +
                ", option2='" + option2 + '\'' +
                ", option3='" + option3 + '\'' +
                ", answer1='" + answer1 + '\'' +
                ", answer2='" + answer2 + '\'' +
                ", answer3='" + answer3 + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isResult() {
        return result;
    }

    //Values for JavaFx TableView

    public StringProperty getQuestionProperty() {
        return new SimpleStringProperty(question);
    }
    public StringProperty getOption1Property() {
        return new SimpleStringProperty(option1);
    }
    public StringProperty getOption2Property() {
        return new SimpleStringProperty(option2);
    }
    public StringProperty getOption3Property() {
        return new SimpleStringProperty(option3);
    }
    public StringProperty getAnswer1Property() {
        return new SimpleStringProperty(answer1);
    }
    public StringProperty getAnswer2Property() {
        return new SimpleStringProperty(answer2);
    }
    public StringProperty getAnswer3Property() {
        return new SimpleStringProperty(answer3);
    }
    public StringProperty getImageProperty() {
        return new SimpleStringProperty(image);
    }
}
