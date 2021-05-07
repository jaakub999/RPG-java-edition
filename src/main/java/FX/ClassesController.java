package FX;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ClassesController implements Initializable {
    public final Stage stage;

    @FXML private Button warriorButton;
    @FXML private Button mageButton;
    @FXML private Button backButton;
    @FXML private Button assassinButton;
    @FXML private Button paladinButton;
    @FXML private Button tankButton;
    @FXML private Button fighterButton;
    @FXML private Button rogueButton;

    public ClassesController(Stage stage) {
        this.stage = stage;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/classes.fxml"));
            loader.setController(this);
            stage.setScene(new Scene(loader.load()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        warriorButton.setOnAction(event -> {
            String description = "\nKLASA:\t\t\t\t\tWojownik\n" +
                                 "ZDROWIE:\t\t\t\t3900\n" +
                                 "ATAK:\t\t\t\t\t45\n" +
                                 "MOC UMIEJĘTNOŚCI:\t\t45\n\n" +
                                 "OPIS:\n" +
                                 "Wojownik charakteryzuje się wysoką wytrzymałością\noraz wysokimi obrażeniami.\nPrzystosowany jest do dłuższych walk.\n\n" +
                                 "UMIEJĘTNOŚCI:" +
                                 "\n- Potężne uderzenie\n" +
                                 "\tObrażenia: 155\n\tKoszt many: 30\n" +
                                 "\n- Miazga\n" +
                                 "\tObrażenia: 200\n\tKoszt many: 40\n" +
                                 "\n- Garda\n" +
                                 "\tUnik: +80\n\tKoszt many: 20";

            DescriptionController controller = new DescriptionController(stage, description, 0);
        });

        mageButton.setOnAction(event -> {
            String description = "\nKLASA:\t\t\t\t\tMag\n" +
                                 "ZDROWIE:\t\t\t\t2800\n" +
                                 "ATAK:\t\t\t\t\t15\n" +
                                 "MOC UMIEJĘTNOŚCI:\t\t90\n\n" +
                                 "OPIS:\n" +
                                 "Niska wytrzymałość maga oraz jego bezużyteczny\natak podstawowy są rekompensoane przez\nogromne obrażenia umiejętności.\n\n" +
                                 "UMIEJĘTNOŚCI:" +
                                 "\n- Podpalenie\n" +
                                 "\tObrażenia: 110\n\tKoszt many: 30\n" +
                                 "\n- Meteor\n" +
                                 "\tObrażenia: 200\n\tKoszt many: 40\n" +
                                 "\n- Porażenie\n" +
                                 "\tObrażenia: 190\n\tKoszt many: 35\n" +
                                 "\n- Błyskawica\n" +
                                 "\tObrażenia: 280\n\tKoszt many: 45\n" +
                                 "\n- Kula żywiołów\n" +
                                 "\tObrażenia: 340\n\tKoszt many: 50\n";

            DescriptionController controller = new DescriptionController(stage, description, 0);
        });

        assassinButton.setOnAction(event -> {
            String description = "\nKLASA:\t\t\t\t\tSkrytobójca\n" +
                                 "ZDROWIE:\t\t\t\t2550\n" +
                                 "ATAK:\t\t\t\t\t50\n" +
                                 "MOC UMIEJĘTNOŚCI:\t\t55\n\n" +
                                 "OPIS:\n" +
                                 "Skrytobójca jest postacią wysokiego ryzyka.\nZadaje ogromne obrażenia,\nale nie jest przystosowanydo dłuższych walk\n\n" +
                                 "UMIEJĘTNOŚCI:" +
                                 "\n- Porażenie\n" +
                                 "\tObrażenia: 190\n\tKoszt many: 35\n" +
                                 "\n- Nawałnica stali\n" +
                                 "\tObrażenia: 500\n\tKoszt many: 70\n" +
                                 "\n- Penetracja\n" +
                                 "\tObrażenia: 650\n\tKoszt many: 85\n" +
                                 "\n- Natychmiastowe zabójstwo\n" +
                                 "(Umiejętność ma tylko 1% szans na powodzenie\nna każde brakujące 1% HP przeciwnika.\nW przypadku niepowodzenia,\nbohater traci 25% obecnego HP)\n" +
                                 "\n\tObrażenia: MAXIMUM\n\tKoszt many: 250\n";

            DescriptionController controller = new DescriptionController(stage, description, 0);
        });

        paladinButton.setOnAction(event -> {
            String description = "\nKLASA:\t\t\t\t\tPaladyn\n" +
                                 "ZDROWIE:\t\t\t\t3100\n" +
                                 "ATAK:\t\t\t\t\t30\n" +
                                 "MOC UMIEJĘTNOŚCI:\t\t35\n\n" +
                                 "OPIS:\n" +
                                 "Święty rycerz, który zadaje wysokie obrażenia\nza pomocą swoich umiejętności\noraz z możliwością leczenia,\nświetnie nadaje sie do dłuższych walk.\n\n" +
                                 "UMIEJĘTNOŚCI:" +
                                 "\n- Potężne uderzenie\n" +
                                 "\tObrażenia: 155\n\tKoszt many: 30\n" +
                                 "\n- Podpalenie\n" +
                                 "\tObrażenia: 110\n\tKoszt many: 30\n" +
                                 "\n- Meteor\n" +
                                 "\tObrażenia: 200\n\tKoszt many: 40\n" +
                                 "\n- Błyskawica\n" +
                                 "\tObrażenia: 280\n\tKoszt many: 45\n" +
                                 "\n- Uleczenie\n" +
                                 "\tRegeneracja HP: 300\n\tKoszt many: 55\n" +
                                 "\n- Uzdrowienie\n" +
                                 "\tRegeneracja HP: MAXIMUM\n\tKoszt many: 200\n";

            DescriptionController controller = new DescriptionController(stage, description, 0);
        });

        tankButton.setOnAction(event -> {
            String description = "\nKLASA:\t\t\t\t\tOsiłek\n" +
                                 "ZDROWIE:\t\t\t\t4300\n" +
                                 "ATAK:\t\t\t\t\t30\n" +
                                 "MOC UMIEJĘTNOŚCI:\t\t0\n\n" +
                                 "OPIS:\n" +
                                 "Bardzo wysoka wytrzymałość osiłków\nczyni ich trudnymi do zabicia,\nale sami nie zadają wysokich obrażeń.\n\n" +
                                 "UMIEJĘTNOŚCI:" +
                                 "\n- 'Tanio skóry nie sprzedam'\n" +
                                 "\tObrażenia: 15% obecnego HP bohatera\n\tKoszt many: 220\n" +
                                 "\n- Uleczenie\n" +
                                 "\tRegeneracja HP: 300\n\tKoszt many: 55\n";

            DescriptionController controller = new DescriptionController(stage, description, 0);
        });

        fighterButton.setOnAction(event -> {
            String description = "\nKLASA:\t\t\t\t\tPięściarz\n" +
                                 "ZDROWIE:\t\t\t\t3650\n" +
                                 "ATAK:\t\t\t\t\t80\n" +
                                 "MOC UMIEJĘTNOŚCI:\t\t20\n\n" +
                                 "OPIS:\n" +
                                 "Pięściarz to postać\nspecjalizująca się w atakach podstawowych.\nJest przystosowany do dłuższych walk.\n\n" +
                                 "UMIEJĘTNOŚCI:" +
                                 "\n- Potężne uderzenie\n" +
                                 "\tObrażenia: 155\n\tKoszt many: 30\n" +
                                 "\n- Kamienne pięści\n" +
                                 "\tObrażenia: 200\n\tKoszt many: 40\n" +
                                 "\n- Garda\n" +
                                 "\tUnik: +80\n\tKoszt many: 20\n";

            DescriptionController controller = new DescriptionController(stage, description, 0);
        });

        rogueButton.setOnAction(event -> {
            String description = "\nKLASA:\t\t\t\t\tOsiłek\n" +
                                 "ZDROWIE:\t\t\t\t3650\n" +
                                 "ATAK:\t\t\t\t\t80\n" +
                                 "MOC UMIEJĘTNOŚCI:\t\t20\n\n" +
                                 "OPIS:\n" +
                                 "Łotrzyk pełni rolę wspierającego drużyny.\nPotrafi wykradać życie i manę przeciwników\noraz ogłuszać ich.\n\n" +
                                 "UMIEJĘTNOŚCI:" +
                                 "\n- Podpalenie\n" +
                                 "\tObrażenia: 110\n\tKoszt many: 30\n" +
                                 "\n- Kradzież zdrowia\n" +
                                 "\tKradzież: 250\n\tKoszt many: 50\n" +
                                 "\n- Kradzież many\n" +
                                 "\tKradzież: 50\n\tKoszt many: 25\n" +
                                 "\n- Ogłuszenie\n" +
                                 "\tOgłusza przeciwnika na czas jednej rundy\n\tKoszt many: 120\n";

            DescriptionController controller = new DescriptionController(stage, description, 0);
        });

        backButton.setOnAction(event -> {MenuController controller = new MenuController(stage);});
    }
}