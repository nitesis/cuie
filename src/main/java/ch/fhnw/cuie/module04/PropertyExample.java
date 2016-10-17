package ch.fhnw.cuie.module04;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Created by nitesis on 17.10.16.
 */
public class PropertyExample {
    //Merke: Für alle Basistypen gibt es die jeweilige Entsprechung bei Property
    //Simple steht IMMER am Anfang einer PropertyInstanz
    //werden grundsätzlich als final deklariert, da sie sich nicht mehr ändern
    //Initialwerte können (müssen aber nicht) in Klammern angegeben werden
    private final IntegerProperty age = new SimpleIntegerProperty();
    private final BooleanProperty isAdult = new SimpleBooleanProperty();

    public int getAge() {
        return age.get();
    }

    public IntegerProperty ageProperty() {
        return age;
    }

    public void setAge(int age) {
        this.age.set(age);
    }

    public boolean isIsAdult() {
        return isAdult.get();
    }

    public BooleanProperty isAdultProperty() {
        return isAdult;
    }

    public void setIsAdult(boolean isAdult) {
        this.isAdult.set(isAdult);
    }
}
