package API;

public class Player extends Person {
    private String nickname;
    private Inventory inventory;

    public Player(String nickname,
                  String name,
                  Buff buff,
                  int HP,
                  int MP,
                  int dmg,
                  int power,
                  Magic magic,
                  Inventory inventory)
    {
        super(name, buff, HP, MP, dmg, power, magic);
        this.nickname = nickname;
        this.inventory = inventory;
    }

    public String getStats() {
        StringBuilder label = new StringBuilder("                \t");
        StringBuilder hpBar = new StringBuilder();
        StringBuilder mpBar = new StringBuilder();
        float hpTicks = ((float) getHP() / (float) getMaxHP()) * 100 / 4;
        float mpTics = ((float) getMP() / (float) getMaxMP()) * 100 / 10;
        int decreased;

        label.replace(0, nickname.length(), nickname);

        while (hpTicks > 0) {
            hpBar.append("█");
            hpTicks -= 1;
        }

        while (hpBar.length() < 25)
            hpBar.append("░");

        while (mpTics > 0) {
            mpBar.append("█");
            mpTics -= 1;
        }

        while (mpBar.length() < 10)
            mpBar.append("░");

        String hp = getHP() + "/" + getMaxHP();
        StringBuilder currentHP = new StringBuilder();

        if (hp.length() < 9) {
            decreased = 9 - hp.length();

            while (decreased > 0) {
                currentHP.append(" ");
                decreased -= 1;
            }

            currentHP.append(hp);
        }

        else
            currentHP.replace(0, currentHP.length(), hp);

        String mp = getMP() + "/" + getMaxMP();
        StringBuilder currentMP = new StringBuilder();

        if (mp.length() < 7) {
            decreased = 7 - mp.length();

            while (decreased > 0) {
                currentMP.append(" ");
                decreased -= 1;
            }

            currentMP.append(mp);
        }

        else
            currentMP.replace(0, currentMP.length(), mp);

        label.append(" ");
        label.append(currentHP);
        label.append(" |");
        label.append(hpBar);
        label.append("|                         ");
        label.append(currentMP);
        label.append(" |");
        label.append(mpBar);
        label.append("|");

        return label.toString();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
