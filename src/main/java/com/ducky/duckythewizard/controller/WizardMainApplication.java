package com.ducky.duckythewizard.controller;

import com.ducky.duckythewizard.model.CardDeck;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class WizardMainApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("/com/ducky/duckythewizard/scenes/startingScene.fxml")));
        Scene scene = new Scene(root);
        stage.setTitle("Ducky the wizard");
        String css = Objects.requireNonNull(this.getClass().getResource("/com/ducky/duckythewizard/styles/styleRenate.css")).toExternalForm();
        scene.getStylesheets().clear();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {

        CardDeck testDeck = new CardDeck();
        //testDeck.showAllCardDeckInfo();
        testDeck.takeCardsFromDeck(5);
        testDeck.showAllCardDeckInfo();

        launch();
    }
}