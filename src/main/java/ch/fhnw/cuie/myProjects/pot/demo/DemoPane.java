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
    private Slider      pulseSlider;
    private ColorPicker colorPicker;
    private CheckBox  isAnimated;
    private ListView buildingsList;

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

        pulseSlider = new Slider(0.5, 2.0, 1.0);
        pulseSlider.setShowTickLabels(true);
        pulseSlider.setShowTickMarks(true);

        colorPicker = new ColorPicker();

        buildingsList= new ListView();
        for (int i=0; i < customControl.getBuildings().size(); i++)
        {
            buildingsList.getItems().add(customControl.getBuildings().get(i).getBuilding());
        }
        buildingsList.setPrefHeight(customControl.getBuildings().size() * 24);
    }

    private void layoutControls() {
        setCenter(customControl);
        VBox box = new VBox(10, new Label("Control Properties"), titleField, heightField, isAnimated, circleAnimationRunningBox, pulseSlider, colorPicker, new Label("choose building"), buildingsList);
        box.setPadding(new Insets(10));
        box.setSpacing(10);
        setRight(box);
    }

    private void addBindings() {

        StringConverter converter = new DoubleStringConverter();

        customControl.titleProperty().bindBidirectional(titleField.textProperty());

        Bindings.bindBidirectional(heightField.textProperty(), customControl.heightValueProperty(), new NumberStringConverter(new Locale("ch", "CH"), ".##"));
        //heightField.textProperty().bind(customControl.heightValueProperty().asString("%.2f"));

        //customControl.heightValueProperty().bindBidirectional(heightField.textProperty());
        isAnimated.selectedProperty().bindBidirectional(customControl.animatedProperty());

        customControl.circleAnimationIsRunningProperty().bindBidirectional(circleAnimationRunningBox.selectedProperty());
        customControl.pulseProperty().bind(Bindings.createObjectBinding(() -> Duration.seconds(pulseSlider.getValue()), pulseSlider.valueProperty()));

        colorPicker.valueProperty().bindBidirectional(customControl.baseColorProperty());
    }

}
