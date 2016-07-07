package de.intektor.open_strategy.packet;

import de.intektor.open_strategy.OpenStrategy;
import de.intektor.open_strategy.client.chat.ChatMessage;
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
public class IdentificationPacket implements Packet {

    String playerName;

    public IdentificationPacket() {
    }

    public IdentificationPacket(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeUTF(playerName);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        playerName = in.readUTF();
    }

    @Override
    public void handle(ConnectionInfo from, Side side) {
        if (side == Side.SERVER) {
            OpenStrategyServer server = OpenStrategy.getOpenStrategy().getIntegratedServer();
            server.thread.addScheduledTask(() -> {
                PlayerInfo playerInfo = new PlayerInfo(UUID.randomUUID(), playerName);
                UUID uuid = UUID.randomUUID();
                new IdentificationEndPacket(playerInfo).send(from);
                new LobbyChatMessagePacket(new ChatMessage(playerName + " joined the lobby!", new PlayerInfo(UUID.randomUUID(), "Server"))).sendToAll();
                server.playerInfoMap.put(uuid, playerInfo);
                server.connectedPlayers.put(playerInfo, from);
            });
        }
    }
}
