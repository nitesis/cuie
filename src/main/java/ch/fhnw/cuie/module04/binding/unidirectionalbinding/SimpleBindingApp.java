package ch.fhnw.cuie.module04.binding.unidirectionalbinding;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SimpleBindingApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent rootPanel = new SimpleBindingExample();

		Scene scene = new Scene(rootPanel);

		primaryStage.setTitle("Unidirectional Binding");
		primaryStage.setScene(scene);

		primaryStage.setResizable(false);

		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
