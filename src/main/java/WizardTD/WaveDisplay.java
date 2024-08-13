package WizardTD;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class WaveDisplay {

    private ArrayList<Monster> monsters;
    private JSONInfo jsonInfo;
    private final JSONObject json;
    private String level;
    private final PApplet parent;
    private double timer;
    private int amount;
    private char[][] grid;
    private ArrayList<int[]> startingPoints;
    public Random random = new Random();
    private int[] spawn;
    private int jsonInfoCounter;
    private int index;
    private int count;
    private double testCounter;
    private boolean maxWaves;

    public WaveDisplay(PApplet parent, JSONObject json) {
        this.parent = parent;
        this.json = json;
        this.index = 0;

        this.jsonInfo = new JSONInfo(this.json, this.index);
        this.monsters = new ArrayList<>();

        this.amount = this.jsonInfo.getQuantity();
        this.level = this.json.getString("layout");

        this.timer = App.FPS * this.jsonInfo.getPauseTime();
        this.count = 1;
        this.jsonInfoCounter = 1;
        this.testCounter = App.FPS * this.jsonInfo.getPauseTime();
        this.maxWaves = false;

        this.grid = EdgePointsFinder.getDetails(this.level);
        this.startingPoints = EdgePointsFinder.findEdgePoints(this.grid);
        this.spawn = getSpawn();
    }

    /**
     * Retrieves the current value of the test counter.
     *
     * @return The current value of the test counter as a double.
     */
    public double getTestCounter() {
        return this.testCounter;
    }

    /**
     * Moves a list of monsters to their current positions and reduces their moving timers.
     *
     * @param monsters An ArrayList of Monster objects to move and update.
     */
    public void moveMonster(ArrayList<Monster> monsters) {
        for (Monster monster : monsters) {
            monster.move(monster.getCurrentPosition());
            monster.reduceMovingTimer();
        }
    }

    /**
     * Moves monsters to new positions based on their moving timers and game settings.
     *
     * @param monsters An ArrayList of Monster objects to move.
     */
    public void monsterMover(ArrayList<Monster> monsters) {
        for (Monster monster : monsters) {
            if (monster.getMovingTimer() == 0 && !monster.isKilled()) {
                monster.movePosition();
                 if (App.fast) {
                     monster.setMovingTimer(monster.startingMovingTimer() / 2);
                 } else {
                     monster.setMovingTimer(monster.startingMovingTimer());
                 }
            }
        }
    }

    /**
     * Draws a list of monsters on the screen.
     *
     * @param monsters An ArrayList of Monster objects to be drawn.
     */
    public void drawMonster(ArrayList<Monster> monsters) {
        for (Monster monster : monsters) {
            monster.draw(this.parent);
        }
    }

    /**
     * Retrieves the coordinates for spawning a monster based on entry paths.
     *
     * @return An array of two ints for the coordinates [x, y].
     */
    public int[] getSpawn() {
        int[] cords = new int[2];
        int rand = this.random.nextInt(this.startingPoints.size());
        cords[0] = this.startingPoints.get(rand)[0];
        cords[1] = this.startingPoints.get(rand)[1];
        return cords;
    }

    /**
     * Retrieves the current wave counter.
     *
     * @return The current wave counter as an integer.
     */
    public int getWaveCounter() {
        return this.jsonInfoCounter;
    }

    /**
     * Adds a new monster to the game at the specified position.
     *
     * @param x The x-coordinate of the monster's position.
     * @param y The y-coordinate of the monster's position.
     */
    public void addMonster(int x, int y) {
        PImage monsterImg = this.parent.loadImage("src/main/resources/WizardTD/" + this.jsonInfo.getType() + ".png");
        this.monsters.add(new Monster(this.parent, monsterImg, x * App.CELLSIZE, y * App.CELLSIZE, this.json, this.index));
    }

    /**
     * Retrieves the list of monsters currently present in the game.
     *
     * @return An ArrayList of Monster objects representing the game's monsters.
     */
    public ArrayList<Monster> getMonsters() {
        return this.monsters;
    }

    /**
     * Updates and manages the game's display, including moving monsters and wave progression.
     * This method is responsible for the game's main loop.
     */
    public void display() {

        drawMonster(this.monsters);

        if (!App.paused) {
            moveMonster(this.monsters);
            monsterMover(this.monsters);

            this.timer--;
            this.testCounter--;

            if (this.timer < 0 && this.amount > 0) {
                int x = this.spawn[0]; int y = this.spawn[1];
                addMonster(x, y);
                System.out.println(this.count + ": Monster added");
                this.count++;

                this.timer = App.FPS * (double) this.jsonInfo.getWaveDuration() / this.jsonInfo.getQuantity();
                this.amount--;
                if (this.amount == 0) {

                    this.jsonInfo.nextWave();

                    try {
                        this.jsonInfo.setCurrentWave(this.jsonInfo.getIndex());
                        System.out.println("Wave: " + jsonInfoCounter);
                        this.count = 1;
                        this.jsonInfoCounter++;

                        this.spawn = getSpawn();

                        this.index++;
                        this.jsonInfo = new JSONInfo(this.json, this.index);

                        this.amount = this.jsonInfo.getQuantity();
                        this.timer = App.FPS * this.jsonInfo.getPauseTime();

                    } catch (RuntimeException e) {
                        System.out.println("End of waves.");
                        this.maxWaves = true;
                    }
                }
            }

            if (this.testCounter == 0 && this.jsonInfoCounter != this.jsonInfo.getWaveAmount()) {
                this.testCounter = App.FPS * this.jsonInfo.getPauseTime();
            } else if (this.jsonInfoCounter == this.jsonInfo.getWaveAmount()) {
                this.testCounter = 0;
            }
        }
    }
}
