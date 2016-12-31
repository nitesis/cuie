package ch.fhnw.cuie.myProjects.pot;

import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;
import javafx.util.Duration;

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

    // all parts
    private HBox titleLabelBox;
    private Text titleLabel;
    private Line heightLine;
    private Circle heightCircleSmall;
    private Circle heightCircleBig;
    private Arc baseArc;
    private Line baseLine;
    private HBox heightLabelBox;
    private Text heightLabel;

    //private Arc bar;


    // all properties
    private final StringProperty title = new SimpleStringProperty();
    //private final StringProperty heightValue = new SimpleStringProperty();
    private final DoubleProperty heightValue = new SimpleDoubleProperty();

    private final BooleanProperty          timerIsRunning = new SimpleBooleanProperty(false);
    private final ObjectProperty<Duration> pulse          = new SimpleObjectProperty<>(Duration.seconds(1.0));

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
        titleLabelBox = new HBox();
        titleLabelBox.setStyle("-fx-padding: 10");
        titleLabelBox.setAlignment(Pos.CENTER);
        titleLabelBox.setPrefSize(ARTBOARD_WIDTH, 50);
        titleLabel = new Text("Burj Kalifa");
        titleLabelBox.getChildren().add(titleLabel);

        titleLabel.getStyleClass().add("titleLabel");

        heightCircleSmall = new Circle(199, 101, 6);
        heightCircleSmall.getStyleClass().add("heightCircleSmall");

        heightCircleBig = new Circle(199, 101, 16);
        heightCircleBig.getStyleClass().add("heightCircleBig");

        heightLine = new Line(200, 328, 200, 121);
        heightLine.getStyleClass().add("heightLine");
        heightLine.setStrokeLineCap(StrokeLineCap.ROUND);

        baseArc = new Arc(200, 378, 50, 50, 0, 180);
        baseArc.getStyleClass().add("heightCircleBig");

        baseLine = new Line(150, 380, 250, 380);
        baseLine.getStyleClass().add("heightLine");

        heightLabelBox = new HBox();
        heightLabelBox.setTranslateY(330);
        heightLabelBox.setStyle("-fx-padding: 10");
        heightLabelBox.setAlignment(Pos.CENTER);
        heightLabelBox.setPrefSize(ARTBOARD_WIDTH, 50);


        heightLabel = new Text("830 m");
        heightLabel.getStyleClass().add("heightLabel");
        heightLabelBox.getChildren().add(heightLabel);





        // always needed
        drawingPane = new Pane();
        drawingPane.setMaxSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
        drawingPane.setMinSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
        drawingPane.setPrefSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
        drawingPane.getStyleClass().add("drawingPane");
    }

    private void layoutParts() {
        // add all your parts here
        drawingPane.getChildren().addAll(titleLabelBox, baseArc, baseLine, heightLine, heightCircleBig, heightCircleSmall, heightLabelBox);

        getChildren().add(drawingPane);
    }

    private void initializeAnimations() {

    }

    private void addEventHandlers() {
        heightCircleBig.setOnMouseDragged(event -> {
            setHeightValue((drawingPane.getMaxHeight() - 20.0 - event.getY()) * 4.0);
        });
    }

    private void addValueChangedListeners() {
        heightValue.addListener((observable, oldValue, newValue) -> {
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

       /* //Hier wird Animation festgelegt
        valueProperty().addListener((observable, oldValue, newValue) -> {
            timeline.stop();
            timeline.getKeyFrames().setAll(new KeyFrame(Duration.millis(500),
                    new KeyValue(animatedValue, newValue)));
            timeline.play();
        });
        //Hier wird Wertänderung festgelegt
        animatedValueProperty().addListener((observable, oldValue, newValue) -> {
            bar.setVisible(true);
            checkBoundaries(getValue());
            bar.setLength(getAngle(newValue));
        });*/


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
        titleLabel.textProperty().bind(titleProperty());
        heightLabel.textProperty().bind(heightValueProperty().asString());
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

//    public String getHeightValue() {
//        return heightValue.get();
//    }
//
//    public StringProperty heightValueProperty() {
//        return heightValue;
//    }
//
//    public void setHeightValue(String heightValue) {
//        this.heightValue.set(heightValue);
//    }

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
}
