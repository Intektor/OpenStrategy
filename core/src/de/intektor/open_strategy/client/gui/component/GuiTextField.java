package de.intektor.open_strategy.client.gui.component;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.open_strategy.OpenStrategy;
import de.intektor.open_strategy.client.gui.Gui;
import de.intektor.open_strategy.client.render.RenderHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Intektor
 */
public class GuiTextField extends GuiComponent {

    protected int maxChars, id;
    protected boolean allowLetters, allowNumbers, allowDots, activated;

    protected List<Character> text;
    protected String info = "";

    public GuiTextField(int x, int y, int width, int height, int id, boolean isShown, int maxChars, boolean allowLetters, boolean allowNumbers, boolean allowDots, String standard, String info) {
        super(x, y, width, height, isShown);
        this.id = id;
        this.maxChars = maxChars;
        this.allowLetters = allowLetters;
        this.allowNumbers = allowNumbers;
        this.allowDots = allowDots;
        this.info = info;
        text = new ArrayList<>();
        for (char c : standard.toCharArray()) {
            text.add(c);
        }
    }

    @Override
    public void renderComponent(ShapeRenderer renderer, int mouseX, int mouseY) {
        if (isShown()) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            renderer.begin();
            renderer.set(ShapeRenderer.ShapeType.Line);
            renderer.setColor(isHovered ? Color.RED : Color.ORANGE);
            renderer.rect(x, y, width, height);
            renderer.identity();
            renderer.set(ShapeRenderer.ShapeType.Filled);
            renderer.setColor(new Color(0x88888888));
            renderer.rect(x + 1, y + 1, width - 2, height - 2);
            renderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
            OpenStrategy.spriteBatch.begin();
            RenderHelper.drawString(x + width / 2, y + height / 4 * 3, info, OpenStrategy.perfectPixel12, OpenStrategy.spriteBatch, true, true);
            RenderHelper.drawString(x + width / 2, y + height / 4, convertText(), OpenStrategy.perfectPixel12, OpenStrategy.spriteBatch, true, true);
            OpenStrategy.spriteBatch.end();
        }
    }

    @Override
    public void keyDown(int keyCode, boolean isPrioritized) {
    }

    @Override
    public void keyTyped(char c, boolean isPrioritized) {
        if (activated) {
            if (c == '\b') {
                if (text.size() > 0) {
                    text.remove(text.size() - 1);
                }
            } else {
                if (!Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)) {
                    addCharacter(c);
                }
            }
        }
    }

    public void addCharacter(char c) {
        if (text.size() < maxChars) {
            boolean isDigit = (c >= '0' && c <= '9') || (allowDots && c == '.');
            if ((isDigit && allowNumbers) || ((Character.isLetter(c) || c == ' ') && allowLetters)) {
                text.add(c);
            }
        }
    }

    @Override
    public void clicked(int mouseX, int mouseY, int button) {
        if (Gui.isPointInRegion(x, y, x + width, y + height, mouseX, Gdx.graphics.getHeight() - mouseY)) {
            activated = true;
        } else {
            activated = false;
        }
    }

    public void removeChar() {
        if (text.size() > 0) {
            text.remove(text.size() - 1);
        }
    }

    public String convertText() {
        String s = "";
        for (Character c : text) {
            s += c;
        }
        return s;
    }

    @Override
    public void clickedOnComponent(int x, int y, int button) {

    }

    public void setText(String text) {
        this.text.clear();
        for (char c : text.toCharArray()) {
            this.text.add(c);
        }
    }

    public int getId() {
        return id;
    }
}
