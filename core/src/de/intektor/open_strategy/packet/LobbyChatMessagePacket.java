package de.intektor.open_strategy.packet;

import de.intektor.open_strategy.OpenStrategy;
import de.intektor.open_strategy.client.chat.ChatMessage;
import de.intektor.open_strategy.client.guis.GuiLobby;
import de.intektor.open_strategy.net.ConnectionInfo;
import de.intektor.open_strategy.net.Side;
import de.intektor.open_strategy.net.packet.Packet;
import de.intektor.open_strategy.net.server.OpenStrategyServer;
import de.intektor.open_strategy.player.PlayerInfo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Intektor
 */
public class LobbyChatMessagePacket implements Packet {

    ChatMessage message;

    public LobbyChatMessagePacket() {
    }

    public LobbyChatMessagePacket(ChatMessage message) {
        this.message = message;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeUTF(message.getMessage());
        out.writeBoolean(message.getSender() != null);
        if (message.getSender() != null) {
            out.writeUTF(message.getSender().uuid.toString());
            out.writeUTF(message.getSender().playerName);
        }
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        this.message = new ChatMessage(in.readUTF(), in.readBoolean() ? new PlayerInfo(UUID.fromString(in.readUTF()), in.readUTF()) : null);
    }

    @Override
    public void handle(ConnectionInfo from, Side side) {
        if (side == Side.CLIENT) {
            ((GuiLobby) OpenStrategy.getOpenStrategy().guiMap.get(OpenStrategy.LOBBY)).chat.publishChatMessage(message);
        } else {
            OpenStrategyServer server = OpenStrategy.getOpenStrategy().getIntegratedServer();
            server.thread.addScheduledTask(() -> {
                for (ConnectionInfo connectionInfo : server.connectedPlayers.values()) {
                    send(connectionInfo);
                }
            });
        }
    }
}
