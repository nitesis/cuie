package ch.fhnw.cuie.myProjects.pot;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Dieter Holz
 */
public class BuildingPM {
    private static final BuildingPM[] SOME_BUILDINGS = {
            new BuildingPM(1000, 1, "Burj Khalifa", "Dubai", "UAE", 830.0, 2723.0971336364746, 163, 2010, "Adrian Smith at SOM", "Neo-futurism", 1.5, "glass, steel, aluminum, reinforced concrete", 25.1971, 55.2741, "https://upload.wikimedia.org/wikipedia/en/thumb/9/93/Burj_Khalifa.jpg/240px-Burj_Khalifa.jpg"),
            new BuildingPM(1001, 2, "Shanghai Tower", "Shanghai", "China", 632.0, 2073.4908294677734, 128, 2015, "Jun Xia (Gensler)", "", 2.4, "", 31.2355, 121.501, "https://upload.wikimedia.org/wikipedia/commons/thumb/3/32/Shanghai_Tower_2015.jpg/480px-Shanghai_Tower_2015.jpg"),
            new BuildingPM(1002, 3, "Abraj Al-Bait Clock Tower", "Mecca", "Saudi Arabia", 601.0, 1971.7847919464111, 120, 2012, "Dar Al-Handasah Architects", "", 15.0, "steel/concrete composite construction", 21.4189, 39.8264, "https://upload.wikimedia.org/wikipedia/en/thumb/f/f4/Abraj-al-Bait-Towers.JPG/550px-Abraj-al-Bait-Towers.JPG"),
            new BuildingPM(1003, 4, "Ping An Finance Centre", "Shenzhen", "China", 599.0, 1965.2231121063232, 115, 2016, "", "", 0.0, "", 0.0, 0.0, "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a2/Ping%27an_IFC%2C_2014-12-21.jpg/135px-Ping%27an_IFC%2C_2014-12-21.jpg"),
            new BuildingPM(1004, 5, "Lotte World Tower", "Seoul", "South Korea", 555.0, 1820.8661556243896, 123, 2016, "", "", 0.0, "", 0.0, 0.0, "https://upload.wikimedia.org/wikipedia/commons/thumb/9/93/Lotte_World_Tower_2016_02_14.jpg/480px-Lotte_World_Tower_2016_02_14.jpg")
    };

    public static ObservableList<BuildingPM> getBuildings() {
        return FXCollections.observableArrayList(SOME_BUILDINGS);
    }

    private final LongProperty    id                 = new SimpleLongProperty();
    private final IntegerProperty rank               = new SimpleIntegerProperty();
    private final StringProperty  building           = new SimpleStringProperty();
    private final StringProperty  city               = new SimpleStringProperty();
    private final StringProperty  country            = new SimpleStringProperty();
    private final DoubleProperty  height_m           = new SimpleDoubleProperty();
    private final DoubleProperty  height_ft          = new SimpleDoubleProperty();
    private final IntegerProperty floors             = new SimpleIntegerProperty();
    private final IntegerProperty build              = new SimpleIntegerProperty();
    private final StringProperty  architect          = new SimpleStringProperty();
    private final StringProperty  architectual_style = new SimpleStringProperty();
    private final DoubleProperty  cost               = new SimpleDoubleProperty();
    private final StringProperty  material           = new SimpleStringProperty();
    private final DoubleProperty  longitude          = new SimpleDoubleProperty();
    private final DoubleProperty  latitude           = new SimpleDoubleProperty();
    private final StringProperty  image_url          = new SimpleStringProperty();

    public BuildingPM(long id, int rank, String building, String city, String country, double height_m, double height_ft,
                      int floors, int build, String architect, String architectual_style, double cost, String material, double longitude, double latitude, String image_url) {
        addEventListeners();
        setId(id);
        setRank(rank);
        setBuilding(building);
        setCity(city);
        setCountry(country);
        setHeight_m(height_m);
        setHeight_ft(height_ft);
        setFloors(floors);
        setBuild(build);
        setArchitect(architect);
        setArchitectual_style(architectual_style);
        setCost(cost);
        setMaterial(material);
        setLongitude(longitude);
        setLatitude(latitude);
        setImage_url(image_url);
    }

    private void addEventListeners(){

    }

    public long getId() {
        return id.get();
    }

    public LongProperty idProperty() {
        return id;
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public int getRank() {
        return rank.get();
    }

    public IntegerProperty rankProperty() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank.set(rank);
    }

    public String getBuilding() {
        return building.get();
    }

    public StringProperty buildingProperty() {
        return building;
    }

    public void setBuilding(String building) {
        this.building.set(building);
    }

    public String getCity() {
        return city.get();
    }

    public StringProperty cityProperty() {
        return city;
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public String getCountry() {
        return country.get();
    }

    public StringProperty countryProperty() {
        return country;
    }

    public void setCountry(String country) {
        this.country.set(country);
    }

    public double getHeight_m() {
        return height_m.get();
    }

    public DoubleProperty height_mProperty() {
        return height_m;
    }

    public void setHeight_m(double height_m) {
        this.height_m.set(height_m);
    }

    public double getHeight_ft() {
        return height_ft.get();
    }

    public DoubleProperty height_ftProperty() {
        return height_ft;
    }

    public void setHeight_ft(double height_ft) {
        this.height_ft.set(height_ft);
    }

    public int getFloors() {
        return floors.get();
    }

    public IntegerProperty floorsProperty() {
        return floors;
    }

    public void setFloors(int floors) {
        this.floors.set(floors);
    }

    public int getBuild() {
        return build.get();
    }

    public IntegerProperty buildProperty() {
        return build;
    }

    public void setBuild(int build) {
        this.build.set(build);
    }

    public String getArchitect() {
        return architect.get();
    }

    public StringProperty architectProperty() {
        return architect;
    }

    public void setArchitect(String architect) {
        this.architect.set(architect);
    }

    public String getArchitectual_style() {
        return architectual_style.get();
    }

    public StringProperty architectual_styleProperty() {
        return architectual_style;
    }

    public void setArchitectual_style(String architectual_style) {
        this.architectual_style.set(architectual_style);
    }

    public double getCost() {
        return cost.get();
    }

    public DoubleProperty costProperty() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost.set(cost);
    }

    public String getMaterial() {
        return material.get();
    }

    public StringProperty materialProperty() {
        return material;
    }

    public void setMaterial(String material) {
        this.material.set(material);
    }

    public double getLongitude() {
        return longitude.get();
    }

    public DoubleProperty longitudeProperty() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude.set(longitude);
    }

    public double getLatitude() {
        return latitude.get();
    }

    public DoubleProperty latitudeProperty() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude.set(latitude);
    }

    public String getImage_url() {
        return image_url.get();
    }

    public StringProperty image_urlProperty() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url.set(image_url);
    }
}
