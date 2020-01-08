package scemenzo.model;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Sticker {

    private String name;
    private String directoryPath;
    private Image originalImage;
    private ArrayList<Image> modifiedImages;
    private int modifiedImageIndex;
    private int frameMargin;

    public Sticker(String fileName, String directoryPath, String imagePath) {
        this.name = fileName;
        this.directoryPath = directoryPath;
        originalImage = new Image(imagePath);
        modifiedImages = new ArrayList<>();
        frameMargin = 0;
        modifiedImageIndex = -1;
    }

    public void addModifiedImage(Image image){
        modifiedImageIndex++;
        if(modifiedImageIndex >= modifiedImages.size()) {
            modifiedImages.add(image);
            System.out.println("Added modified image. Size of modified images: " + modifiedImages.size());
        }
        else {
            modifiedImages.set(modifiedImageIndex, image);
            modifiedImages.subList(modifiedImageIndex, modifiedImages.size() - 1).clear();
            System.out.println("Added modified image cuting list. Size of modified images: " + modifiedImages.size());
        }
    }

    public Image getCurrentImage(){
        if(modifiedImageIndex == -1) {
            return originalImage;
        } else {
            return modifiedImages.get(modifiedImageIndex);
        }
    }

    public void resetModifiedImages(){
        modifiedImages.clear();
        modifiedImageIndex = -1;
    }

    public void decrementIndex() {
        if(modifiedImageIndex > -1) modifiedImageIndex--;
    }

    public void incrementIndex() {
        if(modifiedImageIndex < modifiedImages.size() - 1) modifiedImageIndex++;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public Image getOriginalImage() {
        return originalImage;
    }

    public void setOriginalImage(Image originalImage) {
        this.originalImage = originalImage;
    }

    public int getFrameMargin() {
        return frameMargin;
    }

    public void setFrameMargin(int frameMargin) {
        this.frameMargin = frameMargin;
    }

    public ArrayList<Image> getModifiedImages() {
        return modifiedImages;
    }

    public void setModifiedImages(ArrayList<Image> modifiedImages) {
        this.modifiedImages = modifiedImages;
    }

    public int getModifiedImageIndex() {
        return modifiedImageIndex;
    }

    public void setModifiedImageIndex(int modifiedImageIndex) {
        this.modifiedImageIndex = modifiedImageIndex;
    }

    @Override
    public String toString() {
        return name;
    }
}
