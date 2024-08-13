package WizardTD;

import processing.core.PApplet;
import processing.data.JSONObject;

public class Mana {

    private PApplet parent;
    private int initialMana;
    private int maxMana;
    private int currentMana;
    private int manaPerSecond;
    private JSONObject json;

    public Mana(PApplet parent) {
        this.parent = parent;

        this.json = new JsonFileReader(this.parent).readJsonFile("config.json");
        this.maxMana = this.json.getInt("initial_mana_cap");
        this.manaPerSecond = this.json.getInt("initial_mana_gained_per_second");

        this.initialMana = this.json.getInt("initial_mana");
        this.currentMana = initialMana;
    }

    /**
     * Get the maximum mana capacity.
     *
     * @return The maximum mana capacity.
     */
    public int getMaxMana() {
        return this.maxMana;
    }

    /**
     * Get the current mana amount.
     *
     * @return The current amount of mana.
     */
    public int getCurrentMana() {
        return this.currentMana;
    }

    /**
     * Get the rate at which mana regenerates per second.
     *
     * @return The rate of mana regeneration per second.
     */
    public int getManaPerSecond() {
        return manaPerSecond;
    }

    /**
     * Increase the current mana by a specified amount, up to the maximum mana capacity.
     *
     * @param amount The amount of mana to increase.
     */
    public void increaseMana(int amount) {
        if (this.currentMana < this.maxMana) {
            this.currentMana += amount;
        } else {
            this.currentMana = this.maxMana;
        }
    }

    /**
     * Decrease the current mana by a specified amount.
     *
     * @param amount The amount of mana to decrease.
     */
    public void decreaseMana(int amount) {
        this.currentMana -= amount;
    }
}
