package ch.fhnw.cuie.module04.valuechanged;

import javafx.geometry.Insets;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ValueChangedExample extends VBox {
    private TextField textField;
    private Slider    slider;

    public ValueChangedExample() {
        initializeControls();
        layoutControls();
        addValueChangedListeners();
    }

    private void initializeControls() {
        textField = new TextField("0");
        slider = new Slider(0, 1000, 0);
    }

    private void layoutControls() {
        setPadding(new Insets(10));
        setSpacing(10);
        getChildren().addAll(textField, slider);
    }

    private void addValueChangedListeners() {
        //valueProperty()hält den Wert der Property
        //passt Value im Textfield entsprechd Slider an
        slider.valueProperty().addListener((observable, oldValue, newValue) -> textField.setText(String.valueOf(newValue)));
        //passt Slider entsprechd Eingabe Textfield an
        textField.textProperty().addListener((observable, oldValue, newValue) -> slider.setValue(Double.valueOf(newValue)));
        // Tu es oder tu es nicht. Es gibt kein Versuchen
    }

}
