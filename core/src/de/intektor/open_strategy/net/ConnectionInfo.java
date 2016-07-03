package de.intektor.open_strategy.net;

import com.badlogic.gdx.net.Socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Intektor
 */
public class ConnectionInfo {

    public final Socket socket;
    public final InputStream inputStream;
    public final OutputStream outputStream;
    public boolean isActive = true;

    public ConnectionInfo(Socket socket) {
        this.socket = socket;
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
    }

    public DataInputStream getIn() {
        return new DataInputStream(inputStream);
    }

    public DataOutputStream getOut() {
        return new DataOutputStream(outputStream);
    }
}
