package de.intektor.open_strategy.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;

/**
 * @author Intektor
 */
public class ConnectionHelper {

    public static ConnectionInfo connectClient(String serverIP, int port, Net.Protocol protocol, SocketHints hints) {
        Socket socket = Gdx.net.newClientSocket(protocol, serverIP, port, hints);
        return new ConnectionInfo(socket);
    }
}
