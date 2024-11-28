package com.upchiapas.worker;

import com.upchiapas.model.Direction;
import com.upchiapas.model.RenderData;
import com.upchiapas.render.collection.RenderEntityCollection;
import com.upchiapas.service.RestauranteService;
import com.upchiapas.model.Orden;

public class Mesero extends Thread {
    private final RestauranteService restauranteService;
    RenderEntityCollection waiters;
    double x = 0;
    double y = 425;
    double speed = 3;

    public Mesero(RestauranteService restauranteService, RenderEntityCollection waiters, int waiterCount) {
        this.restauranteService = restauranteService;
        this.waiters = waiters;
        this.x = 10 + (waiterCount * 50);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Orden orden = restauranteService.atenderCliente();
                // Va a la mesa del cliente
                while (true) {
                    this.y -= this.speed;
                    waiters.addOrUpdate(threadId(),
                            new RenderData(x, y, 0, Direction.DOWN)
                    );
                    if (this.y <= 300) {
                        break;
                    }
                    Thread.sleep(1000 / 60);
                }
                Thread.sleep((long) (Math.random() * 1000 + 500));
                restauranteService.ordenEnCola(orden);
                // Va a la barra
                while (true) {
                    this.y += this.speed;
                    waiters.addOrUpdate(threadId(),
                            new RenderData(x, y, 0, Direction.UP)
                    );
                    if (this.y >= 480) {
                        break;
                    }
                    Thread.sleep(1000 / 60);
                }
                orden = restauranteService.tomarOrden();
                while (true) {
                    this.y -= this.speed;
                    waiters.addOrUpdate(threadId(),
                            new RenderData(x, y, 0, Direction.DOWN)
                    );
                    if (this.y <= 300) {
                        break;
                    }
                    Thread.sleep(1000 / 60);
                }
                restauranteService.servirOrden(orden);
                Thread.sleep((long) (Math.random() * 1000 + 500));
            } catch (InterruptedException e) {
                System.out.println("Error " + threadId() + " - " + e.getMessage());
            }
        }
    }
}