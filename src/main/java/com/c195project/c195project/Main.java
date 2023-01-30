package com.c195project.c195project;

import com.c195project.c195project.controller.LoginPage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 409, 235);
        if(Locale.getDefault().getLanguage().equals("fr")) {
            stage.setTitle(LoginPage.convertWordToFrenchCA("Login"));
        }else{
            stage.setTitle("Login");
        }
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}