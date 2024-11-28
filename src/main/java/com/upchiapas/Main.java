package com.upchiapas;

import com.upchiapas.render.Render;
import com.upchiapas.render.collection.RenderEntityCollection;
import com.upchiapas.service.*;
import com.upchiapas.worker.*;
import com.upchiapas.config.RestauranteConfig;

public class Main {
    public static void main(String[] args) {

        RenderEntityCollection clients = new RenderEntityCollection();
        RenderEntityCollection dishes = new RenderEntityCollection();
        RenderEntityCollection chefs = new RenderEntityCollection();

        RestauranteService restauranteService = new RestauranteService();

        Render render = new Render();

        render.setTables(restauranteService.obtenerMesasRenderizables());

        render.setClients(clients);
        render.setDishes(dishes);
        render.setChefs(chefs);

        Thread renderThread = new Thread(() -> render.run(args));
        renderThread.setDaemon(true); // Marca el hilo como daemon para que no bloquee la salida de la aplicación.
        renderThread.start();


        for (int i = 0; i < RestauranteConfig.NUM_MESEROS; i++) {
            new Mesero(restauranteService).start();
        }

        for (int i = 0; i < RestauranteConfig.NUM_COCINEROS; i++) {
            new Cocinero(restauranteService, chefs, i).start();
        }

        for (int i = 0; i < RestauranteConfig.NUM_CLIENTES; i++) {
            new Cliente(restauranteService, clients).start();
            try {
                Thread.sleep(RestauranteConfig.DELAY_BASE);
            } catch (InterruptedException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}