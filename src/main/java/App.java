import FX.MenuController;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        Stage stage = new Stage();
        stage.setTitle("RPG");
        MenuController controller = new MenuController(stage);
        controller.stage.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
