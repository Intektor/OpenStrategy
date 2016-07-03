package de.intektor.open_strategy.client.chat;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.open_strategy.OpenStrategy;
import de.intektor.open_strategy.client.gui.Gui;
import de.intektor.open_strategy.client.gui.component.GuiComponent;
import de.intektor.open_strategy.client.render.RenderHelper;
import de.intektor.open_strategy.packet.LobbyChatMessagePacket;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Intektor
 */
public class LobbyChat extends GuiComponent {

    List<ChatMessage> messages = new ArrayList<>();

    String currentlyWritten = "";

    public LobbyChat(int x, int y, int width, int height, boolean isShown) {
        super(x, y, width, height, isShown);
    }

    public void publishChatMessage(ChatMessage message) {
        messages.add(message);
    }

    @Override
    public void keyTyped(char c, boolean isPrioritized) {
        if (isPrioritized) {
            currentlyWritten += c;
        }
    }

    @Override
    public void keyDown(int keyCode, boolean isPrioritized) {
        if (prioritized) {
            if (keyCode == Input.Keys.ENTER) {
                new LobbyChatMessagePacket(new ChatMessage(currentlyWritten, OpenStrategy.getOpenStrategy().playerInfo)).sendToServer();
            } else if (keyCode == Input.Keys.BACKSPACE) {
                if (currentlyWritten.length() > 1) {
                    currentlyWritten = (String) currentlyWritten.subSequence(0, currentlyWritten.length() - 2);
                }
            }
        }
    }

    @Override
    public void onClicked(int x, int y) {
        if (Gui.isPointInRegion(x, y, x + width, y + 20, x, y)) {
            gui.setComponentPrioritized(this);
        }
    }

    @Override
    protected void renderComponent(ShapeRenderer renderer, int mouseX, int mouseY) {
        renderer.begin();

        //outer square
        renderer.set(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.WHITE);
        renderer.rect(x, y, width, height);

        renderer.identity();

        renderer.set(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.WHITE);
        renderer.rect(x, y, width, 20);

        renderer.end();

        SpriteBatch sb = OpenStrategy.spriteBatch;
        sb.begin();
        RenderHelper.drawString(x, y + 10, currentlyWritten.equals("") ? "_" : currentlyWritten, OpenStrategy.consolas12, sb, false, true);

        for (int i = 0; i < messages.size(); i++) {
            RenderHelper.drawString(x + 1, y + height - 13 * i, messages.get(i).getMessage(), OpenStrategy.consolas12, sb, false, false);
        }

        sb.end();

    }
}
