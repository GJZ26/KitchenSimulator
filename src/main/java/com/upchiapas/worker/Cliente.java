package com.upchiapas.worker;

import com.upchiapas.model.Direction;
import com.upchiapas.model.RenderData;
import com.upchiapas.render.collection.RenderEntityCollection;
import com.upchiapas.service.RestauranteService;

import java.util.Random;

public class Cliente extends Thread {
    private final RestauranteService restauranteService;
    private final RenderEntityCollection clients;
    private int textureAlternative = (new Random()).nextInt(4);
    double x = 0;
    double y = 0;
    double speed = 5;

    public Cliente(RestauranteService restauranteService, RenderEntityCollection clients) {
        this.restauranteService = restauranteService;
        this.clients = clients;
    }

    @Override
    public void run() {
        try {
            RenderData numeroMesa = restauranteService.intentarSentarse();
            this.x = numeroMesa.x-3;
            while (true) {
                // Camina a su mesa
                clients.addOrUpdate(threadId(), new RenderData(
                        this.x,
                        this.y,
                        this.textureAlternative,
                        Direction.UP
                ));
                Thread.sleep(1000 / 60);
                if (this.y >= numeroMesa.y -30) {
                    break;
                }
                this.y += speed;
            }
            while (!restauranteService.retirarCliente(threadId())) {
                // Espera su orden
                Thread.sleep(1000);
            }
            // Come
            Thread.sleep((long) (Math.random() * 5000 + 3000));
            while (true) {
                // Se retira
                clients.addOrUpdate(threadId(), new RenderData(
                        this.x,
                        this.y,
                        this.textureAlternative,
                        Direction.DOWN
                ));
                Thread.sleep(1000 / 60);
                if (this.y <= 0) {
                    break;
                }
                this.y -= speed;
            }
            clients.remove(threadId());
            restauranteService.liberarMesa(numeroMesa.id);
        } catch (InterruptedException e) {
            System.out.println("Error " + threadId() + " - " + e.getMessage());
        }
    }
}