package ch.fhnw.cuie.module04.layout.hbox;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class HBoxExample extends HBox {
	private Button button1;
	private Button button2;
	private Button button3;

	public HBoxExample() {
		initializeControls();
		layoutControls();
	}

	private void initializeControls() {
		button1 = new Button("first");
		button2 = new Button("second");
		button3 = new Button("third");
	}

	private void layoutControls() {
		setPadding(new Insets(10));
		setSpacing(5);

		button2.setMaxWidth(Double.MAX_VALUE);

		// Aendern der Default Resizing-Strategie

		//setHgrow(button2, Priority.ALWAYS);
		//button2.setMaxHeight(Double.MAX_VALUE);

		getChildren().addAll(button1, button2, button3);
	}
}
