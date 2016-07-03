package de.intektor.open_strategy.net.server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import de.intektor.open_strategy.net.ConnectionInfo;
import de.intektor.open_strategy.net.Side;
import de.intektor.open_strategy.net.packet.Packet;
import de.intektor.open_strategy.net.packet.PacketHelper;
import de.intektor.open_strategy.packet.RequestIdentificationPacket;
import de.intektor.open_strategy.player.PlayerInfo;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Intektor
 */
public class OpenStrategyServer {

    ServerSocketHints serverSocketHints;
    int port;
    Net.Protocol protocol;

    ServerSocket serverSocket;

    public volatile MainThread thread;

    volatile boolean runServer = true;

    public BiMap<PlayerInfo, ConnectionInfo> connectedPlayers = HashBiMap.create();

    public Map<UUID, PlayerInfo> playerInfoMap = new ConcurrentHashMap<>();

    public OpenStrategyServer(ServerSocketHints serverSocketHints, int port, Net.Protocol protocol) {
        this.serverSocketHints = serverSocketHints;
        this.port = port;
        this.protocol = protocol;
    }

    public void startServer() {
        thread = new MainThread(this);
        thread.start();

        new Thread() {
            @Override
            public void run() {
                serverSocket = Gdx.net.newServerSocket(protocol, port, serverSocketHints);
                while (runServer) {
                    Socket accept = serverSocket.accept(new SocketHints());
                    ConnectionInfo connection = new ConnectionInfo(accept);
                    new RequestIdentificationPacket().send(connection);
                    new Thread() {
                        @Override
                        public void run() {
                            while (runServer) {
                                Packet packet = PacketHelper.readPacket(connection.getIn());
                                if (packet != null) {
                                    if (onPacketReceivedPRE(connection, packet)) {
                                        packet.handle(connection, Side.SERVER);
                                        onPacketReceivedPOST(connection, packet);
                                    }
                                }
                            }
                        }
                    }.start();
                }
            }
        }.start();

    }

    public boolean onPacketReceivedPRE(ConnectionInfo connection, Packet packet) {
        return true;
    }

    public void onPacketReceivedPOST(ConnectionInfo connection, Packet packet) {

    }

    public void stopServer() {
        runServer = false;
    }

}
