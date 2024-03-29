package com.ducky.duckythewizard.controller.scenes;

import com.ducky.duckythewizard.controller.Controller;
import com.ducky.duckythewizard.model.Host;
import com.ducky.duckythewizard.model.ServerFacade;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.util.Objects;


public class LoginSceneController extends Controller {

    @FXML
    private TextField userTextField;
    @FXML
    private Label infoLabel;
    private ServerFacade serverFacade;

    @FXML
    public void initialize(){

        this.serverFacade = new ServerFacade();
        this.infoLabel.setVisible(false);
        this.infoLabel.setAlignment(Pos.CENTER);

        int maxLength = 15;
        userTextField.setPromptText("Username");
        userTextField.setFocusTraversable(false);
        userTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (userTextField.getText().length() > maxLength) {
                    String s = userTextField.getText().substring(0, maxLength);
                    userTextField.setText(s);
                }
            }
        });
    }

    public void registerUser(MouseEvent mouseEvent) {
        this.infoLabel.setVisible(true);
        if(userTextField.getText().length() == 0){
            infoLabel.setText("Please enter a name");
            return;
        }
        boolean success = serverFacade.addNewUser(userTextField.getText());
        if (success) {
            createHostAndNavigateToStartScreen(mouseEvent, userTextField.getText());
        } else {
            infoLabel.setText("Username already exists");
        }
    }

    public void loginUser(MouseEvent mouseEvent) {
        infoLabel.setVisible(true);
        if(userTextField.getText().length() == 0){
            infoLabel.setText("Please enter a name");
            return;
        }
        boolean success = serverFacade.getIfUserExists(userTextField.getText());
        if (success) {
            createHostAndNavigateToStartScreen(mouseEvent, userTextField.getText());
        } else {
            infoLabel.setText("User not found");
        }
    }

    private void createHostAndNavigateToStartScreen(MouseEvent mouseEvent, String userName){
        if (Host.getInstance() == null){
            Host.createHostInstance(userName);
        } else {
            Host.getInstance().setPlayerName(userName);
        }

        // navigate to menu
        String scene = "/com/ducky/duckythewizard/scenes/startingScene.fxml";

        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource(scene)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        super.showWindowStage(mouseEvent, root);
    }
}