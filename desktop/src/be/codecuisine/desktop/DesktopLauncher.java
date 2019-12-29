package be.codecuisine.desktop;

import be.codecuisine.GluttoGame;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1600;
        config.height = 900;
        config.fullscreen = false;
        config.title = "Glutto, the eater of worlds";
        new LwjglApplication(new GluttoGame(), config);
    }
}
