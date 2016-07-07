package de.intektor.open_strategy.client.guis;

import com.badlogic.gdx.utils.GdxRuntimeException;
import de.intektor.open_strategy.OpenStrategy;
import de.intektor.open_strategy.client.gui.Gui;
import de.intektor.open_strategy.client.gui.component.GuiButton;
import de.intektor.open_strategy.client.gui.component.GuiNumberField;
import de.intektor.open_strategy.client.gui.component.GuiTextField;
import de.intektor.open_strategy.net.ConnectionHelper;

import java.util.Random;

/**
 * @author Intektor
 */
public class ConnectGui extends Gui {

    final int TEXT_FIELD_IP = 0, TEXT_FIELD_PORT = 1, TEXT_FIELD_USERNAME = 2;
    final int BUTTON_CONNECT = 0;

    @Override
    public void onButtonTouched(int id) {
        switch (id) {
            case BUTTON_CONNECT:
                try {
                    os.username = getTextFieldByID(2).convertText();
                    try {
                        os.connection = ConnectionHelper.connectClient(getTextFieldByID(0).convertText(), Integer.parseInt(getTextFieldByID(1).convertText()));
                    } catch(GdxRuntimeException ex) {
                        ex.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    os.connection.socket.dispose();
                }
                break;
        }
    }

    @Override
    public void update() {
        super.update();
        GuiButton connect = getButtonByID(BUTTON_CONNECT);
        connect.enabled = !getTextFieldByID(TEXT_FIELD_IP).convertText().equals("") && !getTextFieldByID(TEXT_FIELD_PORT).convertText().equals("") && getTextFieldByID(TEXT_FIELD_USERNAME).convertText().length() > 0;
    }

    @Override
    public int getID() {
        return OpenStrategy.CONNECT;
    }

    @Override
    public void addComponents() {
        addComponent(new GuiTextField(width / 2 - 100, height / 2 + 30, 200, 40, TEXT_FIELD_IP, true, 300, true, true, true, "", "IP:"));
        addComponent(new GuiNumberField(width / 2 - 100, height / 2 - 10, 200, 40, TEXT_FIELD_PORT, true, 300, "Port", "27013", false));
        Random r = new Random();
        addComponent(new GuiTextField(width / 2 - 100, height / 2 - 50, 200, 40, TEXT_FIELD_USERNAME, true, 30, true, true, false, "Player" + r.nextInt(1000), "Username"));
        addComponent(new GuiButton(width / 2 - 100, height / 2 - 90, 200, 40, "Connect!", BUTTON_CONNECT, true));
    }
}
