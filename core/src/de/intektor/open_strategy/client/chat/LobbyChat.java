package de.intektor.open_strategy.client.chat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import de.intektor.open_strategy.OpenStrategy;
import de.intektor.open_strategy.client.gui.Gui;
import de.intektor.open_strategy.client.gui.component.GuiComponent;
import de.intektor.open_strategy.client.render.RenderHelper;
import de.intektor.open_strategy.packet.LobbyChatMessagePacket;
import de.intektor.open_strategy.utils.FontHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Intektor
 */
public class LobbyChat extends GuiComponent {

    List<ChatMessage> messages = new ArrayList<>();

    List<Character> currentlyWritten = new ArrayList<>();

    int scrollAmount;

    long lastCursorTickTime;
    boolean cursorShown;
    int cursorPosition;
    int shownStringStartPos;
    int firstShownCharIndex;

    BitmapFont font;

    public LobbyChat(int x, int y, int width, int height, boolean isShown, BitmapFont font) {
        super(x, y, width, height, isShown);
        this.font = font;
    }

    public void publishChatMessage(ChatMessage message) {
        messages.add(message);
        scrollAmount += message.getFormattedMessage(this).size() * font.getLineHeight();
        checkScroll();
    }

    @Override
    public void keyTyped(char c, boolean isPrioritized) {
        if (isPrioritized) {
            if (c == '\b') {
                if (currentlyWritten.size() > 0 && cursorPosition > 0) {
                    currentlyWritten.remove(cursorPosition - 1);
                    cursorPosition--;
                    lastCursorTickTime = System.currentTimeMillis();
                    cursorShown = true;
                    setShownStringStartPos(false);
                }
            } else {
                if (currentlyWritten.size() < 200) {
                    if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '?' || c == '!' || c == ' ' || c == '.' || c == ',') {
                        if (!Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)) {
                            currentlyWritten.add(cursorPosition, c);
                            cursorPosition++;
                            lastCursorTickTime = System.currentTimeMillis();
                            cursorShown = true;
                            setShownStringStartPos(true);
                        }
                    }
                }
            }
        }
    }

    public void setShownStringStartPos(boolean right) {
        String written = convertToString(currentlyWritten);
        String shown = (String) written.subSequence(firstShownCharIndex, written.length());
        float shownWidth = FontHelper.stringWidth(shown, font);
        if (shownWidth > this.width - 3) {
            while (FontHelper.stringWidth(written, font) + shownStringStartPos > this.width - 3) {
                shownStringStartPos -= FontHelper.stringWidth((String) written.subSequence(firstShownCharIndex, ++firstShownCharIndex), font);
            }
        } else if (shownWidth < this.width - 3 && written.length() > shown.length()) {
            while (FontHelper.stringWidth(written, font) + shownStringStartPos < this.width - 3) {
                shownStringStartPos += FontHelper.stringWidth((String) written.subSequence(firstShownCharIndex - 1, firstShownCharIndex--), font);
            }
        }
    }

    public void checkScroll() {
        if (scrollAmount < 0) {
            scrollAmount = 0;
        }
        if (calculateMessagesHeight() > height - 20) {
            if (scrollAmount > calculateMessagesHeight() - height + 20) {
                scrollAmount = calculateMessagesHeight() - height + 20;
            }
        } else {
            scrollAmount = 0;
        }
    }

    public int calculateMessagesHeight() {
        int height = 0;
        for (ChatMessage message : messages) {
            String string = message.getSender().playerName + ": " + message.getMessage();
            height += (FontHelper.splitString(string, width - 15, font).size()) * font.getLineHeight();
        }
        return height;
    }

    @Override
    public void mouseScrolled(int mX, int mY, int amt, boolean isPrioritized) {
        if (Gui.isPointInRegion(x, y + 20, x + width, y + height - 20, mX, mY)) {
            scrollAmount += amt * 15;
            checkScroll();
        }
    }

    @Override
    public void onDragged(int prevX, int prevY, int cX, int cY) {
        super.onDragged(prevX, prevY, cX, cY);
        int dX = cX - prevX;
        int dY = cY - prevY;
        if (Gui.isPointInRegion(x, y + 20, x + width, y + height - 20, cX, cY)) {
            scrollAmount -= dY;
            checkScroll();
        }
    }

    @Override
    public void keyDown(int keyCode, boolean isPrioritized) {
        if (prioritized) {
            String written = convertToString(currentlyWritten);
            switch (keyCode) {
                case Input.Keys.ENTER:
                    if (written.length() > 0) {
                        new LobbyChatMessagePacket(new ChatMessage(written, OpenStrategy.getOpenStrategy().playerInfo)).sendToServer();
                        currentlyWritten.clear();
                        cursorPosition = 0;
                        firstShownCharIndex = 0;
                        shownStringStartPos = 0;
                    }
                    break;
                case Input.Keys.LEFT:
                    if (cursorPosition > 0) {
                        cursorPosition--;
                    }
                    break;
                case Input.Keys.RIGHT:
                    if (cursorPosition < written.length()) {
                        cursorPosition++;
                    }
                    break;
            }

        }
    }

    @Override
    public void clickedOnComponent(int x, int y, int button) {
        if (Gui.isPointInRegion(x, y, x + width, y + 20, x, y)) {
            if (isPrioritized()) {
                String written = convertToString(currentlyWritten);
                int localClickX = x - this.x;
                boolean clickedInsideString = false;
                for (int i = 0; i < written.length(); i++) {
                    String s = (String) written.subSequence(0, i);
                    float v = FontHelper.stringWidth(s, font);
                    if (localClickX <= shownStringStartPos + v) {
                        cursorPosition = i;
                        clickedInsideString = true;
                        break;
                    }
                }
                if (!clickedInsideString) {
                    cursorPosition = written.length();
                }
            }
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

        String written = convertToString(currentlyWritten);
        String shown = (String) written.subSequence(firstShownCharIndex, written.length());
        if (System.currentTimeMillis() - 500 >= lastCursorTickTime) {
            cursorShown = !cursorShown;
            lastCursorTickTime = System.currentTimeMillis();
        }

        sb.end();

        sb.begin();

        RenderHelper.drawString(x, y + 10, shown, font, sb, false, true);
        if (isPrioritized()) {
            if (cursorShown) {
                RenderHelper.drawString(shownStringStartPos + x + FontHelper.stringWidth((String) written.subSequence(0, cursorPosition), font) + 1, y + 10, "|", font, sb, true, true);
            }
        }
        Intersector.intersectRayBoundsFast()
        sb.end();


        Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
        sb.begin();

        int localY = y - scrollAmount;
        for (int i = 0; i < messages.size(); i++) {
            ChatMessage chatMessage = messages.get(i);
            List<String> formattedMessage = chatMessage.getFormattedMessage(this);
            for (String s : formattedMessage) {
                font.draw(sb, s, x, Gdx.graphics.getHeight() - localY);
                localY += font.getLineHeight();
            }
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
