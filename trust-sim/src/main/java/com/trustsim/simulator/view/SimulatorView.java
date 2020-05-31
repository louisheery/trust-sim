package com.trustsim.simulator.view;

import com.trustsim.home.controller.HomeController;
import com.trustsim.home.model.HomeEngine;
import com.trustsim.simulator.controller.SimulatorController;
import com.trustsim.simulator.model.SimulatorEngine;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SimulatorView {

  private BorderPane view;
  private SimulatorController controller;
  private SimulatorEngine model;

  public SimulatorView(SimulatorController simulatorController, SimulatorEngine simulatorEngine) {

    controller = simulatorController;
    model = simulatorEngine;

    // TopMenu (File Menu)
    Menu fileMenu = new Menu("File");

    MenuItem fileMenuItemOne = new MenuItem("GoTo Scene1");

    MenuItem fileMenuItemTwo = new MenuItem("GoTo Scene2");


    fileMenu.getItems().addAll(fileMenuItemOne, fileMenuItemTwo);

    Menu editMenu = new Menu("Edit");

    Menu helpMenu = new Menu("Help");


    // Main Menu Bar
    MenuBar menuBar = new MenuBar();
    menuBar.getMenus().addAll(fileMenu, editMenu, helpMenu);


    // LeftMenu
    VBox leftMenu = new VBox();
    Label label1 = new Label("Scene1");
    label1.setPadding(new Insets(0,10,0,0));
    Button button3 = new Button("Show Alert");
    Button button4 = new Button("Close Program");
    leftMenu.getChildren().addAll(label1, button3, button4);
    leftMenu.setPadding(new Insets(10,10,10,10));

    view = new BorderPane();

    view.setTop(menuBar);
    view.setLeft(leftMenu);
  }

  public Parent returnParent() {
    return view;
  }


}