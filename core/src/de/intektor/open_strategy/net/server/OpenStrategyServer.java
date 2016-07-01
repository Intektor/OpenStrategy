package de.intektor.open_strategy.net.server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import de.intektor.open_strategy.net.ConnectionInfo;
import de.intektor.open_strategy.net.Side;
import de.intektor.open_strategy.net.packet.Packet;
import de.intektor.open_strategy.net.packet.PacketHelper;

/**
 * @author Intektor
 */
public class OpenStrategyServer {

    ServerSocketHints serverSocketHints;
    int port;
    Net.Protocol protocol;

    ServerSocket serverSocket;

    volatile boolean runServer = true;

    public OpenStrategyServer(ServerSocketHints serverSocketHints, int port, Net.Protocol protocol) {
        this.serverSocketHints = serverSocketHints;
        this.port = port;
        this.protocol = protocol;
    }

    public void startServer() {
        new Thread() {
            @Override
            public void run() {
                serverSocket = Gdx.net.newServerSocket(protocol, port, serverSocketHints);
                while (runServer) {
                    Socket accept = serverSocket.accept(new SocketHints());
                    ConnectionInfo connection = new ConnectionInfo(accept);
                    new Thread() {
                        @Override
                        public void run() {
                            while (runServer) {
                                Packet packet = PacketHelper.readPacket(connection.getIn());
                                if (packet != null) {
                                    packet.handle(connection, Side.SERVER);
                                }
                            }
                        }
                    }.start();
                }
            }
        }.start();
    }

    public void stopServer() {
        runServer = false;
    }

}
