package com.zaidhuda.pollease;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Zaid on 17/12/2015.
 */
public class Poll implements Serializable {
    private final int id;
    private String question;
    private ArrayList<Choice> choices;


    public Poll(int id) {
        this.id = id;
        choices = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<Choice> getChoices() {
        return choices;
    }

    public void setChoices(ArrayList<Choice> choices) {
        this.choices = choices;
    }

    public void addChoices(Choice choice) {
        this.choices.add(choice);
    }

    public int getVoteCasted() {
        int votes = 0;

        for (Choice choice : choices){
            votes += choice.getVoteCount();
        }

        return votes;
    }

    public void updateVoteCount(int selectedChoiceID, int previousChoice) {
        for (Choice choice : choices) {
            if (choice.getId() == selectedChoiceID)
                choice.setVoteCount(choice.getVoteCount() + 1);
            if (choice.getId() == previousChoice)
                choice.setVoteCount(choice.getVoteCount() - 1);
        }
    }
}
