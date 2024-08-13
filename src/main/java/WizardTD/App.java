package WizardTD;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;
import processing.event.MouseEvent;

import java.util.*;

public class App extends PApplet {

    public static final int CELLSIZE = 32;
    public static final int SIDEBAR = 120;
    public static final int TOPBAR = 40;
    public static final int BOARD_WIDTH = 20;
    public static final int HALF_CELL = 16;
    public static int WIDTH = CELLSIZE*BOARD_WIDTH+SIDEBAR;
    public static int HEIGHT = BOARD_WIDTH*CELLSIZE+TOPBAR;
    public static final int BARWIDTH = 256;
    public static final int BARHEIGHT = 16;
    public static final int FPS = 60;
    public static final int THICK = 2;
    public static final int THIN = 1;
    public static final int BLACK = 0;
    public String configPath;
    public String level;
    public JSONObject json;
    public WaveDisplay waves;
    public Screen screen;
    public static PImage beetle;
    public static PImage fireball;
    public static PImage grass;
    public static PImage gremlin;
    public static PImage gremlin1;
    public static PImage gremlin2;
    public static PImage gremlin3;
    public static PImage gremlin4;
    public static PImage gremlin5;
    public static PImage path0;
    public static PImage path1;
    public static PImage path2;
    public static PImage path3;
    public static PImage shrub;
    public static PImage tower0;
    public static PImage tower1;
    public static PImage tower2;
    public static PImage wizard_house;
    public static PImage worm;
    public ButtonManager buttonManager;
    public ArrayList<MyButton> buttons;
    public int timer;
    public static boolean paused;
    public static boolean fast;
    public TowerManager towerManager;
    public Tower selectedTower;
    public Mana mana;
    public FireballManager fireballManager;
    public int counter;
    public int towerCost;
    public int currentMana;
    public int speed;
    public boolean gameOver = false;

     public App() {
         this.configPath = "config.json";
     }

