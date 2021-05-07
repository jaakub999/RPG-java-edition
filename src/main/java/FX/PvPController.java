package FX;

import API.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class PvPController implements Initializable {
    static int ITERATION;

    public final Stage stage;
    public final Stage stage0;
    public PlayerContainer players;
    public Map<String, Person> enemies;
    public Magic magic;
    public List<Player> team1;
    public List<Player> team2;
    private boolean turn;
    private int alive1;
    private int alive2;
    private final StringBuilder logBuilder;
    private final ObservableList<String> personData;
    private final ObservableList<BattleTable> skillData;

    @FXML private TextArea stats;
    @FXML private TextArea logs;
    @FXML private Text hero;
    @FXML private Text endingText;
    @FXML private TableView<BattleTable> table;
    @FXML private TableColumn<BattleTable, String> nameColumn;
    @FXML private TableColumn<BattleTable, Integer> secondColumn;
    @FXML private TableColumn<BattleTable, Integer> thirdColumn;
    @FXML private ListView<String> personList;
    @FXML private Button attackButton;
    @FXML private Button skillsButton;
    @FXML private Button itemsButton;
    @FXML private Button confirmButton;
    @FXML private Button cancelButton;
    @FXML private Button escapeButton;
    @FXML private Button continueButton;
    @FXML private Ellipse ellipse;

    public PvPController(Stage stage,
                         Stage stage0,
                         PlayerContainer players,
                         Map<String, Person> enemies,
                         Magic magic,
                         List<Player> team1,
                         List<Player> team2)
    {
        this.stage = stage;
        this.stage0 = stage0;
        this.players = players;
        this.enemies = enemies;
        this.magic = magic;
        this.team1 = team1;
        this.team2 = team2;
        personData = FXCollections.observableArrayList();
        skillData = FXCollections.observableArrayList();
        logBuilder = new StringBuilder();
        alive1 = team1.size();
        alive2 = team2.size();
        turn = alive1 >= alive2;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/battle.fxml"));
            loader.setController(this);
            stage.setScene(new Scene(loader.load()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setStats();

        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        secondColumn.setCellValueFactory(cellData -> cellData.getValue().xProperty().asObject());
        thirdColumn.setCellValueFactory(cellData -> cellData.getValue().yProperty().asObject());

        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        secondColumn.setCellFactory(TextFieldTableCell.<BattleTable, Integer>forTableColumn(new IntegerStringConverter()));
        thirdColumn.setCellFactory(TextFieldTableCell.<BattleTable, Integer>forTableColumn(new IntegerStringConverter()));

        attackButton.setOnAction(event -> attack());
        skillsButton.setOnAction(event -> spell());
        itemsButton.setOnAction(event -> item());
        cancelButton.setOnAction(event -> cancel());
        continueButton.setOnAction(event -> exit());
    }

    private void setStats() {
        StringBuilder sb = new StringBuilder();
        sb.append("Drużyna 1\n");
        sb.append("\n\t\t\t\t\t    HP\t\t\t\t\t\t\t\t\t\t\t\t     MP\n");

        for (Player player : team1) sb.append(player.getStats()).append("\n");

        sb.append("\nDrużyna 2\n");
        sb.append("\n\t\t\t\t\t    HP\t\t\t\t\t\t\t\t\t\t\t\t     MP\n");

        for (Player player : team2) sb.append(player.getStats()).append("\n");

        if (turn)
            hero.setText(team1.get(ITERATION).getNickname());

        else
            hero.setText(team2.get(ITERATION).getNickname());

        stats.setText(sb.toString());
    }

    private void attack() {
        personData.clear();

        if (turn)
            for (Player player : team2) personData.add(player.getNickname());

        else
            for (Player player : team1) personData.add(player.getNickname());

        personList.setItems(personData);
        personList.setVisible(true);
        confirmButton.setVisible(true);
        cancelButton.setVisible(true);

        confirmButton.setOnAction(event -> basic());
    }

    private void spell() {
        skillData.clear();

        if (turn) {
            Set<Map.Entry<String, Spell>> entrySet = team1.get(ITERATION).getMagic().getSpells_list().entrySet();
            for (Map.Entry<String, Spell> entry : entrySet)
                skillData.add(new BattleTable(entry.getKey(), entry.getValue().getDmg(), entry.getValue().getCost()));
        }

        else {
            Set<Map.Entry<String, Spell>> entrySet = team2.get(ITERATION).getMagic().getSpells_list().entrySet();
            for (Map.Entry<String, Spell> entry : entrySet)
                skillData.add(new BattleTable(entry.getKey(), entry.getValue().getDmg(), entry.getValue().getCost()));
        }

        table.setItems(skillData);
        table.setVisible(true);
        confirmButton.setVisible(true);
        cancelButton.setVisible(true);

        confirmButton.setOnAction(event -> chooseSpell());
    }

    private void item() {
        skillData.clear();

        if (turn) {
            Set<Map.Entry<String, Item>> entrySet = team1.get(ITERATION).getInventory().getItems().entrySet();
            for (Map.Entry<String, Item> entry : entrySet)
                skillData.add(new BattleTable(entry.getKey(), entry.getValue().getProp(), entry.getValue().getQuantity()));
        }

        else {
            Set<Map.Entry<String, Item>> entrySet = team2.get(ITERATION).getInventory().getItems().entrySet();
            for (Map.Entry<String, Item> entry : entrySet)
                skillData.add(new BattleTable(entry.getKey(), entry.getValue().getProp(), entry.getValue().getQuantity()));
        }

        table.setItems(skillData);
        table.setVisible(true);
        confirmButton.setVisible(true);
        cancelButton.setVisible(true);

        confirmButton.setOnAction(event -> chooseItem());
    }

    private void basic() {
        int index = personList.getSelectionModel().getSelectedIndex();
        int dodge;
        int dmg;
        String str;

        if (turn)
            dodge = team2.get(index).getDodgeChance();

        else
            dodge = team1.get(index).getDodgeChance();


        if (dodge >= 0 && dodge < 7) {
            dmg = 0;
            str = " (Unik)";
        }

        else {
            if (turn)
                dmg = team1.get(ITERATION).generateDamage();

            else
                dmg = team2.get(ITERATION).generateDamage();

            str = "";
        }

        if (turn) {
            team2.get(index).takeDamage(dmg);

            logBuilder.append(team1.get(ITERATION).getNickname());
            logBuilder.append(" : Atak Podstawowy : ");
            logBuilder.append(dmg);
            logBuilder.append(" DMG => ");
            logBuilder.append(team2.get(index).getNickname());
        }

        else {
            team1.get(index).takeDamage(dmg);

            logBuilder.append(team2.get(ITERATION).getNickname());
            logBuilder.append(" : Atak Podstawowy : ");
            logBuilder.append(dmg);
            logBuilder.append(" DMG => ");
            logBuilder.append(team1.get(index).getNickname());
        }

        logBuilder.append(str);
        logBuilder.append("\n");
        logs.setText(logBuilder.toString());
        logs.setScrollTop(Double.MAX_VALUE);

        isDead(index);
        goNext();
    }

    private void chooseSpell() {
        String choice = table.getSelectionModel().getSelectedItem().getName();
        Spell spell;

        if (turn)
            spell = team1.get(ITERATION).getMagic().getSpells_list().get(choice);

        else
            spell = team2.get(ITERATION).getMagic().getSpells_list().get(choice);

        if (spell.getType().equals(Type.BLACK))
            confirmButton.setOnAction(event -> blackSpell(spell));

        else if (spell.getType().equals(Type.WHITE))
            confirmButton.setOnAction(event -> whiteSpell(spell));

        else if (spell.getType().equals(Type.STUN))
            confirmButton.setOnAction(event -> stunSpell(spell));

        else if (spell.getType().equals(Type.BLOCK))
            confirmButton.setOnAction(event -> blockSpell(spell));

        else if (spell.getType().equals(Type.HP_STEAL))
            confirmButton.setOnAction(event -> hpStealSpell(spell));

        else if (spell.getType().equals(Type.MP_STEAL))
            confirmButton.setOnAction(event -> mpStealSpell(spell));
    }

    private void blackSpell(Spell spell) {
        List<Player> allies;
        List<Player> foes;

        if (turn) {
            allies = team1;
            foes = team2;
        }

        else {
            allies = team2;
            foes = team1;
        }

        if (spell.getCost() > allies.get(ITERATION).getMP()) {
            logBuilder.append(allies.get(ITERATION).getNickname());
            logBuilder.append(" posiada niewystarczającą ilość many, aby użyć umiejętności ");
            logBuilder.append(spell.getName());
            logBuilder.append("\n");
            logs.setText(logBuilder.toString());
            logs.setScrollTop(Double.MAX_VALUE);
            chooseSpell();
        }

        else {
            personData.clear();

            for (Player foe : foes) personData.add(foe.getNickname());

            personList.setItems(personData);
            personList.setVisible(true);

            if (Objects.equals(spell.getName(), "Natychmiastowe Zabójstwo"))
                confirmButton.setOnAction(event -> assassinSpell(spell));

            else
                confirmButton.setOnAction(event -> spellAttack(spell));
        }
    }

    private void whiteSpell(Spell spell) {
        List<Player> allies;

        if (turn)
            allies = team1;

        else
            allies = team2;

        if (spell.getCost() > allies.get(ITERATION).getMP()) {
            logBuilder.append(allies.get(ITERATION).getNickname());
            logBuilder.append(" posiada niewystarczającą ilość many, aby użyć umiejętności ");
            logBuilder.append(spell.getName());
            logBuilder.append("\n");
            logs.setText(logBuilder.toString());
            logs.setScrollTop(Double.MAX_VALUE);
            chooseSpell();
        }

        else {
            personData.clear();

            for (Player ally : allies) personData.add(ally.getNickname());

            personList.setItems(personData);
            personList.setVisible(true);

            confirmButton.setOnAction(event -> spellHeal(spell));
        }
    }

    private void stunSpell(Spell spell) {
        List<Player> allies;
        List<Player> foes;

        if (turn) {
            allies = team1;
            foes = team2;
        }

        else {
            allies = team2;
            foes = team1;
        }

        if (spell.getCost() > allies.get(ITERATION).getMP()) {
            logBuilder.append(allies.get(ITERATION).getNickname());
            logBuilder.append(" posiada niewystarczającą ilość many, aby użyć umiejętności ");
            logBuilder.append(spell.getName());
            logBuilder.append("\n");
            logs.setText(logBuilder.toString());
            logs.setScrollTop(Double.MAX_VALUE);
            chooseSpell();
        }

        else {
            personData.clear();

            for (Player foe : foes) personData.add(foe.getNickname());

            personList.setItems(personData);
            personList.setVisible(true);

            confirmButton.setOnAction(event -> stun(spell));
        }
    }

    private void blockSpell(Spell spell) {
        if (turn) {
            if (spell.getCost() > team1.get(ITERATION).getMP()) {
                logBuilder.append(team1.get(ITERATION).getNickname());
                logBuilder.append(" posiada niewystarczającą ilość many, aby użyć umiejętności ");
                logBuilder.append(spell.getName());
                logBuilder.append("\n");
                logs.setText(logBuilder.toString());
                logs.setScrollTop(Double.MAX_VALUE);
                chooseSpell();
            }

            else {
                team1.get(ITERATION).setBlock(true);

                logBuilder.append(team1.get(ITERATION).getNickname());
                logBuilder.append(" : ");
                logBuilder.append(spell.getName());
                logBuilder.append("\n");
                logs.setText(logBuilder.toString());
                logs.setScrollTop(Double.MAX_VALUE);

                goNext();
            }
        }

        else {
            if (spell.getCost() > team2.get(ITERATION).getMP()) {
                logBuilder.append(team2.get(ITERATION).getNickname());
                logBuilder.append(" posiada niewystarczającą ilość many, aby użyć umiejętności ");
                logBuilder.append(spell.getName());
                logBuilder.append("\n");
                logs.setText(logBuilder.toString());
                logs.setScrollTop(Double.MAX_VALUE);
                chooseSpell();
            }

            else {
                team2.get(ITERATION).setBlock(true);

                logBuilder.append(team2.get(ITERATION).getNickname());
                logBuilder.append(" : ");
                logBuilder.append(spell.getName());
                logBuilder.append("\n");
                logs.setText(logBuilder.toString());
                logs.setScrollTop(Double.MAX_VALUE);

                goNext();
            }
        }
    }

    private void hpStealSpell(Spell spell) {
        List<Player> allies;
        List<Player> foes;

        if (turn) {
            allies = team1;
            foes = team2;
        }

        else {
            allies = team2;
            foes = team1;
        }

        if (spell.getCost() > allies.get(ITERATION).getMP()) {
            logBuilder.append(allies.get(ITERATION).getNickname());
            logBuilder.append(" posiada niewystarczającą ilość many, aby użyć umiejętności ");
            logBuilder.append(spell.getName());
            logBuilder.append("\n");
            logs.setText(logBuilder.toString());
            logs.setScrollTop(Double.MAX_VALUE);
            chooseSpell();
        }

        else {
            personData.clear();

            for (Player foe : foes) personData.add(foe.getNickname());

            personList.setItems(personData);
            personList.setVisible(true);

            confirmButton.setOnAction(event -> stealHP(spell));
        }
    }

    private void mpStealSpell(Spell spell) {
        List<Player> allies;
        List<Player> foes;

        if (turn) {
            allies = team1;
            foes = team2;
        }

        else {
            allies = team2;
            foes = team1;
        }

        if (spell.getCost() > allies.get(ITERATION).getMP()) {
            logBuilder.append(allies.get(ITERATION).getNickname());
            logBuilder.append(" posiada niewystarczającą ilość many, aby użyć umiejętności ");
            logBuilder.append(spell.getName());
            logBuilder.append("\n");
            logs.setText(logBuilder.toString());
            logs.setScrollTop(Double.MAX_VALUE);
            chooseSpell();
        }

        else {
            personData.clear();

            for (Person foe : foes) personData.add(foe.getName());

            personList.setItems(personData);
            personList.setVisible(true);

            confirmButton.setOnAction(event -> stealMP(spell));
        }
    }

    private void spellAttack(Spell spell) {
        int index = personList.getSelectionModel().getSelectedIndex();
        int cost = spell.getCost();
        int dodge;
        int dmg;
        String str;

        if (turn)
            dodge = team2.get(index).getDodgeChance();

        else
            dodge = team1.get(index).getDodgeChance();

        if (dodge >= 0 && dodge < 5) {
            dmg = 0;
            str = " (Unik)";
        }

        else {
            if (turn) {
                if (Objects.equals(spell.getName(), "'Tanio skóry nie sprzedam'"))
                    dmg = (int) (0.15 * team1.get(ITERATION).getHP());

                else
                    dmg = spell.generateDamage() + team1.get(ITERATION).getPower();
            }

            else {
                if (Objects.equals(spell.getName(), "'Tanio skóry nie sprzedam'"))
                    dmg = (int) (0.15 * team2.get(ITERATION).getHP());

                else
                    dmg = spell.generateDamage() + team2.get(ITERATION).getPower();
            }

            str = "";
        }

        if (turn) {
            team2.get(index).takeDamage(dmg);
            team1.get(ITERATION).reduceMP(cost);

            logBuilder.append(team1.get(ITERATION).getNickname());
            logBuilder.append(" : ");
            logBuilder.append(spell.getName());
            logBuilder.append(" : ");
            logBuilder.append(dmg);
            logBuilder.append(" DMG => ");
            logBuilder.append(team2.get(index).getNickname());
        }

        else {
            team1.get(index).takeDamage(dmg);
            team2.get(ITERATION).reduceMP(cost);

            logBuilder.append(team2.get(ITERATION).getNickname());
            logBuilder.append(" : ");
            logBuilder.append(spell.getName());
            logBuilder.append(" : ");
            logBuilder.append(dmg);
            logBuilder.append(" DMG => ");
            logBuilder.append(team1.get(index).getNickname());
        }

        logBuilder.append(str);
        logBuilder.append("\n");
        logs.setText(logBuilder.toString());
        logs.setScrollTop(Double.MAX_VALUE);

        isDead(index);
        goNext();
    }

    private void spellHeal(Spell spell) {
        int index = personList.getSelectionModel().getSelectedIndex();

        if (turn) {
            if (team1.get(index).getHP() == team1.get(index).getMaxHP()) {
                logBuilder.append(team1.get(index).getNickname());
                logBuilder.append(" już ma pełne zdrowie\n");
                logs.setText(logBuilder.toString());
                logs.setScrollTop(Double.MAX_VALUE);
            }

            else {
                int prop = spell.getDmg() + team1.get(ITERATION).getPower();
                int cost = spell.getCost();
                int hpDiff = team1.get(index).getMaxHP() - team1.get(index).getHP();
                int currentHealing = Math.min(hpDiff, prop);

                team1.get(index).heal(prop);
                team1.get(ITERATION).reduceMP(cost);

                logBuilder.append(team1.get(ITERATION).getNickname());
                logBuilder.append(" : ");
                logBuilder.append(spell.getName());
                logBuilder.append(" : +");
                logBuilder.append(currentHealing);
                logBuilder.append(" HP => ");
                logBuilder.append(team1.get(index).getNickname());
                logBuilder.append("\n");
                logs.setText(logBuilder.toString());
                logs.setScrollTop(Double.MAX_VALUE);

                goNext();
            }
        }

        else {
            if (team2.get(index).getHP() == team2.get(index).getMaxHP()) {
                logBuilder.append(team2.get(index).getNickname());
                logBuilder.append(" już ma pełne zdrowie\n");
                logs.setText(logBuilder.toString());
                logs.setScrollTop(Double.MAX_VALUE);
            }

            else {
                int prop = spell.getDmg() + team2.get(ITERATION).getPower();
                int cost = spell.getCost();
                int hpDiff = team2.get(index).getMaxHP() - team2.get(index).getHP();
                int currentHealing = Math.min(hpDiff, prop);

                team2.get(index).heal(prop);
                team2.get(ITERATION).reduceMP(cost);

                logBuilder.append(team2.get(ITERATION).getNickname());
                logBuilder.append(" : ");
                logBuilder.append(spell.getName());
                logBuilder.append(" : +");
                logBuilder.append(currentHealing);
                logBuilder.append(" HP => ");
                logBuilder.append(team2.get(index).getNickname());
                logBuilder.append("\n");
                logs.setText(logBuilder.toString());
                logs.setScrollTop(Double.MAX_VALUE);

                goNext();
            }
        }
    }

    private void stun(Spell spell) {
        int index = personList.getSelectionModel().getSelectedIndex();
        int cost = spell.getCost();
        int dodge;
        String str;

        if (turn)
            dodge = team2.get(index).getDodgeChance();

        else
            dodge = team1.get(index).getDodgeChance();

        if (dodge >= 0 && dodge < 5)
            str = " (Unik)";

        else {
            if (turn)
                team2.get(index).setStun(true);

            else
                team1.get(index).setStun(true);

            str = "";
        }

        if (turn) {
            team1.get(ITERATION).reduceMP(cost);

            logBuilder.append(team1.get(ITERATION).getNickname());
            logBuilder.append(" : ");
            logBuilder.append(spell.getName());
            logBuilder.append(" => ");
            logBuilder.append(team2.get(index).getNickname());
            logBuilder.append(str);
            logBuilder.append("\n");
        }

        else {
            team2.get(ITERATION).reduceMP(cost);

            logBuilder.append(team2.get(ITERATION).getNickname());
            logBuilder.append(" : ");
            logBuilder.append(spell.getName());
            logBuilder.append(" => ");
            logBuilder.append(team1.get(index).getNickname());
            logBuilder.append(str);
            logBuilder.append("\n");
        }

        logs.setText(logBuilder.toString());
        logs.setScrollTop(Double.MAX_VALUE);

        goNext();
    }

    private void stealHP(Spell spell) {
        List<Player> allies;
        List<Player> foes;

        if (turn) {
            allies = team1;
            foes = team2;
        }

        else {
            allies = team2;
            foes = team1;
        }

        int index = personList.getSelectionModel().getSelectedIndex();
        int cost = spell.getCost();
        int dmg = spell.generateDamage() + allies.get(ITERATION).getPower();
        int heal = (int) (dmg * 0.7);

        foes.get(index).takeDamage(dmg);
        allies.get(ITERATION).reduceMP(cost);

        logBuilder.append(allies.get(ITERATION).getNickname());
        logBuilder.append(" : ");
        logBuilder.append(spell.getName());
        logBuilder.append(" : ");
        logBuilder.append(dmg);
        logBuilder.append(" DMG => ");
        logBuilder.append(foes.get(index).getNickname());
        logBuilder.append(" : ");

        personData.clear();

        for (Player ally : allies) personData.add(ally.getNickname());

        personList.setItems(personData);
        cancelButton.setVisible(false);

        confirmButton.setOnAction(event -> giveHP(heal));
    }

    private void stealMP(Spell spell) {
        List<Player> allies;
        List<Player> foes;

        if (turn) {
            allies = team1;
            foes = team2;
        }

        else {
            allies = team2;
            foes = team1;
        }

        int index = personList.getSelectionModel().getSelectedIndex();
        int cost = spell.getCost();
        int dmg = spell.generateDamage() + allies.get(ITERATION).getPower();
        int mana = foes.get(index).getMP();
        int stolen = Math.min(dmg, mana);

        foes.get(index).reduceMP(dmg);
        allies.get(ITERATION).reduceMP(cost);

        logBuilder.append(allies.get(ITERATION).getNickname());
        logBuilder.append(" : ");
        logBuilder.append(spell.getName());
        logBuilder.append(" : ");
        logBuilder.append(dmg);
        logBuilder.append(" => ");
        logBuilder.append(foes.get(index).getNickname());
        logBuilder.append(" : ");

        personData.clear();

        for (Player ally : allies) personData.add(ally.getNickname());

        personList.setItems(personData);
        cancelButton.setVisible(false);

        confirmButton.setOnAction(event -> giveMP(stolen));
    }

    private void giveHP(int heal) {
        int index = personList.getSelectionModel().getSelectedIndex();

        if (turn) {
            int hpDiff = team1.get(index).getMaxHP() - team1.get(index).getHP();
            int currentHealing = Math.min(hpDiff, heal);

            team1.get(index).heal(currentHealing);

            logBuilder.append(currentHealing);
            logBuilder.append(" HP => ");
            logBuilder.append(team1.get(index).getNickname());
        }

        else {
            int hpDiff = team2.get(index).getMaxHP() - team2.get(index).getHP();
            int currentHealing = Math.min(hpDiff, heal);

            team2.get(index).heal(currentHealing);

            logBuilder.append(currentHealing);
            logBuilder.append(" HP => ");
            logBuilder.append(team2.get(index).getNickname());
        }

        logBuilder.append("\n");
        logs.setText(logBuilder.toString());
        logs.setScrollTop(Double.MAX_VALUE);

        isDead(index);
        goNext();
    }

    private void giveMP(int mana) {
        int index = personList.getSelectionModel().getSelectedIndex();

        if (turn) {
            int mpDiff = team1.get(index).getMaxMP() - team1.get(index).getMP();
            int currentManaRestore = Math.min(mpDiff, mana);

            team1.get(index).manaRestore(currentManaRestore);

            logBuilder.append(currentManaRestore);
            logBuilder.append(" MP => ");
            logBuilder.append(team1.get(index).getNickname());
        }

        else {
            int mpDiff = team2.get(index).getMaxMP() - team2.get(index).getMP();
            int currentManaRestore = Math.min(mpDiff, mana);

            team2.get(index).manaRestore(currentManaRestore);

            logBuilder.append(currentManaRestore);
            logBuilder.append(" MP => ");
            logBuilder.append(team2.get(index).getNickname());
        }

        logBuilder.append("\n");
        logs.setText(logBuilder.toString());
        logs.setScrollTop(Double.MAX_VALUE);

        goNext();
    }

    private void assassinSpell(Spell spell) {
        Random rand = new Random();
        int chance = rand.nextInt(100);
        int index = personList.getSelectionModel().getSelectedIndex();
        int cost = spell.getCost();

        if (turn) {
            if (chance < (100 - ((team2.get(index).getHP() * 100) / team2.get(index).getMaxHP()))) {
                team2.get(index).takeDamage(spell.getDmg());
                logBuilder.append("Próba zabójstwa zakończona powodzeniem\n");
            }

            else {
                team1.get(ITERATION).takeDamage(team1.get(ITERATION).getHP() / 4);
                logBuilder.append("Próba zabójstwa zakończona niepowodzeniem\n");
            }

            team1.get(ITERATION).reduceMP(cost);
        }

        else {
            if (chance < (100 - ((team1.get(index).getHP() * 100) / team1.get(index).getMaxHP()))) {
                team1.get(index).takeDamage(spell.getDmg());
                logBuilder.append("Próba zabójstwa zakończona powodzeniem\n");
            }

            else {
                team2.get(ITERATION).takeDamage(team2.get(ITERATION).getHP() / 4);
                logBuilder.append("Próba zabójstwa zakończona niepowodzeniem\n");
            }

            team2.get(ITERATION).reduceMP(cost);
        }

        logs.setText(logBuilder.toString());
        logs.setScrollTop(Double.MAX_VALUE);

        isDead(index);
        goNext();
    }

    private void chooseItem() {
        String choice = table.getSelectionModel().getSelectedItem().getName();
        Item item;

        if (turn)
            item = team1.get(ITERATION).getInventory().getItems().get(choice);

        else
            item = team2.get(ITERATION).getInventory().getItems().get(choice);

        switch (item.getName()) {
            case "Mikstura zdrowia":
                confirmButton.setOnAction(event -> useHealthPotion(item));
                break;

            case "Mikstura many":
                confirmButton.setOnAction(event -> useManaPotion(item));
                break;

            case "Granat":
                confirmButton.setOnAction(event -> useGrenade(item));
                break;
        }
    }

    private void useHealthPotion(Item item) {
        if (turn) {
            boolean used = team1.get(ITERATION).getInventory().useItem(item);

            if (used) {
                team1.get(ITERATION).heal(item.getProp());

                logBuilder.append(team1.get(ITERATION).getNickname());
                logBuilder.append(" : ");
                logBuilder.append(item.getName());
                logBuilder.append("\n");
                logs.setText(logBuilder.toString());
                logs.setScrollTop(Double.MAX_VALUE);
                goNext();
            }

            else {
                logBuilder.append("Brak przedmiotu ");
                logBuilder.append(item.getName());
                logBuilder.append("\n");
                logs.setText(logBuilder.toString());
                logs.setScrollTop(Double.MAX_VALUE);
                chooseItem();
            }
        }

        else {
            boolean used = team2.get(ITERATION).getInventory().useItem(item);

            if (used) {
                team2.get(ITERATION).heal(item.getProp());

                logBuilder.append(team2.get(ITERATION).getNickname());
                logBuilder.append(" : ");
                logBuilder.append(item.getName());
                logBuilder.append("\n");
                logs.setText(logBuilder.toString());
                logs.setScrollTop(Double.MAX_VALUE);
                goNext();
            }

            else {
                logBuilder.append("Brak przedmiotu ");
                logBuilder.append(item.getName());
                logBuilder.append("\n");
                logs.setText(logBuilder.toString());
                logs.setScrollTop(Double.MAX_VALUE);
                chooseItem();
            }
        }
    }

    private void useManaPotion(Item item) {
        if (turn) {
            boolean used = team1.get(ITERATION).getInventory().useItem(item);

            if (used) {
                team1.get(ITERATION).manaRestore(item.getProp());

                logBuilder.append(team1.get(ITERATION).getNickname());
                logBuilder.append(" : ");
                logBuilder.append(item.getName());
                logBuilder.append("\n");
                logs.setText(logBuilder.toString());
                logs.setScrollTop(Double.MAX_VALUE);
                goNext();
            }

            else {
                logBuilder.append("Brak przedmiotu ");
                logBuilder.append(item.getName());
                logBuilder.append("\n");
                logs.setText(logBuilder.toString());
                logs.setScrollTop(Double.MAX_VALUE);
                chooseItem();
            }
        }

        else {
            boolean used = team2.get(ITERATION).getInventory().useItem(item);

            if (used) {
                team2.get(ITERATION).manaRestore(item.getProp());

                logBuilder.append(team2.get(ITERATION).getNickname());
                logBuilder.append(" : ");
                logBuilder.append(item.getName());
                logBuilder.append("\n");
                logs.setText(logBuilder.toString());
                logs.setScrollTop(Double.MAX_VALUE);
                goNext();
            }

            else {
                logBuilder.append("Brak przedmiotu ");
                logBuilder.append(item.getName());
                logBuilder.append("\n");
                logs.setText(logBuilder.toString());
                logs.setScrollTop(Double.MAX_VALUE);
                chooseItem();
            }
        }
    }

    private void useGrenade(Item item) {
        List<Player> allies;
        List<Player> foes;

        if (turn) {
            allies = team1;
            foes = team2;
        }

        else {
            allies = team2;
            foes = team1;
        }

        boolean used = allies.get(ITERATION).getInventory().useItem(item);

        if (used) {
            personData.clear();

            for (Player foe : foes) personData.add(foe.getNickname());

            personList.setItems(personData);
            personList.setVisible(true);

            confirmButton.setOnAction(event -> throwGrenade(item));
            cancelButton.setVisible(false);
        }

        else {
            logBuilder.append("Brak przedmiotu ");
            logBuilder.append(item.getName());
            logBuilder.append("\n");
            logs.setText(logBuilder.toString());
            logs.setScrollTop(Double.MAX_VALUE);
            chooseItem();
        }
    }

    private void throwGrenade(Item item) {
        int index = personList.getSelectionModel().getSelectedIndex();

        if (turn) {
            team2.get(index).takeDamage(item.getProp());

            logBuilder.append(team1.get(ITERATION).getNickname());
            logBuilder.append(" : ");
            logBuilder.append(item.getName());
            logBuilder.append(" => ");
            logBuilder.append(team2.get(index).getNickname());
            logBuilder.append("\n");
            logs.setText(logBuilder.toString());
            logs.setScrollTop(Double.MAX_VALUE);
        }

        isDead(index);
        goNext();
    }

    private void isDead(int index) {
        if (turn) {
            if (team2.get(index).getHP() == 0) {
                logBuilder.append(team2.get(index).getName());
                logBuilder.append(" został(a) pokonany(a)\n");
                logs.setText(logBuilder.toString());
                logs.setScrollTop(Double.MAX_VALUE);

                team2.remove(index);
                alive2--;

                if (alive2 == 0) {
                    stats.setDisable(true);
                    logs.setDisable(true);
                    attackButton.setVisible(false);
                    skillsButton.setVisible(false);
                    itemsButton.setVisible(false);
                    escapeButton.setVisible(false);
                    confirmButton.setVisible(false);
                    cancelButton.setVisible(false);
                    table.setVisible(false);
                    personList.setVisible(false);
                    hero.setVisible(false);
                    continueButton.setVisible(true);
                    ellipse.setVisible(true);
                    endingText.setVisible(true);
                    endingText.setText("Drużyna 1 wygrywa");
                }
            }
        }

        else {
            if (team1.get(index).getHP() == 0) {
                logBuilder.append(team1.get(index).getName());
                logBuilder.append(" został(a) pokonany(a)\n");
                logs.setText(logBuilder.toString());
                logs.setScrollTop(Double.MAX_VALUE);

                team1.remove(index);
                alive1--;

                if (alive1 == 0) {
                    stats.setDisable(true);
                    logs.setDisable(true);
                    attackButton.setVisible(false);
                    skillsButton.setVisible(false);
                    itemsButton.setVisible(false);
                    escapeButton.setVisible(false);
                    confirmButton.setVisible(false);
                    cancelButton.setVisible(false);
                    table.setVisible(false);
                    personList.setVisible(false);
                    hero.setVisible(false);
                    continueButton.setVisible(true);
                    ellipse.setVisible(true);
                    endingText.setVisible(true);
                    endingText.setText("Drużyna 2");
                }
            }
        }
    }

    private void goNext() {
        if (turn) {
            ITERATION++;

            if (ITERATION == alive1) {
                ITERATION = 0;
                turn = false;
            }

            setStats();
            cancel();

            if (team1.get(ITERATION).isStun()) {
                team1.get(ITERATION).setStun(false);
                logBuilder.append(team1.get(ITERATION).getNickname());
                logBuilder.append(" - ogłuszony(a) (brak akcji)\n");
                logs.setText(logBuilder.toString());
                logs.setScrollTop(Double.MAX_VALUE);
            }
        }

        else {
            ITERATION++;

            if (ITERATION == alive2) {
                ITERATION = 0;
                turn = true;
            }

            setStats();
            cancel();

            if (team2.get(ITERATION).isStun()) {
                team2.get(ITERATION).setStun(false);
                logBuilder.append(team2.get(ITERATION).getNickname());
                logBuilder.append(" - ogłuszony(a) (brak akcji)\n");
                logs.setText(logBuilder.toString());
                logs.setScrollTop(Double.MAX_VALUE);
            }
        }
    }

    private void cancel() {
        confirmButton.setVisible(false);
        cancelButton.setVisible(false);
        personList.setVisible(false);
        table.setVisible(false);
        confirmButton.setOnAction(event -> {});
    }

    private void exit() {
        GameMasterController controller = new GameMasterController(stage0, stage, players, magic, enemies);
        MapController map = new MapController(stage);
    }
}