package de.intektor.open_strategy.client.guis;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.net.ServerSocketHints;
import de.intektor.open_strategy.OpenStrategy;
import de.intektor.open_strategy.client.gui.Gui;
import de.intektor.open_strategy.client.gui.component.GuiButton;
import de.intektor.open_strategy.client.gui.component.GuiMultiSwitch;
import de.intektor.open_strategy.client.gui.component.GuiNumberField;
import de.intektor.open_strategy.client.gui.component.GuiTextField;
import de.intektor.open_strategy.client.render.RenderHelper;
import de.intektor.open_strategy.net.ConnectionHelper;
import de.intektor.open_strategy.net.server.OpenStrategyServer;

import java.util.Arrays;
import java.util.Random;

/**
 * @author Intektor
 */
public class CreateLobby extends Gui {

    final int BUTTON_START_LOBBY = 1;
    final int MULTI_SWITCH_LOBBY_TYPE = 0, MULTI_SWITCH_MAX_PLAYERS = 1;
    final int TEXT_FIELD_GATEWAY = 0, TEXT_FIELD_PORT = 1, TEXT_FIELD_USERNAME = 2;

    @Override
    public void render(ShapeRenderer renderer, int mouseX, int mouseY) {
        super.render(renderer, mouseX, mouseY);
        OpenStrategy.spriteBatch.begin();
        RenderHelper.drawString(width / 2, height - 32, "Create Lobby", OpenStrategy.perfectPixel64, OpenStrategy.spriteBatch, true, true);
        OpenStrategy.spriteBatch.end();
    }

    @Override
    public void update() {
        super.update();
        GuiButton createLobbyButton = getButtonByID(BUTTON_START_LOBBY);
    }

    @Override
    public void onButtonTouched(int id) {
        switch (id) {
            case BUTTON_START_LOBBY:
                boolean worldWide = getMultiSwitchByID(MULTI_SWITCH_LOBBY_TYPE).getCurrentlyChosen() == 1;
                int port = Integer.parseInt(getTextFieldByID(TEXT_FIELD_PORT).convertText());
                if (!worldWide) {
                    ServerSocketHints serverSocketHints = new ServerSocketHints();
                    serverSocketHints.acceptTimeout = 0;

                    OpenStrategyServer server = new OpenStrategyServer(serverSocketHints, port, Net.Protocol.TCP);
                    server.startServer();

                    os.setIntegratedServer(server);

                    os.username = getTextFieldByID(TEXT_FIELD_USERNAME).convertText();

                    os.connection = ConnectionHelper.connectClient("127.0.0.1", port);
                } else {

                }
                break;
        }
    }

    @Override
    public int getID() {
        return OpenStrategy.CREATE_LOBBY;
    }

    @Override
    public void multiSwitchChanged(GuiMultiSwitch multiSwitch, int prevChoice, int currentChoice) {
        if (multiSwitch.id == MULTI_SWITCH_LOBBY_TYPE) {
            getTextFieldByID(TEXT_FIELD_GATEWAY).setShown(currentChoice == 1);
        }
    }

    @Override
    public void addComponents() {
        addComponent(new GuiMultiSwitch(0, height - 100, 300, 20, true, Arrays.asList("LAN", "World Wide"), 0, MULTI_SWITCH_LOBBY_TYPE, "Lobby Type: "));
        addComponent(new GuiMultiSwitch(0, height - 200, 300, 20, true, Arrays.asList("1", "2", "3", "4"), 0, MULTI_SWITCH_MAX_PLAYERS, "Max Players: "));
        addComponent(new GuiTextField(310, height - 120, 300, 40, TEXT_FIELD_GATEWAY, false, 1000, true, true, true, "", "Gateway"));
        addComponent(new GuiNumberField(0, height - 160, 300, 40, TEXT_FIELD_PORT, true, 5, "Port", "27013", false));
        Random r = new Random();
        addComponent(new GuiTextField(0, height - 260, 300, 40, TEXT_FIELD_USERNAME, true, 30, true, true, false, "Player" + r.nextInt(1000), "Username"));
        addComponent(new GuiButton(0, height - 300, 300, 20, "Create Lobby", BUTTON_START_LOBBY, true));
    }
}
