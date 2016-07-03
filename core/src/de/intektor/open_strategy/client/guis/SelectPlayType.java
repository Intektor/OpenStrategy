package de.intektor.open_strategy.client.guis;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.open_strategy.OpenStrategy;
import de.intektor.open_strategy.client.gui.Gui;
import de.intektor.open_strategy.client.gui.component.GuiButton;

/**
 * @author Intektor
 */
public class SelectPlayType extends Gui {

    final int CREATE_LOBBY = 0, CONNECT = 1;

    @Override
    public void render(ShapeRenderer renderer, int mouseX, int mouseY) {
        super.render(renderer, mouseX, mouseY);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void onButtonTouched(int id) {
        switch (id) {
            case CREATE_LOBBY:
                os.enterGui(OpenStrategy.CREATE_LOBBY);
                break;
            case CONNECT:
                os.enterGui(OpenStrategy.CONNECT);
                break;
        }
    }

    @Override
    public int getID() {
        return OpenStrategy.PLAY_SELECT;
    }

    @Override
    public void addComponents() {
        int x = width / 4;
        int y = height / 2 / 3;
        addComponent(new GuiButton(x, y + height / 2, width / 2, 40, "Create Lobby", CREATE_LOBBY, true));
        addComponent(new GuiButton(x, y + height / 2 - 40, width / 2, 40, "Connect", CONNECT, true));
    }
}
