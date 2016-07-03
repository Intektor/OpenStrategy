package de.intektor.open_strategy.player;

import java.util.UUID;

/**
 * @author Intektor
 */
public class PlayerInfo {

    public final UUID uuid;
    public final String playerName;

    public PlayerInfo(UUID uuid, String playerName) {
        this.uuid = uuid;
        this.playerName = playerName;
    }
}
