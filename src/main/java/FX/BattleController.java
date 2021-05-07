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

public class BattleController implements Initializable {
    static int ITERATION;

    public final Stage stage;
    public final Stage stage0;
    public PlayerContainer players;
    public Map<String, Person> enemies;
    public Magic magic;
    public List<Player> allies;
    public List<Person> foes;
    public int escape;
    private int alive;
    private int enemiesAlive;
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

    public BattleController(Stage stage,
                            Stage stage0,
                            PlayerContainer players,
                            Map<String, Person> enemies,
                            Magic magic,
                            List<Player> allies,
                            List<Person> foes,
                            int escape)
    {
        this.stage = stage;
        this.stage0 = stage0;
        this.players = players;
        this.enemies = enemies;
        this.magic = magic;
        this.allies = allies;
        this.foes = foes;
        this.escape = escape;
        personData = FXCollections.observableArrayList();
        skillData = FXCollections.observableArrayList();
        logBuilder = new StringBuilder();
        alive = allies.size();
        enemiesAlive = foes.size();

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

        escapeButton.setVisible(true);

        attackButton.setOnAction(event -> attack());
        skillsButton.setOnAction(event -> spell());
        itemsButton.setOnAction(event -> item());
        cancelButton.setOnAction(event -> cancel());
        escapeButton.setOnAction(event -> tryToEscape());
        continueButton.setOnAction(event -> exit());
    }

    private void setStats() {
        StringBuilder sb = new StringBuilder();
        hero.setText(allies.get(ITERATION).getNickname());

        for (Person foe : foes) sb.append(foe.getStats()).append("\n");

        sb.append("\n\t\t\t\t\t    HP\t\t\t\t\t\t\t\t\t\t\t\t     MP\n");

        for (Player ally : allies) sb.append(ally.getStats()).append("\n");

        stats.setText(sb.toString());
    }

    private void attack() {
        personData.clear();

        for (Person foe : foes) personData.add(foe.getName());

        personList.setItems(personData);
        personList.setVisible(true);
        confirmButton.setVisible(true);
        cancelButton.setVisible(true);

        confirmButton.setOnAction(event -> basic());
    }

    private void spell() {
        skillData.clear();

        Set<Map.Entry<String, Spell>> entrySet = allies.get(ITERATION).getMagic().getSpells_list().entrySet();
        for (Map.Entry<String, Spell> entry: entrySet)
            skillData.add(new BattleTable(entry.getKey(), entry.getValue().getDmg(), entry.getValue().getCost()));

        table.setItems(skillData);
        table.setVisible(true);
        confirmButton.setVisible(true);
        cancelButton.setVisible(true);

        confirmButton.setOnAction(event -> chooseSpell());
    }

    private void item() {
        skillData.clear();

        Set<Map.Entry<String, Item>> entrySet = allies.get(ITERATION).getInventory().getItems().entrySet();
        for (Map.Entry<String, Item> entry: entrySet)
            skillData.add(new BattleTable(entry.getKey(), entry.getValue().getProp(), entry.getValue().getQuantity()));

        table.setItems(skillData);
        table.setVisible(true);
        confirmButton.setVisible(true);
        cancelButton.setVisible(true);

        confirmButton.setOnAction(event -> chooseItem());
    }

    private void basic() {
        int index = personList.getSelectionModel().getSelectedIndex();
        int dodge = foes.get(index).getDodgeChance();
        int dmg;
        String str;

        if (dodge >= 0 && dodge < 7) {
            dmg = 0;
            str = " (Unik)";
        }

        else {
            dmg = allies.get(ITERATION).generateDamage();
            str = "";
        }

        foes.get(index).takeDamage(dmg);

        logBuilder.append(allies.get(ITERATION).getNickname());
        logBuilder.append(" : Atak Podstawowy : ");
        logBuilder.append(dmg);
        logBuilder.append(" DMG => ");
        logBuilder.append(foes.get(index).getName());
        logBuilder.append(str);
        logBuilder.append("\n");
        logs.setText(logBuilder.toString());
        logs.setScrollTop(Double.MAX_VALUE);

        isFoeDead(index);
        goNext();
    }

