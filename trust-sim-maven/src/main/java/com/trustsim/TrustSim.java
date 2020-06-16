package com.trustsim;


import com.trustsim.simulator.storage.SQLiteDatabaseManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class TrustSim extends Application {

    public static int NUM_OF_TRUST_DIMS = 5;

    private final SQLiteDatabaseManager sqLiteDatabaseManager = SQLiteDatabaseManager.getInstance();

    public TrustSim() {

        // model = new SimulationEngine();
        // controller = new SimulationController(model);
        // view = new TrustSimGui(controller);

    }

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("Home.fxml")));
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setWidth(1000);
        primaryStage.setHeight(700);
        primaryStage.setTitle("Home - TrustSim");
        primaryStage.show();
        sqLiteDatabaseManager.addTable("agentSystems");
        sqLiteDatabaseManager.addTable("trustModels");

    }

}