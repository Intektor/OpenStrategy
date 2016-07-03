package de.intektor.open_strategy.net.lobby;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocketHints;
import de.intektor.open_strategy.net.ConnectionInfo;
import de.intektor.open_strategy.net.packet.Packet;
import de.intektor.open_strategy.net.server.OpenStrategyServer;

/**
 * @author Intektor
 */
public class LobbyServer extends OpenStrategyServer {

    public LobbyServer(ServerSocketHints serverSocketHints, int port, Net.Protocol protocol) {
        super(serverSocketHints, port, protocol);
    }

    @Override
    public void startServer() {
        super.startServer();
    }

    @Override
    public boolean onPacketReceivedPRE(ConnectionInfo connection, Packet packet) {

        return true;
    }

    @Override
    public void onPacketReceivedPOST(ConnectionInfo connection, Packet packet) {

    }
}
