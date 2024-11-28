package com.upchiapas.render;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import com.upchiapas.config.RenderConfiguration;
import com.upchiapas.model.Direction;
import com.upchiapas.model.RenderData;

public class Render extends GameApplication {
    static RenderData[] tables;

    public void setTables(RenderData[] tablesMan) {
        tables = tablesMan;
    }

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

        this.renderTables();
        this.renderOverlays();
    }

    private void renderBackground() {
        this.render(RenderResource.BACKGROUND, 0, 0, Direction.UP);
    }

    private void renderOverlays() {
        this.render(RenderResource.BAR, 0, 520, Direction.UP);
        this.render(RenderResource.STOVEN, 0, 665, Direction.UP);
    }

    private void renderTables() {
        if (tables != null) {
            for (RenderData table : tables) {
                this.render(RenderResource.TABLE, table.x, table.y, table.direction);
            }
        }
    }

    private void render(Texture image, double x, double y, Direction direction) {
        // Crear una nueva instancia de la textura cada vez que la renderices
        Texture textureInstance = new Texture(image.getImage());

        textureInstance.setTranslateX(x);
        textureInstance.setTranslateY(y);

        switch (direction) {
            case LEFT:
                textureInstance.setRotate(-90);
                break;
            case RIGHT:
                textureInstance.setRotate(90);
                break;
            case UP:
                textureInstance.setRotate(0);
                break;
            case DOWN:
                textureInstance.setRotate(180);
                break;
            default:
                throw new IllegalArgumentException("Dirección no válida: " + direction);
        }

        FXGL.getGameScene().addUINode(textureInstance);
    }

    private void clearScreen() {
        FXGL.getGameScene().clearUINodes();
    }

    public void run(String[] args) {
        launch(args);
    }
}
