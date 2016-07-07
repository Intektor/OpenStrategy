package de.intektor.open_strategy;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.intektor.open_strategy.client.gui.Gui;
import de.intektor.open_strategy.client.guis.*;
import de.intektor.open_strategy.client.render.RenderHelper;
import de.intektor.open_strategy.net.ConnectionInfo;
import de.intektor.open_strategy.net.registry.PacketRegistry;
import de.intektor.open_strategy.net.server.OpenStrategyServer;
import de.intektor.open_strategy.packet.IdentificationEndPacket;
import de.intektor.open_strategy.packet.IdentificationPacket;
import de.intektor.open_strategy.packet.LobbyChatMessagePacket;
import de.intektor.open_strategy.packet.RequestIdentificationPacket;
import de.intektor.open_strategy.player.PlayerInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import static com.badlogic.gdx.Gdx.graphics;

public class OpenStrategy extends ApplicationAdapter {

    private static OpenStrategy game;

    private volatile OpenStrategyServer integratedServer;

    public ConnectionInfo connection;
    public PlayerInfo playerInfo;

    public static BitmapFont perfectPixel12;
    public static BitmapFont perfectPixel16;
    public static BitmapFont perfectPixel32;
    public static BitmapFont perfectPixel64;
    public static BitmapFont perfectPixel72;

    public static BitmapFont consolas12;
    public static BitmapFont consolas16;
    public static BitmapFont consolas32;
    public static BitmapFont consolas64;
    public static BitmapFont consolas72;

    public static SpriteBatch spriteBatch;
    public static GlyphLayout layout;

    public volatile boolean gameRunning = true;

    public static final int MAIN_MENU = 0;
    public static final int PLAY_SELECT = 1;
    public static final int CREATE_LOBBY = 2;
    public static final int LOBBY = 3;
    public static final int CONNECT = 4;

    public Map<Integer, Gui> guiMap = new HashMap<>();

    private int currentGui;

    public volatile String username;

    private volatile Queue<Runnable> tasks = new LinkedBlockingQueue<>();

    @Override
    public void create() {
        game = this;

        PacketRegistry.INSTANCE.registerPacket(RequestIdentificationPacket.class, 0);
        PacketRegistry.INSTANCE.registerPacket(IdentificationPacket.class, 1);
        PacketRegistry.INSTANCE.registerPacket(IdentificationEndPacket.class, 2);
        PacketRegistry.INSTANCE.registerPacket(LobbyChatMessagePacket.class, 3);

        loadGameResources();
        initGame();
    }

    ShapeRenderer renderer;

    public int ticksPerSecond = 60;
    private long lastTickTime;


    @Override
    public void render() {
        if (System.currentTimeMillis() - lastTickTime >= 1000 / ticksPerSecond) {
            lastTickTime = System.currentTimeMillis();
            updateGame();
        }
        renderGame();
    }

    public void renderGame() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gui gui = guiMap.get(currentGui);
        gui.render(renderer, Gdx.input.getX(), Gdx.input.getY());

        spriteBatch.begin();
        RenderHelper.drawString(Gdx.graphics.getWidth() - 20, Gdx.graphics.getHeight() - 5, graphics.getFramesPerSecond() + "", perfectPixel12, spriteBatch, true, true);
        spriteBatch.end();
    }

    public void updateGame() {
        Runnable t;
        while ((t = tasks.poll()) != null) {
            t.run();
        }
        Gui gui = guiMap.get(currentGui);
        gui.update();
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

    public void setIntegratedServer(OpenStrategyServer integratedServer) {
        this.integratedServer = integratedServer;
    }

    private void loadGameResources() {
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

        fontGenerator = new FreeTypeFontGenerator(Gdx.files.local("assets/Consolas.ttf"));
        parameter.size = (int) (12 * size);
        consolas12 = fontGenerator.generateFont(parameter);
        parameter.size = (int) (16 * size);
        consolas16 = fontGenerator.generateFont(parameter);
        parameter.size = (int) (32 * size);
        consolas32 = fontGenerator.generateFont(parameter);
        parameter.size = (int) (64 * size);
        consolas64 = fontGenerator.generateFont(parameter);
        parameter.size = (int) (72 * size);
        consolas72 = fontGenerator.generateFont(parameter);

        fontGenerator.dispose();
    }

    public void initGame() {
        spriteBatch = new SpriteBatch();

        layout = new GlyphLayout();

        renderer = new ShapeRenderer();
        renderer.setAutoShapeType(true);

        initStates();

        enterGui(MAIN_MENU);
    }

    private void initStates() {
        guiMap.put(MAIN_MENU, new MainMenu());
        guiMap.put(PLAY_SELECT, new SelectPlayType());
        guiMap.put(CREATE_LOBBY, new CreateLobby());
        guiMap.put(LOBBY, new GuiLobby());
        guiMap.put(CONNECT, new ConnectGui());
    }

    public void enterGui(int id) {
        currentGui = id;
        Gui gui = guiMap.get(currentGui);
        gui.init();
    }

    public synchronized void addScheduledTask(Runnable task) {
        tasks.offer(task);
    }
}
