package WizardTD;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;

import java.util.ArrayList;

public class TowerManager {

    private PApplet parent;
    private ArrayList<Tower> towers;
    private JSONObject json;

    public TowerManager(PApplet parent, JSONObject json) {
        this.parent = parent;
        this.json = json;

        this.towers = new ArrayList<>();
    }

    /**
     * Get the list of towers on the game board.
     *
     * @return An ArrayList of Tower objects.
     */
    public ArrayList<Tower> getTowers() {
        return this.towers;
    }

    /**
     * Add a new tower to the game board at the specified position.
     *
     * @param tower The Tower object to add.
     * @param xPos  The X-coordinate of the tower's position.
     * @param yPos  The Y-coordinate of the tower's position.
     */
    public void addTower(Tower tower, int xPos, int yPos) {
        this.towers.add(new Tower(this.parent, xPos, yPos, tower.getTowerType()));
    }

    /**
     * Draw the towers and their range circles on the game board.
     */
    public void draw() {
        for (Tower tower : this.towers) {
            this.parent.image(tower.getSprite(), tower.getX(), tower.getY());
            if (tower.isMouseOver() || tower.isMousePressed()) {
                float x = tower.getX() + App.HALF_CELL;
                float y = tower.getY() + App.HALF_CELL;
                tower.drawRange(x, y, 0);
            }
        }
    }

    /**
     * Check if a monster is within the range of a tower's attack.
     *
     * @param tower   The Tower object.
     * @param monster The Monster object to check.
     * @return true if the monster is in range, false otherwise.
     */
    public boolean isMonsterInRange(Tower tower, Monster monster) {
        int range = EdgePointsFinder.getHypoLength(tower.getX(), monster.getCurX(), tower.getY(), monster.getCurY());
        return range < tower.getRange();
    }
}
