package WizardTD;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;

import java.util.*;

public class Monster {

    public static final int HEIGHT = 2;
    private PApplet parent;
    private PImage sprite;
    private float x, y;
    private float startX, startY;
    private float velX, velY;
    private char[][] cords;
    private LinkedList<int[]> pathToHouse;
    private float health;
    private boolean alive;
    private int currentPosition;
    private boolean killed;
    private boolean dying;
    private boolean reached;
    static int[] dx = {-1, 1, 0, 0};
    static int[] dy = {0, 0, -1, 1};
    private int deathHealth;
    private float healthBarWidth;
    private float percentageHealth;
    private JSONInfo jsonInfo;
    private double movingTimer;
    private float initialHp;
    private float speed;
    private int manaGainedOnKill;
    private Mana mana;
    private boolean collectedMana;

    public Monster(PApplet parent, PImage sprite, float x, float y, JSONObject json, int index) {
        this.sprite = sprite;
        this.currentPosition = 0;
        this.parent = parent;

        this.x = x;
        this.y = y;
        this.startX = x;
        this.startY = y;
        this.velX = 0;
        this.velY = 0;

        this.alive = true;
        this.killed = false;
        this.dying = false;
        this.reached = false;
        this.collectedMana = false;

        this.cords = EdgePointsFinder.getDetails(json.getString("layout"));
        this.pathToHouse = bfs(cords);

        this.jsonInfo = new JSONInfo(json, index);
        this.initialHp = this.jsonInfo.getHp() * (1 + jsonInfo.getArmour());
        this.health = this.initialHp;
        this.deathHealth = 0;
        this.speed = this.jsonInfo.getSpeed();

        this.manaGainedOnKill = this.jsonInfo.getMana();
        this.mana = new Mana(this.parent);

        this.movingTimer = 1;
    }

    /**
     * Retrieves the current x-coordinate of the object.
     *
     * @return The current x-coordinate as a float.
     */
    public float getCurX() {
        return this.x;
    }

    /**
     * Retrieves the current y-coordinate of the object.
     *
     * @return The current y-coordinate as a float.
     */
    public float getCurY() {
        return this.y;
    }

    /**
     * Calculates and retrieves the current width of the health bar.
     *
     * @return The width of the health bar as a float.
     */
    public float getHealthBarWidth() {
        return this.healthBarWidth = PApplet.map(this.health, 0, this.initialHp, 0, this.initialHp);
    }

    /**
     * Calculates and retrieves the percentage of health remaining based on the health bar width.
     *
     * @return The percentage of health remaining as a float.
     */
    public float getPercentageHealth() {
        return this.percentageHealth = (1 - getHealthBarWidth() / this.initialHp) * App.BOARD_WIDTH;
    }

    /**
     * Reduces the moving timer by one.
     */
    public void reduceMovingTimer() {
        this.movingTimer--;
    }

    /**
     * Sets the moving timer to the specified time.
     *
     * @param time The new value for the moving timer.
     */
    public void setMovingTimer(double time) {
        this.movingTimer = time;
    }

    /**
     * Retrieves the current value of the moving timer.
     *
     * @return The current moving timer value as a double.
     */
    public double getMovingTimer() {
        return this.movingTimer;
    }

    /**
     * Calculates and sets the initial moving timer value based on the object's speed.
     *
     * @return The initial moving timer value as a double.
     */
    public double startingMovingTimer() {
        return this.movingTimer = (double) App.CELLSIZE / this.jsonInfo.getSpeed();
    }

    /**
     * Inflicts damage on the object and updates its state if its health reaches zero.
     *
     * @param damage The amount of damage to inflict.
     */
    public void damage(int damage) {
        this.health -= damage;
        if (this.health <= 0 && !this.reached) {
            this.mana.increaseMana(this.manaGainedOnKill);
            this.alive = false;
            this.dying = true;
        }
    }

