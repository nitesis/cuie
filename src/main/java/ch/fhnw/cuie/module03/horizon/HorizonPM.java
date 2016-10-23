package ch.fhnw.cuie.module03.horizon;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * Created by nitesis on 23.10.16.
 */
public class HorizonPM {

    private final DoubleProperty valueInput = new SimpleDoubleProperty();
    private final BooleanProperty isValidValueInput = new SimpleBooleanProperty();

    public HorizonPM() {
        addEventListeners();
    }

    private void addEventListeners() {
        valueInputProperty().addListener((observable, oldValue, newValue) -> setIsValidValueInput(newValue.doubleValue() > 999 && newValue.doubleValue() < 10000));
    }

    public double getValueInput() {
        return valueInput.get();
    }

    public DoubleProperty valueInputProperty() {
        return valueInput;
    }

    public void setValueInput(double valueInput) {
        this.valueInput.set(valueInput);
    }

    public boolean isIsValidValueInput() {
        return isValidValueInput.get();
    }

    public BooleanProperty isValidValueInputProperty() {
        return isValidValueInput;
    }

    public void setIsValidValueInput(boolean isValidValueInput) {
        this.isValidValueInput.set(isValidValueInput);
    }
}
