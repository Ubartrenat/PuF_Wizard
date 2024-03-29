package com.ducky.duckythewizard.controller;

import com.ducky.duckythewizard.model.HighScore;
import com.ducky.duckythewizard.model.Host;
import com.ducky.duckythewizard.model.ServerFacade;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import java.io.IOException;


public class ScoresController extends VBox {

    private enum HighScoreContext {
        ALL,
        USER
    }

    @FXML
    private VBox scoresRoot;
    @FXML
    private TableView scoresTable;
    @FXML
    private TableColumn rankColumn;
    @FXML
    private TableColumn nameColumn;
    @FXML
    private TableColumn scoreColumn;

    private int limit = 5;
    private ObservableList<HighScore> topHighScoresObservable;
    private ServerFacade serverFacade;
    private HighScoreContext highScoreContext = HighScoreContext.ALL;
    private EventHandler<ScrollEvent> scrollEventEventHandler = new EventHandler<ScrollEvent>() {
        @Override
        public void handle(ScrollEvent scrollEvent) {
            scrollEvent.consume();
        }
    };

    public ScoresController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "/com/ducky/duckythewizard/scenes/helpScenes/scores.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        this.initialize();
    }

    private void initialize() {
        serverFacade = new ServerFacade();

        // getting top n high scores from server
        topHighScoresObservable = FXCollections.observableList(serverFacade.getTopHighScoresFromServer(limit));

        // binding columns to object attributes
        rankColumn.setCellValueFactory(
                new PropertyValueFactory<HighScore, Integer>("rank")
        );
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<HighScore, String>("name")
        );
        scoreColumn.setCellValueFactory(
                new PropertyValueFactory<HighScore, Integer>("score")
        );

        // setting table size and preventing scrolling
        this.setTable();

        // inserting server data into table
        scoresTable.setItems(topHighScoresObservable);
    }

    public boolean setGameScore(int score) {
        // send player name and score to server if score is high enough
        if (score > 0 &&
                (topHighScoresObservable.size() < limit || score > topHighScoresObservable.get(limit-1).getScore())) {

            // defining a label and the Name text field
            final Label scoreLabel = new Label();
            scoreLabel.setText("Congratulations! New high score!");
            scoreLabel.getStyleClass().add("infoLabel");
            scoreLabel.setAlignment(Pos.CENTER);
            scoreLabel.setPrefWidth(450.0);
            scoreLabel.setPadding(new Insets(20, 0, 0, 0));
            scoresRoot.getChildren().add(scoreLabel);

            serverFacade.sendHighScoreToServer(Host.getInstance().getPlayerName(), score);
            // getting updated high score list from server
            topHighScoresObservable.setAll(FXCollections.observableList(
                    serverFacade.getTopHighScoresFromServer(limit)));
            return true;
        }
        return false;
    }

    private void setTable() {
        this.scoresTable.setPlaceholder(new Label("No High Scores Yet"));
        this.scoresTable.setFixedCellSize(50);
        this.scoresTable.prefHeight((50 * topHighScoresObservable.size()) + 30);
        this.scoresTable.addEventFilter(ScrollEvent.ANY, scrollEventEventHandler);
        this.scoresTable.addEventFilter(MouseEvent.ANY, Event::consume);
    }

    // toggles between:
    // showing top <limit> high scores of all users and
    // all high scores of logged-in user
    public void toggleTableView() {
        if (highScoreContext == HighScoreContext.USER){
            highScoreContext = HighScoreContext.ALL;
            topHighScoresObservable.setAll(FXCollections.observableList(
                    serverFacade.getTopHighScoresFromServer(limit)));
            this.scoresTable.addEventFilter(ScrollEvent.ANY, scrollEventEventHandler);
        } else {
            highScoreContext = HighScoreContext.USER;
            topHighScoresObservable.setAll(FXCollections.observableList(
                    serverFacade.getAllHighScoresOfUser(Host.getInstance().getPlayerName())));
            this.scoresTable.removeEventFilter(ScrollEvent.ANY, scrollEventEventHandler);
        }
    }
}