    /**
     * Checks whether the object is alive.
     *
     * @return `true` if the object is alive, `false` otherwise.
     */
    public boolean isAlive() {
        return this.alive;
    }

    /**
     * Advances the object's current position.
     */
    public void movePosition() {
        this.currentPosition++;
    }

    /**
     * Retrieves the current position of the object.
     *
     * @return The current position as an integer.
     */
    public int getCurrentPosition() {
        return this.currentPosition;
    }

    /**
     * Retrieves the x-coordinate of a specified position in the path.
     *
     * @param posX The position in the path.
     * @return The x-coordinate at the specified position.
     */
    public float getX(int posX) {
        return this.pathToHouse.get(posX)[0];
    }

    /**
     * Retrieves the y-coordinate of a specified position in the path.
     *
     * @param posY The position in the path.
     * @return The y-coordinate at the specified position.
     */
    public float getY(int posY) {
        return this.pathToHouse.get(posY)[1];
    }

    /**
     * Retrieves the x-coordinate of the next position in the path.
     *
     * @param posX The current position in the path.
     * @return The x-coordinate of the next position.
     */
    public float getNextX(int posX) {
        return this.pathToHouse.get(posX + 1)[0];
    }

    /**
     * Retrieves the y-coordinate of the next position in the path.
     *
     * @param posY The current position in the path.
     * @return The y-coordinate of the next position.
     */
    public float getNextY(int posY) {
        return this.pathToHouse.get(posY + 1)[1];
    }

    /**
     * Sets the "reached" state of the object, indicating whether it has reached its destination.
     *
     * @param reached The new "reached" state (true or false).
     */
    public void setReached(boolean reached) {
        this.reached = reached;
    }

    /**
     * Retrieves the sprite image associated with the object.
     *
     * @return The sprite image as a PImage.
     */
    public PImage getSprite() {
        return this.sprite;
    }

    /**
     * Checks whether the object has been killed.
     *
     * @return `true` if the object has been killed, `false` otherwise.
     */
    public boolean isKilled() {
        return this.killed;
    }

    /**
     * Retrieves the amount of mana gained when the object is killed.
     *
     * @return The mana gained on kill as an integer.
     */
    public int getManaGainedOnKill() {
        return this.manaGainedOnKill;
    }

    /**
     * Sets the "collectedMana" state of the object, indicating whether it has collected mana.
     *
     * @param collectedMana The new "collectedMana" state (true or false).
     */
    public void setCollectedMana(boolean collectedMana) {
        this.collectedMana = collectedMana;
    }

    /**
     * Checks whether the object has collected mana.
     *
     * @return `true` if the object has collected mana, `false` otherwise.
     */
    public boolean hasCollectedMana() {
        return this.collectedMana;
    }

    /**
     * Checks whether the object has reached its destination.
     *
     * @return `true` if the object has reached its destination, `false` otherwise.
     */
    public boolean hasReached() {
        return this.reached;
    }

    /**
     * Moves the object to the specified position in the path.
     *
     * @param pos The position in the path to move to.
     */
    public void move(int pos) {

        float curX = 0;
        float curY = 0;
        try {
            curX = this.getX(pos);
            curY = this.getY(pos);
        } catch (IndexOutOfBoundsException e) {
            this.alive = false;
        }

        float nextX;
        float nextY;
        try {
            nextX = this.getNextX(pos);
            nextY = this.getNextY(pos);

            this.velY = nextX - curX;
            this.velX = nextY - curY;

        } catch (IndexOutOfBoundsException e) {
            if (!this.reached) {
                this.mana.decreaseMana(this.manaGainedOnKill);
            }
            setReached(true);
        }

        if (!this.alive) {
            this.velX = 0; this.velY = 0;
        }

        if (App.fast) {
            this.x += this.velX * (this.speed * 2);
            this.y += this.velY * (this.speed * 2);
        } else {
            this.x += this.velX * this.speed;
            this.y += this.velY * this.speed;
        }
    }

