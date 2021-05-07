package FX;

import API.PlayerContainer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    public final Stage stage;

    @FXML private Button startButton;
    @FXML private Button classButton;
    @FXML private Button elementButton;
    @FXML private Button itemButton;
    @FXML private Button enemyButton;

    public MenuController(Stage stage) {
        this.stage = stage;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/menu.fxml"));
            loader.setController(this);
            stage.setScene(new Scene(loader.load()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startButton.setOnAction(event -> start());
        classButton.setOnAction(event -> {ClassesController controller = new ClassesController(stage);});
        elementButton.setOnAction(event -> {
            String description = "\nŻYWIOŁY:\n" +
                                 "\n- ZIEMIA:\n" +
                                 "\tZdrowie: +350\n" +
                                 "\tRegeneracja many: -20%\n" +
                                 "\n- WODA:\n" +
                                 "\t+10% bardziej efektywne leczenie\n" +
                                 "\tMana: +100\n" +
                                 "\n- OGIEŃ:\n" +
                                 "\tAtak: +25\n" +
                                 "\tMoc umiejętności: +10%\n" +
                                 "\n- WIATR:\n" +
                                 "\tRegeneracja many: +10\n" +
                                 "\tSzansa na unik: +15%";

            DescriptionController controller = new DescriptionController(stage, description, 1);
        });

        itemButton.setOnAction(event -> {
            String description = "\nPRZEDMIOTY:\n" +
                                 "\n- MIKSTURA ZDROWIA:\n" +
                                 "\tPrzywraca 150 HP\n" +
                                 "\n- MIKSTURA MANY:\n" +
                                 "\tPrzywraca 50 MP\n" +
                                 "\n- GRANAT:\n" +
                                 "\tZadaje 300 DMG";

            DescriptionController controller = new DescriptionController(stage, description, 1);
        });

        enemyButton.setOnAction(event -> {
            String description = "\nW trakcie rozgrywki Mistrz Gry może sam\n" +
                                 "tworzyć przeciwników dla graczy.\n" +
                                 "\nDomyślnie utworzeni są tacy przeciwnicy jak:" +
                                 "\n- Ork\n- Goblin\n- Rycerz\n- Gladiator\n- Imp";

            DescriptionController controller = new DescriptionController(stage, description, 1);
        });
    }

    private void start() {
        stage.close();
        Stage stage1 = new Stage();
        stage1.setTitle("RPG");
        PlayerContainer players = new PlayerContainer();
        InitPlayersController controller = new InitPlayersController(stage1, players);
        controller.stage.show();
    }
}
