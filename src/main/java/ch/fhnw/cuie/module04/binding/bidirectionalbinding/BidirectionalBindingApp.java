package ch.fhnw.cuie.module04.binding.bidirectionalbinding;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BidirectionalBindingApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent rootPanel = new BidirectionalBindingExample();

		Scene scene = new Scene(rootPanel);

		primaryStage.setTitle("Bidirectional Bindings");
		primaryStage.setScene(scene);

		primaryStage.setResizable(false);

		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
