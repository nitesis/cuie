package ch.fhnw.cuie.myProjects.pot;

import java.text.NumberFormat;
import java.util.List;

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
import javafx.scene.control.TextField;
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
    private StrokeTransition strokeTransition;
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
        titleLabelBox = new HBox();
        titleLabelBox.setStyle("-fx-padding: 10");
        titleLabelBox.setAlignment(Pos.CENTER);
        titleLabelBox.setPrefSize(ARTBOARD_WIDTH, 50);
        titleLabel = new TextField("Burj Kalifa");
        //titleLabelBox.getChildren().add(titleLabel);

        titleLabel.getStyleClass().add("titleLabel");
        titleLabel.setStyle("-fx-border-radius: 10px");

        heightCircleSmall = new Circle(199, 200, 6);
        heightCircleSmall.getStyleClass().add("heightCircleSmall");

        heightCircleBig = new Circle(199, 200, 16);
        heightCircleBig.getStyleClass().add("heightCircleBig");

        heightLine = new Line(200, 400, 200, 200);
        heightLine.getStyleClass().add("heightLine");
        heightLine.setStrokeLineCap(StrokeLineCap.ROUND);

        baseArc = new Arc(200, 400, 50, 50, 0, 180);
        baseArc.getStyleClass().add("heightCircleBig");

        baseLine = new Line(150, 400, 250, 400);
        baseLine.getStyleClass().add("heightLine");

        heightLabelBox = new HBox();
        heightLabelBox.setTranslateY(350);
        heightLabelBox.setStyle("-fx-padding: 10");
        heightLabelBox.setAlignment(Pos.CENTER);
        heightLabelBox.setPrefSize(ARTBOARD_WIDTH, 25);


        heightLabel = new TextField("830 m");
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
        drawingPane.getChildren().addAll(titleLabel, heightLine, baseArc, baseLine, heightCircleBig, heightCircleSmall, heightLabelBox);

        getChildren().add(drawingPane);
    }

    private void initializeAnimations() {

    }

    private void addEventHandlers() {
        heightCircleBig.setOnMouseDragged(event -> {
            setHeightValue((drawingPane.getMaxHeight() - event.getY()) * 2.5);
        });

        heightCircleBig.setOnMouseEntered(event -> {
            if (scaleTransition == null) {
                scaleTransition = new ScaleTransition(Duration.seconds(0.5), heightCircleBig);
                scaleTransition.setFromX(1.0);
                scaleTransition.setFromY(1.0);
                scaleTransition.setByX(0.5);
                scaleTransition.setByY(0.5);
                scaleTransition.setCycleCount(2);
                scaleTransition.setAutoReverse(true);
            }

            if (fillTransition == null) {
                fillTransition = new FillTransition(Duration.seconds(1.2), heightCircleBig, Color.WHITE, Color.VIOLET);
                fillTransition.setOnFinished(event1 -> setBaseColor(Color.WHITE));
                //fillTransition.setInterpolator(Interpolator.EASE_BOTH);
            }
            /*if (strokeTransition == null) {
                strokeTransition = new StrokeTransition(Duration.seconds(1), heightCircleBig);
                strokeTransition.setFromValue(Color.BLACK);
                strokeTransition.setToValue(Color.WHITE);
                strokeTransition.setCycleCount(2);
                strokeTransition.setAutoReverse(true);
            }*/
            if (!fillTransition.getStatus().equals(Animation.Status.RUNNING)) {
                circleTransition = new ParallelTransition(scaleTransition, fillTransition);
                circleTransition.setInterpolator(Interpolator.EASE_BOTH);
                circleTransition.play();
            }
        });
    }

    /*private void addEventHandlers() {
        heightCircleSmall.setOnMouseDragged(event -> {
            if(((drawingPane.getMaxHeight() - event.getY()) * 4.0) > 800.0)
                setHeightValue(1000.0);
            else {
                if (((drawingPane.getMaxHeight() - event.getY()) * 4.0) < -100.0)
                    setHeightValue(-100.0);
                else
                    setHeightValue((drawingPane.getMaxHeight() - event.getY()) * 4.0);
            }
        });
    }*/

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
            double lineLength =  heightLine.getStartY() - (newValue.doubleValue() * 0.4);

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
