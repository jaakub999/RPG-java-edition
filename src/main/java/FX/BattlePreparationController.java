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
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class BattlePreparationController implements Initializable {
    public final Stage stage;
    public final Stage stage0;
    public PlayerContainer players;
    public Magic magic;
    public Map<String, Person> enemies;
    private final ObservableList<SimpleTable> playerData;
    private final ObservableList<ExtendedTable> enemyData;
    private ObservableList<SimpleTable> playerItems;
    private ObservableList<ExtendedTable> enemyItems;

    @FXML private AnchorPane mainPane;
    @FXML private TableView<SimpleTable> playerTable;
    @FXML private TableColumn<SimpleTable, String> playerColumn;
    @FXML private TableColumn<SimpleTable, CheckBox> selectPlayerColumn;
    @FXML private TableView<ExtendedTable> enemyTable;
    @FXML private TableColumn<ExtendedTable, String> enemyColumn;
    @FXML private TableColumn<ExtendedTable, CheckBox> selectEnemyColumn;
    @FXML private TableColumn<ExtendedTable, Integer> quantityColumn;
    @FXML private CheckBox selectAllPlayers;
    @FXML private CheckBox selectAllEnemies;
    @FXML private Button startButton;
    @FXML private Button backButton;
    @FXML private Slider chanceSlider;
    @FXML private Label chanceLabel;
    @FXML private Label errorLabel;
    @FXML private Text text;

    public BattlePreparationController(Stage stage, Stage stage0, PlayerContainer players, Magic magic, Map<String, Person> enemies) {
        this.stage = stage;
        this.stage0 = stage0;
        this.players = players;
        this.magic = magic;
        this.enemies = enemies;
        playerData = FXCollections.observableArrayList();
        enemyData = FXCollections.observableArrayList();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/preparation.fxml"));
            loader.setController(this);
            stage.setScene(new Scene(loader.load()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Set<Map.Entry<String, Player>> entrySet0 = players.getPlayers_list().entrySet();
        Set<Map.Entry<String, Person>> entrySet1 = enemies.entrySet();

        for (Map.Entry<String, Player> entry: entrySet0)
            playerData.add(new SimpleTable(entry.getKey()));

        for (Map.Entry<String, Person> entry: entrySet1)
            enemyData.add(new ExtendedTable(entry.getKey(), 1));

        playerColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        enemyColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());

        playerColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        enemyColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        quantityColumn.setCellFactory(TextFieldTableCell.<ExtendedTable, Integer>forTableColumn(new IntegerStringConverter()));

        selectPlayerColumn.setCellValueFactory(new PropertyValueFactory<SimpleTable, CheckBox>("select"));
        selectEnemyColumn.setCellValueFactory(new PropertyValueFactory<ExtendedTable, CheckBox>("select"));

        playerTable.setItems(playerData);
        enemyTable.setItems(enemyData);

        selectAllPlayers.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                playerItems = playerTable.getItems();

                for (SimpleTable item: playerItems) {
                    item.getSelect().setSelected(selectAllPlayers.isSelected());
                }
            }
        });

        selectAllEnemies.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                enemyItems = enemyTable.getItems();

                for (SimpleTable item: enemyItems) {
                    item.getSelect().setSelected(selectAllEnemies.isSelected());
                }
            }
        });

        chanceSlider.setMax(100);
        chanceSlider.setMin(0);
        chanceSlider.setBlockIncrement(1);

        chanceSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                chanceLabel.setText(String.valueOf(Math.round((Double) newValue)) + "%");
            }
        });

        startButton.setOnAction(event -> start());
        backButton.setOnAction(event -> {GameMasterController controller = new GameMasterController(stage, stage0, players, magic, enemies);});
    }

    private void start() {
        BattleController controller;
        List<Player> allies = new ArrayList<Player>();
        List<Person> foes = new ArrayList<Person>();

        for (SimpleTable player : playerData) {
            if (player.getSelect().isSelected())
                allies.add(players.getPlayers_list().get(player.getName()));
        }

        for (ExtendedTable enemy : enemyData) {
            if (enemy.getSelect().isSelected() && enemy.getQuantity() > 0) {
                for (int i = 0; i < enemy.getQuantity(); i++) {
                    Person foe = new Person(enemies.get(enemy.getName()).getName(),
                                            enemies.get(enemy.getName()).getBuff(),
                                            enemies.get(enemy.getName()).getMaxHP(),
                                            enemies.get(enemy.getName()).getMaxMP(),
                                            enemies.get(enemy.getName()).getDmg(),
                                            enemies.get(enemy.getName()).getPower(),
                                            enemies.get(enemy.getName()).getMagic());
                    foes.add(foe);
                }
            }
        }

        if (!allies.isEmpty() || !foes.isEmpty()) {
            int escape = (int) chanceSlider.getValue();
            BattleController.ITERATION = 0;
            controller = new BattleController(stage0, stage, players, enemies, magic, allies, foes, escape);

            text.setVisible(true);
            mainPane.setDisable(true);
        }

        else
            errorLabel.setText("Wybierz przynajmniej po jednym graczu i przeciwniku!");
    }
}
