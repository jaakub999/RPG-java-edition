package API;

import com.github.javafaker.Faker;

public class Spell {
    private String name;
    private Type type;
    private int cost;
    private int dmg;
    private int lowDmg;
    private int highDmg;

    public Spell(String name, int cost, int dmg, Type type) {
        this.name = name;
        this.dmg = dmg;
        this.lowDmg = dmg - 15;
        this.highDmg = dmg + 15;
        this.cost = cost;
        this.type = type;
    }

    public int generateDamage() {
        Faker faker = new Faker();
        return faker.number().numberBetween(lowDmg, highDmg);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getDmg() {
        return dmg;
    }

    public void setDmg(int dmg) {
        this.dmg = dmg;
    }

    public int getLowDmg() {
        return lowDmg;
    }

    public void setLowDmg(int lowDmg) {
        this.lowDmg = lowDmg;
    }

    public int getHighDmg() {
        return highDmg;
    }

    public void setHighDmg(int highDmg) {
        this.highDmg = highDmg;
    }
}