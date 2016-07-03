package de.intektor.open_strategy.client.chat;

import de.intektor.open_strategy.player.PlayerInfo;

/**
 * @author Intektor
 */
public class ChatMessage {

    private final PlayerInfo sender;
    private final String message;

    public ChatMessage(String message, PlayerInfo sender) {
        this.message = message;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public PlayerInfo getSender() {
        return sender;
    }
}
