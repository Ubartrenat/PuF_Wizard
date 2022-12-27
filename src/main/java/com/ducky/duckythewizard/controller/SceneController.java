package com.ducky.duckythewizard.controller;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/*Superclass of all SceneControllers*/
public class SceneController {

    String css = Objects.requireNonNull(this.getClass().getResource("/com/ducky/duckythewizard/styles/style.css")).toExternalForm();

    /*die bereits beim Start erstellte Stage wird anhand des Events ermittelt. Darauf wird eine neue Scene erstellt*/
    protected void getWindowStage(Event event, Parent root) {
        root.requestFocus();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.setRoot(root);
        scene.getStylesheets().clear();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }

    public void startGame(Event event) throws IOException {
        /*Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        WizardMainApplication reload = new WizardMainApplication();
        reload.start(stage);*/
        AnchorPane newRoot = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("/com/ducky/duckythewizard/scenes/game-view.fxml")));
        getWindowStage(event, newRoot);
    }
}
