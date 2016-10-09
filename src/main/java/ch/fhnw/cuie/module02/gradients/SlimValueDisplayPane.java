package ch.fhnw.cuie.module02.gradients;

import javafx.application.Application;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;

/**
 * @author Dieter Holz
 */
public class SlimValueDisplayPane extends StackPane{
    private StackPane pane;
	private Circle circle;
    private Arc arc;

	public SlimValueDisplayPane() {
        initializeSelf();
		initializeParts();
		layoutParts();
	}

    private void initializeSelf() {
        String fonts = getClass().getResource("fonts.css").toExternalForm();
        getStylesheets().add(fonts);

        String stylesheet = getClass().getResource("style.css").toExternalForm();
        getStylesheets().add(stylesheet);
    }


	private void initializeParts() {
        pane = new StackPane();
        circle = new Circle(100.0f);
        circle.setCenterX(125.0f);
        circle.setCenterY(150.0f);
		circle.getStyleClass().addAll("playground", "slimValueDisplayCircle");

        arc = new Arc(125.0f, 150.0f, 100.0f, 100.0f, 90.0f, -314.1f);
        arc.getStyleClass().addAll("playground", "slimValueDisplayArc");
	}

	private void layoutParts() {
		getChildren().addAll(circle, arc);
	}
}
