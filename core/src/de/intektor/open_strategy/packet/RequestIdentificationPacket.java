package de.intektor.open_strategy.packet;

import de.intektor.open_strategy.OpenStrategy;
import de.intektor.open_strategy.net.ConnectionInfo;
import de.intektor.open_strategy.net.Side;
import de.intektor.open_strategy.net.packet.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * @author Intektor
 */
public class RequestIdentificationPacket implements Packet {


    public RequestIdentificationPacket() {
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
    }

    @Override
    public void read(DataInputStream in) throws IOException {
    }

    @Override
    public void handle(ConnectionInfo from, Side side) {
        if (side == Side.CLIENT) {
            OpenStrategy.getOpenStrategy().addScheduledTask(() -> {
                Random r = new Random();
                new IdentificationPacket("Player" + r.nextInt(10000)).sendToServer();
            });
        }
    }
}
