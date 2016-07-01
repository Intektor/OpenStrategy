package de.intektor.open_strategy.net.registry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import de.intektor.open_strategy.net.packet.Packet;

/**
 * @author Intektor
 */
public class PacketRegistry {

    public static PacketRegistry INSTANCE = new PacketRegistry();

    BiMap<Class<? extends Packet>, Integer> regMap = HashBiMap.create();

    public void registerPacket(Class<? extends Packet> clazz, int identifier) {
        regMap.put(clazz, identifier);
    }

    public Class<? extends Packet> getClassByIdentifier(int identifier) {
        return regMap.inverse().get(identifier);
    }

    public int getIdentifierByClass(Class<? extends Packet> clazz) {
        return regMap.get(clazz);
    }
}
