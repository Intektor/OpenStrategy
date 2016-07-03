package de.intektor.open_strategy.client.gui.component;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.open_strategy.OpenStrategy;
import de.intektor.open_strategy.client.render.RenderHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Intektor
 */
public class GuiTextField extends GuiComponent {

    protected int maxChars, id;
    protected boolean allowLetters, allowNumbers;

    protected List<Character> text;
    protected String info = "";

    public GuiTextField(int x, int y, int width, int height, int id, boolean isShown, int maxChars, boolean allowLetters, boolean allowNumbers, String info, String standard) {
        super(x, y, width, height, isShown);
        this.id = id;
        this.maxChars = maxChars;
        this.allowLetters = allowLetters;
        this.allowNumbers = allowNumbers;
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
        if (isPrioritized) {
            if (keyCode == Input.Keys.BACKSPACE) {
                removeChar();

            } else if (keyCode == Input.Keys.ENTER) {
                gui.setComponentPrioritized(null);
            }
        }
    }

    @Override
    public void keyTyped(char c, boolean isPrioritized) {
        if (isPrioritized) {
            addCharacter(c);
        }
    }

    public void addCharacter(char c) {
        if (text.size() < maxChars) {
            boolean isDigit = (c >= '0' && c <= '9') || c == '.';
            if ((isDigit && allowNumbers) || ((Character.isLetter(c) || c == ' ') && allowLetters)) {
                text.add(c);
            }
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
    public void onClicked(int x, int y) {
        gui.setComponentPrioritized(this);
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
