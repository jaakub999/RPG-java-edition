package FX;

import API.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

public class GameMasterController implements Initializable {
    public final Stage stage;
    public final Stage stage0;
    public PlayerContainer players;
    public Magic magic;
    public Map<String, Person> enemies;
    private final ObservableList<PlayerMasterTable> playerData;
    private ObservableList<PlayerMasterTable> items;

    @FXML private Button pot0minus;
    @FXML private Button pot1minus;
    @FXML private Button grminus;
    @FXML private Button pot0plus;
    @FXML private Button pot1plus;
    @FXML private Button grplus;
    @FXML private Button createButton;
    @FXML private Button pvpButton;
    @FXML private Button pvnpcButton;
    @FXML private Button hpButton;
    @FXML private Button mpButton;
    @FXML private Button diceButton;
    @FXML private Slider diceSlider;
    @FXML private Label diceLabel;
    @FXML private CheckBox selectAllBox;
    @FXML private TableView<PlayerMasterTable> table;
    @FXML private TableColumn<PlayerMasterTable, String> playerColumn;
    @FXML private TableColumn<PlayerMasterTable, String> classColumn;
    @FXML private TableColumn<PlayerMasterTable, String> buffColumn;
    @FXML private TableColumn<PlayerMasterTable, Integer> pot0Column;
    @FXML private TableColumn<PlayerMasterTable, Integer> pot1Column;
    @FXML private TableColumn<PlayerMasterTable, Integer> grColumn;
    @FXML private TableColumn<PlayerMasterTable, Integer> hpColumn;
    @FXML private TableColumn<PlayerMasterTable, Integer> mpColumn;
    @FXML private TableColumn<PlayerMasterTable, CheckBox> selectColumn;

