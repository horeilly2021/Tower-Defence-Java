package WizardTD;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class EdgePointsFinder {

    /**
     * Read details from a file and parse it into a char array.
     *
     * @param filename The name of the file to read.
     * @return A 2D char array representing the parsed content.
     */
    public static char[][] getDetails(String filename) {

        File f;
        Scanner scan = null;
        try {
            f = new File(filename);
            scan = new Scanner(f);
        } catch (FileNotFoundException e) {
            System.err.println("Error with test file");
            System.exit(1);
        }

        char[][] content = new char[App.BOARD_WIDTH][App.BOARD_WIDTH];

        int row = 0;
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            for (int col = 0; col < line.length(); col++) {
                if (line.charAt(col) == 'X') {
                    content[row][col] = 'X';
                } else if (line.charAt(col) == 'S') {
                    content[row][col] = 'S';
                } else if (line.charAt(col) == 'W') {
                    content[row][col] = 'W';
                } else {
                    content[row][col] = ' ';
                }
            }
            row++;
        }
        scan.close();
        return content;
    }

    /**
     * Find edge points in a grid of characters ('X' values) and return their positions.
     *
     * @param grid A 2D char array representing the grid.
     * @return An ArrayList of int arrays with x and y coordinates of edge points.
     */
    public static ArrayList<int[]> findEdgePoints(char[][] grid) {
        ArrayList<int[]> edgePoints = new ArrayList<>();

        if (grid.length == 0 || grid[0].length == 0) {
            return edgePoints;
        }

        int numRows = grid.length;
        int numCols = grid[0].length;

        for (int col = 0; col < numCols; col++) {
            if (grid[0][col] == 'X') {
                int[] point = new int[2];
                point[0] = col;
                edgePoints.add(point);
            }
        }

        for (int col = 0; col < numCols; col++) {
            if (grid[numRows - 1][col] == 'X') {
                int[] point = new int[2];
                point[1] = numRows - 1;
                point[0] = col;
                edgePoints.add(point);
            }
        }

        for (int row = 1; row < numRows - 1; row++) {
            if (grid[row][0] == 'X') {
                int[] point = new int[2];
                point[1] = row;
                edgePoints.add(point);
            }
        }

        for (int row = 1; row < numRows - 1; row++) {
            if (grid[row][numCols - 1] == 'X') {
                int[] point = new int[2];
                point[1] = row;
                point[0] = numCols - 1;
                edgePoints.add(point);
            }
        }

        return edgePoints;
    }

    /**
     * Check if a specific position in the grid represents a grass tile (' ').
     *
     * @param filename The name of the file containing the grid details.
     * @param posX The X position of the tile.
     * @param posY The Y position of the tile.
     * @return true if the tile is a grass tile, false otherwise.
     */
    public static boolean isGrassTile(String filename, int posX, int posY) {
        try {
            char type = getDetails(filename)[posY - 1][posX ];
            return type == ' ';
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * Calculate the hypotenuse length (Euclidean distance) between two points.
     *
     * @param x1 The X coordinate of the first point.
     * @param x2 The X coordinate of the second point.
     * @param y1 The Y coordinate of the first point.
     * @param y2 The Y coordinate of the second point.
     * @return The length of the hypotenuse between the points as an integer.
     */
    public static int getHypoLength(float x1, float x2, float y1, float y2) {
        float xDiff = Math.abs(x2-x1);
        float yDiff = Math.abs(y2-y1);

        return (int) Math.hypot(xDiff, yDiff);
    }
}
