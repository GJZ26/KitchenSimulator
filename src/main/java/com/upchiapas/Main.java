import service.*;
import worker.*;
import config.RestauranteConfig;

public class Main {
    public static void main(String[] args) {
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