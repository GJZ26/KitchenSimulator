package com.upchiapas.render;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import com.upchiapas.config.RenderConfiguration;
import com.upchiapas.model.Direction;

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
        FXGL.getGameScene().addUINode(RenderResource.BACKGROUND);
    }

    @Override
    protected void onUpdate(double tpf) {
        super.onUpdate(tpf);
        this.clearScreen();
        this.renderBackground();
        this.renderOverlays();
    }

    private void renderBackground() {
        this.render(RenderResource.BACKGROUND, 0, 0, Direction.UP);
    }

    private void renderOverlays() {
        this.render(RenderResource.BAR, 0, 520, Direction.UP);
        this.render(RenderResource.STOVEN, 0,665,Direction.UP);
    }

    private void render(Texture image, double x, double y, Direction direction) {
        image.setTranslateX(x);
        image.setTranslateY(y);

        switch (direction) {
            case LEFT:
                image.setRotate(-90);
                break;
            case RIGHT:
                image.setRotate(90);
                break;
            case UP:
                image.setRotate(0);
                break;
            case DOWN:
                image.setRotate(180);
                break;
            default:
                throw new IllegalArgumentException("Dirección no válida: " + direction);
        }
        FXGL.getGameScene().addUINode(image);
    }


    private void clearScreen() {
        FXGL.getGameScene().clearUINodes();
    }

    public static void run(String[] args) {
        launch(args);
    }
}