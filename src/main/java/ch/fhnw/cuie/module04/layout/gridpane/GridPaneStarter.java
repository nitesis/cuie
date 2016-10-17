package ch.fhnw.cuie.module04.layout.gridpane;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GridPaneStarter extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent rootPanel = new GridPaneExample();

		Scene scene = new Scene(rootPanel);

		primaryStage.setTitle("GridPane Example");
		primaryStage.setScene(scene);

		primaryStage.centerOnScreen();
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
