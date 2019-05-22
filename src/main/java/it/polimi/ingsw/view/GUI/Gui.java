package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.view.UserInterface;
import it.polimi.ingsw.view.View;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Gui extends Application implements UserInterface {

    public static final float DEFAULT_MIN_WIDTH = 900;
    public static final float DEFAULT_MIN_HEIGHT = 506;

    public Gui(){
        super();
    }

    private View view;

    public void setView(View view) {
        this.view = view;
    }

    @FXML
    Button login;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sample.fxml"));
        root.setId("pane");

        Scene scene = new Scene(root, DEFAULT_MIN_WIDTH, DEFAULT_MIN_HEIGHT);
        scene.getStylesheets().addAll(this.getClass().getClassLoader().getResource("style.css").toExternalForm());
        Image img = new Image("/images/background_image.png");

        stage.setMinWidth(DEFAULT_MIN_WIDTH);
        stage.setMinHeight(DEFAULT_MIN_HEIGHT);
        stage.setTitle("Adrenalina");
        stage.setScene(scene);
        stage.show();

    }

    @Override
    public void startUI() {
        launch();
    }

    @Override
    public void show(String s) {

    }

    @Override
    public void gameSelection() {

    }

    @Override
    public void login() {

    }

    @Override
    public void retryLogin(String error) {

    }

    @Override
    public void retryLogin(Exception e) {

    }

    @Override
    public void startSpawn() {

    }

    @Override
    public void startPowerUp() {

    }

    @Override
    public void startAction() {

    }

    @Override
    public void startReload() {

    }
}
