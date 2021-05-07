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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

public class EnemyController implements Initializable {
    public final Stage stage;
    public final Stage stage0;
    public PlayerContainer players;
    public Magic magic;
    public Map<String, Person> enemies;
    private final ObservableList<SimpleTable> spellData;
    private final ObservableList<EnemyTable> enemyData;
    private ObservableList<SimpleTable> items;

    @FXML private TableView<SimpleTable> spellTable;
    @FXML private TableView<EnemyTable> enemyTable;
    @FXML private TableColumn<SimpleTable, String> spellColumn;
    @FXML private TableColumn<SimpleTable, CheckBox> selectColumn;
    @FXML private TableColumn<EnemyTable, String> nameColumn;
    @FXML private TableColumn<EnemyTable, Integer> hpColumn;
    @FXML private TableColumn<EnemyTable, Integer> mpColumn;
    @FXML private TableColumn<EnemyTable, Integer> adColumn;
    @FXML private TableColumn<EnemyTable, Integer> apColumn;
    @FXML private TextField name;
    @FXML private TextField hp;
    @FXML private TextField mp;
    @FXML private TextField ad;
    @FXML private TextField ap;
    @FXML private Button addButton;
    @FXML private Button removeButton;
    @FXML private Button backButton;
    @FXML private Button generateButton;
    @FXML private CheckBox selectAllBox;
    @FXML private Label errorLabel;

