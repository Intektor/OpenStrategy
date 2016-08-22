package de.intektor.open_strategy.client.guis;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.open_strategy.OpenStrategy;
import de.intektor.open_strategy.client.chat.LobbyChat;
import de.intektor.open_strategy.client.gui.Gui;
import de.intektor.open_strategy.client.gui.component.GuiMultiSwitch;
import de.intektor.open_strategy.client.gui.component.GuiTextField;

/**
 * @author Intektor
 */
public class GuiLobby extends Gui {

    public LobbyChat chat;

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void render(ShapeRenderer renderer, int mouseX, int mouseY) {
        super.render(renderer, mouseX, mouseY);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void multiSwitchChanged(GuiMultiSwitch multiSwitch, int prevChoice, int currentChoice) {
    }

    @Override
    public void textFieldDeactivated(GuiTextField field) {
    }

    @Override
    public void onButtonTouched(int id) {

    }

    @Override
    public int getID() {
        return 0;
    }

    @Override
    public void addComponents() {
        chat = new LobbyChat(width / 2 - 350, height / 2 - 250, 700, 500, true, OpenStrategy.consolas16);
        addComponent(chat);
    }
}
