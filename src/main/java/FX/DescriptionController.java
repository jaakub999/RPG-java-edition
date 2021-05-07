package FX;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DescriptionController implements Initializable {
    public final Stage stage;
    public final String description;
    public int bc;

    @FXML private TextArea desc;
    @FXML private Button backButton;

    public DescriptionController(Stage stage, String description, int bc) {
        this.stage = stage;
        this.description = description;
        this.bc = bc;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/description.fxml"));
            loader.setController(this);
            stage.setScene(new Scene(loader.load()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        desc.setText(description);
        backButton.setOnAction(event -> back());
    }

    private void back() {
        ClassesController controller0;
        MenuController controller1;

        if (bc == 0)
            controller0 = new ClassesController(stage);

        else
            controller1 = new MenuController(stage);
    }
}
