package scemenzo.utils;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import java.util.LinkedList;
import java.util.Queue;

public class FloodAlgorithm {

    private static PixelReader reader;
    private static PixelWriter writer;
    private static int width;
    private static int height;
    private static Color colorSelected;
    private static Color colorToReplace;
    private static double normalizedTreshold;
    private static boolean pixelMask[][];
    private static Queue<Point> queue = new LinkedList<>();

    public FloodAlgorithm(PixelReader reader, PixelWriter writer, int width, int height, Color colorSelected, Color colorToReplace, double normalizedTreshold) {
        this.reader = reader;
        this.writer = writer;
        this.width = width;
        this.height = height;
        this.colorSelected = colorSelected;
        this.colorToReplace = colorToReplace;
        this.normalizedTreshold = normalizedTreshold;
        pixelMask = new boolean[width][height];
    }

    public static void floodFill(int x, int y) {
        System.out.println("floodFill start at " + x + "," + y);
        Color actualColor = reader.getColor(x,y);

        queue.add(new Point(x,y));

        while (!queue.isEmpty()){
            Point point = queue.remove();
            actualColor = reader.getColor(point.getX(),point.getY());

            if(floodIn(point.getX(), point.getY(), actualColor)) {
                queue.add(new Point(point.getX(),point.getY()+1));
                queue.add(new Point(point.getX()+1,point.getY()));
                queue.add(new Point(point.getX()-1,point.getY()));
                queue.add(new Point(point.getX(),point.getY()-1));
            }
        }
    }

    private static boolean floodIn(int x, int y, Color actualColor) {
        if (x > 0 && y > 0 && x < width - 1 && y < height - 1 && !pixelMask[x][y]) {
            if (Math.abs(colorSelected.getGreen() - actualColor.getGreen()) < normalizedTreshold
                    && Math.abs(colorSelected.getRed() - actualColor.getRed()) < normalizedTreshold
                    && Math.abs(colorSelected.getBlue() - actualColor.getBlue()) < normalizedTreshold) {
                writer.setColor(x, y, colorToReplace);
                pixelMask[x][y] = true;
                return true;
            }
        }
        return false;
    }

    private static class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}