    /**
     * Initialise the setting of the window size.
     */
	@Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Loads all images into the game
     */
    public void loadImgs() {
        beetle = loadImage("src/main/resources/WizardTD/beetle.png");
        fireball = loadImage("src/main/resources/WizardTD/fireball.png");
        grass = loadImage("src/main/resources/WizardTD/grass.png");
        gremlin = loadImage("src/main/resources/WizardTD/gremlin.png");
        gremlin1 = loadImage("src/main/resources/WizardTD/gremlin1.png");
        gremlin2 = loadImage("src/main/resources/WizardTD/gremlin2.png");
        gremlin3 = loadImage("src/main/resources/WizardTD/gremlin3.png");
        gremlin4 = loadImage("src/main/resources/WizardTD/gremlin4.png");
        gremlin5 = loadImage("src/main/resources/WizardTD/gremlin5.png");
        path0 = loadImage("src/main/resources/WizardTD/path0.png");
        path1 = loadImage("src/main/resources/WizardTD/path1.png");
        path2 = loadImage("src/main/resources/WizardTD/path2.png");
        path3 = loadImage("src/main/resources/WizardTD/path3.png");
        shrub = loadImage("src/main/resources/WizardTD/shrub.png");
        tower0 = loadImage("src/main/resources/WizardTD/tower0.png");
        tower1 = loadImage("src/main/resources/WizardTD/tower1.png");
        tower2 = loadImage("src/main/resources/WizardTD/tower2.png");
        wizard_house = loadImage("src/main/resources/WizardTD/wizard_house.png");
        worm = loadImage("src/main/resources/WizardTD/worm.png");
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player, enemies and map elements.
     */
	@Override
    public void setup() {
        frameRate(FPS);

        this.json = new JsonFileReader(this).readJsonFile(this.configPath);
        this.level = this.json.getString("layout");

        this.screen = new Screen(this, level);
        this.waves = new WaveDisplay(this, this.json);
        this.buttonManager = new ButtonManager(this);
        this.towerManager = new TowerManager(this, this.json);
        this.mana = new Mana(this);
        this.fireballManager = new FireballManager(this, this.json);

        this.towerCost = this.json.getInt("tower_cost");
        this.buttons = buttonManager.getButtons();
        this.currentMana = this.mana.getCurrentMana();
        this.timer = 1;
        this.counter = 0;
        this.speed = 1;

        paused = false;
        fast = false;

        loadImgs();
    }

    /**
     * Receive key pressed signal from the keyboard.
     */
	@Override
    public void keyPressed(){

        char lowerKey = Character.toLowerCase(key);

        if (lowerKey == 'p') {
            paused = !paused;
        } else if (lowerKey == 't') {
            selectedTower = new Tower(this, 0, 0, "tower0");
            setSelectedTower(selectedTower);
        } else if (lowerKey == 'f') {
            fast = !fast;
        }

        for (MyButton button : this.buttons) {
            if (lowerKey == button.getKey()) {
                button.setMousePressed(!button.isMousePressed());
                if (button.getKey() == 't' && !button.isMousePressed()) {
                    selectedTower = null;
                }
            }
        }
        
    }

    /**
     * Set the selected tower for placement.
     *
     * @param selectedTower The tower to be selected.
     */
    public void setSelectedTower(Tower selectedTower) {
        if (this.towerCost <= this.currentMana) {
            this.selectedTower = selectedTower;
        }
    }

    /**
     * Draw the selected tower at the current mouse position.
     */
    public void drawSelectedTower() {
        if (selectedTower != null) {
            image(selectedTower.getSprite(), mouseX - HALF_CELL, mouseY - TOPBAR - HALF_CELL);
            selectedTower.drawRange(mouseX, mouseY, TOPBAR);
        }
    }

    /**
     * Handle mouse button press event.
     *
     * @param e The MouseEvent representing the mouse button press.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == 37) {
            for (MyButton button : this.buttons) {
                if ((button.getX() < e.getX()) && (e.getX() < button.getX() + button.getWidth())) {
                    if ((button.getY() < e.getY()) && (e.getY() < button.getHeight() + button.getY())) {
                        button.setMousePressed(!button.isMousePressed());

                        if (Objects.equals(button.getText(), "P")) paused = !paused;
                        if (Objects.equals(button.getText(), "FF")) fast = !fast;
                        if (Objects.equals(button.getText(), "T")) {
                            selectedTower = new Tower(this, 0, 0, "tower0");
                            setSelectedTower(selectedTower);
                        }
                    }
                }
            }

            for (Tower tower : this.towerManager.getTowers()) {

                if (mouseX >= tower.getX() && mouseX <= tower.getX() + tower.getSprite().width &&
                        mouseY >= tower.getY() + tower.getSprite().height && mouseY <= tower.getY() + 2 * tower.getSprite().height) {
                    tower.setMousePressed(!tower.isMousePressed());
                }
            }
        }
    }

    /**
     * Handle mouse click event.
     *
     * @param e The MouseEvent representing the mouse click.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (selectedTower != null) {
            if (this.towerCost > this.currentMana) {
                selectedTower = null;
            }
            if (EdgePointsFinder.isGrassTile(this.level, (mouseX / CELLSIZE), (mouseY / CELLSIZE))) {
                for (Tower tower : this.towerManager.getTowers()) {
                    if (mouseX >= tower.getX() && mouseX <= tower.getX() + tower.getSprite().width &&
                            mouseY >= tower.getY() + tower.getSprite().height && mouseY <= tower.getY() + 2 * tower.getSprite().height) {
                        System.out.println("CANNOT PLACE HERE");
                        selectedTower = null;
                    }
                }
                if (selectedTower != null) {
                    towerManager.addTower(selectedTower, mouseX - HALF_CELL, mouseY - TOPBAR - HALF_CELL);
                    this.mana.decreaseMana(this.towerCost);
                }
            }
        }
        selectedTower = null;
        this.buttons.get(2).setMousePressed(false);
    }

    /**
     * Handle mouse moved event. Updates button and tower mouse over states and displays tower description.
     *
     * @param e The MouseEvent representing the mouse moved event.
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        for (MyButton button : buttons) {
            if (mouseX >= button.getX() && mouseX <= button.getX() + button.getWidth() &&
                    mouseY >= button.getY() && mouseY <= button.getY() + button.getHeight()) {
                 button.setMouseOver(true);
                if (button.getKey() == 't') {
                    towerDesc(button, "cost");
                }
            } else {
                button.setMouseOver(false);
            }
        }

        for (Tower tower : this.towerManager.getTowers()) {

            if (mouseX >= tower.getX() && mouseX <= tower.getX() + tower.getSprite().width &&
                    mouseY >= tower.getY() + tower.getSprite().height && mouseY <= tower.getY() + 2 * tower.getSprite().height) {
                tower.setMouseOver(true);
            } else {
                tower.setMouseOver(false);
            }
        }
    }


    /**
     * Display tower description.
     *
     * @param button The button for which the description is displayed.
     * @param text   The description text to be displayed.
     */
    public void towerDesc(MyButton button, String text) {
        fill(255);
        rect(button.getX() - 80, button.getY(), 65, 20);
        fill(0);
        textSize(12);
        text(text + ": " + this.towerCost, button.getX() - 48, button.getY() + 8);
    }

    /**
     * Shoot a fireball from the given tower towards the specified monster.
     *
     * @param tower   The tower shooting the fireball.
     * @param monster The target monster.
     */
    public void shootMonster(Tower tower, Monster monster) {
        this.fireballManager.newFireball(tower, monster);
    }

    /**
     * Attack enemies within the range of towers by shooting fireballs at them.
     *
     * @param monsters The list of monsters to target.
     * @param towers   The list of towers that will attack the monsters.
     */
    public void attackEnemy(ArrayList<Monster> monsters, ArrayList<Tower> towers) {
        if (this.counter == FPS) {
            for (Tower tower : towers) {
                for (Monster monster : monsters) {
                    if (this.towerManager.isMonsterInRange(tower, monster) && monster.isAlive()) {
                        shootMonster(tower, monster);
                         break;
                    }
                }
            }
            this.counter = 0;
        }
        this.counter++;
    }

    /**
     * Increase the current mana for each monster that has been killed
     */
    public void collectMana() {
        for (Monster monster : this.waves.getMonsters()) {
            if (monster.isKilled() && !monster.hasCollectedMana()) {
                monster.setCollectedMana(true);
                this.mana.increaseMana(monster.getManaGainedOnKill());
            }
        }
    }

    /**
     * Reduce the current mana for each monster if the monster has reached the wizard house
     */
    public void loseMana() {
        for (Monster monster : this.waves.getMonsters()) {
            if (monster.hasReached() && !monster.hasCollectedMana()) {
                monster.setCollectedMana(true);
                this.mana.decreaseMana(monster.getManaGainedOnKill());
            }
        }
    }

    /**
     * Draw all elements in the game by current frame.
     */
	@Override
    public void draw() {

        background(177, 165, 134);  // Brown color
        this.buttonManager.draw();

        translate(0, TOPBAR);
        this.screen.display();
        this.waves.display();
        this.towerManager.draw();
        drawSelectedTower();
        this.fireballManager.draw(this.waves.getMonsters());
        if (!paused) attackEnemy(this.waves.getMonsters(), this.towerManager.getTowers());
        this.currentMana = this.mana.getCurrentMana();
        collectMana();
        loseMana();


        if (!paused) {
            if (fast) this.speed = 2;
            if (!fast) this.speed = 1;
            if (this.timer * this.speed % FPS == 0) {
                this.mana.increaseMana(this.mana.getManaPerSecond());
            }
            this.timer++;
        }
        translate(0, -TOPBAR);

        textSize(24); // Set the text size
        fill(BLACK); // Set the text color (black)
        text("Wave " + this.waves.getWaveCounter() + " starts in " + (int) (this.waves.getTestCounter() / FPS), 114, 20);

        textSize(24);
        fill(BLACK);
        text("MANA:", 416, 20);
        drawMana();

        if (this.currentMana < 0 && !gameOver) {
            System.out.println("GAME OVER");
            paused = true;
            gameOver = true;
        }
    }

    /**
     * Draw mana bar in the top right corner representing the players current vs max mana
     */
    public void drawMana() {
        strokeWeight(THICK);
        fill(255, 255, 255);
        rect(464, 15, BARWIDTH, BARHEIGHT);

        float mana = map(this.mana.getMaxMana(), 0, this.mana.getMaxMana(), 0, this.currentMana);
        float percentageBar = (mana / this.mana.getMaxMana()) * BARWIDTH;
        if (percentageBar >= BARWIDTH) percentageBar = BARWIDTH;
        if (percentageBar <= 0) {
            percentageBar = 0;
        }

        fill(97, 211, 217);
        rect(464, 15, percentageBar, BARHEIGHT);
        strokeWeight(THIN);
        int displayMana = this.currentMana;
        if (displayMana <= 0) displayMana = 0;

        fill(BLACK);
        textSize(15);
        float textX = 480 + (float) BARWIDTH / 2 - textWidth("mana") / 2;
        float textY = 6 + (float) BARHEIGHT / 2 + textAscent() / 2;
        text( displayMana + "/" + this.mana.getMaxMana(), textX, textY);
    }

    public static void main(String[] args) {
        System.out.println("Starting game...");
        PApplet.main("WizardTD.App");
    }
}