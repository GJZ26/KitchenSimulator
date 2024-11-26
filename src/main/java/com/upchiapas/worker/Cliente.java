package com.upchiapas.worker;


import com.upchiapas.service.RestauranteService;

public class Cliente extends Thread {
    private final RestauranteService restauranteService;
    private int numeroMesa;
    
    public Cliente(RestauranteService restauranteService) {
        this.restauranteService = restauranteService;
    }
    
    @Override
    public void run() {
        try {
            numeroMesa = restauranteService.intentarSentarse();
            while (!restauranteService.retirarCliente(threadId())) {
                Thread.sleep(1000);
            }
            Thread.sleep((long)(Math.random() * 5000 + 3000));
            restauranteService.liberarMesa(numeroMesa);
        } catch (InterruptedException e) {
            System.out.println("Error " + threadId() + " - " + e.getMessage());
        }             
    }
}