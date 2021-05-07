package FX;

import API.Buff;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.CheckBox;

public class PlayerMasterTable extends PlayerTable {
    private final SimpleIntegerProperty currentHP;
    private final SimpleIntegerProperty currentMP;
    private CheckBox select;

    public PlayerMasterTable(String nickname,
                             String character,
                             Buff element,
                             Integer healthPotion,
                             Integer manaPotion,
                             Integer grenade,
                             Integer currentHP,
                             Integer currentMP)
    {
        super(nickname, character, element, healthPotion, manaPotion, grenade);
        this.currentHP = new SimpleIntegerProperty(currentHP);
        this.currentMP = new SimpleIntegerProperty(currentMP);
        select = new CheckBox();
    }

    public int getCurrentHP() {
        return currentHP.get();
    }

    public SimpleIntegerProperty currentHPProperty() {
        return currentHP;
    }

    public void setCurrentHP(int currentHP) {
        this.currentHP.set(currentHP);
    }

    public int getCurrentMP() {
        return currentMP.get();
    }

    public SimpleIntegerProperty currentMPProperty() {
        return currentMP;
    }

    public void setCurrentMP(int currentMP) {
        this.currentMP.set(currentMP);
    }

    public CheckBox getSelect() {
        return select;
    }

    public void setSelect(CheckBox select) {
        this.select = select;
    }
}
