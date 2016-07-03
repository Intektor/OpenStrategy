package de.intektor.open_strategy.client.gui.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.open_strategy.OpenStrategy;
import de.intektor.open_strategy.client.gui.Gui;
import de.intektor.open_strategy.client.render.RenderHelper;

import java.util.List;

/**
 * @author Intektor
 */
public class GuiMultiSwitch extends GuiComponent {

    List<String> choices;
    int currentlyChosen;
    boolean activated;

    public final int id;

    String prefix;

    public GuiMultiSwitch(int x, int y, int width, int height, boolean isShown, List<String> choices, int standard, int id, String prefix) {
        super(x, y, width, height, isShown);
        this.choices = choices;
        this.currentlyChosen = standard;
        this.id = id;
        this.prefix = prefix;
    }

    @Override
    public void onClicked(int mouseX, int mouseY) {
        int min = Math.min(width, height);
        if (Gui.isPointInRegion(x + width - min, y, x + width, y + min, mouseX, Gdx.graphics.getHeight() - mouseY)) {
            activated = !activated;
            gui.setComponentPrioritized(activated ? this : null);
        }
        if (activated) {
            int localY = y - height;
            for (int i = 0; i < choices.size(); i++) {
                if (Gui.isPointInRegion(x, localY, x + width, localY + height, mouseX, Gdx.graphics.getHeight() - mouseY)) {
                    gui.multiSwitchChanged(this, currentlyChosen, i);
                    currentlyChosen = i;
                    gui.setComponentPrioritized(null);
                    activated = false;
                    break;
                }
                localY -= height;
            }
        }
    }

    @Override
    public boolean isHoveredOver(int mouseX, int mouseY) {
        GuiComponent prioritizedComponent = gui.getPrioritizedComponent();
        return Gui.isPointInRegion(x, activated ? y - height * (choices.size()) : y, x + width, y + height, mouseX, Gdx.graphics.getHeight() - mouseY) && this.isShown && (prioritizedComponent == null || prioritizedComponent == this);

    }

    @Override
    protected void renderComponent(ShapeRenderer renderer, int mouseX, int mouseY) {
        renderer.begin();

        //Outer Line
        renderer.set(ShapeRenderer.ShapeType.Line);
        renderer.setColor(STANDARD_OUTLINE_COLOR);
        renderer.rect(x, y, width, height);

        renderer.identity();

        //Inner rect
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(new Color(0x22222288));
        renderer.rect(x + 1, y + 1, width - 2, height - 2);

        renderer.identity();

        //inner rect for arrow
        int min = Math.min(width, height);
        renderer.set(ShapeRenderer.ShapeType.Line);
        renderer.setColor(STANDARD_OUTLINE_COLOR);
        renderer.rect(x + width - min, y + height - min, min, min);

        renderer.identity();

        //arrow
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(!Gui.isPointInRegion(x + width - min, y, x + width, y + min, mouseX, Gdx.graphics.getHeight() - mouseY) ? STANDARD_OUTLINE_COLOR : new Color(0xeeeeeeff));
        if (!activated) {
            renderer.triangle(x + width - min, y + min, x + width - min / 2, y, x + width, y + min);
        } else {
            renderer.triangle(x + width - min, y, x + width - min / 2, y + min, x + width, y);
        }

        renderer.end();

        if (activated) {
            int localY = y - height;
            //the choices
            for (String choice : choices) {
                renderer.identity();

                renderer.begin();

                //Outer Line
                renderer.set(ShapeRenderer.ShapeType.Line);
                renderer.setColor(STANDARD_OUTLINE_COLOR);
                renderer.rect(x, localY, width, height);

                renderer.identity();

                //Inner rect
                renderer.set(ShapeRenderer.ShapeType.Filled);
                renderer.setColor(!Gui.isPointInRegion(x, localY, x + width, localY + height, mouseX, Gdx.graphics.getHeight() - mouseY) ? new Color(0x22222288) : new Color(0x999999ff));
                renderer.rect(x + 1, localY + 1, width - 2, height - 2);

                renderer.end();

                OpenStrategy.spriteBatch.begin();
                RenderHelper.drawString(x + width / 2, localY + height / 2, choice, OpenStrategy.perfectPixel16, OpenStrategy.spriteBatch, true, true);
                OpenStrategy.spriteBatch.end();

                localY -= height;
            }
        }


        OpenStrategy.spriteBatch.begin();
        RenderHelper.drawString(x + width / 2, y + height / 2, prefix + choices.get(currentlyChosen), OpenStrategy.perfectPixel16, OpenStrategy.spriteBatch, true, true);
        OpenStrategy.spriteBatch.end();
    }

    public int getCurrentlyChosen() {
        return currentlyChosen;
    }
}
