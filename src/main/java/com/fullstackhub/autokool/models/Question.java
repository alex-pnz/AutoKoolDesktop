package com.fullstackhub.autokool.models;

public class Question {
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String answer1;
    private String answer2;
    private String answer3;
    private String image;

    public Question(String question, String option1, String option2, String option3, String answer1, String answer2, String answer3, String image) {
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
}
