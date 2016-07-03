package de.intektor.open_strategy.client.guis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.open_strategy.OpenStrategy;
import de.intektor.open_strategy.client.gui.Gui;
import de.intektor.open_strategy.client.gui.component.GuiButton;
import de.intektor.open_strategy.client.render.RenderHelper;

/**
 * @author Intektor
 */
public class MainMenu extends Gui {

    final int START_GAME = 0, OPTIONS = 1, EXIT_GAME = 2;

    @Override
    public void render(ShapeRenderer renderer, int mouseX, int mouseY) {
        OpenStrategy.spriteBatch.begin();
        RenderHelper.drawString(width / 2, height / 10 * 9, "Open Strategy", OpenStrategy.perfectPixel72, OpenStrategy.spriteBatch, true, true);
        OpenStrategy.spriteBatch.end();
        super.render(renderer, mouseX, mouseY);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void onButtonTouched(int id) {
        switch (id) {
            case START_GAME:
                os.enterGui(OpenStrategy.PLAY_SELECT);
                break;
            case OPTIONS:

                break;
            case EXIT_GAME:
                Gdx.app.exit();
                break;
        }
    }

    @Override
    public int getID() {
        return OpenStrategy.MAIN_MENU;
    }

    @Override
    public void addComponents() {
        int x = width / 4;
        int y = height / 2 / 3;
        addComponent(new GuiButton(x, y + height / 2 / 3 * 2, width / 2, height / 2 / 3, "Start Game", START_GAME, true));
        addComponent(new GuiButton(x, y + height / 2 / 3, width / 2, height / 2 / 3, "Options", OPTIONS, true));
        addComponent(new GuiButton(x, y, width / 2, height / 2 / 3, "Exit Game", EXIT_GAME, true));
    }
}
