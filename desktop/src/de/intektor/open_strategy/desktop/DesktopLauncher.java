package de.intektor.open_strategy.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.intektor.open_strategy.OpenStrategy;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1280;
        config.height = 720;
        config.vSyncEnabled = false;
        config.foregroundFPS = 0;
        new LwjglApplication(new OpenStrategy(), config);
    }
}
