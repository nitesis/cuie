package ch.fhnw.cuie.myProjects.pot;

import java.util.HashMap;
import java.util.List;
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
import javafx.scene.control.TextField;
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
    private Map<String,Double> buildings= new HashMap<String,Double>();

    // all properties
    private final StringProperty title = new SimpleStringProperty();
    private final DoubleProperty heightValue = new SimpleDoubleProperty();

    private final BooleanProperty          timerIsRunning = new SimpleBooleanProperty(false);
    private final ObjectProperty<Duration> pulse          = new SimpleObjectProperty<>(Duration.seconds(1.0));

    private final BooleanProperty animated      = new SimpleBooleanProperty(true);
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

    public PotHeightControl() {
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

        buildings.put("Burj Kalifa", 830.0);

        titleLabelBox = new HBox();
        titleLabelBox.setStyle("-fx-padding: 10");
        titleLabelBox.setAlignment(Pos.CENTER);
        titleLabelBox.setPrefSize(ARTBOARD_WIDTH, 25);
        titleLabel = new TextField((String) buildings.keySet().toArray()[0]);
        //titleLabelBox.getChildren().add(titleLabel);

        titleLabel.getStyleClass().add("titleLabel");

        heightCircleSmall = new Circle(200, ARTBOARD_HEIGHT - buildings.get("Burj Kalifa") * HEIGHT_FACTOR, 2);
        heightCircleSmall.getStyleClass().add("heightCircleSmall");

        heightCircleBig = new Circle(200, ARTBOARD_HEIGHT - buildings.get("Burj Kalifa") * HEIGHT_FACTOR, 17);
        heightCircleBig.getStyleClass().add("heightCircleBig");

        heightLine = new Line(200, 400, 200, ARTBOARD_HEIGHT - buildings.get("Burj Kalifa") * HEIGHT_FACTOR);
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


        heightLabel = new TextField(buildings.get("Burj Kalifa").toString());
        heightLabel.getStyleClass().add("heightLabel");
        heightLabelBox.getChildren().add(heightLabel);

        // initialize parts for other buildings
        labelB1B2 = new Label("");
        labelB1B2.setTranslateX(130);
        labelB1B2.setTranslateY(140);
        labelB1B2.setWrapText(true);
        labelB1B2.setMaxSize(70,70);
        labelB1B2.setFont(Font.font("Arial",9));

        labelB3B4 = new Label("");
        labelB3B4.setTranslateX(210);
        labelB3B4.setTranslateY(130);
        labelB3B4.setWrapText(true);
        labelB3B4.setMaxSize(70,70);
        labelB3B4.setFont(Font.font("Arial",9));

        buildings.put("Shanghai Tower", 632.0);
        buildings.put("Abraj Al-Bait Clock Tower", 601.0);
        buildings.put("Ping An Finance Centre", 599.0);
        buildings.put("Lotte World Tower", 554.5);

        heightCircleB1 = new Circle(160, ARTBOARD_HEIGHT - buildings.get("Shanghai Tower") * HEIGHT_FACTOR, 7);
        heightCircleB1.getStyleClass().add("heightCircleSmall");

        heightLineB1 = new Line(160, 400, 160, ARTBOARD_HEIGHT - buildings.get("Shanghai Tower") * HEIGHT_FACTOR);
        heightLineB1.getStyleClass().add("heightLine");
        heightLineB1.setStrokeLineCap(StrokeLineCap.ROUND);

        heightCircleB2 = new Circle(180, ARTBOARD_HEIGHT - buildings.get("Abraj Al-Bait Clock Tower") * HEIGHT_FACTOR, 7);
        heightCircleB2.getStyleClass().add("heightCircleSmall");

        heightLineB2 = new Line(180, 400, 180, ARTBOARD_HEIGHT - buildings.get("Abraj Al-Bait Clock Tower") * HEIGHT_FACTOR);
        heightLineB2.getStyleClass().add("heightLine");
        heightLineB2.setStrokeLineCap(StrokeLineCap.ROUND);

        heightCircleB3 = new Circle(220, ARTBOARD_HEIGHT - buildings.get("Ping An Finance Centre") * HEIGHT_FACTOR, 7);
        heightCircleB3.getStyleClass().add("heightCircleSmall");

        heightLineB3 = new Line(220, 400, 220, ARTBOARD_HEIGHT - buildings.get("Ping An Finance Centre") * HEIGHT_FACTOR);
        heightLineB3.getStyleClass().add("heightLine");
        heightLineB3.setStrokeLineCap(StrokeLineCap.ROUND);

        heightCircleB4 = new Circle(240, ARTBOARD_HEIGHT - buildings.get("Lotte World Tower") * HEIGHT_FACTOR, 7);
        heightCircleB4.getStyleClass().add("heightCircleSmall");

        heightLineB4 = new Line(240, 400, 240, ARTBOARD_HEIGHT - buildings.get("Lotte World Tower") * HEIGHT_FACTOR);
        heightLineB4.getStyleClass().add("heightLine");
        heightLineB4.setStrokeLineCap(StrokeLineCap.ROUND);

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
                heightLabelBox);

        getChildren().add(drawingPane);
    }

    private void initializeAnimations() {

    }

    private void addEventHandlers() {

        heightCircleBig.setOnMouseDragged(event -> {
            if(((drawingPane.getMaxHeight() - event.getY()) / HEIGHT_FACTOR) > 1000.0)
                setHeightValue(1000.0);
            else {
                if (((drawingPane.getMaxHeight() - event.getY()) / HEIGHT_FACTOR) < 0.0)
                    setHeightValue(0.0);
                else
                    setHeightValue((drawingPane.getMaxHeight() - event.getY()) / HEIGHT_FACTOR);
            }
        });

        heightCircleBig.setOnMouseEntered(event -> {
            if (scaleTransition == null) {
                scaleTransition = new ScaleTransition(Duration.seconds(0.4), heightCircleBig);
                scaleTransition.setFromX(1.0);
                scaleTransition.setFromY(1.0);
                scaleTransition.setByX(0.5);
                scaleTransition.setByY(0.5);
                scaleTransition.setCycleCount(2);
                scaleTransition.setInterpolator(Interpolator.EASE_OUT);
                scaleTransition.setAutoReverse(true);
            }

            if (fillTransition == null) {
                fillTransition = new FillTransition(Duration.seconds(1.2), heightCircleBig, getBaseColor(), getBaseColor().darker());
                fillTransition.setOnFinished(event1 -> getBaseColor());
            }

            if (!fillTransition.getStatus().equals(Animation.Status.RUNNING)) {
                circleTransition = new ParallelTransition(scaleTransition, fillTransition);
                circleTransition.setInterpolator(Interpolator.EASE_BOTH);
                circleTransition.play();
            }
        });

        heightCircleBig.setOnMouseExited(event -> {
            heightCircleBig.getStyleClass().add("heightCircleBig");
        });

        // B1 OnMouseEntered/Exited
        heightCircleB1.setOnMouseEntered(event ->{
            heightCircleB1.setRadius(10.0);
            heightCircleB1.setFill(Color.LIGHTPINK);
            labelB1B2.setText("Shanghai Tower: " + buildings.get("Shanghai Tower")+" m");
        });

        heightCircleB1.setOnMouseExited(event ->{
            heightCircleB1.setRadius(7.0);
            heightCircleB1.setFill(Color.WHITE);
            labelB1B2.setText("");
        });

        // B2 OnMouseEntered/Exited
        heightCircleB2.setOnMouseEntered(event ->{
            heightCircleB2.setRadius(10.0);
            heightCircleB2.setFill(Color.LIGHTPINK);
            labelB1B2.setText("Abraj Al-Bait Clock Tower: " + buildings.get("Abraj Al-Bait Clock Tower")+" m");
        });

        heightCircleB2.setOnMouseExited(event ->{
            heightCircleB2.setRadius(7.0);
            heightCircleB2.setFill(Color.WHITE);
            labelB1B2.setText("");
        });

        // B3 OnMouseEntered/Exited
        heightCircleB3.setOnMouseEntered(event ->{
            heightCircleB3.setRadius(10.0);
            heightCircleB3.setFill(Color.LIGHTPINK);
            labelB3B4.setText("Ping An Finance Centre: " + buildings.get("Ping An Finance Centre")+" m");
        });

        heightCircleB3.setOnMouseExited(event ->{
            heightCircleB3.setRadius(7.0);
            heightCircleB3.setFill(Color.WHITE);
            labelB3B4.setText("");
        });

        // B4 OnMouseEntered/Exited
        heightCircleB4.setOnMouseEntered(event ->{
            heightCircleB4.setRadius(10.0);
            heightCircleB4.setFill(Color.LIGHTPINK);
            labelB3B4.setText("Lotte World Tower " + buildings.get("Lotte World Tower")+" m");
        });

        heightCircleB4.setOnMouseExited(event ->{
            heightCircleB4.setRadius(7.0);
            heightCircleB4.setFill(Color.WHITE);
            labelB3B4.setText("");
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
        timerIsRunning.addListener((observable, oldValue, newValue) -> {
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
        Bindings.bindBidirectional(heightLabel.textProperty(), this.heightValueProperty(), new NumberStringConverter());

    }

    private void performPeriodicTask() {
    }


    // some useful helper-methods

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

    public boolean isTimerIsRunning() {
        return timerIsRunning.get();
    }

    public BooleanProperty timerIsRunningProperty() {
        return timerIsRunning;
    }

    public void setTimerIsRunning(boolean timerIsRunning) {
        this.timerIsRunning.set(timerIsRunning);
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
