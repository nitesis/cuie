package ch.fhnw.cuie.module02.gradients;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Dieter Holz
 */
public class GradientPlayground extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		//Main panel
		//Group mainPanel = new Group();

		Parent gradientPanel = new GradientPane();
		//Parent slimDisplayPanel = new SlimValueDisplayPane();


		//mainPanel.getChildren().addAll(gradientPanel, slimDisplayPanel);

		Scene scene = new Scene(gradientPanel);
		primaryStage.setTitle("Gradient Playground");
		primaryStage.setScene(scene);

		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
