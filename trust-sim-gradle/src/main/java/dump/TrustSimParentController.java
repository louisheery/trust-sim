//package dump;
//
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.fxml.Initializable;
//import javafx.scene.Scene;
//import javafx.scene.layout.Pane;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//import java.net.URL;
//import java.util.ResourceBundle;
//
//public class TrustSimParentController implements Initializable {
//
//  @FXML Pane homePane, ingestorPane, simulatorPane, synthesiserPane, evaluatorPane;
//  public Stage stage;
//
////
////  @FXML private HomeController homeController;
////  @FXML private IngestorController ingestorController;
////  @FXML private SimulatorController simulatorController;
////  @FXML private SynthesiserController synthesiserController;
////  private HomeEngine homeEngine;
////  private IngestorEngine ingestorEngine;
////  private SimulatorEngine simulatorEngine;
////  private SynthesiserEngine synthesiserEngine;
////  private Parent homeScreen;
////  private Parent ingestorScreen;
////  private Parent simulatorScreen;
////  private Parent synthesiserScreen;
////  private String screen;
////  private HashMap<String, Pane> screenMap = new HashMap<>();
//
//  @Override
//  public void initialize(URL location, ResourceBundle resources) {
//
//  }
//
//  public void changeScene(String sceneName) throws IOException {
//
//    switch (sceneName) {
//      case "ingestor": {
//        stage = (Stage) homePane.getScene().getWindow();
//        ingestorPane = FXMLLoader.load(getClass().getResource("/Ingestor.fxml"));
//        Scene scene = new Scene(ingestorPane);
//        stage.setScene(scene);
//        stage.setTitle("Ingestor - TrustSim");
//        stage.show();
//        break;
//      }
//      case "simulator": {
//        stage = (Stage) simulatorPane.getScene().getWindow();
//        simulatorPane = FXMLLoader.load(getClass().getResource("/Simulator.fxml"));
//        Scene scene = new Scene(simulatorPane);
//        stage.setScene(scene);
//        stage.setTitle("Simulator - TrustSim");
//        stage.show();
//        break;
//      }
//      case "synthesiser": {
//        stage = (Stage) synthesiserPane.getScene().getWindow();
//        synthesiserPane = FXMLLoader.load(getClass().getResource("/Synthesiser.fxml"));
//        Scene scene = new Scene(synthesiserPane);
//        stage.setScene(scene);
//        stage.setTitle("Synthesiser - TrustSim");
//        stage.show();
//        break;
//      }
//      case "evaluator": {
//        stage = (Stage) evaluatorPane.getScene().getWindow();
//        evaluatorPane = FXMLLoader.load(getClass().getResource("/Evaluator.fxml"));
//        Scene scene = new Scene(evaluatorPane);
//        stage.setScene(scene);
//        stage.setTitle("Evaluator - TrustSim");
//        stage.show();
//        break;
//      }
//    }
//  }
//
//
//}
