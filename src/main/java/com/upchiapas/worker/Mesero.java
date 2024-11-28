package worker;

import service.RestauranteService;
import model.Orden;

public class Mesero extends Thread {
    private final RestauranteService restauranteService;
    
    public Mesero(RestauranteService restauranteService) {
        this.restauranteService = restauranteService;
    }
    
    @Override
    public void run() {
        while (true) {
            try {
                Orden orden = restauranteService.atenderCliente();
                Thread.sleep((long)(Math.random() * 1000 + 500));
                restauranteService.ordenEnCola(orden);
                orden = restauranteService.servirOrden();
                Thread.sleep((long)(Math.random() * 1000 + 500));
            } catch (InterruptedException e) {
                System.out.println("Error " + threadId() + " - " + e.getMessage());
            }
        }
    }
}