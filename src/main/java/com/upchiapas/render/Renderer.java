package com.upchiapas.render;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;

import java.util.LinkedList;
import java.util.List;

public class Renderer extends GameApplication {
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(600);
        settings.setFullScreenAllowed(false);
        settings.setVersion("1.0.0");
        settings.setTitle("Kitchen Simulator");
    }

    public static void run(String[] args) {
        launch(args);
    }
}
