package com.trustsim.home;

import com.trustsim.TrustSimSceneController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/** Class is Controller of the Home Scene of application. */
public class HomeController implements Initializable {

  @FXML private HBox trustSimLogoHBox;
  @FXML private ImageView trustSimLogoView;
  @FXML private Button importerButton;
  @FXML private Button synthesiserButton;
  @FXML private Button simulatorButton;
  @FXML private Button evaluatorButton;
  @FXML private Button calculatorEvaluatorButton;
  @FXML private Button importerHelpButton;
  @FXML private Button evaluatorHelpButton;
  @FXML private Button calculatorEvaluatorHelpButton;
  @FXML private Button simulatorHelpButton;
  @FXML private Button synthesiserHelpButton;
  @FXML private MenuItem toAboutPageButton;

  public Stage stage;

  /**
   * Function initialises controller for Home screen.
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
   * Function used as event dispatcher for scene's buttons, that for changing Scene of application
   * or display tooltip information objects.
   *
   * @param event stores data related to caller of event
   */
  @FXML
  private void onMouseClicked(ActionEvent event) {

    Object eventSource = event.getSource();
    TrustSimSceneController sceneController = TrustSimSceneController.getInstance(null);

    try {
      if (eventSource.equals(importerButton)) {
        sceneController.changeScene("Importer");
      } else if (eventSource.equals(simulatorButton)) {
        sceneController.changeScene("Simulator");
      } else if (eventSource.equals(synthesiserButton)) {
        sceneController.changeScene("Synthesiser");
      } else if (eventSource.equals(evaluatorButton)) {
        sceneController.changeScene("Evaluator");
      } else if (eventSource.equals(calculatorEvaluatorButton)) {
        sceneController.changeScene("CalculatorEvaluator");
      } else if (eventSource.equals(toAboutPageButton)) {
        sceneController.changeScene("About");
      } else if (eventSource.equals(importerHelpButton)
          || eventSource.equals(synthesiserHelpButton)
          || eventSource.equals(simulatorHelpButton)
          || eventSource.equals(evaluatorHelpButton)
          || eventSource.equals(calculatorEvaluatorHelpButton)) {

        final Tooltip helpTooltip = new Tooltip();
        helpTooltip.setShowDelay(new Duration(0.1));

        if (importerHelpButton.equals(eventSource)) {
          helpTooltip.setText("Import & Calculate Trust Scores of a Trust System");
        } else if (eventSource.equals(synthesiserHelpButton)) {
          helpTooltip.setText("Generate a Synthetic Trust System");
        } else if (eventSource.equals(simulatorHelpButton)) {
          helpTooltip.setText("Simulate a Trust System");
        } else if (eventSource.equals(evaluatorHelpButton)) {
          helpTooltip.setText("Analyse Results from Trust System Simulations");
        } else if (eventSource.equals(calculatorEvaluatorHelpButton)) {
          helpTooltip.setText("Analyse Results from Trust System Calculations");
        }
        ((Button) eventSource).setTooltip(helpTooltip);
        helpTooltip.setAutoHide(true);
        Point2D p = ((Button) eventSource).localToScene(20.0, 20.0);
        helpTooltip.show(
            stage,
            p.getX()
                + ((Button) eventSource).getScene().getX()
                + ((Button) eventSource).getScene().getWindow().getX(),
            p.getY()
                + ((Button) eventSource).getScene().getY()
                + ((Button) eventSource).getScene().getWindow().getY());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
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
