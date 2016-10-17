package ch.fhnw.cuie.module04.buildings.singlebuilding;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.util.StringConverter;

import ch.fhnw.cuie.module04.buildings.BuildingPM;

public class StandardFormPane extends GridPane {

    private Label idLabel;
    private Label rankLabel;
    private Label buildingLabel;
    private Label cityLabel;
    private Label countryLabel;
    private Label height_mLabel;
    private Label height_ftLabel;
    private Label floorsLabel;
    private Label buildLabel;
    private Label architectLabel;
    private Label architectual_styleLabel;
    private Label costLabel;
    private Label materialLabel;
    private Label longitudeLabel;
    private Label latitudeLabel;
    private Label image_urlLabel;

    private TextField idField;
    private TextField rankField;
    private TextField buildingField;
    private TextField cityField;
    private TextField countryField;
    private TextField height_mField;
    private TextField height_ftField;
    private TextField floorsField;
    private TextField buildField;
    private TextField architectField;
    private TextField architectual_styleField;
    private TextField costField;
    private TextField materialField;
    private TextField longitudeField;
    private TextField latitudeField;
    private TextField image_urlField;

    private final BuildingPM building;

    public StandardFormPane(BuildingPM building) {
        this.building = building;
        initializeSelf();
        initializeParts();
        layoutParts();
        setupBindings();
    }

    private void initializeSelf() {
        String fonts = getClass().getResource("fonts.css").toExternalForm();
        getStylesheets().add(fonts);

        String stylesheet = getClass().getResource("style.css").toExternalForm();
        getStylesheets().add(stylesheet);

        setPadding(new Insets(10));
        setHgap(3);
        setVgap(10);

        ColumnConstraints cc = new ColumnConstraints();
        cc.setMinWidth(350);
        cc.setHgrow(Priority.ALWAYS);

        getColumnConstraints().addAll(new ColumnConstraints(), cc);
    }

    private void initializeParts() {
        idLabel = new Label("ID");
        rankLabel = new Label("Rank");
        buildingLabel = new Label("Building");
        cityLabel = new Label("City");
        countryLabel = new Label("Country");
        height_mLabel = new Label("Height (m)");
        height_ftLabel = new Label("Height (ft)");
        floorsLabel = new Label("Floors");
        buildLabel = new Label("Build");
        architectLabel = new Label("Architect");
        architectual_styleLabel = new Label("Architectual Style");
        costLabel = new Label("Cost");
        materialLabel = new Label("Material");
        longitudeLabel = new Label("Longitude");
        latitudeLabel = new Label("Latitude");
        image_urlLabel = new Label("Image URL");

        idField = new TextField();
        rankField = new TextField();
        buildingField = new TextField();
        cityField = new TextField();
        countryField = new TextField();
        height_mField = new TextField();
        height_ftField = new TextField();
        floorsField = new TextField();
        buildField = new TextField();
        architectField = new TextField();
        architectual_styleField = new TextField();
        costField = new TextField();
        materialField = new TextField();
        longitudeField = new TextField();
        latitudeField = new TextField();
        image_urlField = new TextField();
    }

    private void layoutParts() {


        addRow(0, idLabel, idField);
        addRow(1, rankLabel, rankField);
        addRow(2, buildingLabel, buildingField);
        addRow(3, cityLabel, cityField);
        addRow(4, countryLabel, countryField);
        addRow(5, height_mLabel, height_mField);
        addRow(6, height_ftLabel, height_ftField);
        addRow(7, floorsLabel, floorsField);
        addRow(8, buildLabel, buildField);
        addRow(9, architectLabel, architectField);
        addRow(10, architectual_styleLabel, architectual_styleField);
        addRow(11, costLabel, costField);
        addRow(12, materialLabel, materialField);
        addRow(13, longitudeLabel, longitudeField);
        addRow(14, latitudeLabel, latitudeField);
        addRow(15, image_urlLabel, image_urlField);
    }

    private void setupBindings() {
        StringConverter<Number> longStringConverter = new StringConverter<Number>() {
            @Override
            public String toString(Number number) {
                return number.toString();
            }

            @Override
            public Number fromString(String string) {
                try {
                    return Long.valueOf(string);
                }
                catch (NumberFormatException ex){
                    return 0L;
                }
            }
        };
        StringConverter<Number> integerStringConverter = new StringConverter<Number>() {
            @Override
            public String toString(Number n) {
                return n.toString();
            }

            @Override
            public Number fromString(String string) {
                try {
                    return Integer.valueOf(string);
                }
                catch (NumberFormatException ex){
                    return 0;
                }
            }
        };

        StringConverter<Number> doubleStringConverter = new StringConverter<Number>() {
            @Override
            public String toString(Number n) {
                return n.toString();
            }

            @Override
            public Number fromString(String string) {
                try {
                    return Double.valueOf(string);
                }
                catch (NumberFormatException ex){
                    return 0.0;
                }
            }
        };

        Bindings.bindBidirectional(idField.textProperty(), building.idProperty(), longStringConverter);
        Bindings.bindBidirectional(rankField.textProperty(), building.rankProperty(), integerStringConverter);
        buildingField.textProperty().bindBidirectional(building.buildingProperty());
        cityField.textProperty().bindBidirectional(building.cityProperty());
        countryField.textProperty().bindBidirectional(building.countryProperty());
        Bindings.bindBidirectional(height_mField.textProperty(), building.height_mProperty(), doubleStringConverter);
        Bindings.bindBidirectional(height_ftField.textProperty(), building.height_ftProperty(), doubleStringConverter);
        Bindings.bindBidirectional(floorsField.textProperty(), building.floorsProperty(), integerStringConverter);
        Bindings.bindBidirectional(buildField.textProperty(), building.buildProperty(), integerStringConverter);
        architectField.textProperty().bindBidirectional(building.architectProperty());
        architectual_styleField.textProperty().bindBidirectional(building.architectual_styleProperty());
        Bindings.bindBidirectional(costField.textProperty(), building.costProperty(), doubleStringConverter);
        materialField.textProperty().bindBidirectional(building.materialProperty());
        Bindings.bindBidirectional(longitudeField.textProperty(), building.longitudeProperty(), doubleStringConverter);
        Bindings.bindBidirectional(latitudeField.textProperty(), building.latitudeProperty(), doubleStringConverter);
        image_urlField.textProperty().bindBidirectional(building.image_urlProperty());
    }

}
