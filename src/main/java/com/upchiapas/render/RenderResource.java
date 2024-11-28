package com.upchiapas.render;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;

public class RenderResource {
    public static Texture BACKGROUND = FXGL.getAssetLoader().loadTexture("Map/Floor.png");
    public static Texture BAR = FXGL.getAssetLoader().loadTexture("Map/DivisorBar.png");
    public static Texture STOVEN = FXGL.getAssetLoader().loadTexture("Map/Stoven.png");
    public static Texture TABLE = FXGL.getAssetLoader().loadTexture("Entities/Other/Table.png");
    public static Texture WAITER = FXGL.getAssetLoader().loadTexture("Entities/Human/waiter.png");
    public static Texture CHEF = FXGL.getAssetLoader().loadTexture("Entities/Human/chef.png");
    public static Texture[] DISHES = new Texture[]{
            FXGL.getAssetLoader().loadTexture("Entities/Dish/1.png"),
            FXGL.getAssetLoader().loadTexture("Entities/Dish/2.png"),
            FXGL.getAssetLoader().loadTexture("Entities/Dish/3.png"),
            FXGL.getAssetLoader().loadTexture("Entities/Dish/4.png"),
    };
    public static Texture[] CLIENTS = new Texture[]{
            FXGL.getAssetLoader().loadTexture("Entities/Human/client-blue.png"),
            FXGL.getAssetLoader().loadTexture("Entities/Human/client-green.png"),
            FXGL.getAssetLoader().loadTexture("Entities/Human/client-orange.png"),
            FXGL.getAssetLoader().loadTexture("Entities/Human/client-red.png"),
    };
}
