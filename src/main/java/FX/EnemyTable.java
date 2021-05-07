package FX;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class EnemyTable {
    private final SimpleStringProperty name;
    private final SimpleIntegerProperty hp;
    private final SimpleIntegerProperty mp;
    private final SimpleIntegerProperty ad;
    private final SimpleIntegerProperty ap;

    public EnemyTable(String name, Integer hp, Integer mp, Integer ad, Integer ap) {
        this.name = new SimpleStringProperty(name);
        this.hp = new SimpleIntegerProperty(hp);
        this.mp = new SimpleIntegerProperty(mp);
        this.ad = new SimpleIntegerProperty(ad);
        this.ap = new SimpleIntegerProperty(ap);
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

    public int getHp() {
        return hp.get();
    }

    public SimpleIntegerProperty hpProperty() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp.set(hp);
    }

    public int getMp() {
        return mp.get();
    }

    public SimpleIntegerProperty mpProperty() {
        return mp;
    }

    public void setMp(int mp) {
        this.mp.set(mp);
    }

    public int getAd() {
        return ad.get();
    }

    public SimpleIntegerProperty adProperty() {
        return ad;
    }

    public void setAd(int ad) {
        this.ad.set(ad);
    }

    public int getAp() {
        return ap.get();
    }

    public SimpleIntegerProperty apProperty() {
        return ap;
    }

    public void setAp(int ap) {
        this.ap.set(ap);
    }
}
