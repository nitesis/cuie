package ch.fhnw.cuie.module02.gradients;

import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 * @author Dieter Holz
 */
public class SlimValueDisplayPane extends VBox {

	private Circle circle;

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
        circle = new Circle(100.0f);
        circle.setAccessibleText("1234");
		circle.getStyleClass().addAll("playground", "slimValueDisplayPlayground");
	}

	private void layoutParts() {
		setVgrow(circle, Priority.ALWAYS);
		getChildren().addAll(circle);
	}
}
