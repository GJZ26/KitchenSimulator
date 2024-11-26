package com.upchiapas.worker;

import com.upchiapas.model.Orden;
import com.upchiapas.service.RestauranteService;

public class Cocinero extends Thread {
    private final RestauranteService restauranteService;
    
    public Cocinero(RestauranteService restauranteService) {
        this.restauranteService = restauranteService;
    }
    
    @Override
    public void run() {
        while (true) {
            try {
                Orden orden = restauranteService.prepararOrden();
                Thread.sleep((long)(Math.random() * 3000 + 2000));
                restauranteService.terminarOrden(orden);
            } catch (InterruptedException e) {
                System.out.println("Error " + threadId() + " - " + e.getMessage());
            }
        }
    }
}