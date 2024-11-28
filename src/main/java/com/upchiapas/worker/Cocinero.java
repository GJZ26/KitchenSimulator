package com.upchiapas.worker;

import com.upchiapas.model.Direction;
import com.upchiapas.model.RenderData;
import com.upchiapas.render.collection.RenderEntityCollection;
import com.upchiapas.service.RestauranteService;
import com.upchiapas.model.Orden;

public class Cocinero extends Thread {
    private final RestauranteService restauranteService;
    RenderEntityCollection chefs;
    double x;
    double y = 600;
    double speed = 6;
    int chefNumber;

    // 650, 550
    public Cocinero(RestauranteService restauranteService, RenderEntityCollection chefs, int chefNumber) {
        this.restauranteService = restauranteService;
        this.chefs = chefs;
        this.chefNumber = chefNumber;
        this.x = 10 + (chefNumber * 50);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Orden orden = restauranteService.prepararOrden();
                // Camina hacia la estufa
                while (true) {
                    this.y += this.speed;
                    chefs.addOrUpdate(threadId(), new RenderData(
                            this.x, this.y, 1, Direction.UP
                    ));
                    if (this.y >= 620) break;
                    Thread.sleep(1000 / 60);
                }
                // Cocina
                Thread.sleep((long) (Math.random() * 100 + 200));
                // Camina hacia la barra
                while (true) {
                    this.y -= speed;
                    chefs.addOrUpdate(threadId(), new RenderData(
                            this.x, this.y, 1, Direction.DOWN
                    ));
                    if (this.y <= 550) break;
                    Thread.sleep(1000 / 60);
                }
                restauranteService.terminarOrden(orden);
            } catch (InterruptedException e) {
                System.out.println("Error " + threadId() + " - " + e.getMessage());
            }
        }
    }
}