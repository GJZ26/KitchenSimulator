package com.upchiapas.render;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.upchiapas.config.RenderConfiguration;


public class Render extends GameApplication {
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(RenderConfiguration.WINDOW_WIDTH);
        settings.setHeight(RenderConfiguration.WINDOW_HEIGHT);
        settings.setTitle(RenderConfiguration.WINDOW_NAME);
        settings.setGameMenuEnabled(RenderConfiguration.SHOW_MENU);
    }

    @Override
    protected void initUI() {
        var texture = FXGL.getAssetLoader().loadTexture("Map/Floor.png");
        FXGL.getGameScene().addUINode(texture);
    }

    public static void run(String[] args) {
        launch(args);
    }
}