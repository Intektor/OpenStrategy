package de.intektor.open_strategy.net.packet;

import de.intektor.open_strategy.net.registry.PacketRegistry;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Intektor
 */
public class PacketHelper {

    public static void writePacket(DataOutputStream out, Packet packet) throws IOException {
        out.writeInt(PacketRegistry.INSTANCE.getIdentifierByClass(packet.getClass()));
        packet.write(out);
    }

    public static Packet readPacket(DataInputStream in) throws IOException {
        int identifier = in.readInt();
        Class<? extends Packet> packetClass;
        Packet packet;
        try {
            packetClass = PacketRegistry.INSTANCE.getClassByIdentifier(identifier);
            packet = packetClass.newInstance();
            packet.read(in);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return packet;
    }
}
