package de.intektor.open_strategy.packet;

import de.intektor.open_strategy.net.ConnectionInfo;
import de.intektor.open_strategy.net.Side;
import de.intektor.open_strategy.net.packet.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public class MessagePacket implements Packet {

    String message;

    public MessagePacket() {
    }

    public MessagePacket(String message) {
        this.message = message;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeUTF(message);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        message = in.readUTF();
    }

    @Override
    public void handle(ConnectionInfo from, Side side) {
        System.out.println(message);
        if (side == Side.SERVER) {
            new MessagePacket("Erhalten: " + message).send(from);
        }
    }
}
