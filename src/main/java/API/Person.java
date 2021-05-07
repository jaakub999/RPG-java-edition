package API;

import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Person {
    private String name;
    private Buff buff;
    private Magic magic;
    private int HP;
    private int maxHP;
    private int maxMP;
    private int MP;
    private int dmg;
    private int lowAttack;
    private int highAttack;
    private int power;
    private boolean stun;
    private boolean block;

    public Person(String name,
                  Buff buff,
                  int HP,
                  int MP,
                  int dmg,
                  int power,
                  Magic magic)
    {
        switch (buff) {
            case WATER:
                MP += 100;
                break;

            case FIRE:
                dmg += 25;
                power = (int) (power * 1.1);
                break;

            case SOIL:
                HP += 350;
                break;
        }

        this.name = name;
        this.buff = buff;
        this.maxHP = HP;
        this.HP = HP;
        this.maxMP = MP;
        this.MP = MP;
        this.magic = magic;
        this.power = power;
        this.dmg = dmg;
        lowAttack = dmg - 10;
        highAttack = dmg + 10;
        stun = false;
        block = false;
    }

    public String getStats() {
        StringBuilder label = new StringBuilder("                \t");
        StringBuilder hpBar = new StringBuilder();
        float hpTicks = ((float) HP / (float) maxHP) * 100 / 2;
        int decreased;

        label.replace(0, name.length(), name);

        while (hpTicks > 0) {
            hpBar.append("█");
            hpTicks -= 1;
        }

        while (hpBar.length() < 50)
            hpBar.append("░");

        String hp = HP + "/" + maxHP;
        StringBuilder currentHP = new StringBuilder();

        if (hp.length() < 11) {
            decreased = 11 - hp.length();

            while (decreased > 0) {
                currentHP.append(" ");
                decreased -= 1;
            }

            currentHP.append(hp);
        }

        else
            currentHP.replace(0, currentHP.length(), hp);

        label.append(" ");
        label.append(currentHP);
        label.append(" |");
        label.append(hpBar);
        label.append("|");

        return label.toString();
    }

    public void takeDamage(int dmg) {
        HP -= dmg;

        if (HP < 0)
            HP = 0;
    }

    public int generateDamage() {
        Faker faker = new Faker();
        return faker.number().numberBetween(lowAttack, highAttack);
    }

    public void heal(int x) {
        if (buff == Buff.WATER)
            HP += (int) (1.1 * x);

        else
            HP += x;

        if (HP > maxHP)
            HP = maxHP;
    }

    public void reduceMP(int cost) {
        MP -= cost;
    }

    public void manaRestore(int x) {
        if (buff == Buff.SOIL)
            MP += (int) (x * 0.8);

        else if (buff == Buff.WIND)
            MP += x + 10;

        else
            MP += x;

        if (MP > maxMP)
            MP = maxMP;
    }

    public Spell chooseSpell() {
        List<Spell> values = new ArrayList<Spell>(magic.getSpells_list().values());
        int index = new Random().nextInt(values.size());

        return values.get(index);
    }

    public int getDodgeChance() {
        Random rand = new Random();
        int bound = 100;

        if (buff == Buff.WIND)
            bound -= 15;

        if (block)
            bound -= 75;

        return rand.nextInt(bound);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Buff getBuff() {
        return buff;
    }

    public void setBuff(Buff buff) {
        this.buff = buff;
    }

    public Magic getMagic() {
        return magic;
    }

    public void setMagic(Magic magic) {
        this.magic = magic;
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }

    public int getMaxMP() {
        return maxMP;
    }

    public void setMaxMP(int maxMP) {
        this.maxMP = maxMP;
    }

    public int getMP() {
        return MP;
    }

    public void setMP(int MP) {
        this.MP = MP;
    }

    public int getDmg() {
        return dmg;
    }

    public void setDmg(int dmg) {
        this.dmg = dmg;
    }

    public int getLowAttack() {
        return lowAttack;
    }

    public void setLowAttack(int lowAttack) {
        this.lowAttack = lowAttack;
    }

    public int getHighAttack() {
        return highAttack;
    }

    public void setHighAttack(int highAttack) {
        this.highAttack = highAttack;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public boolean isStun() {
        return stun;
    }

    public void setStun(boolean stun) {
        this.stun = stun;
    }

    public boolean isBlock() {
        return block;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }
}