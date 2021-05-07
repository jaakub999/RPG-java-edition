package FX;

import com.github.javafaker.Faker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MapController {
    public Stage stage;

    @FXML private ImageView image;
    @FXML private Label dragLabel;
    @FXML private Label diceLabel;

    public MapController(Stage stage) {
        this.stage = stage;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/image.fxml"));
            loader.setController(this);
            stage.setScene(new Scene(loader.load()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MapController(Stage stage, int x) {
        this.stage = stage;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/image.fxml"));
            loader.setController(this);
            stage.setScene(new Scene(loader.load()));

        } catch (IOException e) {
            e.printStackTrace();
        }

        Faker faker = new Faker();
        int y = 0;

        switch (x) {
            case 1:
                y = faker.number().numberBetween(1, 7);
                break;

            case 2:
                y = faker.number().numberBetween(2, 13);
                break;

            case 3:
                y = faker.number().numberBetween(3, 19);
                break;

            case 4:
                y = faker.number().numberBetween(4, 25);
                break;
        }

        URL url = getClass().getResource("/images/" + x + "_" + y + ".png");
        Image img = new Image(url.toString());
        setImage(img);
    }

    @FXML
    private void handleDragOver(DragEvent event) {
        if (event.getDragboard().hasFiles())
            event.acceptTransferModes(TransferMode.ANY);
    }

    @FXML
    private void handleDrop(DragEvent event) throws FileNotFoundException {
        List<File> files = event.getDragboard().getFiles();
        Image img = new Image(new FileInputStream(files.get(0)));
        setImage(img);
    }

    private void setImage(Image img) {
        image.setImage(img);
        dragLabel.setVisible(false);
    }
}
