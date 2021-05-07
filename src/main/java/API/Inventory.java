package API;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private Map<String, Item> items;

    public Inventory() {
        items = new HashMap<String, Item>();
    }

    public void addItem(Item item) {
        items.put(item.getName(), item);
    }

    public boolean useItem(Item item) {
        int quantity = items.get(item.getName()).getQuantity();

        if (quantity != 0) {
            item.setQuantity(--quantity);
            items.put(item.getName(), item);

            return true;
        }

        return false;
    }

    public Map<String, Item> getItems() {
        return items;
    }

    public void setItems(Map<String, Item> items) {
        this.items = items;
    }
}