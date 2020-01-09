package scemenzo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private List<File> imageFiles;

    @Override
    public void init() throws Exception {
        super.init();
        Font.loadFont(App.class.getResource("magicFont.ttf").toExternalForm(), 12);
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Telegram Sticker Wizard");
        try {
            stage.getIcons().add(new Image(App.class.getResource("icon.png").toURI().toString()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        scene = new Scene(loadFXML("primary"));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}