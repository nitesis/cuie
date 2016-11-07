package ch.fhnw.cuie.module07.controllibrary;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * @author Dieter Holz
 */
class PieSkin extends SkinBase<NumberRangeControl> {
	private static final String FONTS_CSS = "fonts.css";
	private static final String STYLE_CSS = "pieStyle.css";

	private static final double PREFERRED_WIDTH  = 250;
	private static final double PREFERRED_HEIGHT = 40;

	private static final double ASPECT_RATIO = PREFERRED_WIDTH / PREFERRED_HEIGHT;

	private static final double MINIMUM_WIDTH  = 80;
	private static final double MINIMUM_HEIGHT = MINIMUM_WIDTH / ASPECT_RATIO;

	private static final double MAXIMUM_WIDTH  = 800;
	private static final double MAXIMUM_HEIGHT = MAXIMUM_WIDTH / ASPECT_RATIO;

	private static final String FORMAT     = "%.1f";
	private static final long   BLINK_RATE = 500_000_000L;

	// all parts
	private Circle border;
	private Arc pieSlice;
	private TextField valueField;

	private Pane drawingPane;

	// animations
	private AnimationTimer timer = new AnimationTimer() {
		private long lastTimerCall;

		@Override
		public void handle(long now) {
			if (now > lastTimerCall + BLINK_RATE) {

				lastTimerCall = now;
			}
		}
	};

	PieSkin(NumberRangeControl control) {
		super(control);
		init();
		initializeParts();
		layoutParts();
		addEventHandlers();
		addValueChangedListeners();
		addBindings();

		Platform.runLater(this::relocateTexts);
	}

	private void init() {
		String fonts = getClass().getResource(FONTS_CSS).toExternalForm();
		getSkinnable().getStylesheets().add(fonts);

		String stylesheet = getClass().getResource(STYLE_CSS).toExternalForm();
		getSkinnable().getStylesheets().add(stylesheet);
	}

	private void initializeParts() {

		double center = PREFERRED_HEIGHT * 0.5;
		//Kreis an Position (20,20) mit Radius 20 -> alles in Anhängigkeit von der Höhe
		border = new Circle(center, center, center);
		border.getStyleClass().add("border");

		pieSlice = new Arc(center, center, center -1, center -1, 90, -45);
		pieSlice.setType(ArcType.ROUND);
		pieSlice.getStyleClass().add("pieSlice");

		// always needed
		drawingPane = new Pane();
		drawingPane.setMaxSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);
		drawingPane.setMinSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);
		drawingPane.setPrefSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);
	}

	private void layoutParts() {
		drawingPane.getChildren().addAll(border, pieSlice);
		getChildren().add(drawingPane);
	}

	private void addEventHandlers() {
	}

	private void addValueChangedListeners() {


		getSkinnable().outOfRangeProperty()
		              .addListener((observable, oldValue, newValue) -> {
			              if (newValue) {
				              timer.start();
			              } else {
				              timer.stop();

			              }
		              });

		// always needed
		getSkinnable().widthProperty().addListener((observable, oldValue, newValue) -> resize());
		getSkinnable().heightProperty().addListener((observable, oldValue, newValue) -> resize());
	}

	private void addBindings() {

	}

	private void relocateTexts() {

	}

	private void resize() {
		Insets padding         = getSkinnable().getPadding();
		double availableWidth  = getSkinnable().getWidth() - padding.getLeft() - padding.getRight();
		double availableHeight = getSkinnable().getHeight() - padding.getTop() - padding.getBottom();

		double width = Math.max(Math.min(Math.min(availableWidth, availableHeight * ASPECT_RATIO), MAXIMUM_WIDTH), MINIMUM_WIDTH);

		double scalingFactor = width / PREFERRED_WIDTH;

		if (availableWidth > 0 && availableHeight > 0) {
			drawingPane.relocate((getSkinnable().getWidth() - PREFERRED_WIDTH) * 0.5, (getSkinnable().getHeight() - PREFERRED_HEIGHT) * 0.5);
			drawingPane.setScaleX(scalingFactor);
			drawingPane.setScaleY(scalingFactor);
		}
	}

	// compute sizes

	@Override
	protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
		double horizontalPadding = leftInset + rightInset;

		return MINIMUM_WIDTH + horizontalPadding;
	}

	@Override
	protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
		double verticalPadding = topInset + bottomInset;

		return MINIMUM_HEIGHT + verticalPadding;
	}

	@Override
	protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
		double horizontalPadding = leftInset + rightInset;

		return PREFERRED_WIDTH + horizontalPadding;
	}

	@Override
	protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
		double verticalPadding = topInset + bottomInset;

		return PREFERRED_HEIGHT + verticalPadding;
	}

	@Override
	protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
		double horizontalPadding = leftInset + rightInset;

		return MAXIMUM_WIDTH + horizontalPadding;
	}

	@Override
	protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
		double verticalPadding = topInset + bottomInset;

		return MAXIMUM_HEIGHT + verticalPadding;
	}
}
