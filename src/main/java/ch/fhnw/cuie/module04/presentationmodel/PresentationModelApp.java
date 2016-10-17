package ch.fhnw.cuie.module04.presentationmodel;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PresentationModelApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        PresentationModelExample pm        = new PresentationModelExample();
        Parent                   rootPanel = new AppUI(pm);

        Scene scene = new Scene(rootPanel);

        primaryStage.setTitle("PresentationModel is Cool");
        primaryStage.setScene(scene);

        primaryStage.setWidth(400);
        primaryStage.setResizable(false);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