    public GameMasterController(Stage stage, Stage stage0, PlayerContainer players, Magic magic, Map<String, Person> enemies) {
        this.stage = stage;
        this.stage0 = stage0;
        this.players = players;
        this.magic = magic;
        this.enemies = enemies;
        playerData = FXCollections.observableArrayList();
        items = FXCollections.observableArrayList();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/master.fxml"));
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
            playerData.add(new PlayerMasterTable(entry.getValue().getNickname(),
                                                 entry.getValue().getName(),
                                                 entry.getValue().getBuff(),
                                                 entry.getValue().getInventory().getItems().get("Mikstura zdrowia").getQuantity(),
                                                 entry.getValue().getInventory().getItems().get("Mikstura many").getQuantity(),
                                                 entry.getValue().getInventory().getItems().get("Granat").getQuantity(),
                                                 entry.getValue().getHP(),
                                                 entry.getValue().getMP()));

        Comparator<PlayerMasterTable> comparator = Comparator.comparing(PlayerMasterTable::getNickname);
        playerData.sort(comparator);

        playerColumn.setCellValueFactory(cellData -> cellData.getValue().nicknameProperty());
        classColumn.setCellValueFactory(cellData -> cellData.getValue().characterProperty());
        buffColumn.setCellValueFactory(cellData -> cellData.getValue().elementProperty());
        pot0Column.setCellValueFactory(cellData -> cellData.getValue().healthPotionProperty().asObject());
        pot1Column.setCellValueFactory(cellData -> cellData.getValue().manaPotionProperty().asObject());
        grColumn.setCellValueFactory(cellData -> cellData.getValue().grenadeProperty().asObject());
        hpColumn.setCellValueFactory(cellData -> cellData.getValue().currentHPProperty().asObject());
        mpColumn.setCellValueFactory(cellData -> cellData.getValue().currentMPProperty().asObject());
        selectColumn.setCellValueFactory(new PropertyValueFactory<PlayerMasterTable, CheckBox>("select"));

        playerColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        classColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        buffColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        pot0Column.setCellFactory(TextFieldTableCell.<PlayerMasterTable, Integer>forTableColumn(new IntegerStringConverter()));
        pot1Column.setCellFactory(TextFieldTableCell.<PlayerMasterTable, Integer>forTableColumn(new IntegerStringConverter()));
        grColumn.setCellFactory(TextFieldTableCell.<PlayerMasterTable, Integer>forTableColumn(new IntegerStringConverter()));
        hpColumn.setCellFactory(TextFieldTableCell.<PlayerMasterTable, Integer>forTableColumn(new IntegerStringConverter()));
        mpColumn.setCellFactory(TextFieldTableCell.<PlayerMasterTable, Integer>forTableColumn(new IntegerStringConverter()));

        table.setItems(playerData);

        selectAllBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                items = table.getItems();

                for (PlayerMasterTable item: items) {
                    item.getSelect().setSelected(selectAllBox.isSelected());
                }
            }
        });

        diceSlider.setMax(4);
        diceSlider.setMin(1);
        diceSlider.setBlockIncrement(1);

        diceSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                diceLabel.setText(String.valueOf(Math.round((Double) newValue)));
            }
        });

        pot0plus.setOnAction(event -> addItem("Mikstura zdrowia"));
        pot1plus.setOnAction(event -> addItem("Mikstura many"));
        grplus.setOnAction(event -> addItem("Granat"));
        pot0minus.setOnAction(event -> removeItem("Mikstura zdrowia"));
        pot1minus.setOnAction(event -> removeItem("Mikstura many"));
        grminus.setOnAction(event -> removeItem("Granat"));
        hpButton.setOnAction(event -> healPlayer());
        mpButton.setOnAction(event -> manaRestore());
        createButton.setOnAction(event -> {EnemyController controller = new EnemyController(stage, stage0, players, magic, enemies);});
        pvnpcButton.setOnAction(event -> {BattlePreparationController controller = new BattlePreparationController(stage, stage0, players, magic, enemies);});
        pvpButton.setOnAction(event -> {PvpPreparationController controller = new PvpPreparationController(stage, stage0, players, magic, enemies);});
        diceButton.setOnAction(event -> {MapController controller = new MapController(stage0, Integer.parseInt(diceLabel.getText()));});
    }

    private void addItem(String name) {
        ObservableList<PlayerMasterTable> dataListSelection = FXCollections.observableArrayList();
        int quantity;

        for (PlayerMasterTable player : playerData) {
            if (player.getSelect().isSelected())
                dataListSelection.add(player);
        }

        for (PlayerMasterTable player : dataListSelection) {
            quantity = players.getPlayers_list().get(player.getNickname()).getInventory().getItems().get(name).getQuantity();
            players.getPlayers_list().get(player.getNickname()).getInventory().getItems().get(name).setQuantity(++quantity);
        }

        playerData.removeAll(playerData);
        Set<Map.Entry<String, Player>> entrySet = players.getPlayers_list().entrySet();
        for (Map.Entry<String, Player> entry: entrySet)
            playerData.add(new PlayerMasterTable(entry.getValue().getNickname(),
                                                 entry.getValue().getName(),
                                                 entry.getValue().getBuff(),
                                                 entry.getValue().getInventory().getItems().get("Mikstura zdrowia").getQuantity(),
                                                 entry.getValue().getInventory().getItems().get("Mikstura many").getQuantity(),
                                                 entry.getValue().getInventory().getItems().get("Granat").getQuantity(),
                                                 entry.getValue().getHP(),
                                                 entry.getValue().getMP()));
    }

    private void removeItem(String name) {
        ObservableList<PlayerMasterTable> dataListSelection = FXCollections.observableArrayList();
        int quantity;

        for (PlayerMasterTable player : playerData) {
            if (player.getSelect().isSelected())
                dataListSelection.add(player);
        }

        for (PlayerMasterTable player : dataListSelection) {
            quantity = players.getPlayers_list().get(player.getNickname()).getInventory().getItems().get(name).getQuantity();

            if (quantity > 0)
                players.getPlayers_list().get(player.getNickname()).getInventory().getItems().get(name).setQuantity(--quantity);
        }

        playerData.removeAll(playerData);
        Set<Map.Entry<String, Player>> entrySet = players.getPlayers_list().entrySet();
        for (Map.Entry<String, Player> entry: entrySet)
            playerData.add(new PlayerMasterTable(entry.getValue().getNickname(),
                                                 entry.getValue().getName(),
                                                 entry.getValue().getBuff(),
                                                 entry.getValue().getInventory().getItems().get("Mikstura zdrowia").getQuantity(),
                                                 entry.getValue().getInventory().getItems().get("Mikstura many").getQuantity(),
                                                 entry.getValue().getInventory().getItems().get("Granat").getQuantity(),
                                                 entry.getValue().getHP(),
                                                 entry.getValue().getMP()));
    }

    private void healPlayer() {
        ObservableList<PlayerMasterTable> dataListSelection = FXCollections.observableArrayList();
        int quantity;

        for (PlayerMasterTable player : playerData) {
            if (player.getSelect().isSelected())
                dataListSelection.add(player);
        }

        for (PlayerMasterTable player : dataListSelection)
            players.getPlayers_list().get(player.getNickname()).heal(100000);

        playerData.removeAll(playerData);
        Set<Map.Entry<String, Player>> entrySet = players.getPlayers_list().entrySet();
        for (Map.Entry<String, Player> entry: entrySet)
            playerData.add(new PlayerMasterTable(entry.getValue().getNickname(),
                                                 entry.getValue().getName(),
                                                 entry.getValue().getBuff(),
                                                 entry.getValue().getInventory().getItems().get("Mikstura zdrowia").getQuantity(),
                                                 entry.getValue().getInventory().getItems().get("Mikstura many").getQuantity(),
                                                 entry.getValue().getInventory().getItems().get("Granat").getQuantity(),
                                                 entry.getValue().getHP(),
                                                 entry.getValue().getMP()));
    }

    private void manaRestore() {
        ObservableList<PlayerMasterTable> dataListSelection = FXCollections.observableArrayList();
        int quantity;

        for (PlayerMasterTable player : playerData) {
            if (player.getSelect().isSelected())
                dataListSelection.add(player);
        }

        for (PlayerMasterTable player : dataListSelection)
            players.getPlayers_list().get(player.getNickname()).manaRestore(100000);

        playerData.removeAll(playerData);
        Set<Map.Entry<String, Player>> entrySet = players.getPlayers_list().entrySet();
        for (Map.Entry<String, Player> entry: entrySet)
            playerData.add(new PlayerMasterTable(entry.getValue().getNickname(),
                                                 entry.getValue().getName(),
                                                 entry.getValue().getBuff(),
                                                 entry.getValue().getInventory().getItems().get("Mikstura zdrowia").getQuantity(),
                                                 entry.getValue().getInventory().getItems().get("Mikstura many").getQuantity(),
                                                 entry.getValue().getInventory().getItems().get("Granat").getQuantity(),
                                                 entry.getValue().getHP(),
                                                 entry.getValue().getMP()));
    }
}
