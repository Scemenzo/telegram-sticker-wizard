package scemenzo;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import scemenzo.model.Sticker;
import scemenzo.utils.ImageManipulator;
import scemenzo.utils.FileSaver;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class PrimaryController {

    @FXML private ListView<Sticker> stickerListView;
    @FXML private ImageView stickerImageView;
    @FXML private Button clearButton;
    @FXML private Button undoButton;
    @FXML private Button redoButton;
    @FXML private Button exportButton;
    @FXML private Button resetButton;
    @FXML private Button infoButton;
    @FXML private Slider tresholdValue;
    @FXML private VBox imageFrame;
    @FXML private TextField croppingSpinner;
    @FXML private Label titleLabel;

    private ObservableList<Sticker> stickerListItems;
    private Sticker selectedSticker;

    @FXML public void initialize() {
        stickerListItems = FXCollections.observableArrayList();
        stickerListView.setItems(stickerListItems);
        try {
            infoButton.setGraphic(new ImageView(App.class.getResource("infoIcon.png").toURI().toString()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        deactivateTools();

        //List selection click
        stickerListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                selectedSticker = stickerListView.getSelectionModel().getSelectedItem();
                if(selectedSticker != null) {
                    System.out.println("Selected: " + selectedSticker.toString());
                    updateImageView();
                    croppingSpinner.setText(String.valueOf(selectedSticker.getFrameMargin()));
                }
            }
        });

        //Item list on change listener
        stickerListItems.addListener(new ListChangeListener<Sticker>() {
            @Override
            public void onChanged(Change<? extends Sticker> c) {
                if(stickerListItems.size() == 0) {
                    deactivateTools();
                } else {
                    activateTools();
                }
            }
        });

        //Treshold setted
        tresholdValue.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Treshold set to: " + getTreshold());
            }
        });

        //Magic wand click
        stickerImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(selectedSticker != null) {
                    Image image = selectedSticker.getCurrentImage();
                    PixelReader pixelReader = image.getPixelReader();
                    int selectedPixelX = (int) Math.floor(image.getWidth() * event.getX() / (stickerImageView.getLayoutBounds().getWidth() + 1));
                    int selectedPixelY = (int) Math.floor(image.getHeight() * event.getY() / (stickerImageView.getLayoutBounds().getHeight() + 1));
                    System.out.println("Selected x,y: " + selectedPixelX + "," + selectedPixelY);

                    //Color picking
                    Color selectedColor = pixelReader.getColor(selectedPixelX, selectedPixelY);

                    //Treshold applying
                    if(getTreshold() > 0) {
                        image = ImageManipulator.applyColorElimination(image, selectedPixelX, selectedPixelY, selectedColor, getTreshold());
                        selectedSticker.addModifiedImage(image);
                    }
                    updateImageView();
                } else {
                    alertDialog("No sticker has been selected from the list");
                }
            }
        });

        //Forces the image margin field to be numeric and crops the image
        croppingSpinner.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.compareTo("") != 0) {
                    String polishedValue = null;
                    if (!newValue.matches("\\d*")) {
                        polishedValue = newValue.replaceAll("[^\\d]", "");
                        croppingSpinner.setText(polishedValue);
                    } else {
                        if(Double.valueOf(newValue) < selectedSticker.getCurrentImage().getHeight()/2 && Double.valueOf(newValue) < selectedSticker.getCurrentImage().getWidth()/2) {
                            selectedSticker.setFrameMargin(Integer.valueOf(newValue));
                        } else {
                            alertDialog("You can't crop more than the image size");
                        }
                    }
                }
                updateImageView();
            }
        });
    }

    @FXML private void loadImages() throws IOException {
        //File selector window
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Image files", "*.png", "*.jpeg", "*.jpg");
        fileChooser.getExtensionFilters().add(extensionFilter);
        fileChooser.setTitle("Select your images");
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(new Stage());
        loadFilesInList(selectedFiles);
    }

    @FXML private void clearStickerList() throws IOException {
        stickerListItems.clear();
    }

    @FXML private void saveStickers() throws IOException {
        if(stickerListItems != null) {
            System.out.println("Elaborating " + stickerListItems.size() + " stickers");
            boolean problemsSaving = false;
            for (Sticker stickerListItem : stickerListItems) {
                if(!FileSaver.saveSticker(stickerListItem)) {
                    problemsSaving = true;
                }
            }
            if(problemsSaving) {
                alertDialog("Problems during exporting");
            }
        } else {
            System.out.println("No stickers loaded in list");
        }
    }

    @FXML private void switchToInfo() throws IOException {
        App.setRoot("info");
    }

    @FXML private void resetImage() throws IOException {
        selectedSticker.resetModifiedImages();
        updateImageView();
    }

    @FXML private void undo() throws IOException{
        selectedSticker.decrementIndex();
        updateImageView();
    }

    @FXML private void redo() throws IOException{
        selectedSticker.incrementIndex();
        updateImageView();
    }

    private void loadFilesInList(List<File> selectedFiles) {
        if (selectedFiles != null && selectedFiles.size() > 0) {
            for (File selectedFile : selectedFiles) {
                System.out.println("Loaded image file from: " + selectedFile.getAbsolutePath());
                Sticker sticker = new Sticker(selectedFile.getName(), selectedFile.getParent(), selectedFile.toURI().toString());
                stickerListItems.add(sticker);
            }
        } else {
            System.out.println("No file accepted from the fileChooser");
        }
    }

    private void updateImageView() {
        stickerImageView.setImage(ImageManipulator.cropImage(selectedSticker.getCurrentImage(), selectedSticker.getFrameMargin()));
    }

    private int getTreshold() {
        return (int) Math.round(tresholdValue.getValue());
    }

    private void activateTools() {
        resetButton.setDisable(false);
        clearButton.setDisable(false);
        undoButton.setDisable(false);
        redoButton.setDisable(false);
        croppingSpinner.setDisable(false);
        exportButton.setDisable(false);
        tresholdValue.setDisable(false);
    }

    private void deactivateTools() {
        resetButton.setDisable(true);
        clearButton.setDisable(true);
        undoButton.setDisable(true);
        redoButton.setDisable(true);
        croppingSpinner.setDisable(true);
        exportButton.setDisable(true);
        tresholdValue.setDisable(true);
    }

    private void alertDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.NONE, message, ButtonType.OK);
        alert.showAndWait();
    }
}