    private void chooseSpell() {
        String choice = table.getSelectionModel().getSelectedItem().getName();
        Spell spell = allies.get(ITERATION).getMagic().getSpells_list().get(choice);

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

            if (Objects.equals(spell.getName(), "Natychmiastowe Zabójstwo"))
                confirmButton.setOnAction(event -> assassinSpell(spell));

            else
                confirmButton.setOnAction(event -> spellAttack(spell));
        }
    }

    private void whiteSpell(Spell spell) {
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

            confirmButton.setOnAction(event -> stun(spell));
        }
    }

    private void blockSpell(Spell spell) {
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
            allies.get(ITERATION).setBlock(true);

            logBuilder.append(allies.get(ITERATION).getNickname());
            logBuilder.append(" : ");
            logBuilder.append(spell.getName());
            logBuilder.append("\n");
            logs.setText(logBuilder.toString());
            logs.setScrollTop(Double.MAX_VALUE);

            goNext();
        }
    }

    private void hpStealSpell(Spell spell) {
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

            confirmButton.setOnAction(event -> stealHP(spell));
        }
    }

    private void mpStealSpell(Spell spell) {
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
        int dodge = foes.get(index).getDodgeChance();
        int dmg;
        String str;

        if (dodge >= 0 && dodge < 5) {
            dmg = 0;
            str = " (Unik)";
        }

        else {
            if (Objects.equals(spell.getName(), "'Tanio skóry nie sprzedam'"))
                dmg = (int) (0.15 * allies.get(ITERATION).getHP());

            else
                dmg = spell.generateDamage() + allies.get(ITERATION).getPower();

            str = "";
        }

        foes.get(index).takeDamage(dmg);
        allies.get(ITERATION).reduceMP(cost);

        logBuilder.append(allies.get(ITERATION).getNickname());
        logBuilder.append(" : ");
        logBuilder.append(spell.getName());
        logBuilder.append(" : ");
        logBuilder.append(dmg);
        logBuilder.append(" DMG => ");
        logBuilder.append(foes.get(index).getName());
        logBuilder.append(str);
        logBuilder.append("\n");
        logs.setText(logBuilder.toString());
        logs.setScrollTop(Double.MAX_VALUE);

        isFoeDead(index);
        goNext();
    }

    private void spellHeal(Spell spell) {
        int index = personList.getSelectionModel().getSelectedIndex();

        if (allies.get(index).getHP() == allies.get(index).getMaxHP()) {
            logBuilder.append(allies.get(index).getNickname());
            logBuilder.append(" już ma pełne zdrowie\n");
            logs.setText(logBuilder.toString());
            logs.setScrollTop(Double.MAX_VALUE);
        }

        else {
            int prop = spell.getDmg() + allies.get(ITERATION).getPower();
            int cost = spell.getCost();
            int hpDiff = allies.get(index).getMaxHP() - allies.get(index).getHP();
            int currentHealing = Math.min(hpDiff, prop);

            allies.get(index).heal(prop);
            allies.get(ITERATION).reduceMP(cost);

            logBuilder.append(allies.get(ITERATION).getNickname());
            logBuilder.append(" : ");
            logBuilder.append(spell.getName());
            logBuilder.append(" : +");
            logBuilder.append(currentHealing);
            logBuilder.append(" HP => ");
            logBuilder.append(allies.get(index).getNickname());
            logBuilder.append("\n");
            logs.setText(logBuilder.toString());
            logs.setScrollTop(Double.MAX_VALUE);

            goNext();
        }
    }

    private void stun(Spell spell) {
        int index = personList.getSelectionModel().getSelectedIndex();
        int cost = spell.getCost();
        int dodge = foes.get(index).getDodgeChance();
        String str;

        if (dodge >= 0 && dodge < 5)
            str = " (Unik)";

        else {
            foes.get(index).setStun(true);
            str = "";
        }

        allies.get(ITERATION).reduceMP(cost);

        logBuilder.append(allies.get(ITERATION).getNickname());
        logBuilder.append(" : ");
        logBuilder.append(spell.getName());
        logBuilder.append(" => ");
        logBuilder.append(foes.get(index).getName());
        logBuilder.append(str);
        logBuilder.append("\n");
        logs.setText(logBuilder.toString());
        logs.setScrollTop(Double.MAX_VALUE);

        goNext();
    }

    private void stealHP(Spell spell) {
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
        logBuilder.append(foes.get(index).getName());
        logBuilder.append(" : ");

        personData.clear();

        for (Player ally : allies) personData.add(ally.getNickname());

        personList.setItems(personData);
        cancelButton.setVisible(false);

        confirmButton.setOnAction(event -> giveHP(heal));
    }

    private void stealMP(Spell spell) {
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
        logBuilder.append(foes.get(index).getName());
        logBuilder.append(" : ");

        personData.clear();

        for (Player ally : allies) personData.add(ally.getNickname());

        personList.setItems(personData);
        cancelButton.setVisible(false);

        confirmButton.setOnAction(event -> giveMP(stolen));
    }

    private void giveHP(int heal) {
        int index = personList.getSelectionModel().getSelectedIndex();
        int hpDiff = allies.get(index).getMaxHP() - allies.get(index).getHP();
        int currentHealing = Math.min(hpDiff, heal);

        allies.get(index).heal(currentHealing);

        logBuilder.append(currentHealing);
        logBuilder.append(" HP => ");
        logBuilder.append(allies.get(index).getNickname());
        logBuilder.append("\n");
        logs.setText(logBuilder.toString());
        logs.setScrollTop(Double.MAX_VALUE);

        isFoeDead(index);
        goNext();
    }

    private void giveMP(int mana) {
        int index = personList.getSelectionModel().getSelectedIndex();
        int mpDiff = allies.get(index).getMaxMP() - allies.get(index).getMP();
        int currentManaRestore = Math.min(mpDiff, mana);

        allies.get(index).manaRestore(currentManaRestore);

        logBuilder.append(currentManaRestore);
        logBuilder.append(" MP => ");
        logBuilder.append(allies.get(index).getNickname());
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

        if (chance < (100 - ((foes.get(index).getHP() * 100) / foes.get(index).getMaxHP()))) {
            foes.get(index).takeDamage(spell.getDmg());
            logBuilder.append("Próba zabójstwa zakończona powodzeniem\n");
        }

        else {
            allies.get(ITERATION).takeDamage(allies.get(ITERATION).getHP() / 4);
            logBuilder.append("Próba zabójstwa zakończona niepowodzeniem\n");
        }

        allies.get(ITERATION).reduceMP(cost);
        logs.setText(logBuilder.toString());
        logs.setScrollTop(Double.MAX_VALUE);

        isFoeDead(index);
        goNext();
    }

    private void chooseItem() {
        String choice = table.getSelectionModel().getSelectedItem().getName();
        Item item = allies.get(ITERATION).getInventory().getItems().get(choice);

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
        boolean used = allies.get(ITERATION).getInventory().useItem(item);

        if (used) {
            allies.get(ITERATION).heal(item.getProp());

            logBuilder.append(allies.get(ITERATION).getNickname());
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

    private void useManaPotion(Item item) {
        boolean used = allies.get(ITERATION).getInventory().useItem(item);

        if (used) {
            allies.get(ITERATION).manaRestore(item.getProp());

            logBuilder.append(allies.get(ITERATION).getNickname());
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

    private void useGrenade(Item item) {
        boolean used = allies.get(ITERATION).getInventory().useItem(item);

        if (used) {
            personData.clear();

            for (Person foe : foes) personData.add(foe.getName());

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
        foes.get(index).takeDamage(item.getProp());

        logBuilder.append(allies.get(ITERATION).getNickname());
        logBuilder.append(" : ");
        logBuilder.append(item.getName());
        logBuilder.append(" => ");
        logBuilder.append(foes.get(index).getName());
        logBuilder.append("\n");
        logs.setText(logBuilder.toString());
        logs.setScrollTop(Double.MAX_VALUE);

        isFoeDead(index);
        goNext();
    }

    private void enemyTurn() {
        for (int i = 0; i < enemiesAlive; i++) {
            if (foes.get(i).isStun()) {
                foes.get(i).setStun(false);
                logBuilder.append(foes.get(i).getName());
                logBuilder.append(" - ogłuszony(a) (brak akcji)\n");
            }

            else {
                while (true) {
                    Random rand = new Random();
                    int choice = rand.nextInt(2);
                    int target = rand.nextInt(alive);
                    int dodge = allies.get(target).getDodgeChance();
                    int dmg;
                    String str;

                    if (choice == 0) {
                        if (dodge >= 0 && dodge < 7) {
                            dmg = 0;
                            str = " (Unik)";
                        }

                        else {
                            dmg = foes.get(i).generateDamage();
                            str = "";
                        }

                        allies.get(target).takeDamage(dmg);

                        logBuilder.append(foes.get(i).getName());
                        logBuilder.append(" : Atak Podstawowy : ");
                        logBuilder.append(dmg);
                        logBuilder.append(" DMG => ");
                        logBuilder.append(allies.get(target).getNickname());
                        logBuilder.append(str);
                        logBuilder.append("\n");

                        isAllyDead(target);
                    }

                    else {
                        Spell spell = foes.get(i).chooseSpell();

                        if (spell.getCost() > foes.get(i).getMP() || (spell.getType().equals(Type.WHITE) && foes.get(i).getHP() > foes.get(i).getMaxHP() / 1.5))
                            continue;

                        foes.get(i).reduceMP(spell.getCost());

                        if (spell.getType().equals(Type.WHITE)) {
                            dmg = spell.getDmg() + foes.get(i).getPower();
                            foes.get(i).heal(dmg);

                            logBuilder.append(foes.get(i).getName());
                            logBuilder.append(" +");
                            logBuilder.append(dmg);
                            logBuilder.append(" HP\n");
                        }

                        else if (spell.getType().equals(Type.BLACK)) {
                            if (Objects.equals(spell.getName(), "Rekwiem")) {
                                int currentlyAlive = alive;

                                for (int j = 0; j < currentlyAlive; j++) {
                                    allies.get(j).takeDamage(spell.getDmg());
                                    logBuilder.append(foes.get(i).getName());
                                    logBuilder.append(" : ");
                                    logBuilder.append(spell.getName());
                                    logBuilder.append("\n");
                                    isAllyDead(j);
                                }
                            }

                            else {
                                if (dodge >= 0 && dodge < 5) {
                                    dmg = 0;
                                    str = " (Unik)";
                                }

                                else {
                                    dmg = spell.generateDamage();
                                    str = "";
                                }

                                allies.get(target).takeDamage(dmg);

                                logBuilder.append(foes.get(i).getName());
                                logBuilder.append(" : ");
                                logBuilder.append(spell.getName());
                                logBuilder.append(" : ");
                                logBuilder.append(dmg);
                                logBuilder.append(" DMG => ");
                                logBuilder.append(allies.get(target).getNickname());
                                logBuilder.append(str);
                                logBuilder.append("\n");

                                isAllyDead(target);
                            }
                        }

                        else if (spell.getType().equals(Type.STUN)) {
                            if (dodge >= 0 && dodge < 5)
                                str = " (Unik)";

                            else {
                                allies.get(target).setStun(true);
                                str = "";
                            }

                            logBuilder.append(foes.get(i).getName());
                            logBuilder.append(" : ");
                            logBuilder.append(spell.getName());
                            logBuilder.append(" => ");
                            logBuilder.append(allies.get(target).getNickname());
                            logBuilder.append(str);
                            logBuilder.append("\n");
                        }
                    }

                    break;
                }
            }

            if (alive == 0)
                break;
        }

        logs.setText(logBuilder.toString());
        logs.setScrollTop(Double.MAX_VALUE);

        if (alive != 0) {
            for (int i = 0; i < alive; i++) {
                allies.get(i).setBlock(false);
                allies.get(i).heal(10);
                allies.get(i).manaRestore(5);
            }
        }

        if (enemiesAlive != 0) {
            for (int i = 0; i < enemiesAlive; i++) {
                foes.get(i).heal(15);
                foes.get(i).manaRestore(10);
            }
        }
    }

    private void isFoeDead(int index) {
        if (foes.get(index).getHP() == 0) {
            logBuilder.append(foes.get(index).getName());
            logBuilder.append(" został(a) pokonany(a)\n");
            logs.setText(logBuilder.toString());
            logs.setScrollTop(Double.MAX_VALUE);

            foes.remove(index);
            enemiesAlive--;

            if (enemiesAlive == 0) {
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
                endingText.setText("Zwycięstwo");
            }
        }
    }

    private void isAllyDead(int index) {
        if (allies.get(index).getHP() == 0) {
            logBuilder.append(allies.get(index).getNickname());
            logBuilder.append(" został(a) pokonany(a)\n");

            allies.remove(index);
            alive--;

            if (alive == 0) {
                logs.setText(logBuilder.toString());
                logs.setScrollTop(Double.MAX_VALUE);
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
                endingText.setText("Porażka");
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

    private void tryToEscape() {
        Random rand = new Random();
        int chance = rand.nextInt(100);

        if (chance < escape) {
            stats.setDisable(true);
            logs.setDisable(true);
            attackButton.setVisible(false);
            skillsButton.setVisible(false);
            itemsButton.setVisible(false);
            escapeButton.setVisible(false);
            hero.setVisible(false);
            continueButton.setVisible(true);
            ellipse.setVisible(true);
            endingText.setVisible(true);
            endingText.setText("Ucieczka");
        }

        else {
            escapeButton.setDisable(true);
            logBuilder.append("Ucieczka zakończona niepowodzeniem\n");
            logs.setText(logBuilder.toString());
            logs.setScrollTop(Double.MAX_VALUE);
            goNext();
        }
    }

    private void goNext() {
        ITERATION++;

        if (ITERATION == alive) {
            ITERATION = 0;
            enemyTurn();
        }

        setStats();
        cancel();

        if (allies.get(ITERATION).isStun()) {
            allies.get(ITERATION).setStun(false);
            logBuilder.append(allies.get(ITERATION).getNickname());
            logBuilder.append(" - ogłuszony(a) (brak akcji)\n");
            logs.setText(logBuilder.toString());
            logs.setScrollTop(Double.MAX_VALUE);
        }
    }

    private void exit() {
        GameMasterController controller = new GameMasterController(stage0, stage, players, magic, enemies);
        MapController map = new MapController(stage);
    }
}