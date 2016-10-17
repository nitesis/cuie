package ch.fhnw.cuie.module04.layout.borderpane;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

public class BorderPaneExample extends BorderPane {
	private Button   topButton;
	private Button   bottomButton;
	private Button   leftButton;
	private Button   rightButton;
	private TextArea textArea;

	public BorderPaneExample() {
		initializeControls();
		layoutControls();
	}

	private void initializeControls() {
		topButton    = new Button("top");
		bottomButton = new Button("bottom");
		leftButton   = new Button("left");
		rightButton  = new Button("right");
		textArea     = new TextArea();
	}

	private void layoutControls() {
		topButton.setMaxWidth(Double.MAX_VALUE);
		bottomButton.setMaxWidth(Double.MAX_VALUE);

		BorderPane.setMargin(topButton, new Insets(5));
		BorderPane.setMargin(bottomButton, new Insets(5));
		BorderPane.setMargin(leftButton, new Insets(0, 5, 0, 5));
		BorderPane.setMargin(rightButton, new Insets(0, 5, 0, 5));

		setTop(topButton);
		setLeft(leftButton);
		setRight(rightButton);
		setBottom(bottomButton);
		setCenter(textArea);
	}

}
