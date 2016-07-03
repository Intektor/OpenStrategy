package de.intektor.open_strategy.client.gui.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.open_strategy.client.gui.Gui;

/**
 * @author Intektor
 */
public abstract class GuiComponent {

    protected int x, y, width, height, prevX, prevY;
    protected boolean isShown;
    protected boolean isHovered, isDragged;

    public static final Color STANDARD_OUTLINE_COLOR = new Color(0x999999ff);

    protected boolean prioritized;

    protected Gui gui;

    public GuiComponent(int x, int y, int width, int height, boolean isShown) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isShown = isShown;
    }

    public void updateComponent(int mouseX, int mouseY) {
        isHovered = isHoveredOver(mouseX, mouseY);
        if (isDragged) {
            isDragged = isHovered;
        }
    }

    public boolean isHoveredOver(int mouseX, int mouseY) {
        GuiComponent prioritizedComponent = gui.getPrioritizedComponent();
        return Gui.isPointInRegion(x, y, x + width, y + height, mouseX, Gdx.graphics.getHeight() - mouseY) && this.isShown && (prioritizedComponent == null || prioritizedComponent == this);
    }

    public void render(ShapeRenderer renderer, int mouseX, int mouseY) {
        if (isShown) {
            renderComponent(renderer, mouseX, mouseY);
        }
    }

    public void keyTyped(char c, boolean isPrioritized) {

    }

    public void keyDown(int keyCode, boolean isPrioritized) {

    }

    public void mouseScrolled(int mouseX, int mouseY, int amt, boolean isPrioritized) {

    }

    protected abstract void renderComponent(ShapeRenderer renderer, int mouseX, int mouseY);

    public GuiComponent setX(int x) {
        this.prevX = this.x;
        this.x = x;
        return this;
    }

    public GuiComponent setY(int y) {
        this.prevY = this.y;
        this.y = y;
        return this;
    }

    public GuiComponent setPosition(int x, int y) {
        this.prevX = this.x;
        this.prevY = this.y;
        this.x = x;
        this.y = y;
        return this;
    }

    public GuiComponent setWidth(int width) {
        this.width = width;
        return this;
    }

    public GuiComponent setHeight(int height) {
        this.height = height;
        return this;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isShown() {
        return isShown;
    }

    public GuiComponent setShown(boolean shown) {
        isShown = shown;
        return this;
    }

    public void onClicked(int x, int y) {

    }

    public void onDragged(int prevX, int prevY, int cX, int cY) {
        isDragged = true;
    }

    public void setPrioritized(boolean prioritized) {
        this.prioritized = prioritized;
    }

    public boolean isPrioritized() {
        return prioritized;
    }

    public void setGui(Gui gui) {
        this.gui = gui;
    }
}
