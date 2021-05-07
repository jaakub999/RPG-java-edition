package FX;

import API.Magic;
import API.Person;
import API.Player;
import API.PlayerContainer;
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

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class PvpPreparationController implements Initializable {
    public final Stage stage;
    public final Stage stage0;
    public PlayerContainer players;
    public Magic magic;
    public Map<String, Person> enemies;
    private final ObservableList<SimpleTable> data1;
    private final ObservableList<SimpleTable> data2;
    private ObservableList<SimpleTable> playerItems1;
    private ObservableList<SimpleTable> playerItems2;

    @FXML private AnchorPane mainPane;
    @FXML private TableView<SimpleTable> team1;
    @FXML private TableView<SimpleTable> team2;
    @FXML private TableColumn<SimpleTable, String> teamColumn1;
    @FXML private TableColumn<SimpleTable, String> teamColumn2;
    @FXML private TableColumn<SimpleTable, CheckBox> selectPlayerColumn1;
    @FXML private TableColumn<SimpleTable, CheckBox> selectPlayerColumn2;
    @FXML private CheckBox selectAllPlayers1;
    @FXML private CheckBox selectAllPlayers2;
    @FXML private Button startButton;
    @FXML private Button backButton;
    @FXML private Label errorLabel;
    @FXML private Text text;

    public PvpPreparationController(Stage stage, Stage stage0, PlayerContainer players, Magic magic, Map<String, Person> enemies) {
        this.stage = stage;
        this.stage0 = stage0;
        this.players = players;
        this.magic = magic;
        this.enemies = enemies;
        data1 = FXCollections.observableArrayList();
        data2 = FXCollections.observableArrayList();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/pvp_prep.fxml"));
            loader.setController(this);
            stage.setScene(new Scene(loader.load()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Set<Map.Entry<String, Player>> entrySet = players.getPlayers_list().entrySet();
        for (Map.Entry<String, Player> entry: entrySet) {
            data1.add(new SimpleTable(entry.getKey()));
            data2.add(new SimpleTable(entry.getKey()));
        }

        teamColumn1.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        teamColumn2.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        teamColumn1.setCellFactory(TextFieldTableCell.forTableColumn());
        teamColumn2.setCellFactory(TextFieldTableCell.forTableColumn());
        selectPlayerColumn1.setCellValueFactory(new PropertyValueFactory<SimpleTable, CheckBox>("select"));
        selectPlayerColumn2.setCellValueFactory(new PropertyValueFactory<SimpleTable, CheckBox>("select"));

        team1.setItems(data1);
        team2.setItems(data2);

        selectAllPlayers1.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                playerItems1 = team1.getItems();

                for (SimpleTable item: playerItems1) {
                    item.getSelect().setSelected(selectAllPlayers1.isSelected());
                }
            }
        });

        selectAllPlayers2.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                playerItems2 = team2.getItems();

                for (SimpleTable item: playerItems2) {
                    item.getSelect().setSelected(selectAllPlayers2.isSelected());
                }
            }
        });

        startButton.setOnAction(event -> start());
        backButton.setOnAction(event -> {GameMasterController controller = new GameMasterController(stage, stage0, players, magic, enemies);});
    }

    private void start() {
        PvPController controller;
        List<Player> t1 = new ArrayList<Player>();
        List<Player> t2 = new ArrayList<Player>();

        for (SimpleTable player : data1) {
            if (player.getSelect().isSelected())
                t1.add(players.getPlayers_list().get(player.getName()));
        }

        for (SimpleTable player : data2) {
            if (player.getSelect().isSelected())
                t2.add(players.getPlayers_list().get(player.getName()));
        }

        if (!t1.isEmpty() && !t2.isEmpty()) {
            boolean check = true;

            for (Player player : t1) {
                for (Player value : t2) {
                    if (player == value) {
                        check = false;
                        break;
                    }
                }
            }

            if (check) {
                PvPController.ITERATION = 0;
                controller = new PvPController(stage0, stage, players, enemies, magic, t1, t2);
                text.setVisible(true);
                mainPane.setDisable(true);
            }

            else
                errorLabel.setText("Jeden gracz nie może przynależeć do dwóch drużyn!");
        }

        else
            errorLabel.setText("Wybierz przynajmniej po jednym graczu na drużynę!");
    }
}
