package de.intektor.open_strategy.client.guis;

import de.intektor.open_strategy.OpenStrategy;
import de.intektor.open_strategy.client.gui.Gui;
import de.intektor.open_strategy.client.gui.component.GuiButton;
import de.intektor.open_strategy.client.gui.component.GuiNumberField;
import de.intektor.open_strategy.client.gui.component.GuiTextField;
import de.intektor.open_strategy.net.ConnectionHelper;

/**
 * @author Intektor
 */
public class ConnectGui extends Gui {

    @Override
    public void onButtonTouched(int id) {
        switch (id) {
            case 0:
                try {
                    os.connection = ConnectionHelper.connectClient(getTextFieldByID(0).convertText(), Integer.parseInt(getTextFieldByID(1).convertText()));
                } catch (Exception e) {
                    e.printStackTrace();
                    os.connection.socket.dispose();
                }
                break;
        }
    }

    @Override
    public int getID() {
        return OpenStrategy.CONNECT;
    }

    @Override
    public void addComponents() {
        addComponent(new GuiTextField(width / 2 - 100, height / 2 + 30, 200, 40, 0, true, 300, true, true, "IP:", ""));
        addComponent(new GuiNumberField(width / 2 - 100, height / 2 - 10, 200, 40, 1, true, 300, "Port", "27013", false));
        addComponent(new GuiButton(width / 2 - 100, height / 2 - 50, 200, 40, "Connect!", 0, true));
    }
}
