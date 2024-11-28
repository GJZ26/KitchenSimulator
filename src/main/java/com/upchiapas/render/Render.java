package com.upchiapas.render;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import com.upchiapas.config.RenderConfiguration;
import com.upchiapas.model.Direction;
import com.upchiapas.model.RenderData;
import com.upchiapas.render.collection.RenderEntityCollection;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Render extends GameApplication {
    static RenderData[] tables;
    static RenderEntityCollection clients;
    static RenderEntityCollection dishes;
    static RenderEntityCollection chefs;
    static RenderEntityCollection waiters;

    public void renderClients() {
        if (clients == null) {
            return;
        }
        RenderData[] clientsRender = clients.getRenderData();
        for (RenderData data : clientsRender) {
            if (data.isEating) {
                Circle circle = new Circle();
                circle.setCenterX(data.x + 25);
                circle.setCenterY(data.y - 15);
                circle.setRadius(10);
                Group root = new Group(circle);
                circle.setFill(Color.GREENYELLOW);
                FXGL.getGameScene().addUINode(root);
            }
            this.render(RenderResource.CLIENTS[data.texture], data.x, data.y, data.direction);
        }
    }

    public void renderDishes() {
        if (dishes == null) {
            return;
        }
        RenderData[] dishesRender = dishes.getRenderData();
        for (int i = 0; i < dishesRender.length; i++) {
            var data = dishesRender[i];
            this.render(RenderResource.DISHES[data.texture], 10 + (i * 40), 518, data.direction);
        }
    }

    public void renderChefs() {
        if (chefs == null) {
            return;
        }
        RenderData[] chefsRender = chefs.getRenderData();
        for (RenderData data : chefsRender) {
            this.render(RenderResource.CHEF, data.x, data.y, data.direction);
        }
    }

    public void renderWaiters() {
        if (waiters == null) {
            return;
        }
        RenderData[] waitersRender = waiters.getRenderData();
        for (RenderData data : waitersRender) {
            this.render(RenderResource.WAITER, data.x, data.y, data.direction);
        }
    }

    public void setWaiters(RenderEntityCollection waiters) {
        Render.waiters = waiters;
    }

    public void setTables(RenderData[] tablesMan) {
        tables = tablesMan;
    }

    public void setClients(RenderEntityCollection clientsMan) {
        clients = clientsMan;
    }

    public void setDishes(RenderEntityCollection dishesMan) {
        dishes = dishesMan;
    }

    public void setChefs(RenderEntityCollection chefsMan) {
        chefs = chefsMan;
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
        this.renderClients();
        this.renderTables();
        this.renderChefs();
        this.renderWaiters();
        this.renderOverlays();
        this.renderDishes();
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
