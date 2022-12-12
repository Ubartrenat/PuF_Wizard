package com.ducky.duckythewizard.model;

import com.ducky.duckythewizard.model.card.Card;
import com.ducky.duckythewizard.model.card.CardDeck;
import com.ducky.duckythewizard.model.color.GameColorObject;
import com.ducky.duckythewizard.model.config.GameConfig;
import com.ducky.duckythewizard.controller.CardController;
import com.ducky.duckythewizard.controller.MovementController;
import com.ducky.duckythewizard.view.FightScene;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;

public class Game {

    private boolean isRunning;
    private Player player; //Attribut für Daten zum Spieler selbst (Name etc.)
    private Card clickedCardFight;
    private Fight activeFight;
    private FightScene fightScene;
    //private Sprite ducky = new DuckySprite(5, collisionHandler); //Attribut für Player-Sprite
    private AnchorPane rootAnchorPane;
    private CardDeck cardDeck;
    private AnchorPane anchorPaneCards;
    private GameColorObject gameColorObject;

    //die Game Config wird einmalig zum Start des Games (Erstellung des Game-Klassen-Objekts) erstellt
    private GameConfig gameConfig;
    public GameObject[][] objectGrid; // [row][column] - is filled in GameController
    public ArrayList<Stone> stoneArrayList;

    private Scene startScene;
    private Scene deathScene;
    private Scene winScene;
    private Scene playScene;

    private CardController cardCtrl;
    private MovementController movementCtrl;

    public Game(){
        isRunning = true;
        gameConfig = new GameConfig();
        this.gameColorObject = new GameColorObject();
        this.cardDeck = new CardDeck(this.gameColorObject);
        this.player = new Player(this.cardDeck);
        this.stoneArrayList = new ArrayList<>();
        this.fightScene = new FightScene();
        //System.out.println("*** Game-object is created.");
    }
    public boolean getIsRunning(){

        return isRunning;
    }
    public void toggleIsRunning() {
        System.out.println("--> toggleIsRunning");
        isRunning = !isRunning;
    }

    // create anstatt set, weil hier ein neues Controller-Objekt erstellt wird
    public void createCardCtrlObj() {
        this.cardCtrl = new CardController(this);
    }
    public void createMovementCtrlObj() {
        this.movementCtrl = new MovementController(this);
    }

    public GameConfig getGameConfig() {

        return this.gameConfig;
    }

    public CardController getCardCtrl() {

        return this.cardCtrl;
    }
    public MovementController getMovementCtrl() {

        return this.movementCtrl;
    }

    public CardDeck getCardDeck() {
        return this.cardDeck;
    }

    public AnchorPane getAnchorPaneCards() {
        return anchorPaneCards;
    }

    public void setAnchorPaneCards(AnchorPane anchorPane) {
        this.anchorPaneCards = anchorPane;
    }

    public AnchorPane getRootAnchorPane() {
        return rootAnchorPane;
    }

    public void setRootAnchorPane(AnchorPane rootAnchorPane) {
        this.rootAnchorPane = rootAnchorPane;
    }

    public GameColorObject getGameColorObject() {
        return gameColorObject;
    }

    public Player getPlayer() {
        return this.player;
    }

    public ArrayList<Stone> getStoneArrayList() {
        return stoneArrayList;
    }

    public Card getClickedCardFight() {
        return clickedCardFight;
    }

    public void setClickedCardFight(Card clickedCardFight) {
        this.clickedCardFight = clickedCardFight;
    }

    public Fight getActiveFight() {
        return activeFight;
    }

    public void setActiveFight(Fight activeFight) {
        this.activeFight = activeFight;
    }

    public FightScene getFightScene() {
        return fightScene;
    }
}
