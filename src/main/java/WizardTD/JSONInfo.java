package WizardTD;

import processing.data.JSONArray;
import processing.data.JSONObject;

public class JSONInfo {

    private int index;
    private JSONObject json;
    private JSONArray waves;
    private JSONObject currentWave;
    private JSONArray monsterInfo;
    private JSONObject currentMonsterInfo;

    private int waveAmount;

    private int quantity;
    private int hp;
    private int mana;
    private String type;
    private int speed;
    private float armour;
    private int amount;
    private int towerRange;

    public JSONInfo(JSONObject json, int index) {
        this.json = json;
        this.index = index;

        this.waves = getWaves();
        setCurrentWave(this.index);
        this.currentWave = getCurrentWave();
        this.currentMonsterInfo = getCurrentMonsterInfo();
    }

    /**
     * Get the index of the current wave.
     *
     * @return The index of the current wave.
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * Get the JSON array containing all waves.
     *
     * @return A JSONArray representing the waves.
     */
    public JSONArray getWaves() {
        this.waves = (JSONArray) this.json.get("waves");
        return this.waves;
    }

    /**
     * Get the total number of waves in the level.
     *
     * @return The total number of waves.
     */
    public int getWaveAmount() {
        return this.waves.size();
    }

    /**
     * Set the current wave by its index.
     *
     * @param index The index of the wave to set as the current wave.
     */
    public void setCurrentWave(int index) {
        this.currentWave = (JSONObject) this.waves.get(index);
    }

    /**
     * Get the JSON object representing the current wave.
     *
     * @return A JSONObject representing the current wave.
     */
    public JSONObject getCurrentWave() {
        return this.currentWave;
    }

    /**
     * Get the JSON array containing monster information for the current wave.
     *
     * @return A JSONArray containing monster information for the current wave.
     */
    public JSONArray getMonsterInfo() {
        return this.monsterInfo = (JSONArray) this.currentWave.get("monsters");
    }

    /**
     * Get the JSON object representing information about the first monster in the current wave.
     *
     * @return A JSONObject representing the information of the first monster in the current wave.
     */
    public JSONObject getCurrentMonsterInfo() {
        return this.currentMonsterInfo = (JSONObject) this.getMonsterInfo().get(0);
    }

    /**
     * Get the duration of the current wave.
     *
     * @return The duration of the current wave.
     */
    public int getWaveDuration() {
        return this.amount = (int) this.currentWave.get("duration");
    }

    /**
     * Get the pause time before the current wave.
     *
     * @return The pause time before the current wave.
     */
    public double getPauseTime() {
        Object preWavePauseObj = this.currentWave.get("pre_wave_pause");

        if (preWavePauseObj instanceof Double) {
            return (Double) preWavePauseObj;
        } else if (preWavePauseObj instanceof Integer) {
            return ((Integer) preWavePauseObj).doubleValue();
        } else {
            return 0.0;
        }
    }

    /**
     * Move to the next wave.
     */
    public void nextWave() {
        this.index = this.index + 1;
    }

    /**
     * Get the quantity of the first monster in the current wave.
     *
     * @return The quantity of the first monster in the current wave.
     */
    public int getQuantity() {
        return this.currentMonsterInfo.getInt("quantity");
    }

    /**
     * Get the hit points (HP) of the first monster in the current wave.
     *
     * @return The hit points (HP) of the first monster in the current wave.
     */
    public int getHp() {
        return this.currentMonsterInfo.getInt("hp");
    }

    /**
     * Get the mana gained on kill for the first monster in the current wave.
     *
     * @return The mana gained on kill for the first monster in the current wave.
     */
    public int getMana() {
        return this.currentMonsterInfo.getInt("mana_gained_on_kill");
    }

    /**
     * Get the type of the first monster in the current wave.
     *
     * @return The type of the first monster in the current wave.
     */
    public String getType() {
        return this.type = this.currentMonsterInfo.getString("type");
    }

    /**
     * Get the speed of the first monster in the current wave.
     *
     * @return The speed of the first monster in the current wave.
     */
    public int getSpeed() {
        return this.speed = this.currentMonsterInfo.getInt("speed");
    }

    /**
     * Get the armor value of the first monster in the current wave.
     *
     * @return The armor value of the first monster in the current wave.
     */
    public float getArmour() {
        return this.armour = this.currentMonsterInfo.getFloat("armour");
    }
}
