package com.upchiapas.model;

import com.almasb.fxgl.texture.Texture;

public class RenderData {
    public  double x;
    public  double y;
    public  int texture;
    public  Direction direction;

    public  RenderData(double x, double y, int texture, Direction direction) {
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.direction = direction;
    }
}
