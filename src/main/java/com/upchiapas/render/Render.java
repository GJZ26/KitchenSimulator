package com.upchiapas.render;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;


public class Render extends GameApplication {
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(700);
        settings.setHeight(700);
        settings.setTitle("Learning how to use this shit");
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