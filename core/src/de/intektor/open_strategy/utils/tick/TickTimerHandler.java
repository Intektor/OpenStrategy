package de.intektor.open_strategy.utils.tick;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * @author Intektor
 */
public class TickTimerHandler {

    public static TickTimerHandler INSTANCE = new TickTimerHandler();

    BiMap<TickTimer, String> tickRegistry = HashBiMap.create();

    public void registerTickTimer(TickTimer tickTimer, String identifier) {
        tickRegistry.put(tickTimer, identifier);
    }

    public void removeTickTimer(TickTimer timer) {
        tickRegistry.remove(timer);
    }

    public void removeTickTimer(String identifier) {
        tickRegistry.inverse().remove(identifier);
    }

    public TickTimer getTickTimer(String identifier) {
        return tickRegistry.inverse().get(identifier);
    }

    public String getIdentifier(TickTimer tickTimer) {
        return tickRegistry.get(tickTimer);
    }
}
