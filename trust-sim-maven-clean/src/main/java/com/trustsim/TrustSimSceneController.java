package com.trustsim;

import com.trustsim.about.AboutController;
import com.trustsim.calculatorEvaluator.CalculatorEvaluatorController;
import com.trustsim.evaluator.EvaluatorController;
import com.trustsim.home.HomeController;
import com.trustsim.importer.ImporterController;
import com.trustsim.simulator.SimulatorController;
import com.trustsim.synthesiser.SynthesiserController;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Singleton Class is Scene Controller of application, used to manage and switch JavaFX displayed.
 */
public class TrustSimSceneController {

    private final Stage stage;
    private static TrustSimSceneController INSTANCE = null;

    /**
     * Singleton instance getter function.
     *
     * @return Reference to Singleton instance of this class
     */
    public static TrustSimSceneController getInstance(Stage stage) {
        if (INSTANCE == null) {
            INSTANCE = new TrustSimSceneController(stage);
        }
        return INSTANCE;
    }

    /**
     * Constructor, used to initialise current JavaFX Stage which this class manages
     * the scenes of.
     *
     * @param stage JavaFX Stage
     */
    private TrustSimSceneController(Stage stage) {
        this.stage = stage;
    }

    /**
     * Function used to switch the JavaFX Scene currently displayed on the JavaFX Stage
     * of the application.
     *
     * @param sceneName the name of the scene to be displayed
     * @throws IOException is thrown if the scene change does not complete successfully.
     */
    public void changeScene(String sceneName) throws IOException {
        switch(sceneName) {
            case "Home" -> {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
                Parent pane = loader.load();
                HomeController homeController = loader.getController();
                homeController.setStage(stage);
                Scene scene = stage.getScene();
                if (scene == null) scene = new Scene(pane);
                scene.setRoot(pane);
                stage.setTitle("Home - TrustSim");
                stage.setScene(scene);
                setStageDimensions();
            }
            case "Importer" -> {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Importer.fxml"));
                Parent pane = loader.load();
                ImporterController importerController = loader.getController();
                importerController.setStage(stage);
                Scene scene = stage.getScene();
                if (scene == null) scene = new Scene(pane);
                scene.setRoot(pane);
                stage.setTitle("Importer - TrustSim");
                stage.setScene(scene);
                setStageDimensions();
            }
            case "Simulator" -> {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Simulator.fxml"));
                Parent pane = loader.load();
                SimulatorController simulatorController = loader.getController();
                simulatorController.setStage(stage);
                Scene scene = stage.getScene();
                if (scene == null) scene = new Scene(pane);
                scene.setRoot(pane);
                stage.setTitle("Simulator - TrustSim");
                stage.setScene(scene);
                setStageDimensions();
            }
            case "Synthesiser" -> {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Synthesiser.fxml"));
                Parent pane = loader.load();
                SynthesiserController synthesiserController = loader.getController();
                synthesiserController.setStage(stage);
                Scene scene = stage.getScene();
                if (scene == null) scene = new Scene(pane);
                scene.setRoot(pane);
                stage.setTitle("Synthesiser - TrustSim");
                stage.setScene(scene);
                setStageDimensions();
            }
            case "CalculatorEvaluator" -> {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/CalculatorEvaluator.fxml"));
                Parent pane = loader.load();
                CalculatorEvaluatorController evaluatorController = loader.getController();
                evaluatorController.setStage(stage);
                Scene scene = stage.getScene();
                if (scene == null) scene = new Scene(pane);
                scene.setRoot(pane);
                stage.setTitle("Calculator Evaluator - TrustSim");
                stage.setScene(scene);
                setStageDimensions();
            }
            case "Evaluator" -> {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Evaluator.fxml"));
                Parent pane = loader.load();
                EvaluatorController evaluatorController = loader.getController();
                evaluatorController.setStage(stage);
                Scene scene = stage.getScene();
                if (scene == null) scene = new Scene(pane);
                scene.setRoot(pane);
                stage.setTitle("Evaluator - TrustSim");
                stage.setScene(scene);
                setStageDimensions();
            }
            case "About" -> {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/About.fxml"));
                Parent pane = loader.load();
                AboutController aboutController = loader.getController();
                aboutController.setStage(stage);
                Scene scene = stage.getScene();
                if (scene == null) scene = new Scene(pane);
                scene.setRoot(pane);
                stage.setTitle("About - TrustSim");
                stage.setScene(scene);
                setStageDimensions();
            }
        }
    }

    /**
     * Function initialises Stage dimensions.
     */
    private void setStageDimensions() {
        stage.setWidth(1000);
        stage.setHeight(700);
        stage.setMinWidth(1000);
        stage.setMaxWidth(1000);
        stage.setMinHeight(700);
        stage.setMaxHeight(700);
        stage.show();
    }


}
