package ch.fhnw.cuie.module04.valuechanged;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ValueChangedApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent rootPanel = new ValueChangedExample();

		Scene scene = new Scene(rootPanel);

		primaryStage.setTitle("Listen to Value Changes");
		primaryStage.setScene(scene);

		String stylesheet = getClass().getResource("style.css").toExternalForm();
		scene.getStylesheets().add(stylesheet);

		primaryStage.setResizable(false);

		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
