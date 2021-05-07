package API;

import java.util.HashMap;
import java.util.Map;

public class Magic {
    private Map<String, Spell> spells_list;

    public Magic() {
        spells_list = new HashMap<String, Spell>();
    }

    public void addSpell(Spell[] spell) {
        for (Spell value : spell) spells_list.put(value.getName(), value);
    }

    public Map<String, Spell> getSpells_list() {
        return spells_list;
    }

    public void setSpells_list(Map<String, Spell> spells_list) {
        this.spells_list = spells_list;
    }
}
