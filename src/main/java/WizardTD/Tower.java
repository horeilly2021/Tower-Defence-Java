package WizardTD;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;

public class Tower {

    private int x, y;
    private String towerType;
    private PApplet parent;
    private int damage, range;
    private double cooldown;
    private JSONObject json;

    private boolean mouseOver, mousePressed;

    public Tower(PApplet parent, int x, int y, String towerType) { // add id potentially
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.towerType = towerType;
        this.json = new JsonFileReader(this.parent).readJsonFile("config.json");

        setDefaultDmg();
        setDefaultRange();
        setDefaultCooldown();

    }

    /**
     * Set the default cooldown (firing speed) of the tower based on configuration.
     */
    private void setDefaultCooldown() {
        this.cooldown = this.json.getDouble("initial_tower_firing_speed");
    }

    /**
     * Set the default range of the tower based on configuration.
     */
    private void setDefaultRange() {
        this.range = this.json.getInt("initial_tower_range");
    }

    /**
     * Set the default damage of the tower based on configuration.
     */
    private void setDefaultDmg() {
        this.damage = this.json.getInt("initial_tower_damage");
    }

    /**
     * Get the range of the tower.
     *
     * @return The range of the tower.
     */
    public int getRange() {
        return this.range;
    }

    /**
     * Get the X-coordinate of the tower's position.
     *
     * @return The X-coordinate of the tower.
     */
    public int getX() {
        return x;
    }

    /**
     * Get the Y-coordinate of the tower's position.
     *
     * @return The Y-coordinate of the tower.
     */
    public int getY() {
        return y;
    }

    /**
     * Get the type of the tower.
     *
     * @return The type of the tower.
     */
    public String getTowerType() {
        return towerType;
    }

    /**
     * Get the sprite (image) representing the tower type.
     *
     * @return The sprite image of the tower.
     */
    public PImage getSprite() {
        return this.parent.loadImage("src/main/resources/WizardTD/" + this.towerType + ".png");
    }

    /**
     * Set the mouse-over status for the tower.
     *
     * @param mouseOver true if the mouse is over the tower, false otherwise.
     */
    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    /**
     * Check if the mouse is over the tower.
     *
     * @return true if the mouse is over the tower, false otherwise.
     */
    public boolean isMouseOver() {
        return mouseOver;
    }

    /**
     * Set the mouse-pressed status for the tower.
     *
     * @param mousePressed true if the mouse is pressed on the tower, false otherwise.
     */
    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    /**
     * Check if the mouse is pressed on the tower.
     *
     * @return true if the mouse is pressed on the tower, false otherwise.
     */
    public boolean isMousePressed() {
        return mousePressed;
    }

    /**
     * Draw the range (a circular area) of the tower at the specified position.
     *
     * @param posX     The X-coordinate of the position to draw the range around.
     * @param posY     The Y-coordinate of the position to draw the range around.
     * @param offset   An offset value for the center of the tower.
     */
    public void drawRange(float posX, float posY, int offset) {
        float centerTowerY = posY - offset;
        float radius = this.getRange();

        this.parent.noFill();
        this.parent.strokeWeight(App.THICK);
        this.parent.stroke(255, 255, 0);
        this.parent.ellipse(posX, centerTowerY, radius * 2, radius * 2);
        this.parent.strokeWeight(App.THIN);
        this.parent.stroke(0);
    }
}
