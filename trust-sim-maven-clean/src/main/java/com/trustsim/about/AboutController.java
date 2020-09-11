package com.trustsim.about;

import com.trustsim.TrustSimSceneController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/** Class is Controller of the About Scene of application. */
public class AboutController implements Initializable {

  @FXML private AnchorPane aboutPane;
  @FXML private HBox trustSimLogoHBox;
  @FXML private ImageView trustSimLogoView;

  private Stage stage;

  /**
   * Function initialises controller for About screen.
   *
   * @param location specifies fxml file location
   * @param resources specifies resource bundle
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    trustSimLogoHBox.setBackground(
        new Background(new BackgroundFill(Color.DARKBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
    Image trustSimLogo = new Image(getClass().getResourceAsStream("/images/logo/trustsimlogo.png"));
    trustSimLogoView.setImage(trustSimLogo);
  }

  /**
   * Function used to switch to Main Menu Scene.
   *
   * @param event stores data related to caller of event
   * @throws IOException if scene change not successful
   */
  @FXML
  public void toMainMenu(Event event) throws IOException {
    TrustSimSceneController sceneController = TrustSimSceneController.getInstance(null);
    sceneController.changeScene("Home");
  }

  /**
   * Function used externally to set the stage object that this evaluator controller's view is
   * displayed within.
   *
   * @param stage JavaFX stage object
   */
  public void setStage(Stage stage) {
    this.stage = stage;
  }
}
