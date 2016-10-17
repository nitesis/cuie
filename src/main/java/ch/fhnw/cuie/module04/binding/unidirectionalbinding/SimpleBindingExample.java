package ch.fhnw.cuie.module04.binding.unidirectionalbinding;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;

public class SimpleBindingExample extends VBox {
	private Label  label;
	private Slider slider;

	public SimpleBindingExample() {
		initializeControls();
		layoutControls();
		addBindings();
	}

	private void initializeControls() {
		label = new Label();
		slider = new Slider(0, 100, 50);
	}

	private void layoutControls() {
		setPadding(new Insets(10));
		setSpacing(10);

		getChildren().addAll(label, slider);
	}

	private void addBindings() {
		//Im Label soll immer Wert von Slider angezeigt werden
		//Voraussetzung für Binding sind immer Poperties!!!
		label.textProperty().bind(slider.valueProperty().asString());
	}


}
