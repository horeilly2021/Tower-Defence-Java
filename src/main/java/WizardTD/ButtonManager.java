package WizardTD;

import processing.core.PApplet;

import java.util.ArrayList;

public class ButtonManager {

    private ArrayList<MyButton> buttons;
    private PApplet parent;

    public ButtonManager(PApplet parent) {
        this.parent = parent;
        this.buttons = new ArrayList<>();
        addButtons();
    }

    /**
     * Add a MyButton to the list of buttons.
     *
     * @param button The MyButton object to add.
     */
    public void addButton(MyButton button) {
        this.buttons.add(button);
    }

    /**
     * Get the list of MyButton objects.
     *
     * @return An ArrayList of MyButton objects.
     */
    public ArrayList<MyButton> getButtons() {
        return this.buttons;
    }

    /**
     * Draw a list of buttons on the screen.
     *
     * @param buttons The list of buttons to draw.
     */
    public void drawButtons(ArrayList<MyButton> buttons) {
        for (MyButton button : buttons) {
            button.draw();
        }
    }

    /**
     * Adds side buttons to the buttons list
     */
    public void addButtons() {
        this.addButton(new MyButton(this.parent, "FF", "2x speed", 650, 64, 50, 50, 'f'));
        this.addButton(new MyButton(this.parent, "P", "PAUSE", 650, 128, 50, 50, 'p'));
        this.addButton(new MyButton(this.parent, "T", "Build\nTower", 650, 192, 50, 50, 't'));
        this.addButton(new MyButton(this.parent, "U1", "Upgrade\nrange", 650, 254, 50, 50, '1'));
        this.addButton(new MyButton(this.parent, "U2", "Upgrade\nspeed", 650, 318, 50, 50, '2'));
        this.addButton(new MyButton(this.parent, "U3", "Upgrade\ndamage", 650, 382, 50, 50, '3'));
        this.addButton(new MyButton(this.parent, "M", "Mana\npool", 650, 446, 50, 50, 'm'));
    }

    /**
     * Draw all the buttons in the list of buttons.
     */
    public void draw() {
        drawButtons(this.buttons);
    }

}
