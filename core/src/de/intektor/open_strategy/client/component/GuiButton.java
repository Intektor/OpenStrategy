package de.intektor.open_strategy.client.component;

/**
 * @author Intektor
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.open_strategy.OpenStrategy;

import java.util.List;


public class GuiButton extends GuiComponent {
    public String description;
    public int id;
    public Color[] colors = new Color[6];
    public boolean drawImage, enabled = true;

    public GuiButton(float x, float y, float width, float height, String des, int id, boolean show) {
        this((int) x, (int) y, (int) width, (int) height, des, id, show);
    }

    public GuiButton(int x, int y, int width, int height, String des, int id, boolean show) {
        this(x, y, width, height, des, id, Color.WHITE, new Color(0x88888888), Color.WHITE, new Color(0x999999ff), Color.RED, new Color(0x22222288), show);
    }

    public GuiButton(int x, int y, int width, int height, String des, int id, Color c1, Color c2, Color c3, Color c4, Color c5, Color c6, boolean show) {
        super(x, y, width, height, show);
        this.description = des;
        this.id = id;
        this.colors[0] = c1;
        this.colors[1] = c2;
        this.colors[2] = c3;
        this.colors[3] = c4;
        this.colors[4] = c5;
        this.colors[5] = c6;
    }

    protected Texture image;

    public GuiButton(float x, float y, float width, float height, int id, boolean show, Texture image) {
        this((int) x, (int) y, (int) width, (int) height, id, show, image);
    }

    public GuiButton(int x, int y, int width, int height, int id, boolean show, Texture image) {
        super(x, y, width, height, show);
        this.id = id;
        this.image = image;
        drawImage = true;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public void renderComponent(ShapeRenderer renderer) {
        if (this.isShown) {
            if (!drawImage) {
                Gdx.gl.glEnable(GL20.GL_BLEND);
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                renderer.begin();
                renderer.set(ShapeRenderer.ShapeType.Line);
                if (enabled) {
                    if (!this.isHovered) {
                        renderer.setColor(this.colors[0]);
                    } else {
                        renderer.setColor(this.colors[2]);
                    }
                } else {
                    renderer.setColor(this.colors[4]);
                }
                renderer.rect((float) this.x, (float) this.y, (float) this.width, (float) this.height);
                renderer.identity();
                renderer.set(ShapeRenderer.ShapeType.Filled);
                if (enabled) {
                    if (!this.isHovered) {
                        renderer.setColor(this.colors[1]);
                    } else {
                        renderer.setColor(this.colors[3]);
                    }
                } else {
                    renderer.setColor(this.colors[5]);
                }
                renderer.rect((float) (this.x + 1), (float) (this.y + 1), (float) (this.width - 2), (float) (this.height - 2));
                renderer.end();
                Gdx.gl.glDisable(GL20.GL_BLEND);
                OpenStrategy.perfectPixel12.setColor(Color.WHITE);
                OpenStrategy.layout.setText(OpenStrategy.perfectPixel12, getDescription());
                OpenStrategy.spriteBatch.begin();
                OpenStrategy.perfectPixel12.draw(OpenStrategy.spriteBatch, getDescription(), x + width / 2 - OpenStrategy.layout.width / 2, y + height / 2 + OpenStrategy.layout.height / 2);
                OpenStrategy.spriteBatch.end();
            } else {
                if (image != null) {
                    Batch batch = OpenStrategy.spriteBatch;
                    batch.begin();
                    batch.draw(image, x, y, width, height);
                    batch.end();
                }
            }
        }
    }

    public void setImage(Texture image) {
        this.image = image;
    }

    public static class GuiButtonSwitchONOFF extends GuiButton {

        boolean on;

        public GuiButtonSwitchONOFF(float x, float y, float width, float height, String des, int id, boolean show, boolean on) {
            super(x, y, width, height, des, id, show);
            this.on = on;
        }

        public GuiButtonSwitchONOFF(int x, int y, int width, int height, String des, int id, boolean show, boolean on) {
            super(x, y, width, height, des, id, show);
            this.on = on;
        }

        public GuiButtonSwitchONOFF(int x, int y, int width, int height, String des, int id, Color c1, Color c2, Color c3, Color c4, Color c5, Color c6, boolean show, boolean on) {
            super(x, y, width, height, des, id, c1, c2, c3, c4, c5, c6, show);
            this.on = on;
        }

        @Override
        public String getDescription() {
            return description + " " + (on ? "ON" : "OFF");
        }

        @Override
        public void onClicked(int x, int y) {
            super.onClicked(x, y);
            setOn(!on);
        }

        public GuiButtonSwitchONOFF setOn(boolean on) {
            this.on = on;
            colors[0] = colors[2] = on ? Color.GREEN : Color.RED;
            return this;
        }

        public boolean isOn() {
            return on;
        }
    }

    public static class GuiButtonSwitch extends GuiButton {

        List<String> list;
        int current;

        public GuiButtonSwitch(float x, float y, float width, float height, int id, boolean show, List<String> list, int startPoint) {
            super(x, y, width, height, list.get(startPoint), id, show);
            this.list = list;
            this.current = startPoint;
        }

        @Override
        public void onClicked(int x, int y) {
            super.onClicked(x, y);
            int next;
            if (current + 1 == list.size()) {
                next = 0;
            } else {
                next = current + 1;
            }
            description = list.get(next);
            current = next;
        }

        public int getCurrentState() {
            return current;
        }

        public GuiButtonSwitch setCurrentState(int current) {
            this.current = current;
            description = list.get(current);
            return this;
        }
    }
}
