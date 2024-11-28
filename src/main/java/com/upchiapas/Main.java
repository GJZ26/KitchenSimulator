package com.upchiapas;

import com.upchiapas.render.Render;
import com.upchiapas.service.*;
import com.upchiapas.worker.*;
import com.upchiapas.config.RestauranteConfig;

public class Main {
    public static void main(String[] args) {

        Thread renderThread = new Thread(() -> Render.run(args));
        renderThread.setDaemon(true); // Marca el hilo como daemon para que no bloquee la salida de la aplicación.
        renderThread.start();

        RestauranteService restauranteService = new RestauranteService();
        
        for (int i = 0; i < RestauranteConfig.NUM_MESEROS; i++) {
            new Mesero(restauranteService).start();
        }
        
        for (int i = 0; i < RestauranteConfig.NUM_COCINEROS; i++) {
            new Cocinero(restauranteService).start();
        }
        
        for (int i = 0; i < RestauranteConfig.NUM_CLIENTES; i++) {
            new Cliente(restauranteService).start();
            try {
                Thread.sleep(RestauranteConfig.DELAY_BASE);
            } catch (InterruptedException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}