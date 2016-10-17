package ch.fhnw.cuie.module04.buildings.table;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;

import ch.fhnw.cuie.module04.buildings.BuildingPM;


public class TablePane extends StackPane {
    private TableView<BuildingPM> table;

    public TablePane() {
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
        table = new TableView<>(BuildingPM.getBuildings());

        TableColumn<BuildingPM, Number> rankCol = new TableColumn<>("Rank");
        rankCol.setCellValueFactory(cell -> cell.getValue().rankProperty());

        TableColumn<BuildingPM, String> buildingCol = new TableColumn<>("Building");
        buildingCol.setCellValueFactory(cell -> cell.getValue().buildingProperty());

        TableColumn<BuildingPM, String> cityCol = new TableColumn<>("City");
        cityCol.setCellValueFactory(cell -> cell.getValue().cityProperty());

        TableColumn<BuildingPM, String> countryCol = new TableColumn<>("Country");
        countryCol.setCellValueFactory(cell -> cell.getValue().countryProperty());

        TableColumn<BuildingPM, Number> height_mCol = new TableColumn<>("Height (m)");
        height_mCol.setCellValueFactory(cell -> cell.getValue().height_mProperty());



        table.getColumns().addAll(rankCol, buildingCol, cityCol, countryCol, height_mCol);
    }

    private void layoutParts() {
        getChildren().add(table);
    }


}
