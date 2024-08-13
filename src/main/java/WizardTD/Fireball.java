package WizardTD;

import processing.core.PImage;
import processing.core.PVector;
import processing.data.JSONObject;

public class Fireball {

    private PVector pos;
    private PImage projectileType;
    private float velX, velY;
    private int dmg;
    private JSONObject json;
    private boolean hit;

    public Fireball(JSONObject json, float x, float y, float velX, float velY, PImage projectileType) {
        this.json = json;
        this.pos = new PVector(x, y);
        this.projectileType = projectileType;
        this.velX = velX;
        this.velY = velY;

        this.dmg = this.json.getInt("initial_tower_damage");
    }

    /**
     * Move the fireball projectile's position based on its velocity.
     */
    public void move() {
        this.pos.x += this.velX;
        this.pos.y += this.velY;
    }

    /**
     * Get the current position of the fireball projectile.
     *
     * @return A PVector representing the current position.
     */
    public PVector getPos() {
        return pos;
    }

    /**
     * Get the image representing the type of the fireball projectile.
     *
     * @return The image of the fireball projectile.
     */
    public PImage getProjectileType() {
        return this.projectileType;
    }

    /**
     * Set the damage value for the fireball projectile.
     *
     * @param dmg The damage value to set.
     */
    public void setDmg(int dmg) {
        this.dmg = dmg;
    }

    /**
     * Set the hit status of the fireball projectile.
     *
     * @param hit true if the fireball has hit a target, false otherwise.
     */
    public void setHit(boolean hit) {
        this.hit = hit;
    }

    /**
     * Check if the fireball projectile has hit a target.
     *
     * @return true if the fireball has hit a target, false otherwise.
     */
    public boolean isHit() {
        return this.hit;
    }

    /**
     * Get the damage value of the fireball projectile.
     *
     * @return The damage value of the fireball.
     */
    public int getDmg() {
        return this.dmg;
    }
}
