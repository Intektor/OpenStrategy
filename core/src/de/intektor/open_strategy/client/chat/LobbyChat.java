package de.intektor.open_strategy.client.chat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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

    List<Character> currentlyWritten = new ArrayList<>();

    int scrollAmount;

    public LobbyChat(int x, int y, int width, int height, boolean isShown) {
        super(x, y, width, height, isShown);
    }

    public void publishChatMessage(ChatMessage message) {
        messages.add(message);
    }

    @Override
    public void keyTyped(char c, boolean isPrioritized) {
        if (isPrioritized) {
            if (c != '\b') {
                currentlyWritten.add(c);
            }
        }
    }

    @Override
    public void mouseScrolled(int mX, int mY, int amt, boolean isPrioritized) {
        if (Gui.isPointInRegion(x, y + 20, x + width, y + height - 20, mX, mY)) {
            scrollAmount -= amt * 6;
        }
    }

    @Override
    public void onDragged(int prevX, int prevY, int cX, int cY) {
        super.onDragged(prevX, prevY, cX, cY);
        int dX = cX - prevX;
        int dY = cY - prevY;
        if (Gui.isPointInRegion(x, y + 20, x + width, y + height - 20, cX, cY)) {
            scrollAmount -= dY;
        }
    }

    @Override
    public void keyDown(int keyCode, boolean isPrioritized) {
        if (prioritized) {
            if (keyCode == Input.Keys.ENTER) {
                new LobbyChatMessagePacket(new ChatMessage(convertToString(currentlyWritten), OpenStrategy.getOpenStrategy().playerInfo)).sendToServer();
                currentlyWritten.clear();
            } else if (keyCode == Input.Keys.BACKSPACE) {
                if (currentlyWritten.size() > 0) {
                    currentlyWritten.remove(currentlyWritten.size() - 1);
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
        String s = convertToString(currentlyWritten);
        RenderHelper.drawString(x, y + 10, s.equals("") ? "_" : s, OpenStrategy.consolas16, sb, false, true);
        sb.end();

        Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
        sb.begin();

        int localY = scrollAmount;
        for (int i = 0; i < messages.size(); i++) {
            ChatMessage chatMessage = messages.get(i);
            RenderHelper.drawString(x + 1, y + height - 13 * i + localY, chatMessage.getSender().playerName + ": " + chatMessage.getMessage(), OpenStrategy.consolas16, sb, false, false);
        }
        sb.end();
        Gdx.gl.glScissor(x, y + 20, width, height - 20);
        Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
    }

    public static String convertToString(List<Character> chars) {
        String s = "";
        for (Character aChar : chars) {
            s += aChar;
        }
        return s;
    }
}
