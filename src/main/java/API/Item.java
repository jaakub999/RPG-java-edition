package API;

public class Item {
    private String name, description;
    private int quantity;
    private int prop;

    public Item(String name,  int prop, int quantity, String description) {
        this.name = name;
        this.prop = prop;
        this.quantity = quantity;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getProp() {
        return prop;
    }

    public void setProp(int prop) {
        this.prop = prop;
    }
}
