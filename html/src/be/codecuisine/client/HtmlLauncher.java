package be.codecuisine.client;

import be.codecuisine.GluttoGame;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;

public class HtmlLauncher extends GwtApplication {

    private boolean resizable = true;
    private final int PADDING = 20;

    // USE THIS CODE FOR A FIXED SIZE APPLICATION
    @Override
    public GwtApplicationConfiguration getConfig() {
        if (!resizable) {
            return new GwtApplicationConfiguration(1600, 900);
        } else {
            GwtApplicationConfiguration cfg;
            int w = Window.getClientWidth() - PADDING;
            int h = Window.getClientHeight() - PADDING;
            cfg = new GwtApplicationConfiguration(w, h);
            Window.enableScrolling(false);
            Window.setMargin("0");
            Window.addResizeHandler(new ResizeListener());
            cfg.preferFlash = false;
            return cfg;
        }
    }

    private class ResizeListener implements ResizeHandler {

        @Override
        public void onResize(ResizeEvent event) {
            int width = event.getWidth() - PADDING;
            int height = event.getHeight() - PADDING;
            getRootPanel().setWidth("" + width + "px");
            getRootPanel().setHeight("" + height + "px");
            getApplicationListener().resize(width, height);
            Gdx.graphics.setWindowedMode(width, height);
        }
    }

    @Override
    public ApplicationListener createApplicationListener() {
        return new GluttoGame();
    }
}
