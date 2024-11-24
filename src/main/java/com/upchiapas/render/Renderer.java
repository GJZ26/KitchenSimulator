package com.upchiapas.render;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;

import java.util.ArrayList;
import java.util.List;

public class Renderer extends GameApplication {
    @Override
    protected void initSettings(GameSettings settings) {

        settings.setWidth(700);
        settings.setHeight(700);
        settings.setFullScreenAllowed(true);
        settings.setGameMenuEnabled(false);
        settings.setVersion("1.0.0");
        settings.setTitle("Kitchen Simulator");
    }

    public static void run(String[] args) {
        launch(args);
    }
}
