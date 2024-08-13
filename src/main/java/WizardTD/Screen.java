package WizardTD;

import processing.core.PApplet;
import processing.core.PImage;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.*;
import java.io.*;

public class Screen {
    private final PApplet parent;
    private String level;
    private static final int ARGB = 2;

    private int houseX;
    private int houseY;

    public Screen(PApplet parent, String level) {
        this.parent = parent;
        this.level = level;
    }

    /**
     * Reads the content of a file and stores it in a 2D string array.
     *
     * @param level The path to the file to read.
     * @return A 2D string array containing the content of the file.
     * @throws FileNotFoundException If the specified file is not found.
     */
    public static String[][] readFile(String level) throws FileNotFoundException {

        File display = new File(level);
        Scanner scan = new Scanner(display);
        String[][] content = new String[20][20];

        int row = 0;
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            for (int col = 0; col < line.length(); col++) {
                content[row][col] = String.valueOf(line.charAt(col));
            }
            row++;
        }
        scan.close();

        return content;
    }

    /**
     * Displays the game tiles based on the content of the level file.
     */
    public void display() {


        String[][] content = new String[0][];
        try {
            content = readFile(this.level);
        } catch (FileNotFoundException e) {
            System.err.println("Level not found");
            System.exit(1);
        }

        int cellSize = 32;
        int numRows = content.length;
        int numCols = content[0].length;

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                int x = j * cellSize;
                int y = i * cellSize;
                try {
                    if (Objects.equals(content[i][j], "S")) {
                        parent.image(App.shrub, x, y);
                    } else if (Objects.equals(content[i][j], "X") && Objects.equals(content[i + 1][j], "X") && Objects.equals(content[i - 1][j], "X") && Objects.equals(content[i][j + 1], "X") && Objects.equals(content[i][j - 1], "X")) {
                        parent.image(App.path3, x, y);
                    } else if (Objects.equals(content[i][j], "X") && Objects.equals(content[i + 1][j], "X") && Objects.equals(content[i][j + 1], "X") && Objects.equals(content[i][j - 1], "X")) {
                        parent.image(App.path2, x, y);
                    } else if (Objects.equals(content[i][j], "X") && Objects.equals(content[i + 1][j], "X") && Objects.equals(content[i - 1][j], "X") && Objects.equals(content[i][j - 1], "X")) {
                        parent.image(rotateImageByDegrees(App.path2, 90), x, y);
                    } else if (Objects.equals(content[i][j], "X") && Objects.equals(content[i - 1][j], "X") && Objects.equals(content[i][j + 1], "X") && Objects.equals(content[i][j - 1], "X")) {
                        parent.image(rotateImageByDegrees(App.path2, 180), x, y);
                    } else if (Objects.equals(content[i][j], "X") && Objects.equals(content[i + 1][j], "X") && Objects.equals(content[i][j + 1], "X")) {
                        parent.image(rotateImageByDegrees(App.path1, 270), x, y);
                    } else if (Objects.equals(content[i][j], "X") && Objects.equals(content[i + 1][j], "X") && Objects.equals(content[i][j - 1], "X")) {
                        parent.image(App.path1, x, y);
                    } else if (Objects.equals(content[i][j], "X") && Objects.equals(content[i - 1][j], "X") && Objects.equals(content[i][j - 1], "X")) {
                        parent.image(rotateImageByDegrees(App.path1, 90), x, y);
                    }else if (Objects.equals(content[i][j], "X") && Objects.equals(content[i - 1][j], "X") && Objects.equals(content[i][j + 1], "X")) {
                        parent.image(rotateImageByDegrees(App.path1, 180), x, y);
                    } else if (Objects.equals(content[i][j], "X") && (Objects.equals(content[i + 1][j], "X") || Objects.equals(content[i + 1][j], "W") || Objects.equals(content[i - 1][j], "X") || Objects.equals(content[i - 1][j], "W"))) {
                        parent.image(rotateImageByDegrees(App.path0, 90), x, y);
                    } else if (Objects.equals(content[i][j], "X")) {
                        parent.image(App.path0, x, y);
                    } else {
                        parent.image(App.grass, x, y);
                    }

                    if (Objects.equals(content[i][j], "W")) {
                        this.houseX = x - ( App.BOARD_WIDTH / 2);
                        this.houseY = y - ( App.BOARD_WIDTH / 2);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    parent.image(rotateImageByDegrees(App.path0, 90), x, y);
                }
            }
        }
        parent.image(App.wizard_house, this.houseX, this.houseY);
    }

    /**
     * Source: https://stackoverflow.com/questions/37758061/rotate-a-buffered-image-in-java
     * @param pimg The image to be rotated
     * @param angle between 0 and 360 degrees
     * @return the new rotated image
     */
    public PImage rotateImageByDegrees(PImage pimg, double angle) {
        BufferedImage img = (BufferedImage) pimg.getNative();
        double rads = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int w = img.getWidth();
        int h = img.getHeight();
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);

        PImage result = this.parent.createImage(newWidth, newHeight, ARGB);
//        BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        BufferedImage rotated = (BufferedImage) result.getNative();
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate((newWidth - w) / 2, (newHeight - h) / 2);

        int x = w / 2;
        int y = h / 2;

        at.rotate(rads, x, y);
        g2d.setTransform(at);
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();
        for (int i = 0; i < newWidth; i++) {
            for (int j = 0; j < newHeight; j++) {
                result.set(i, j, rotated.getRGB(i, j));
            }
        }

        return result;
    }
}