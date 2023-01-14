package com.ducky.duckythewizard.model;

import com.ducky.duckythewizard.model.card.CardModel;
import com.ducky.duckythewizard.model.config.GameConfig;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;

public class Player {
    private String name = "testname";
    private ArrayList<CardModel> handCards;
    private int healthPoints;
    private int maxHealthPoints = GameConfig.PLAYER_MAX_HEALTH_POINTS;
    private int playableCards;

    public SimpleIntegerProperty score;
    public SimpleStringProperty timerProperty;
    public SimpleStringProperty timerTextLabel;
    public SimpleStringProperty timerLabel;
    public SimpleStringProperty timerLabelStyle;
    private long resetTime;

    private AnimatedSprite playerSprite;

    public Player() {
        this.resetPlayer();
    }

    public void resetPlayer(){
        this.healthPoints = maxHealthPoints;
        this.score = new SimpleIntegerProperty(0);
        this.resetTime = System.nanoTime();
        this.timerProperty = new SimpleStringProperty(Integer.toString(GameConfig.PLAYER_ACTION_TIMER));
        this.timerTextLabel = new SimpleStringProperty();
        this.timerLabel = new SimpleStringProperty();
        this.timerLabelStyle = new SimpleStringProperty();
        this.playableCards = GameConfig.AMOUNT_HAND_CARDS;
    }

    //getter & setter

    public void setPlayerName(String name) {
        this.name = name;
    }
    public String getPlayerName() {
        return this.name;
    }
    public AnimatedSprite getPlayerSprite() {
        return this.playerSprite;
    }
    public int getHealthPoints() {
        return this.healthPoints;
    }
    public int getScore() {
        return this.score.getValue();
    }
    public ArrayList<CardModel> getHandCards() {
        return this.handCards;
    }
    public int getPlayableCards() {
        return this.playableCards;
    }

    public void setHandCards(ArrayList<CardModel> handCards) {
        this.handCards = handCards;
    }
    public void setPlayerSprite(AnimatedSprite sprite) {
        this.playerSprite = sprite;
    }
    public void setPlayableCards(int playableCards) {
        this.playableCards = playableCards;
    }


    public void addToScore (int points) {
        this.score.set(score.getValue() + points);
        System.out.println("==> adding " + points + " to score, TOTAL SCORE is now: " + score.getValue());
    }

    public void reducePlayerTimer() {
        int time = (int)((System.nanoTime() - this.resetTime) / 1000000000.0);
        this.timerProperty.set(Integer.toString(GameConfig.PLAYER_ACTION_TIMER - time <= 0 ? 0 : GameConfig.PLAYER_ACTION_TIMER - time));
        if(this.timerProperty.getValue().equals("3")) {
            this.updateTimerLabel(true);
        }
        if(this.timerProperty.getValue().equals("0")) {
            this.reducePlayerLife();
            this.updateTimerLabel(false);
        }
    }

    public void resetPlayerTimer() {
        this.timerProperty.set(Integer.toString(GameConfig.PLAYER_ACTION_TIMER));
        this.resetTime = System.nanoTime();
    }

    private void reducePlayerLife(){
        this.healthPoints = this.healthPoints <= 0 ? 0 : this.healthPoints - 1;
        this.resetPlayerTimer();
    }

    public void decrementHandCards() {
        this.playableCards--;
    }

    public boolean checkAvailableCards() {
        int cardsAvailable = GameConfig.AMOUNT_HAND_CARDS;
        for (CardModel handCard : this.handCards) {
            if (handCard.getColor().getName().equals("none")) {
                cardsAvailable--;
            }
        }
        return cardsAvailable >= 1;
    }

    public void updateTimerLabel(boolean critical) {
        if (critical) {
            this.timerTextLabel.set("-fx-text-fill: red; -fx-stroke: red; -fx-stroke-type: OUTSIDE; -fx-stroke-width: 1px;");
            this.timerLabelStyle.set("-fx-text-fill: red; -fx-stroke: red; -fx-stroke-type: OUTSIDE; -fx-stroke-width: 1px;");
        } else {
            this.timerTextLabel.set("-fx-stroke: black; -fx-stroke-type: OUTSIDE; -fx-stroke-width: 2px;");
            this.timerLabelStyle.set("-fx-stroke: black; -fx-stroke-type: OUTSIDE; -fx-stroke-width: 2px;");
        }
    }
}
