package ch.fhnw.cuie.module09.timecontrolmanufactory;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import ch.fhnw.cuie.module09.timecontrolexample.TimeSkin;

/**
 * @author Dieter Holz
 */
public class MyTimeSkin extends SkinBase<MyTimeControl> {
    private static final int IMG_SIZE   = 12;
    private static final int IMG_OFFSET = 4;

    private enum State {
        VALID("Valid",      "valid.png"),
        INVALID("Invalid",  "invalid.png");

        public final String    text;
        public final ImageView imageView;

        State(final String text, final String file) {
            this.text = text;
            String url = TimeSkin.class.getResource("icons/" + file).toExternalForm();
            this.imageView = new ImageView(new Image(url,
                                                     IMG_SIZE, IMG_SIZE,
                                                     true, false));
        }
    }


    private TextField timeField;
    private Label timeLabel;

    public MyTimeSkin(MyTimeControl control){
        super(control);
        initializeSelf();
        initializeParts();
        layoutParts();
        addValueChangeListener();
        setupBindings();

    }

    private void initializeSelf() {
        String fonts = getClass().getResource("fonts.css").toExternalForm();
        getSkinnable().getStylesheets().add(fonts);

        String stylesheet = getClass().getResource("style.css").toExternalForm();
        getSkinnable().getStylesheets().add(stylesheet);
    }

    private void initializeParts() {
        timeField = new TextField(getSkinnable().getTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        timeField.getStyleClass().add("timeField");

        timeLabel = new Label();
        //added style class for timeLabel
        timeLabel.getStyleClass().add("timeLabel");
        //soll am Anfang NICHT angezeigt werden
        timeLabel.setVisible(false);
    }

    private void layoutParts(){
        getChildren().addAll(timeField, timeLabel);
    }

    private void addValueChangeListener(){
        timeField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                getSkinnable().setTime(LocalTime.parse(newValue));
            } catch (Exception e) {
                // Todo : setInvalid(true)
            }
        });

        getSkinnable().timeProperty().addListener((observable, oldValue, newValue) -> {
            String newText = newValue.format(DateTimeFormatter.ofPattern("HH:mm"));
            timeField.setText(newText);
        });
    }

    private void setupBindings(){
        //mit getSkinnable komm ich auf Control Klasse
        timeField.visibleProperty().bind(getSkinnable().editableProperty());
        //not = wenn in editableProperty false steht, wird timelabel sichtbar
        timeLabel.visibleProperty().bind(getSkinnable().editableProperty().not());

        timeLabel.textProperty().bind(getSkinnable().timeProperty().asString());
    }
}
