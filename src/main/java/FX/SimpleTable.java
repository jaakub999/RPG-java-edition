package FX;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

public class SimpleTable {
    private final SimpleStringProperty name;
    private CheckBox select;

    public SimpleTable(String name) {
        this.name = new SimpleStringProperty(name);
        select = new CheckBox();
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public CheckBox getSelect() {
        return select;
    }

    public void setSelect(CheckBox select) {
        this.select = select;
    }
}