package de.intektor.open_strategy.packet;

import de.intektor.open_strategy.OpenStrategy;
import de.intektor.open_strategy.net.ConnectionInfo;
import de.intektor.open_strategy.net.Side;
import de.intektor.open_strategy.net.packet.Packet;
import de.intektor.open_strategy.player.PlayerInfo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Intektor
 */
public class IdentificationEndPacket implements Packet {

    PlayerInfo playerInfo;

    public IdentificationEndPacket() {
    }

    public IdentificationEndPacket(PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeUTF(playerInfo.uuid.toString());
        out.writeUTF(playerInfo.playerName);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        playerInfo = new PlayerInfo(UUID.fromString(in.readUTF()), in.readUTF());
    }

    @Override
    public void handle(ConnectionInfo from, Side side) {
        OpenStrategy.getOpenStrategy().addScheduledTask(() -> {
            OpenStrategy.getOpenStrategy().playerInfo = playerInfo;
            OpenStrategy.getOpenStrategy().enterGui(OpenStrategy.LOBBY);
        });
    }
}
