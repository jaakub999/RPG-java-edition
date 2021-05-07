package FX;

import API.Buff;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class PlayerTable {
    private final SimpleStringProperty nickname;
    private final SimpleStringProperty character;
    private final SimpleStringProperty element;
    private final SimpleIntegerProperty healthPotion;
    private final SimpleIntegerProperty manaPotion;
    private final SimpleIntegerProperty grenade;

    public PlayerTable(String nickname,
                       String character,
                       Buff element,
                       Integer healthPotion,
                       Integer manaPotion,
                       Integer grenade)
    {
        this.nickname = new SimpleStringProperty(nickname);
        this.character = new SimpleStringProperty(character);
        this.element = new SimpleStringProperty(String.valueOf(element));
        this.healthPotion = new SimpleIntegerProperty(healthPotion);
        this.manaPotion = new SimpleIntegerProperty(manaPotion);
        this.grenade = new SimpleIntegerProperty(grenade);
    }

    public String getNickname() {
        return nickname.get();
    }

    public SimpleStringProperty nicknameProperty() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname.set(nickname);
    }

    public String getCharacter() {
        return character.get();
    }

    public SimpleStringProperty characterProperty() {
        return character;
    }

    public void setCharacter(String character) {
        this.character.set(character);
    }

    public String getElement() {
        return element.get();
    }

    public SimpleStringProperty elementProperty() {
        return element;
    }

    public void setElement(String element) {
        this.element.set(element);
    }

    public int getHealthPotion() {
        return healthPotion.get();
    }

    public SimpleIntegerProperty healthPotionProperty() {
        return healthPotion;
    }

    public void setHealthPotion(int healthPotion) {
        this.healthPotion.set(healthPotion);
    }

    public int getManaPotion() {
        return manaPotion.get();
    }

    public SimpleIntegerProperty manaPotionProperty() {
        return manaPotion;
    }

    public void setManaPotion(int manaPotion) {
        this.manaPotion.set(manaPotion);
    }

    public int getGrenade() {
        return grenade.get();
    }

    public SimpleIntegerProperty grenadeProperty() {
        return grenade;
    }

    public void setGrenade(int grenade) {
        this.grenade.set(grenade);
    }
}
