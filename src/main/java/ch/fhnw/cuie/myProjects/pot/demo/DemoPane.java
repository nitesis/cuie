package ch.fhnw.cuie.myProjects.pot.demo;

import ch.fhnw.cuie.myProjects.pot.BuildingPM;
import ch.fhnw.cuie.myProjects.pot.PotHeightControl;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.NumberStringConverter;

import java.util.Locale;

/**
 * @author Viviane Bendjus
 */
public class DemoPane extends BorderPane {

    private PotHeightControl customControl;
    private TextField   titleField;
    private TextField   heightField;
    private CheckBox circleAnimationRunningBox;
    private ColorPicker colorPicker;
    private CheckBox  isAnimated;

    public DemoPane() {
        initializeControls();
        layoutControls();
        addBindings();
    }

    private void initializeControls() {
        setPadding(new Insets(10));

        customControl = new PotHeightControl(BuildingPM.getBuildings());

        titleField = new TextField();
        titleField.setText("Burj Kalifa");

        heightField = new TextField();
        heightField.setText("830.0");

        isAnimated = new CheckBox("Height line animated");

        circleAnimationRunningBox = new CheckBox("Circle animation running");
        circleAnimationRunningBox.setSelected(true);

        colorPicker = new ColorPicker();
    }

    private void layoutControls() {
        setCenter(customControl);
        VBox box = new VBox(10, new Label("Control Properties"), titleField, heightField, isAnimated, circleAnimationRunningBox, colorPicker);
        box.setPadding(new Insets(10));
        box.setSpacing(10);
        setRight(box);
    }

    private void addBindings() {
        customControl.titleProperty().bindBidirectional(titleField.textProperty());

        Bindings.bindBidirectional(heightField.textProperty(), customControl.heightValueProperty(), new NumberStringConverter(new Locale("ch", "CH"), ".#"));

        isAnimated.selectedProperty().bindBidirectional(customControl.animatedProperty());

        customControl.circleAnimationIsRunningProperty().bindBidirectional(circleAnimationRunningBox.selectedProperty());

        colorPicker.valueProperty().bindBidirectional(customControl.baseColorProperty());
    }

}
