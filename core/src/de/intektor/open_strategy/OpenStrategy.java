package de.intektor.open_strategy;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.SocketHints;
import de.intektor.open_strategy.net.ConnectionHelper;
import de.intektor.open_strategy.net.ConnectionInfo;
import de.intektor.open_strategy.net.Side;
import de.intektor.open_strategy.net.packet.Packet;
import de.intektor.open_strategy.net.packet.PacketHelper;
import de.intektor.open_strategy.net.registry.PacketRegistry;
import de.intektor.open_strategy.net.server.OpenStrategyServer;
import de.intektor.open_strategy.packet.MessagePacket;

public class OpenStrategy extends ApplicationAdapter {

    private static OpenStrategy game;

    private OpenStrategyServer integratedServer;

    private ConnectionInfo connection;

    public static BitmapFont perfectPixel12;
    public static BitmapFont perfectPixel16;
    public static BitmapFont perfectPixel32;
    public static BitmapFont perfectPixel64;
    public static BitmapFont perfectPixel72;

    public static SpriteBatch spriteBatch;
    public static GlyphLayout layout;

    private volatile boolean gameRunning = true;

    @Override
    public void create() {
        game = this;
        ServerSocketHints serverSocketHints = new ServerSocketHints();
        serverSocketHints.acceptTimeout = 0;

        integratedServer = new OpenStrategyServer(serverSocketHints, 27013, Net.Protocol.TCP);
        integratedServer.startServer();

        connection = ConnectionHelper.connectClient("127.0.0.1", 27013, Net.Protocol.TCP, new SocketHints());

        PacketRegistry.INSTANCE.registerPacket(MessagePacket.class, 0);

        new Thread() {
            @Override
            public void run() {
                while (gameRunning) {
                    Packet packet = PacketHelper.readPacket(connection.getIn());
                    if (packet != null) {
                        packet.handle(connection, Side.CLIENT);
                    }
                }
            }
        }.start();

        loadGameResources();

    }

    @Override
    public void render() {

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void dispose() {
        gameRunning = false;
        if (integratedServer != null) integratedServer.stopServer();
    }

    public static OpenStrategy getOpenStrategy() {
        return game;
    }

    public ConnectionInfo getConnection() {
        return connection;
    }

    public OpenStrategyServer getIntegratedServer() {
        return integratedServer;
    }

    private void loadGameResources() {
        spriteBatch = new SpriteBatch();
        layout = new GlyphLayout();
        loadFonts(1);
    }

    private void loadFonts(float size) {
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.local("assets/PerfectPixel.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int) (12 * size);
        perfectPixel12 = fontGenerator.generateFont(parameter);
        parameter.size = (int) (16 * size);
        perfectPixel16 = fontGenerator.generateFont(parameter);
        parameter.size = (int) (32 * size);
        perfectPixel32 = fontGenerator.generateFont(parameter);
        parameter.size = (int) (64 * size);
        perfectPixel64 = fontGenerator.generateFont(parameter);
        parameter.size = (int) (72 * size);
        perfectPixel72 = fontGenerator.generateFont(parameter);
        fontGenerator.dispose();
    }

}
