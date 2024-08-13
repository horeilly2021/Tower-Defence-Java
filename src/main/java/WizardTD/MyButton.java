package WizardTD;

import processing.core.PApplet;

public class MyButton {

    private final PApplet parent;
    private int x, y, width, height, id;
    private String text, desc;
    private boolean mouseOver, mousePressed;
    private char key;

    public MyButton(PApplet parent, String text, String desc, int x, int y, int width, int height, char key) {
        this.parent = parent;
        this.text = text;
        this.desc = desc;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.id = -1;
        this.key = key;
    }

    /**
     * Get the text displayed on the button.
     *
     * @return The text on the button.
     */
    public String getText() {
        return this.text;
    }

    /**
     * Draw the button with its body, border, text, and description.
     */
    public void draw() {
        drawBody();

        drawBorder();

        drawText();

        description();
    }

    /**
     * Draw the button's border.
     */
    private void drawBorder() {
        parent.strokeWeight(App.THICK);
        parent.stroke(App.BLACK); // Set stroke color to black
        parent.rect(x, y, width, height);
        if (mousePressed) {
            parent.rect(x + 1, y + 1, width - 2, height - 2);
            parent.rect(x + 2, y + 2, width - 4, height - 4);
        }
        parent.strokeWeight(App.THIN);
    }

    /**
     * Draw the button's body.
     */
    private void drawBody() {
        if (mousePressed) {
            parent.fill(255, 255, 0); // Yellow
        } else if (mouseOver) {
            parent.fill(200); // Grey
        } else {
            parent.fill(177, 165, 134); // Brown
        }
        parent.rect(x, y, width, height);
    }

    /**
     * Draw the button's text.
     */
    private void drawText() {
        parent.textSize(28);
        parent.fill(App.BLACK);
        parent.textAlign(App.CENTER, App.CENTER);
        parent.text(this.text, x + width / 2, y + height / 2);
    }

    /**
     * Display the description text of the button.
     */
    private void description() {
        parent.textSize(10);
        parent.fill(App.BLACK);
        parent.textAlign(App.LEFT, App.TOP);
        parent.text(this.desc, x + width + 4, y + 10);
        parent.textAlign(PApplet.CENTER, PApplet.CENTER);
    }

    /**
     * Set the text for the button.
     *
     * @param text The text to be set on the button.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Set the state of the button (pressed or not).
     *
     * @param mousePressed The state of the button being pressed.
     */
    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    /**
     * Set the state of the mouse being over the button.
     *
     * @param mouseOver The state of the mouse being over the button.
     */
    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    /**
     * Check if the button is currently pressed.
     *
     * @return true if the button is pressed, false otherwise.
     */
    public boolean isMousePressed() {
        return mousePressed;
    }

    /**
     * Get the width of the button.
     *
     * @return The width of the button.
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Get the height of the button.
     *
     * @return The height of the button.
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Get the X-coordinate of the button.
     *
     * @return The X-coordinate of the button.
     */
    public int getX() {
        return this.x;
    }

    /**
     * Get the Y-coordinate of the button.
     *
     * @return The Y-coordinate of the button.
     */
    public int getY() {
        return this.y;
    }

    /**
     * Get the key associated with the button.
     *
     * @return The key associated with the button.
     */
    public char getKey() {
        return this.key;
    }
}
