package ch.fhnw.cuie.module03.horizonjavasolution;

import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

public class Horizon extends Region {
    private static final double PREFERRED_SIZE = 300;
    private static final double MINIMUM_SIZE   = 75;
    private static final double MAXIMUM_SIZE   = 800;

    //declare all parts
    private Polygon triangle;
    private Group   plane;
    private Group   ticks;
    private Group   measures;
    private Region  ground;
    private Circle  sky;

    private Group background;

    // needed for resizing
    private Pane drawingPane;

    public Horizon() {
        initializeSelf();
        initializeParts();
        layoutParts();
    }

    private void initializeSelf() {
        // load stylesheets
        String fonts = getClass().getResource("fonts.css").toExternalForm();
        getStylesheets().add(fonts);

        String stylesheet = getClass().getResource("style.css").toExternalForm();
        getStylesheets().add(stylesheet);

        // initialize resizing constraints
        Insets padding           = getPadding();
        double verticalPadding   = padding.getTop() + padding.getBottom();
        double horizontalPadding = padding.getLeft() + padding.getRight();
        setMinSize(MINIMUM_SIZE + horizontalPadding, MINIMUM_SIZE + verticalPadding);
        setPrefSize(PREFERRED_SIZE + horizontalPadding, PREFERRED_SIZE + verticalPadding);
        setMaxSize(MAXIMUM_SIZE + horizontalPadding, MAXIMUM_SIZE + verticalPadding);
    }

    private void initializeParts() {
        triangle = new Polygon(149.5, 6, 155, 21, 144, 21);
        triangle.getStyleClass().add("triangle");

        plane = new Group();
        plane.getStyleClass().add("plane");
        Circle body = new Circle(150, 150, 9);

        Line leftWing = new Line(129, 150, 141, 150);
        Line rightWing = new Line(159, 150, 171, 150);
        Line fin = new Line(150, 129, 150, 141);
        plane.getChildren().addAll(leftWing, body, rightWing, fin);


        ticks = new Group();
        ticks.getStyleClass().add("ticks");

        double center = PREFERRED_SIZE / 2;

        int    numberOfTicks = 13;
        double overallAngle  = 180;
        double tickLength    = 6;
        double indent        = 3;
        double startingAngle = -90;

        double degreesBetweenTicks = overallAngle / (numberOfTicks - 1);
        double outerRadius         = center - indent;
        double innerRadius         = center - indent - tickLength;

        for (int i = 0; i < numberOfTicks; i++) {
            double angle       = 180 + startingAngle + i * degreesBetweenTicks;

            Point2D startPoint = pointOnCircle(center, center, outerRadius, angle);
            Point2D endPoint   = pointOnCircle(center, center, innerRadius, angle);

            Line tick = new Line(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());

            if(i%3 == 0){
                tick.getStyleClass().add("highlightedTick");
            }
            else {
                tick.getStyleClass().add("defaultTick");
            }

            ticks.getChildren().add(tick);
        }

        measures = new Group();
        measures.getStyleClass().add("measures");
        for (int i = 0; i < 5; i++) {
            Line longLine = new Line(110, 150 - i * 30, 190, 150 - i * 30);
            longLine.getStyleClass().add("skyLine");
            Line shortLine = new Line(130, 150 - i * 30 - 15, 170, 150 - i * 30 - 15);
            shortLine.getStyleClass().add("skyLine");
            Text textLeft  = new Text(85, 150 - i * 30, String.valueOf(i * 10));
            Text textRight = new Text(198, 150 - i * 30, String.valueOf(i * 10));
            textLeft.getStyleClass().add("skyLine");
            textRight.getStyleClass().add("skyLine");
            measures.getChildren().addAll(shortLine);
            if (i != 0) {
                measures.getChildren().addAll(longLine, textLeft, textRight);
            }
        }

        Line zeroLine = new Line(2, 150, 296, 150);
        zeroLine.getStyleClass().add("ground");
        measures.getChildren().add(zeroLine);

        for (int i = 0; i < 5; i++) {
            Line longLine = new Line(110, 150 + i * 30, 190, 150 + i * 30);
            longLine.getStyleClass().add("ground");
            Line shortLine = new Line(130, 150 + i * 30 + 15, 170, 150 + i * 30 + 15);
            shortLine.getStyleClass().add("ground");
            Text textLeft  = new Text(85, 150 + i * 30, String.valueOf(-i * 10));
            Text textRight = new Text(198, 150 + i * 30, String.valueOf(-i * 10));
            textLeft.getStyleClass().add("ground");
            textRight.getStyleClass().add("ground");
            measures.getChildren().addAll(shortLine);
            if (i != 0) {
                measures.getChildren().addAll(longLine, textLeft, textRight);
            }
        }

        ground = new Region();
        ground.setPrefSize(300, 150);
        ground.relocate(0, 150);
        ground.getStyleClass().add("ground");

        sky = new Circle(150, 150, 150);
        sky.getStyleClass().add("sky");

        background = new Group(sky, ground, measures, ticks);

        drawingPane = new Pane();
        drawingPane.getStyleClass().add("horizon");
        drawingPane.setMaxSize(PREFERRED_SIZE, PREFERRED_SIZE);
        drawingPane.setMinSize(PREFERRED_SIZE, PREFERRED_SIZE);
        drawingPane.setPrefSize(PREFERRED_SIZE, PREFERRED_SIZE);
    }

    private void layoutParts() {
        // add all parts to drawingPane
        drawingPane.getChildren().addAll(background, plane, triangle);

        getChildren().add(drawingPane);
    }

    //resize by scaling
    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        resize();
    }

    private void resize() {
        double width  = getWidth() - getInsets().getLeft() - getInsets().getRight();
        double height = getHeight() - getInsets().getTop() - getInsets().getBottom();
        double size   = Math.max(Math.min(Math.min(width, height), MAXIMUM_SIZE), MINIMUM_SIZE);

        double scalingFactor = size / PREFERRED_SIZE;

        if (width > 0 && height > 0) {
            drawingPane.relocate((getWidth() - PREFERRED_SIZE) * 0.5, (getHeight() - PREFERRED_SIZE) * 0.5);
            drawingPane.setScaleX(scalingFactor);
            drawingPane.setScaleY(scalingFactor);
        }
    }

    private Point2D pointOnCircle(double centerX, double centerY, double radius, double angle) {
        return new Point2D(centerX - (radius * Math.sin(Math.toRadians(angle))),
                           centerY + (radius * Math.cos(Math.toRadians(angle))));
    }


}