    /**
     * Draws the object, including its health bar and sprite image.
     *
     * @param parent The PApplet object to use for drawing.
     */
    public void draw(PApplet parent) {
        float x = this.x + ( (float) App.BOARD_WIDTH / 5);
        float y = this.y + ( (float) App.BOARD_WIDTH / 5);

        if (this.alive) {
            parent.strokeWeight(0);
            parent.fill(0, 255, 0);
            parent.rect(x, y - 5, App.BOARD_WIDTH, HEIGHT);

            parent.fill(255, 0, 0);
            parent.rect(x, y - 5, getPercentageHealth(), HEIGHT);
            parent.strokeWeight(1);

            parent.image(this.sprite, x, y);
        }

        if (this.dying && !this.killed) {
            this.deathScene();
            parent.image(this.sprite, x, y);
        }
    }

    /**
     * Animates the death scene of the monster at 4 fps.
     */
    public void deathScene() {
        if (!this.reached) {
            if (this.deathHealth == 0) {
                this.sprite = App.gremlin1;
            } else if (this.deathHealth == 4) {
                this.sprite = App.gremlin2;
            } else if (this.deathHealth == 8) {
                this.sprite = App.gremlin3;
            } else if (this.deathHealth == 12) {
                this.sprite = App.gremlin4;
            } else if (this.deathHealth == 16) {
                this.sprite = App.gremlin5;
            } else if (this.deathHealth >= 20) {
                this.killed = true;
                this.dying = false;
            }
            this.deathHealth++;
        }
    }

    /**
     * Performs a breadth-first search to find a path to the destination house.
     *
     * @param grid A 2D grid representing the game map.
     * @return A linked list of coordinates representing the path to the destination house.
     */
    public LinkedList<int[]> bfs(char[][] grid) {
        int numRows = grid.length;
        int numCols = grid[0].length;

        LinkedList<int[]> pathToHouse = new LinkedList<>();

        boolean[][] visited = new boolean[numRows][numCols];

        Queue<int[]> queue = new LinkedList<>();

        int[][][] parent = new int[numRows][numCols][2];

        int startY = (int) this.startX / App.CELLSIZE;
        int startX = (int) this.startY / App.CELLSIZE;

        queue.offer(new int[]{startX, startY});
        visited[startX][startY] = true;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];

            if (grid[x][y] == 'W') {
                int curX = x, curY = y;
                int index = 0;
                while (grid[curX][curY] != 'S') {
                    int[] cords = new int[2];
                    int parentX = parent[curX][curY][0];
                    int parentY = parent[curX][curY][1];
                    cords[0] = curX;
                    cords[1] = curY;
                    pathToHouse.add(index, cords);
                    curX = parentX;
                    curY = parentY;
                    index++;
                }
                return reversed(pathToHouse);
            }

            for (int i = 0; i < 4; i++) {
                int newX = x + dx[i];
                int newY = y + dy[i];

                if (newX >= 0 && newX < numRows && newY >= 0 && newY < numCols && !visited[newX][newY] && grid[newX][newY] != 'S' && grid[newX][newY] != ' ') {
                    queue.offer(new int[]{newX, newY});
                    visited[newX][newY] = true;
                    parent[newX][newY][0] = x;
                    parent[newX][newY][1] = y;
                }
            }
        }

        return null;
    }

    /**
     * Reverses the order of a linked list of coordinates.
     *
     * @param ls The linked list to be reversed.
     * @return The reversed linked list of coordinates.
     */
    public LinkedList<int[]> reversed(LinkedList<int[]> ls) {
        LinkedList<int[]> reverse = new LinkedList<>();
        int j = 0;
        for (int i = ls.size() - 1; i > 0; i--) {
            reverse.add(j, ls.get(i));
            j++;
        }

        reverse.add(0, ls.get(ls.size() - 1));

        return reverse;
    }
}
