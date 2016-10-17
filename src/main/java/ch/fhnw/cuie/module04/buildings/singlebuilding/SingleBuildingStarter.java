package ch.fhnw.cuie.module04.buildings.singlebuilding;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import ch.fhnw.cuie.module04.buildings.BuildingPM;

public class SingleBuildingStarter extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		//zeigt das erste Gebäude an
		Parent rootPanel = new SingleBuildingView(BuildingPM.getBuildings().get(0));

		Scene scene = new Scene(rootPanel);

		primaryStage.setTitle("Buildings");
		primaryStage.setScene(scene);

		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
