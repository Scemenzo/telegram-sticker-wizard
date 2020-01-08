package scemenzo;

import javafx.fxml.FXML;

import java.io.IOException;

public class InfoController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

}