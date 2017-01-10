package ch.fhnw.cuie.myProjects.pot;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javafx.animation.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.css.CssMetaData;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleablePropertyFactory;
import javafx.geometry.*;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Popup;
import javafx.util.Duration;
import javafx.util.converter.NumberStringConverter;

/**
 * @author Viviane Bendjus
 */
public class PotHeightControl extends Region {
    // needed for StyleableProperties
    private static final StyleablePropertyFactory<PotHeightControl> FACTORY = new StyleablePropertyFactory<>(Region.getClassCssMetaData());

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return FACTORY.getCssMetaData();
    }

    private static final String FONTS_CSS = "fonts.css";
    private static final String STYLE_CSS = "style.css";

    private static final double ARTBOARD_WIDTH  = 400;
    private static final double ARTBOARD_HEIGHT = 400;

    private static final double ASPECT_RATIO = ARTBOARD_WIDTH / ARTBOARD_HEIGHT;

    private static final double MINIMUM_WIDTH  = 25;
    private static final double MINIMUM_HEIGHT = MINIMUM_WIDTH / ASPECT_RATIO;

    private static final double MAXIMUM_WIDTH = 800;

    private static final double HEIGHT_FACTOR = 0.32;

    // all parts
    private HBox titleLabelBox;
    private TextField titleLabel;
    private Line heightLine;
    private Circle heightCircleSmall;
    private Circle heightCircleBig;
    private Arc baseArc;
    private Line baseLine;
    private HBox heightLabelBox;
    private TextField heightLabel;

    // parts for other buildings
    private Label labelB1B2;
    private Label labelB3B4;
    private HBox otherBuildingBox;
    private Text otherBuildingLabel;
    private Line heightLineB1;
    private Circle heightCircleB1;
    private Line heightLineB2;
    private Circle heightCircleB2;
    private Line heightLineB3;
    private Circle heightCircleB3;
    private Line heightLineB4;
    private Circle heightCircleB4;

    // data structure for other buildings to compare
    private List<BuildingPM> buildings;

    public List<BuildingPM> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<BuildingPM> buildings) {
        this.buildings = buildings;
    }

    // parts for pop up menu
    private ListView buildingsListB1;
    private ListView buildingsListB2;
    private ListView buildingsListB3;
    private ListView buildingsListB4;
    private Popup popupB1;
    private int indexB1;
    private Popup popupB2;
    private int indexB2;
    private Popup popupB3;
    private int indexB3;
    private Popup popupB4;
    private int indexB4;

    // all properties
    private final StringProperty title = new SimpleStringProperty();
    private final DoubleProperty heightValue = new SimpleDoubleProperty();

    private final BooleanProperty circleAnimationIsRunning = new SimpleBooleanProperty(true);
    private final ObjectProperty<Duration> pulse          = new SimpleObjectProperty<>(Duration.seconds(1.0));

    private final BooleanProperty animated      = new SimpleBooleanProperty(false);
    private final DoubleProperty animatedHeightValue = new SimpleDoubleProperty();

    //CSS stylable properties
    private static final CssMetaData<PotHeightControl, Color> BASE_COLOR_META_DATA = FACTORY.createColorCssMetaData("-base-color", s -> s.baseColor);

    private final StyleableObjectProperty<Color> baseColor = new SimpleStyleableObjectProperty<Color>(BASE_COLOR_META_DATA, this, "baseColor") {
        @Override
        protected void invalidated() {
            setStyle(BASE_COLOR_META_DATA.getProperty() + ": " + (getBaseColor()).toString().replace("0x", "#") + ";");
            applyCss();
        }
    };

    // all animations
    private final Timeline timeline = new Timeline();
    private ScaleTransition scaleTransition;
    private FillTransition fillTransition;
    private ParallelTransition circleTransition;

    // all parts need to be children of the drawingPane
    private Pane drawingPane;

    private final AnimationTimer timer = new AnimationTimer() {
        private long lastTimerCall;

        @Override
        public void handle(long now) {
            if (now > lastTimerCall + (getPulse().toMillis() * 1_000_000L)) {
                performPeriodicTask();
                lastTimerCall = now;
            }
        }
    };

    public PotHeightControl(List<BuildingPM> buildings) {
        this.buildings = buildings;
        initializeSelf();
        initializeParts();
        layoutParts();
        initializeAnimations();
        addEventHandlers();
        addValueChangedListeners();
        setupBindings();
    }

    private void initializeSelf() {
        addStyleSheets(this);
        getStyleClass().add(getStyleClassName());
    }


    private void initializeParts() {

        titleLabelBox = new HBox();
        titleLabelBox.setStyle("-fx-padding: 10");
        titleLabelBox.setAlignment(Pos.CENTER);
        titleLabelBox.setPrefSize(ARTBOARD_WIDTH, 25);
        titleLabel = new TextField("Burj Kalifa");
        //titleLabelBox.getChildren().add(titleLabel);

        titleLabel.getStyleClass().add("titleLabel");

        heightCircleSmall = new Circle(200, ARTBOARD_HEIGHT - buildings.get(0).getHeight_m() * HEIGHT_FACTOR, 2);
        heightCircleSmall.getStyleClass().add("heightCircleSmall");

        heightCircleBig = new Circle(200, ARTBOARD_HEIGHT - buildings.get(0).getHeight_m() * HEIGHT_FACTOR, 17);
        heightCircleBig.getStyleClass().add("heightCircleBig");

        heightLine = new Line(200, 400, 200, ARTBOARD_HEIGHT - buildings.get(0).getHeight_m() * HEIGHT_FACTOR);
        heightLine.getStyleClass().add("heightLine");
        heightLine.setStrokeLineCap(StrokeLineCap.ROUND);

        baseArc = new Arc(200, 400, 50, 50, 0, 180);
        baseArc.getStyleClass().add("heightCircleBig");

        baseLine = new Line(150, 400, 250, 400);
        baseLine.getStyleClass().add("heightLine");

        heightLabelBox = new HBox();
        heightLabelBox.setTranslateY(350);
        heightLabelBox.getStyleClass().add("heightLabelBox");
        heightLabelBox.setAlignment(Pos.CENTER);
        heightLabelBox.setPrefSize(ARTBOARD_WIDTH, 25);


        heightLabel = new TextField("830.0");
        heightLabel.getStyleClass().add("heightLabel");
        heightLabelBox.getChildren().add(heightLabel);

        // initialize parts for other buildings

        heightCircleB1 = new Circle(160, ARTBOARD_HEIGHT - buildings.get(1).getHeight_m() * HEIGHT_FACTOR, 7);
        heightCircleB1.getStyleClass().add("heightCircleSmall");

        heightLineB1 = new Line(160, 400, 160, ARTBOARD_HEIGHT - buildings.get(1).getHeight_m() * HEIGHT_FACTOR);
        heightLineB1.getStyleClass().add("heightLine");
        heightLineB1.setStrokeLineCap(StrokeLineCap.ROUND);

        heightCircleB2 = new Circle(180, ARTBOARD_HEIGHT - buildings.get(2).getHeight_m() * HEIGHT_FACTOR, 7);
        heightCircleB2.getStyleClass().add("heightCircleSmall");

        heightLineB2 = new Line(180, 400, 180, ARTBOARD_HEIGHT - buildings.get(2).getHeight_m() * HEIGHT_FACTOR);
        heightLineB2.getStyleClass().add("heightLine");
        heightLineB2.setStrokeLineCap(StrokeLineCap.ROUND);

        heightCircleB3 = new Circle(220, ARTBOARD_HEIGHT - buildings.get(3).getHeight_m() * HEIGHT_FACTOR, 7);
        heightCircleB3.getStyleClass().add("heightCircleSmall");

        heightLineB3 = new Line(220, 400, 220, ARTBOARD_HEIGHT - buildings.get(3).getHeight_m() * HEIGHT_FACTOR);
        heightLineB3.getStyleClass().add("heightLine");
        heightLineB3.setStrokeLineCap(StrokeLineCap.ROUND);

        heightCircleB4 = new Circle(240, ARTBOARD_HEIGHT - buildings.get(4).getHeight_m() * HEIGHT_FACTOR, 7);
        heightCircleB4.getStyleClass().add("heightCircleSmall");

        heightLineB4 = new Line(240, 400, 240, ARTBOARD_HEIGHT - buildings.get(4).getHeight_m() * HEIGHT_FACTOR);
        heightLineB4.getStyleClass().add("heightLine");
        heightLineB4.setStrokeLineCap(StrokeLineCap.ROUND);

        labelB1B2 = new Label("");
        labelB1B2.setTranslateX(80);
        labelB1B2.setTranslateY(setPositionForSmallCircleLabels(heightLineB1, heightLineB2));
        labelB1B2.setWrapText(true);
        labelB1B2.setMaxSize(100, 70);
        labelB1B2.getStyleClass().add("smallLabel");

        labelB3B4 = new Label("");
        labelB3B4.setTranslateX(230);
        labelB3B4.setTranslateY(setPositionForSmallCircleLabels(heightLineB3, heightLineB4));
        labelB3B4.setWrapText(true);
        labelB3B4.setMaxSize(100, 70);
        labelB3B4.getStyleClass().add("smallLabel");

        buildingsListB1 = new ListView();
        for (int i = 0; i < buildings.size(); i++)
        {
            buildingsListB1.getItems().add(buildings.get(i).getBuilding());
        }
        buildingsListB1.setPrefHeight(buildings.size() * 24);

        popupB1 = new Popup();
        popupB1.getContent().addAll(buildingsListB1);

        buildingsListB2 = new ListView();
        for (int i = 0; i < buildings.size(); i++)
        {
            buildingsListB2.getItems().add(buildings.get(i).getBuilding());
        }
        buildingsListB2.setPrefHeight(buildings.size() * 24);

        popupB2 = new Popup();
        popupB2.getContent().addAll(buildingsListB2);

        buildingsListB3 = new ListView();
        for (int i = 0; i < buildings.size(); i++)
        {
            buildingsListB3.getItems().add(buildings.get(i).getBuilding());
        }
        buildingsListB3.setPrefHeight(buildings.size() * 24);

        popupB3 = new Popup();
        popupB3.getContent().addAll(buildingsListB3);

        buildingsListB4 = new ListView();
        for (int i = 0; i < buildings.size(); i++)
        {
            buildingsListB4.getItems().add(buildings.get(i).getBuilding());
        }
        buildingsListB4.setPrefHeight(buildings.size() * 24);

        popupB4 = new Popup();
        popupB4.getContent().addAll(buildingsListB4);


        // always needed
        drawingPane = new Pane();
        drawingPane.setMaxSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
        drawingPane.setMinSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
        drawingPane.setPrefSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
        drawingPane.getStyleClass().add("drawingPane");
    }

    private void layoutParts() {
        // add all your parts here
        drawingPane.getChildren().addAll(titleLabel, heightLine, heightLineB1, heightCircleB1, heightLineB2, heightCircleB2,
                heightLineB3, heightCircleB3, heightLineB4,
                heightCircleB4, baseArc, baseLine, heightCircleBig, heightCircleSmall,
                heightLabelBox, labelB1B2, labelB3B4);

        getChildren().add(drawingPane);
    }

    private void initializeAnimations() {

    }

    private void addEventHandlers() {

        titleLabel.setOnAction(event -> {
            drawingPane.requestFocus();
        });

        heightLabel.setOnMouseClicked(event -> {
            setAnimated(true);
        });

        heightLabel.setOnAction(event -> {
                setAnimated(false);
                drawingPane.requestFocus();
        });

        /*heightCircleSmall.setOnMouseClicked(event -> {
            setAnimated(false);
            //setCircleAnimationIsRunning(false);
        });*/

        heightCircleSmall.setOnMouseReleased(event -> {
            setCircleAnimationIsRunning(true);
        });

        heightCircleSmall.setOnMouseDragged(event -> {
            if(((drawingPane.getMaxHeight() - event.getY()) / HEIGHT_FACTOR) > 1000.0)
                setHeightValue(1000.0);
            else {
                if (((drawingPane.getMaxHeight() - event.getY()) / HEIGHT_FACTOR) < 0.0)
                    setHeightValue(0.0);
                else
                    setHeightValue((drawingPane.getMaxHeight() - event.getY()) / HEIGHT_FACTOR);
            }
        });

        heightCircleSmall.setOnMousePressed(event -> {
            setAnimated(false);
            if(getCircleAnimationIsRunning()) {
                if (scaleTransition == null) {
                    scaleTransition = new ScaleTransition(Duration.seconds(0.2), heightCircleBig);
                    scaleTransition.setFromX(1.0);
                    scaleTransition.setFromY(1.0);
                    scaleTransition.setByX(0.4);
                    scaleTransition.setByY(0.4);
                    scaleTransition.setCycleCount(2);
                    scaleTransition.setInterpolator(Interpolator.EASE_OUT);
                    scaleTransition.setAutoReverse(true);
                }

                if (fillTransition == null) {
                    fillTransition = new FillTransition(Duration.seconds(1.2), heightCircleBig, getBaseColor(), Color.PINK);
                    fillTransition.setAutoReverse(true);
                }

                if (!fillTransition.getStatus().equals(Animation.Status.RUNNING)) {
                    circleTransition = new ParallelTransition(scaleTransition, fillTransition);
                    circleTransition.setInterpolator(Interpolator.EASE_BOTH);
                    circleTransition.play();
                }
            }
        });

        heightCircleSmall.setOnMouseReleased(event -> {
            heightCircleBig.setFill(Color.WHITE);
        });

        // B1 OnMouseEntered/Exited
        heightCircleB1.setOnMouseEntered(event -> {
            heightCircleB1.setRadius(10.0);
            heightCircleB1.setFill(Color.LIGHTPINK);
            labelB1B2.setText(buildings.get(1).getBuilding() + ": " + buildings.get(1).getHeight_m() + " m");
        });

        heightCircleB1.setOnMouseExited(event -> {
            heightCircleB1.setRadius(7.0);
            heightCircleB1.setFill(Color.WHITE);
            labelB1B2.setText("");
        });

        // B2 OnMouseEntered/Exited
        heightCircleB2.setOnMouseEntered(event -> {
            heightCircleB2.setRadius(10.0);
            heightCircleB2.setFill(Color.LIGHTPINK);
            labelB1B2.setText(buildings.get(2).getBuilding() + ": " + buildings.get(2).getHeight_m() + " m");
        });

        heightCircleB2.setOnMouseExited(event -> {
            heightCircleB2.setRadius(7.0);
            heightCircleB2.setFill(Color.WHITE);
            labelB1B2.setText("");
        });

        // B3 OnMouseEntered/Exited
        heightCircleB3.setOnMouseEntered(event -> {
            heightCircleB3.setRadius(10.0);
            heightCircleB3.setFill(Color.LIGHTPINK);
            labelB3B4.setText(buildings.get(3).getBuilding() + ": " + buildings.get(3).getHeight_m() + " m");
        });

        heightCircleB3.setOnMouseExited(event -> {
            heightCircleB3.setRadius(7.0);
            heightCircleB3.setFill(Color.WHITE);
            labelB3B4.setText("");
        });

        // B4 OnMouseEntered/Exited
        heightCircleB4.setOnMouseEntered(event -> {
            heightCircleB4.setRadius(10.0);
            heightCircleB4.setFill(Color.LIGHTPINK);
            labelB3B4.setText(buildings.get(4).getBuilding() + ": " + buildings.get(4).getHeight_m() + " m");
        });

        heightCircleB4.setOnMouseExited(event -> {
            heightCircleB4.setRadius(7.0);
            heightCircleB4.setFill(Color.WHITE);
            labelB3B4.setText("");
        });

        // events for pop up menu1
        heightCircleB1.setOnMouseClicked(event -> {
            if (popupB1.isShowing()) {
                popupB1.hide();
            } else {
                popupB1.show(heightCircleB1.getScene().getWindow());
            }
        });

        popupB1.setOnShown(event -> {
            Point2D location = heightCircleB1.localToScreen(159, ARTBOARD_HEIGHT - buildings.get(indexB1).getHeight_m() * HEIGHT_FACTOR);
            popupB1.setX(location.getX());
            popupB1.setY(location.getY());
        });

        buildingsListB1.setOnMouseClicked(event ->
        {
            popupB1.hide();
            indexB1 = buildingsListB1.getItems().indexOf(buildingsListB1.getSelectionModel().getSelectedItem().toString());
            heightCircleB1.setCenterY(ARTBOARD_HEIGHT - buildings.get(indexB1).getHeight_m() * HEIGHT_FACTOR);
            heightLineB1.setEndY(ARTBOARD_HEIGHT - buildings.get(indexB1).getHeight_m() * HEIGHT_FACTOR);
        });

        // events for pop up menu2
        heightCircleB2.setOnMouseClicked(event -> {
            if (popupB2.isShowing()) {
                popupB2.hide();
            } else {
                popupB2.show(heightCircleB2.getScene().getWindow());
            }
        });

        popupB2.setOnShown(event -> {
            Point2D location = heightCircleB2.localToScreen(159, ARTBOARD_HEIGHT - buildings.get(indexB2).getHeight_m() * HEIGHT_FACTOR);
            popupB2.setX(location.getX());
            popupB2.setY(location.getY());
        });

        buildingsListB2.setOnMouseClicked(event ->
        {
            popupB2.hide();
            indexB2 = buildingsListB2.getItems().indexOf(buildingsListB2.getSelectionModel().getSelectedItem().toString());
            heightCircleB2.setCenterY(ARTBOARD_HEIGHT - buildings.get(indexB2).getHeight_m() * HEIGHT_FACTOR);
            heightLineB2.setEndY(ARTBOARD_HEIGHT - buildings.get(indexB2).getHeight_m() * HEIGHT_FACTOR);
        });

        // events for pop up menu3
        heightCircleB3.setOnMouseClicked(event -> {
            if (popupB3.isShowing()) {
                popupB3.hide();
            } else {
                popupB3.show(heightCircleB3.getScene().getWindow());
            }
        });

        popupB3.setOnShown(event -> {
            Point2D location = heightCircleB3.localToScreen(159, ARTBOARD_HEIGHT - buildings.get(indexB3).getHeight_m() * HEIGHT_FACTOR);
            popupB3.setX(location.getX());
            popupB3.setY(location.getY());
        });

        buildingsListB3.setOnMouseClicked(event ->
        {
            popupB3.hide();
            indexB3 = buildingsListB3.getItems().indexOf(buildingsListB3.getSelectionModel().getSelectedItem().toString());
            heightCircleB3.setCenterY(ARTBOARD_HEIGHT - buildings.get(indexB3).getHeight_m() * HEIGHT_FACTOR);
            heightLineB3.setEndY(ARTBOARD_HEIGHT - buildings.get(indexB3).getHeight_m() * HEIGHT_FACTOR);
        });

        // events for pop up menu4
        heightCircleB4.setOnMouseClicked(event -> {
            if (popupB4.isShowing()) {
                popupB4.hide();
            } else {
                popupB4.show(heightCircleB4.getScene().getWindow());
            }
        });

        popupB4.setOnShown(event -> {
            Point2D location = heightCircleB4.localToScreen(159, ARTBOARD_HEIGHT - buildings.get(indexB4).getHeight_m() * HEIGHT_FACTOR);
            popupB4.setX(location.getX());
            popupB4.setY(location.getY());
        });

        buildingsListB4.setOnMouseClicked(event ->
        {
            popupB4.hide();
            indexB4 = buildingsListB4.getItems().indexOf(buildingsListB4.getSelectionModel().getSelectedItem().toString());
            heightCircleB4.setCenterY(ARTBOARD_HEIGHT - buildings.get(indexB4).getHeight_m() * HEIGHT_FACTOR);
            heightLineB4.setEndY(ARTBOARD_HEIGHT - buildings.get(indexB4).getHeight_m() * HEIGHT_FACTOR);
        });
    }

    private void addValueChangedListeners() {
        /*heightValue.addListener((observable, oldValue, newValue) -> {
            double lineLength =  heightLine.getStartY() - (newValue.doubleValue() * 0.25);

            heightLine.setEndY(lineLength + 60.0);


            double centerX = heightLine.getEndX();
            double centerY = heightLine.getEndY();
            Point2D circleCenter = new Point2D(centerX, centerY);
            heightCircleBig.setCenterX(circleCenter.getX());
            heightCircleBig.setCenterY(circleCenter.getY());
            heightCircleSmall.setCenterX(circleCenter.getX());
            heightCircleSmall.setCenterY(circleCenter.getY());
        });
*/
        //Hier wird Animation festgelegt
        heightValueProperty().addListener((observable, oldValue, newValue) -> {
            if(isAnimated()) {
                timeline.stop();
                timeline.getKeyFrames().setAll(new KeyFrame(Duration.millis(500),
                        new KeyValue(animatedHeightValue, newValue)));
                timeline.play();
            }
            else {
                setAnimatedHeightValue(newValue.doubleValue());
            }

        });
        //Hier wird Wertänderung festgelegt
        animatedHeightValueProperty().addListener((observable, oldValue, newValue) -> {
            double lineLength =  heightLine.getStartY() - (newValue.doubleValue() * HEIGHT_FACTOR);

            heightLine.setEndY(lineLength);

            double centerX = heightLine.getEndX();
            double centerY = heightLine.getEndY();
            Point2D circleCenter = new Point2D(centerX, centerY);
            heightCircleBig.setCenterX(circleCenter.getX());
            heightCircleBig.setCenterY(circleCenter.getY());
            heightCircleSmall.setCenterX(circleCenter.getX());
            heightCircleSmall.setCenterY(circleCenter.getY());
        });


        // if you need the timer
        circleAnimationIsRunning.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                timer.start();
            } else {
                timer.stop();
            }
        });

        // always needed
        widthProperty().addListener((observable, oldValue, newValue) -> resize());
        heightProperty().addListener((observable, oldValue, newValue) -> resize());
    }

    private void setupBindings() {
        titleLabel.textProperty().bindBidirectional(titleProperty());
        //heightLabel.textProperty().bind(heightValueProperty().asString());
        Bindings.bindBidirectional(heightLabel.textProperty(), this.heightValueProperty(),
                new NumberStringConverter(new Locale("ch", "CH"), ".##"));

    }

    private void performPeriodicTask() {
    }


    // some useful helper-methods

    private double setPositionForSmallCircleLabels(Line line1, Line line2) {
        double endYLine1 = line1.getEndY();
        double endYLine2 = line2.getEndY();

        if (endYLine1 > endYLine2) {
            return (endYLine2 - 50);
        }
        else {
            return (endYLine1 - 50);
        }
    }


    private Text createCenteredText(String styleClass) {
        return createCenteredText(ARTBOARD_WIDTH * 0.5, ARTBOARD_HEIGHT * 0.5, styleClass);
    }

    private Text createCenteredText(double cx, double cy, String styleClass) {
        Text text = new Text();
        text.getStyleClass().add(styleClass);
        text.setTextOrigin(VPos.CENTER);
        text.setTextAlignment(TextAlignment.CENTER);
        double width = cx > ARTBOARD_WIDTH * 0.5 ? ((ARTBOARD_WIDTH - cx) * 2.0) : cx * 2.0;
        text.setWrappingWidth(width);
        text.setBoundsType(TextBoundsType.VISUAL);
        text.setY(cy);

        return text;
    }

    /*
     * angle in degrees, 0 is north
     */
   /* private double angle(double cx, double cy, double x, double y) {
        double deltaX = x - cx;
        double deltaY = y - cy;
        double radius = Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
        double nx     = deltaX / radius;
        double ny     = deltaY / radius;
        double theta  = Math.toRadians(90) + Math.atan2(ny, nx);

        return Double.compare(theta, 0.0) >= 0 ? Math.toDegrees(theta) : Math.toDegrees((theta)) + 360.0;
    }*/

    /*
     * angle in degrees, 0 is north
     */
    /*private Point2D pointOnCircle(double cX, double cY, double radius, double angle) {

        return new Point2D(cX - (radius * Math.sin(Math.toRadians(angle - 180))),
                           cY + (radius * Math.cos(Math.toRadians(angle - 180))));
    }*/

    /*
     * Needed if you want to know what's defined in css during initialization
     */
    private void applyCss(Node node) {
        Group group = new Group(node);
        group.getStyleClass().add(getStyleClassName());
        addStyleSheets(group);
        new Scene(group);
        node.applyCss();
    }

    private void resize() {
        Insets padding         = getPadding();
        double availableWidth  = getWidth() - padding.getLeft() - padding.getRight();
        double availableHeight = getHeight() - padding.getTop() - padding.getBottom();

        double width = Math.max(Math.min(Math.min(availableWidth, availableHeight * ASPECT_RATIO), MAXIMUM_WIDTH), MINIMUM_WIDTH);

        double scalingFactor = width / ARTBOARD_WIDTH;

        if (availableWidth > 0 && availableHeight > 0) {
            drawingPane.relocate((getWidth() - ARTBOARD_WIDTH) * 0.5, (getHeight() - ARTBOARD_HEIGHT) * 0.5);
            drawingPane.setScaleX(scalingFactor);
            drawingPane.setScaleY(scalingFactor);
        }
    }

    private void addStyleSheets(Parent parent) {
        String fonts = getClass().getResource(FONTS_CSS).toExternalForm();
        parent.getStylesheets().add(fonts);

        String stylesheet = getClass().getResource(STYLE_CSS).toExternalForm();
        parent.getStylesheets().add(stylesheet);
    }

    private String getStyleClassName() {
        String className = this.getClass().getSimpleName();

        return className.substring(0, 1).toLowerCase() + className.substring(1);
    }

    // compute sizes

    @Override
    protected double computeMinWidth(double height) {
        Insets padding           = getPadding();
        double horizontalPadding = padding.getLeft() + padding.getRight();

        return MINIMUM_WIDTH + horizontalPadding;
    }

    @Override
    protected double computeMinHeight(double width) {
        Insets padding         = getPadding();
        double verticalPadding = padding.getTop() + padding.getBottom();

        return MINIMUM_HEIGHT + verticalPadding;
    }

    @Override
    protected double computePrefWidth(double height) {
        Insets padding           = getPadding();
        double horizontalPadding = padding.getLeft() + padding.getRight();

        return ARTBOARD_WIDTH + horizontalPadding;
    }

    @Override
    protected double computePrefHeight(double width) {
        Insets padding         = getPadding();
        double verticalPadding = padding.getTop() + padding.getBottom();

        return ARTBOARD_HEIGHT + verticalPadding;
    }

    // getter and setter for all properties

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public Color getBaseColor() {
        return baseColor.get();
    }

    public StyleableObjectProperty<Color> baseColorProperty() {
        return baseColor;
    }

    public void setBaseColor(Color baseColor) {
        this.baseColor.set(baseColor);
    }

    public boolean getCircleAnimationIsRunning() {
        return circleAnimationIsRunning.get();
    }

    public BooleanProperty circleAnimationIsRunningProperty() {
        return circleAnimationIsRunning;
    }

    public void setCircleAnimationIsRunning(boolean circleAnimationIsRunning) {
        this.circleAnimationIsRunning.set(circleAnimationIsRunning);
    }

    public Duration getPulse() {
        return pulse.get();
    }

    public ObjectProperty<Duration> pulseProperty() {
        return pulse;
    }

    public void setPulse(Duration pulse) {
        this.pulse.set(pulse);
    }

    public double getAnimatedHeightValue() {
        return animatedHeightValue.get();
    }

    public DoubleProperty animatedHeightValueProperty() {
        return animatedHeightValue;
    }

    public void setAnimatedHeightValue(double animatedHeightValue) {
        this.animatedHeightValue.set(animatedHeightValue);
    }

    public double getHeightValue() {
        return heightValue.get();
    }

    public DoubleProperty heightValueProperty() {
        return heightValue;
    }

    public void setHeightValue(double heightValue) {
        this.heightValue.set(heightValue);
    }

    public boolean isAnimated() {
        return animated.get();
    }

    public BooleanProperty animatedProperty() {
        return animated;
    }

    public void setAnimated(boolean animated) {
        this.animated.set(animated);
    }
}
