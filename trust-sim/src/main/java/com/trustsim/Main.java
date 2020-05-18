package com.trustsim;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws IOException {

    //Label label = new Label("Hello World!");
    Parent root = null;
    try {
      root = FXMLLoader.load(getClass().getResource("main.fxml"));
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
    Scene scene = new Scene(root);
    primaryStage.setTitle("TrustSim");
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
