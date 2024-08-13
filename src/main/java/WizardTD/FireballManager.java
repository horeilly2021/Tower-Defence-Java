package WizardTD;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;

import java.util.ArrayList;

public class FireballManager {

    private PApplet parent;
    private PImage sprite;
    private JSONObject json;
    private ArrayList<Fireball> fireballs;
    private int damage;
    public static final float SPEED = 5f;

    public FireballManager(PApplet parent, JSONObject json) {
        this.parent = parent;
        this.json = json;
        this.sprite = getSprite();

        this.fireballs = new ArrayList<>();
        this.damage = this.json.getInt("initial_tower_damage");
    }

    /**
     * Get the image sprite for the tower's fireball projectile.
     *
     * @return The image sprite of the fireball.
     */
    private PImage getSprite() {
        return this.parent.loadImage("src/main/resources/WizardTD/fireball.png");
    }

    /**
     * Create a new fireball projectile fired from the tower towards a monster.
     *
     * @param tower The tower firing the fireball.
     * @param monster The target monster.
     */
    public void newFireball(Tower tower, Monster monster) {
        int xDist = (int) Math.abs(tower.getX() - monster.getCurX());
        int yDist = (int) Math.abs(tower.getY() - monster.getCurY());
        int totalDist = xDist + yDist;

        float xPer = (float) xDist / totalDist;

        float velX = xPer * SPEED;
        float velY = SPEED - velX;

        if (tower.getX() > monster.getCurX()) {
            velX *= -1;
        }

        if (tower.getY() > monster.getCurY()) {
            velY *= -1;
        }

        this.fireballs.add(new Fireball(this.json, tower.getX() + App.HALF_CELL, tower.getY() + App.HALF_CELL, velX, velY, this.sprite));
    }

    /**
     * Update the state of fireball projectiles, including their movement and collision with monsters.
     *
     * @param monsters The list of active monsters in the game.
     */
    public void update(ArrayList<Monster> monsters) {
        for (Fireball fireball : this.fireballs) {
            if (!App.paused) fireball.move();
            if (monsterHit(fireball, monsters)) {
                fireball.setHit(true);
                fireball.setDmg(0);
            }
        }
    }

    /**
     * Check if a fireball has hit any monsters and apply damage if so.
     *
     * @param fireball The fireball projectile to check for hits.
     * @param monsters The list of active monsters in the game.
     * @return true if the fireball hits a monster, false otherwise.
     */
    private boolean monsterHit(Fireball fireball, ArrayList<Monster> monsters) {
        for (Monster monster : monsters) {
            if (fireball.getPos().x >= monster.getCurX() && fireball.getPos().x <= monster.getCurX() + monster.getSprite().width &&
                    fireball.getPos().y >= monster.getCurY() && fireball.getPos().y <= monster.getCurY() + monster.getSprite().height) {
                monster.damage(fireball.getDmg());
                return true;
            }
        }
        return false;
    }

    /**
     * Draw the fireball projectiles on the screen and update their state.
     *
     * @param monsters The list of active monsters in the game.
     */
    public void draw(ArrayList<Monster> monsters) {
        for (Fireball fireball : fireballs) {
            if (!fireball.isHit()) this.parent.image(fireball.getProjectileType(), fireball.getPos().x, fireball.getPos().y);
        }
        update(monsters);
    }
}
