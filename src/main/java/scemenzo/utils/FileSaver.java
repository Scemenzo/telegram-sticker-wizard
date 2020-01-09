package scemenzo.utils;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import scemenzo.model.Sticker;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FileSaver {

    public static boolean saveSticker(Sticker sticker) {
        boolean savedCorrectly = false;
        File outputFile = new File(sticker.getDirectoryPath() + "/" + "Sticker_" + sticker.getName().substring(0, sticker.getName().lastIndexOf('.')) + ".png");

        //Get image and crop it
        Image imageToSave = sticker.getCurrentImage();
        imageToSave = ImageManipulator.cropImage(imageToSave, sticker.getFrameMargin());

        //Calculate compatible size for Telegram stickers
        int width = (int) imageToSave.getWidth();
        int height = (int) imageToSave.getHeight();
        if(width > 512 || height > 512) {
            float ratio = (float)width/(float)height;
            System.out.println("Resizing the canvas width ratio: " + ratio);
            if(width > height) {
                width = 512;
                height = Math.round(512/ratio);
            } else {
                width = Math.round(ratio*512);
                height = 512;
            }
            System.out.println("New width, height: " + width + ", " + height);
        }

        //Saving through a Canvas
        Canvas canvas = new Canvas(width, height);
        GraphicsContext ctx = canvas.getGraphicsContext2D();
        ctx.drawImage(imageToSave,0,0,width,height);
        SnapshotParameters snapshotParameters = new SnapshotParameters();
        snapshotParameters.setFill(Color.TRANSPARENT);
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(canvas.snapshot(snapshotParameters, new WritableImage(width,height)), null);
        try {
            savedCorrectly = ImageIO.write(bufferedImage, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return savedCorrectly;
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
