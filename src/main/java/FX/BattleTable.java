package FX;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class BattleTable {
    private final SimpleStringProperty name;
    private final SimpleIntegerProperty x;
    private final SimpleIntegerProperty y;

    public BattleTable(String name, Integer x, Integer y) {
        this.name = new SimpleStringProperty(name);
        this.x = new SimpleIntegerProperty(x);
        this.y = new SimpleIntegerProperty(y);
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

    public int getX() {
        return x.get();
    }

    public SimpleIntegerProperty xProperty() {
        return x;
    }

    public void setX(int x) {
        this.x.set(x);
    }
    public int getY() {
        return y.get();
    }

    public SimpleIntegerProperty yProperty() {
        return y;
    }

    public void setY(int y) {
        this.y.set(y);
    }
}