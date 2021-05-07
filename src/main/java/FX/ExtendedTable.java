package FX;

import javafx.beans.property.SimpleIntegerProperty;

public class ExtendedTable extends SimpleTable {
    private final SimpleIntegerProperty quantity;

    public ExtendedTable(String name, Integer quantity) {
        super(name);
        this.quantity = new SimpleIntegerProperty(quantity);
    }

    public int getQuantity() {
        return quantity.get();
    }

    public SimpleIntegerProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }
}
