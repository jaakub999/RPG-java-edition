package FX;

import API.*;
import com.github.javafaker.Faker;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class InitPlayersController implements Initializable {
    public final Stage stage;
    public PlayerContainer players;
    private final ObservableList<PlayerTable> playerData;
    private final SpinnerValueFactory<Buff> buffValue;
    private final SpinnerValueFactory<String> classValue;

    @FXML private TextField nick;
    @FXML private Button addButton;
    @FXML private Button resetButton;
    @FXML private Button startButton;
    @FXML private Button removeButton;
    @FXML private Button generateButton;
    @FXML private Label pot0Label;
    @FXML private Label pot1Label;
    @FXML private Label grLabel;
    @FXML private Label errorLabel;
    @FXML private Slider pot0Slider;
    @FXML private Slider pot1Slider;
    @FXML private Slider grSlider;
    @FXML private Spinner<String> classSpinner;
    @FXML private Spinner<Buff> buffSpinner;
    @FXML private TableView<PlayerTable> table;
    @FXML private TableColumn<PlayerTable, String> player_column;
    @FXML private TableColumn<PlayerTable, String> class_column;
    @FXML private TableColumn<PlayerTable, String> buff_column;
    @FXML private TableColumn<PlayerTable, Integer> pot0_column;
    @FXML private TableColumn<PlayerTable, Integer> pot1_column;
    @FXML private TableColumn<PlayerTable, Integer> gr_column;

    public InitPlayersController(Stage stage, PlayerContainer players) {
        this.stage = stage;
        this.players = players;
        playerData = FXCollections.observableArrayList();

        ObservableList<Buff> buffs = FXCollections.observableArrayList(Buff.WATER, Buff.FIRE, Buff.SOIL, Buff.WIND);
        buffValue = new SpinnerValueFactory.ListSpinnerValueFactory<Buff>(buffs);
        buffValue.setValue(buffs.get(0));

        ObservableList<String> classes = FXCollections.observableArrayList("Wojownik", "Mag", "Skrytobójca", "Paladyn", "Osiłek", "Pięściarz", "Łotrzyk");
        classValue = new SpinnerValueFactory.ListSpinnerValueFactory<String>(classes);
        classValue.setValue(classes.get(0));

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/players.fxml"));
            loader.setController(this);
            stage.setScene(new Scene(loader.load()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Set<Map.Entry<String, Player>> entrySet = players.getPlayers_list().entrySet();
        for (Map.Entry<String, Player> entry: entrySet)
            playerData.add(new PlayerTable(entry.getValue().getNickname(),
                                           entry.getValue().getName(),
                                           entry.getValue().getBuff(),
                                           entry.getValue().getInventory().getItems().get("Mikstura zdrowia").getQuantity(),
                                           entry.getValue().getInventory().getItems().get("Mikstura many").getQuantity(),
                                           entry.getValue().getInventory().getItems().get("Granat").getQuantity()));

        Comparator<PlayerTable> comparator = Comparator.comparing(PlayerTable::getNickname);
        playerData.sort(comparator);

        player_column.setCellValueFactory(cellData -> cellData.getValue().nicknameProperty());
        class_column.setCellValueFactory(cellData -> cellData.getValue().characterProperty());
        buff_column.setCellValueFactory(cellData -> cellData.getValue().elementProperty());
        pot0_column.setCellValueFactory(cellData -> cellData.getValue().healthPotionProperty().asObject());
        pot1_column.setCellValueFactory(cellData -> cellData.getValue().manaPotionProperty().asObject());
        gr_column.setCellValueFactory(cellData -> cellData.getValue().grenadeProperty().asObject());

        player_column.setCellFactory(TextFieldTableCell.forTableColumn());
        class_column.setCellFactory(TextFieldTableCell.forTableColumn());
        buff_column.setCellFactory(TextFieldTableCell.forTableColumn());
        pot0_column.setCellFactory(TextFieldTableCell.<PlayerTable, Integer>forTableColumn(new IntegerStringConverter()));
        pot1_column.setCellFactory(TextFieldTableCell.<PlayerTable, Integer>forTableColumn(new IntegerStringConverter()));
        gr_column.setCellFactory(TextFieldTableCell.<PlayerTable, Integer>forTableColumn(new IntegerStringConverter()));

        table.setItems(playerData);

        classSpinner.setValueFactory(classValue);
        buffSpinner.setValueFactory(buffValue);

        pot0Slider.setMax(10);
        pot0Slider.setMin(0);
        pot0Slider.setBlockIncrement(1);
        pot1Slider.setMax(10);
        pot1Slider.setMin(0);
        pot1Slider.setBlockIncrement(1);
        grSlider.setMax(10);
        grSlider.setMin(0);
        grSlider.setBlockIncrement(1);

        nick.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.intValue() > oldValue.intValue())
                    if (nick.getText().length() >= 9)
                        nick.setText(nick.getText().substring(0, 9));
            }
        });

        pot0Slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                pot0Label.setText(String.valueOf(Math.round((Double) newValue)));
            }
        });

        pot1Slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                pot1Label.setText(String.valueOf(Math.round((Double) newValue)));
            }
        });

        grSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                grLabel.setText(String.valueOf(Math.round((Double) newValue)));
            }
        });

        addButton.setOnAction(event -> addData());
        removeButton.setOnAction(event -> removeData());
        resetButton.setOnAction(event -> reset());
        generateButton.setOnAction(event -> generateNickname());
        startButton.setOnAction(event -> openNextLayout());
    }

    private void addData() {
        String nickname = nick.getText();

        if (nickname.equals(""))
            errorLabel.setText("Podaj nick gracza");

        else {
            String cls = classSpinner.getValue();
            Buff buff = buffSpinner.getValue();
            Player player;
            Magic magic;
            Spell[] spells;

            Item hpPotion = new Item("Mikstura zdrowia", 150, Integer.parseInt(pot0Label.getText()), "Przywraca 150 HP");
            Item mpPotion = new Item("Mikstura many", 50, Integer.parseInt(pot1Label.getText()), "Przywraca 50 MP");
            Item grenade = new Item("Granat", 300, Integer.parseInt(grLabel.getText()), "Zadaje 300 DMG");

            Inventory inventory = new Inventory();
            inventory.addItem(hpPotion);
            inventory.addItem(mpPotion);
            inventory.addItem(grenade);

            switch (cls) {
                case "Wojownik":
                    Spell powerHit = new Spell("Potężne uderzenie", 30, 180, Type.BLACK);
                    Spell smash = new Spell("Miazga", 40, 220, Type.BLACK);
                    Spell block = new Spell("Garda", 20, 0, Type.BLOCK);
                    spells = new Spell[]{powerHit, smash, block};

                    magic = new Magic();
                    magic.addSpell(spells);

                    player = new Player(nickname, cls, buff, 3900, 250, 45, 45, magic, inventory);
                    break;

                case "Mag":
                    Spell ignite = new Spell("Podpalenie", 30, 110, Type.BLACK);
                    Spell meteor = new Spell("Meteor", 40, 220, Type.BLACK);
                    Spell smite = new Spell("Porażenie", 35, 190, Type.BLACK);
                    Spell thunder = new Spell("Błyskawica", 45, 280, Type.BLACK);
                    Spell ball = new Spell("Kula żywiołów", 50, 340, Type.BLACK);
                    spells = new Spell[]{ignite, meteor, smite, thunder, ball};

                    magic = new Magic();
                    magic.addSpell(spells);

                    player = new Player(nickname, cls, buff, 2800, 400, 15, 90, magic, inventory);
                    break;

                case "Skrytobójca":
                    Spell smite0 = new Spell("Porażenie", 35, 190, Type.BLACK);
                    Spell tempest = new Spell("Nawałnica stali", 70, 500, Type.BLACK);
                    Spell penetration = new Spell("Penetracja", 85, 650, Type.BLACK);
                    Spell assassination = new Spell("Natychmiastowe Zabójstwo", 250, 99999999, Type.BLACK);
                    spells = new Spell[]{smite0, tempest, penetration, assassination};

                    magic = new Magic();
                    magic.addSpell(spells);

                    player = new Player(nickname, cls, buff, 2550, 350, 50, 55, magic, inventory);
                    break;

                case "Paladyn":
                    Spell powerHit0 = new Spell("Potężne uderzenie", 30, 180, Type.BLACK);
                    Spell ignite0 = new Spell("Podpalenie", 30, 110, Type.BLACK);
                    Spell meteor0 = new Spell("Meteor", 40, 220, Type.BLACK);
                    Spell thunder0 = new Spell("Błyskawica", 45, 280, Type.BLACK);
                    Spell cure = new Spell("Uleczenie", 55, 300, Type.WHITE);
                    Spell heal = new Spell("Uzdrowienie", 205, 99999999, Type.WHITE);
                    spells = new Spell[]{powerHit0, ignite0, meteor0, thunder0, cure, heal};

                    magic = new Magic();
                    magic.addSpell(spells);

                    player = new Player(nickname, cls, buff, 3100, 300, 30, 35, magic, inventory);
                    break;

                case "Osiłek":
                    Spell shout = new Spell("'Tanio skóry nie sprzedam'", 220, 0, Type.BLACK);
                    Spell cure0 = new Spell("Uleczenie", 55, 300, Type.WHITE);
                    Spell block0 = new Spell("Garda", 20, 0, Type.BLOCK);
                    spells = new Spell[]{shout, cure0, block0};

                    magic = new Magic();
                    magic.addSpell(spells);

                    player = new Player(nickname, cls, buff, 4300, 300, 30, 0, magic, inventory);
                    break;

                case "Pięściarz":
                    Spell powerHit1 = new Spell("Potężne uderzenie", 30, 180, Type.BLACK);
                    Spell carnage = new Spell("Rzeź", 50, 250, Type.BLACK);
                    Spell fisting = new Spell("Kamienne Pięści", 40, 200, Type.BLACK);
                    Spell block1 = new Spell("Garda", 20, 0, Type.BLOCK);
                    spells = new Spell[]{powerHit1, carnage, fisting, block1};

                    magic = new Magic();
                    magic.addSpell(spells);

                    player = new Player(nickname, cls, buff, 3550, 150, 80, 5, magic, inventory);
                    break;

                case "Łotrzyk":
                    Spell ignite1 = new Spell("Podpalenie", 30, 110, Type.BLACK);
                    Spell HP_steal = new Spell("Kradzież życia", 50, 250, Type.HP_STEAL);
                    Spell MP_steal = new Spell("Kradzież many", 25, 50, Type.MP_STEAL);
                    Spell stun = new Spell("Ogłuszenie", 120, 0, Type.STUN);
                    spells = new Spell[]{ignite1, HP_steal, MP_steal, stun};

                    magic = new Magic();
                    magic.addSpell(spells);

                    player = new Player(nickname, cls, buff, 2600, 190, 40, 20, magic, inventory);
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + cls);
            }

            players.addPlayer(player);
            table.getItems().add(new PlayerTable(player.getNickname(),
                                                 player.getName(),
                                                 player.getBuff(),
                                                 player.getInventory().getItems().get("Mikstura zdrowia").getQuantity(),
                                                 player.getInventory().getItems().get("Mikstura many").getQuantity(),
                                                 player.getInventory().getItems().get("Granat").getQuantity()));

            nick.setText(null);
            errorLabel.setText(null);
        }
    }

    private void removeData() {
        PlayerTable data = table.getSelectionModel().getSelectedItem();
        errorLabel.setText(null);

        if (data != null) {
            players.removePlayer(data.getNickname());
            table.getItems().remove(data);
        }
    }

    private void reset() {
        players.getPlayers_list().clear();
        playerData.clear();
        table.setItems(playerData);
        errorLabel.setText(null);
    }

    private void generateNickname() {
        Faker faker = new Faker();
        String nickname;

        do {
            nickname = faker.superhero().name();

        } while (nickname.length() > 9);

        nick.setText(nickname);
    }

    private void openNextLayout() {
        if (players.getPlayers_list().size() < 1)
            errorLabel.setText("Musisz dodać przynajmniej jednego gracza, aby móc kontynuować!");

        else {
            Spell powerHit = new Spell("Potężne uderzenie", 30, 180, Type.BLACK);
            Spell smash = new Spell("Miazga", 40, 220, Type.BLACK);
            Spell ignite = new Spell("Podpalenie", 30, 110, Type.BLACK);
            Spell meteor = new Spell("Meteor", 40, 220, Type.BLACK);
            Spell smite = new Spell("Porażenie", 35, 190, Type.BLACK);
            Spell thunder = new Spell("Błyskawica", 45, 280, Type.BLACK);
            Spell ball = new Spell("Kula żywiołów", 50, 340, Type.BLACK);
            Spell tempest = new Spell("Nawałnica stali", 70, 500, Type.BLACK);
            Spell penetration = new Spell("Penetracja", 85, 650, Type.BLACK);
            Spell carnage = new Spell("Rzeź", 50, 250, Type.BLACK);
            Spell fisting = new Spell("Kamienne Pięści", 40, 200, Type.BLACK);
            Spell squall = new Spell("Wichura", 60, 370, Type.BLACK);
            Spell breaker = new Spell("Łamanie kości", 95, 795, Type.BLACK);
            Spell cure = new Spell("Uleczenie", 55, 300, Type.WHITE);
            Spell heal = new Spell("Uzdrowienie", 205, 99999999, Type.WHITE);
            Spell stun = new Spell("Ogłuszenie", 120, 0, Type.STUN);
            Spell requiem = new Spell("Rekwiem", 200, 500, Type.BLACK);

            Spell[] impMagicArray = new Spell[]{ignite};
            Spell[] goblinMagicArray = new Spell[]{ignite, smite};
            Spell[] orcMagicArray = new Spell[]{powerHit, smash};
            Spell[] knightMagicArray = new Spell[]{powerHit, smash, carnage, stun};
            Spell[] gladiatorMagicArray = new Spell[]{breaker, powerHit};
            Spell[] magicArray = new Spell[]{powerHit, smash, ignite, meteor, smite, thunder,
                                             ball, tempest, penetration, carnage, fisting, squall,
                                             breaker, cure, heal, stun, requiem};

            Magic impMagic = new Magic();
            Magic goblinMagic = new Magic();
            Magic orcMagic = new Magic();
            Magic knightMagic = new Magic();
            Magic gladiatorMagic = new Magic();
            Magic magic = new Magic();

            impMagic.addSpell(impMagicArray);
            goblinMagic.addSpell(goblinMagicArray);
            orcMagic.addSpell(orcMagicArray);
            knightMagic.addSpell(knightMagicArray);
            gladiatorMagic.addSpell(gladiatorMagicArray);
            magic.addSpell(magicArray);

            Person imp = new Person("Imp", Buff.NULL, 2000, 100, 55, 0, impMagic);
            Person goblin = new Person("Goblin", Buff.NULL, 2300, 100, 70, 10, goblinMagic);
            Person orc = new Person("Ork", Buff.NULL, 3500, 200, 100, 20, orcMagic);
            Person knight = new Person("Rycerz", Buff.NULL, 4000, 300, 150, 40, knightMagic);
            Person gladiator = new Person("Gladiator", Buff.NULL, 4000, 210, 200, 0, gladiatorMagic);

            Map<String, Person> enemies = new HashMap<String, Person>();
            enemies.put(imp.getName(), imp);
            enemies.put(goblin.getName(), goblin);
            enemies.put(orc.getName(), orc);
            enemies.put(knight.getName(), knight);
            enemies.put(gladiator.getName(), gladiator);

            Stage stage0 = new Stage();
            stage0.setTitle("RPG (game master panel)");

            GameMasterController masterController = new GameMasterController(stage0, stage, players, magic, enemies);
            MapController mapController = new MapController(stage);
            masterController.stage.show();
        }
    }
}