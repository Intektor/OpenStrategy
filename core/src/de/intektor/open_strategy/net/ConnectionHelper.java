package de.intektor.open_strategy.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import de.intektor.open_strategy.OpenStrategy;
import de.intektor.open_strategy.net.packet.Packet;
import de.intektor.open_strategy.net.packet.PacketHelper;

import static com.badlogic.gdx.Net.Protocol.TCP;

/**
 * @author Intektor
 */
public class ConnectionHelper {

    public static ConnectionInfo connectClient(String serverIP, int port) {
        SocketHints socketHints = new SocketHints();
        Socket socket = Gdx.net.newClientSocket(TCP, serverIP, port, socketHints);

        ConnectionInfo connection = new ConnectionInfo(socket);

        new Thread() {
            @Override
            public void run() {
                while (OpenStrategy.getOpenStrategy().gameRunning && connection.isActive) {
                    Packet packet = PacketHelper.readPacket(connection.getIn());
                    if (packet != null) {
                        packet.handle(connection, Side.CLIENT);
                    }
                }
            }
        }.start();

        return connection;
    }
}