    public EnemyController(Stage stage, Stage stage0, PlayerContainer players, Magic magic, Map<String, Person> enemies) {
        this.stage = stage;
        this.stage0 = stage0;
        this.players = players;
        this.magic = magic;
        this.enemies = enemies;
        spellData = FXCollections.observableArrayList();
        enemyData = FXCollections.observableArrayList();
        items = FXCollections.observableArrayList();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/enemy.fxml"));
            loader.setController(this);
            stage.setScene(new Scene(loader.load()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Set<Map.Entry<String, Spell>> entrySet = magic.getSpells_list().entrySet();
        for (Map.Entry<String, Spell> entry: entrySet)
            spellData.add(new SimpleTable(entry.getKey()));

        Set<Map.Entry<String, Person>> entrySet1 = enemies.entrySet();
        for (Map.Entry<String, Person> entry: entrySet1) {
            enemyData.add(new EnemyTable(entry.getKey(),
                                         entry.getValue().getMaxHP(),
                                         entry.getValue().getMaxMP(),
                                         entry.getValue().getDmg(),
                                         entry.getValue().getPower()));
            }

        spellColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        spellColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        selectColumn.setCellValueFactory(new PropertyValueFactory<SimpleTable, CheckBox>("select"));

        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        hpColumn.setCellValueFactory(cellData -> cellData.getValue().hpProperty().asObject());
        mpColumn.setCellValueFactory(cellData -> cellData.getValue().mpProperty().asObject());
        adColumn.setCellValueFactory(cellData -> cellData.getValue().adProperty().asObject());
        apColumn.setCellValueFactory(cellData -> cellData.getValue().apProperty().asObject());

        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        hpColumn.setCellFactory(TextFieldTableCell.<EnemyTable, Integer>forTableColumn(new IntegerStringConverter()));
        mpColumn.setCellFactory(TextFieldTableCell.<EnemyTable, Integer>forTableColumn(new IntegerStringConverter()));
        adColumn.setCellFactory(TextFieldTableCell.<EnemyTable, Integer>forTableColumn(new IntegerStringConverter()));
        apColumn.setCellFactory(TextFieldTableCell.<EnemyTable, Integer>forTableColumn(new IntegerStringConverter()));

        spellTable.setItems(spellData);
        enemyTable.setItems(enemyData);

        spellTable.setRowFactory(tv -> new TableRow<SimpleTable>() {
            private final Tooltip tooltip = new Tooltip();

            @Override
            public void updateItem(SimpleTable spell, boolean empty) {
                super.updateItem(spell, empty);

                if (spell == null)
                    setTooltip(null);

                else {
                    tooltip.setText("Siła: " + magic.getSpells_list().get(spell.getName()).getDmg() +
                                    "\nKoszt many: " + magic.getSpells_list().get(spell.getName()).getCost());
                    setTooltip(tooltip);
                }
            }
        });

        name.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.intValue() > oldValue.intValue())
                    if (name.getText().length() >= 10)
                        name.setText(name.getText().substring(0, 9));
            }
        });

        hp.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*"))
                    hp.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        hp.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.intValue() > oldValue.intValue())
                    if (hp.getText().length() >= 6)
                        hp.setText(hp.getText().substring(0, 6));
            }
        });

        mp.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*"))
                    mp.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        mp.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.intValue() > oldValue.intValue())
                    if (hp.getText().length() >= 6)
                        hp.setText(hp.getText().substring(0, 6));
            }
        });

        ad.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*"))
                    ad.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        ad.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.intValue() > oldValue.intValue())
                    if (ad.getText().length() >= 6)
                        ad.setText(ad.getText().substring(0, 6));
            }
        });

        ap.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*"))
                    ap.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        ap.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.intValue() > oldValue.intValue())
                    if (ap.getText().length() >= 6)
                        ap.setText(ap.getText().substring(0, 6));
            }
        });

        selectAllBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                items = spellTable.getItems();

                for (SimpleTable item: items) {
                    item.getSelect().setSelected(selectAllBox.isSelected());
                }
            }
        });

        hp.setTooltip(new Tooltip("> 0"));
        ad.setTooltip(new Tooltip("> 15"));

        addButton.setOnAction(event -> addEnemy());
        removeButton.setOnAction(event -> removeEnemy());
        generateButton.setOnAction(event -> generateName());
        backButton.setOnAction(event -> {GameMasterController controller = new GameMasterController(stage, stage0, players, magic, enemies);});
    }

    private void generateName() {
        Faker faker = new Faker();
        String string;

        do {
            string = faker.superhero().name();

        } while (string.length() > 9);

        name.setText(string);
    }

    private void addEnemy() {
        if (name.getText().equals("") || hp.getText().equals("") || mp.getText().equals("") || ad.getText().equals("") || ap.getText().equals(""))
            errorLabel.setText("Żadne pole nie może być puste");

        else if (Integer.parseInt(hp.getText()) == 0)
            errorLabel.setText("Ilość HP nie może być równa 0");

        else if (Integer.parseInt(ad.getText()) <= 15)
            errorLabel.setText("Siła ataku nie może być mniejsza niż 15");

        else {
            ObservableList<SimpleTable> dataListSelection = FXCollections.observableArrayList();

            for (SimpleTable spell : spellData) {
                if (spell.getSelect().isSelected())
                    dataListSelection.add(spell);
            }

            int N = dataListSelection.size();
            Spell[] data = new Spell[N];

            for (int i = 0; i < N; i++)
                data[i] = magic.getSpells_list().get(dataListSelection.get(i).getName());

            Magic magic = new Magic();
            magic.addSpell(data);

            Person enemy = new Person(name.getText(),
                                      Buff.NULL,
                                      Integer.parseInt(hp.getText()),
                                      Integer.parseInt(mp.getText()),
                                      Integer.parseInt(ad.getText()),
                                      Integer.parseInt(ap.getText()),
                                      magic);
            enemies.put(enemy.getName(), enemy);
            enemyTable.getItems().add(new EnemyTable(enemy.getName(),
                                                     enemy.getMaxHP(),
                                                     enemy.getMaxMP(),
                                                     enemy.getDmg(),
                                                     enemy.getPower()));

            errorLabel.setText(null);
            name.setText(null);
            hp.setText("0");
            mp.setText("0");
            ad.setText("0");
            ap.setText("0");
        }
    }

    private void removeEnemy() {
        EnemyTable data = enemyTable.getSelectionModel().getSelectedItem();
        errorLabel.setText(null);

        if (data != null) {
            enemies.remove(data.getName());
            enemyTable.getItems().remove(data);
        }
    }
}