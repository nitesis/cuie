package ch.fhnw.cuie.module04.buildings.singlebuilding;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;

import ch.fhnw.cuie.module04.buildings.BuildingPM;
/**
 * @author Dieter Holz
 */
public class CustomControlPane extends VBox {
    private final BuildingPM building;

    private Label height_mLabel;
    private Label buildingLabel;
    private Slider height_mSlider;

    public CustomControlPane(BuildingPM building) {
        this.building = building;
        initializeSelf();
        initializeParts();
        layoutParts();
        addValueChangedListeners();
        setupBindings();
    }

    private void addValueChangedListeners() {
        height_mSlider.valueProperty().addListener((observable, oldValue, newValue) -> building.setHeight_m(newValue.doubleValue()));
    }

    private void initializeSelf() {
        setPadding(new Insets(10));
    }

    private void initializeParts() {
        buildingLabel = new Label();
        buildingLabel.setStyle("-fx-font-size: 32;");

        height_mLabel = new Label();
        height_mLabel.setStyle("-fx-font-size: 32;");

        height_mSlider = new Slider(0, 1000, 500);
    }

    private void layoutParts() {
        getChildren().addAll(buildingLabel, height_mLabel, height_mSlider);
    }

    private void setupBindings() {
        buildingLabel.textProperty().bind(building.buildingProperty());
        height_mLabel.textProperty().bind(building.height_mProperty().asString());
        height_mSlider.valueProperty().bind(building.height_mProperty());

    }
}
