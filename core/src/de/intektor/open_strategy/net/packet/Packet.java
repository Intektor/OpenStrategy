package de.intektor.open_strategy.net.packet;

import de.intektor.open_strategy.OpenStrategy;
import de.intektor.open_strategy.net.ConnectionInfo;
import de.intektor.open_strategy.net.Side;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public interface Packet {

    void write(DataOutputStream out) throws IOException;

    void read(DataInputStream in) throws IOException;

    void handle(ConnectionInfo from, Side side);

    default void sendToServer() {
        send(OpenStrategy.getOpenStrategy().getConnection());
    }

    default void send(ConnectionInfo connection) {
        try {
            PacketHelper.writePacket(connection.getOut(), this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    default void sendToAll() {
        for (ConnectionInfo connectionInfo : OpenStrategy.getOpenStrategy().getIntegratedServer().connectedPlayers.values()) {
            send(connectionInfo);
        }
    }
}
