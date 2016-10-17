package ch.fhnw.cuie.module04.binding.bidirectionalbinding;

import javafx.geometry.Insets;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;

public class BidirectionalBindingExample extends VBox {
    private Slider s1;
    private Slider s2;

    public BidirectionalBindingExample() {
        initializeControls();
        layoutControls();
        addBindings();
    }

    private void initializeControls() {
        s1 = new Slider();
        s2 = new Slider();
    }

    private void layoutControls() {
        setPadding(new Insets(10));
        setSpacing(5);
        getChildren().addAll(s1, s2);
    }

    private void addBindings() {
        //bidirektionales Binding
        s1.valueProperty().bindBidirectional(s2.valueProperty());
    }

}
