package scemenzo.utils;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageManipulator {

    public static Image cropImage(Image image, int margin){
        if(margin == 0) {
            return image;
        }

        System.out.println("Cropping procedure with margin: " + margin);
        return new WritableImage(
                image.getPixelReader(),
                margin,
                margin,
                (int) (image.getWidth() - 2 * margin),
                (int) (image.getHeight() - 2 * margin));
    }

    public static WritableImage applyColorElimination(Image inputImage, int selectedPixelX, int selectedPixelY, Color colorSelected, int treshold) {
        int width = (int) inputImage.getWidth();
        int height = (int) inputImage.getHeight();

        WritableImage outputImage = new WritableImage(inputImage.getPixelReader(), width, height);
        PixelReader reader = outputImage.getPixelReader();
        PixelWriter writer = outputImage.getPixelWriter();

        //Use of the flood algorithm
        double normalizedTreshold = (double) treshold / 255;
        System.out.println("Flooding along resolution " + width + "x" + height + " with treshold: " + normalizedTreshold);
        FloodAlgorithm algorithm = new FloodAlgorithm(reader, writer, width, height, colorSelected, Color.TRANSPARENT, normalizedTreshold);
        FloodAlgorithm.floodFill(selectedPixelX, selectedPixelY);
        return outputImage;
    }

    public static void saveSnaphsotOfScene(Region region, String filePath) {
        SnapshotParameters snapshotParameters = new SnapshotParameters();
        snapshotParameters.setFill(Color.TRANSPARENT);
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(region.snapshot(snapshotParameters, new WritableImage((int)region.getWidth(), (int)region.getHeight())), null);
        try {
            ImageIO.write(bufferedImage, "png", new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
