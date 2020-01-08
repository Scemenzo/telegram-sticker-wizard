module scemenzo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.swing;

    opens scemenzo to javafx.fxml;
    exports scemenzo;
}