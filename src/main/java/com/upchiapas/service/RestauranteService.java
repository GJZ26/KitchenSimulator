package com.upchiapas.service;

import com.upchiapas.config.RestauranteConfig;
import com.upchiapas.model.EstadoOrden;
import com.upchiapas.model.Orden;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;


public class RestauranteService {
    private static final int MAX_MESAS = RestauranteConfig.CAPACIDAD;
    private final Object lock = new Object();
    
    private int mesasDisponibles;
    private Queue<Thread> colaEspera;
    private Queue<Orden> colaOrdenes;
    private Map<Long, Orden> ordenesActivas;
    private boolean[] mesasOcupadas;
    private int ordenCounter;

    public RestauranteService() {
        this.mesasDisponibles = MAX_MESAS;
        this.colaEspera = new LinkedList<>();
        this.colaOrdenes = new LinkedList<>();
        this.ordenesActivas = new HashMap<>();
        this.mesasOcupadas = new boolean[MAX_MESAS];
        this.ordenCounter = 0;
    }

    public int intentarSentarse() throws InterruptedException {
        synchronized(lock) {
            Thread cliente = Thread.currentThread();
            if (!colaEspera.contains(cliente)) {
                colaEspera.add(cliente);
            }
    
            while (mesasDisponibles == 0 || !colaEspera.isEmpty() && colaEspera.peek() != cliente) {
                System.out.println("Cliente " + cliente.threadId() + " esperando mesas");
                lock.wait();
            }
    
            if (!colaEspera.isEmpty() && colaEspera.peek() == cliente) {
                int mesaAsignada = -1;
                for (int i = 0; i < MAX_MESAS; i++) {
                    if (!mesasOcupadas[i]) {
                        mesasOcupadas[i] = true;
                        mesaAsignada = i;
                        break;
                    }
                }
                mesasDisponibles--;
                colaEspera.poll(); 
                ordenesActivas.put(cliente.threadId(), null);
                System.out.println("Cliente " + cliente.threadId() + " sentado en la mesa: " + mesaAsignada);
                lock.notifyAll(); 
                return mesaAsignada;
            }
            return -1;
        }
    }
    
    
    public Orden atenderCliente() throws InterruptedException {
        synchronized(lock) {
            while (true) {
                for (Map.Entry<Long, Orden> entry : ordenesActivas.entrySet()) {
                    if (entry.getValue() == null) {
                        long clienteId = entry.getKey();
                        Orden nuevaOrden = new Orden(ordenCounter++, clienteId);
                        nuevaOrden.setEstado(EstadoOrden.ATENDIDA);
                        nuevaOrden.setMeseroAsignado(Thread.currentThread().threadId());
                        ordenesActivas.put(clienteId, nuevaOrden);
                        System.out.println("Mesero: " + Thread.currentThread().threadId() + " atiende la orden: " + nuevaOrden.getId() + " del cliente: " + clienteId);
                        return nuevaOrden;
                    }
                }
                lock.wait(RestauranteConfig.DELAY_BASE);
            }
        }
    }
    
    public void ordenEnCola(Orden orden) {
        synchronized(lock) {
            if (!colaOrdenes.contains(orden)) {
                orden.setEstado(EstadoOrden.EN_COLA_COCINA);
                orden.setPosicionCola(colaOrdenes.size() + 1);
                colaOrdenes.add(orden);
                System.out.println("Mesero: " + Thread.currentThread().threadId() + " coloca la orden: " + orden.getId() + " del cliente: " + orden.getIdCliente() + " en cola de cocina. - Pos: " + orden.getPosicionCola());
                lock.notifyAll();
            }
        }
    }
    
    public Orden prepararOrden() throws InterruptedException {
        synchronized(lock) {
            while (colaOrdenes.isEmpty()) {
                lock.wait(RestauranteConfig.DELAY_BASE);
            }
            Orden orden = colaOrdenes.poll();
            if (orden != null) {
                orden.setEstado(EstadoOrden.EN_PREPARACION);
                System.out.println("Cocinero: " + Thread.currentThread().threadId() + " esta cocinando la orden: " + orden.getId() + " del cliente: " + orden.getIdCliente());
                actualizarCola();
            }
            return orden;
        }
    }
    
    private void actualizarCola() {
        int posicion = 1;
        for (Orden orden : colaOrdenes) {
            if (orden.getPosicionCola() != posicion) {
                orden.setPosicionCola(posicion);
                System.out.println("Orden: " + orden.getId() + " ha avanzado en la cola: " + posicion);
            }
            posicion++;
        }
    }
    
    public void terminarOrden(Orden orden) {
        synchronized(lock) {
            if (orden.getEstado() != EstadoOrden.LISTA) {
                orden.setEstado(EstadoOrden.LISTA);
                System.out.println("Cocinero: " + Thread.currentThread().threadId() + " termina la orden: " + orden.getId() + " del cliente: " + orden.getIdCliente() +  " - Mesero: " + orden.getMeseroAsignado());
                lock.notifyAll();
            }
        }
    }
    
    public Orden servirOrden() throws InterruptedException {
        synchronized(lock) {
            long meseroId = Thread.currentThread().threadId();
            while (true) {
                for (Orden orden : ordenesActivas.values()) {
                    if (orden != null && 
                        orden.getEstado() == EstadoOrden.LISTA && 
                        orden.getMeseroAsignado() == meseroId) {
                        orden.setEstado(EstadoOrden.SERVIDA);
                        orden.setOrdenServida(true);
                        System.out.println("Mesero: " + meseroId + " sirve la orden: " + orden.getId() + " al cliente: " + orden.getIdCliente());
                        return orden;
                    }
                }
                lock.wait(RestauranteConfig.DELAY_BASE);
            }
        }
    }
    
    public boolean retirarCliente(long clienteId) {
        synchronized(lock) {
            Orden orden = ordenesActivas.get(clienteId);
            return orden != null && orden.isOrdenServida();
        }
    }
    
    public void liberarMesa(int numeroMesa) {
        synchronized(lock) {
            long clienteId = Thread.currentThread().threadId();
            Orden orden = ordenesActivas.get(clienteId);
            if (orden != null) {
                orden.setEstado(EstadoOrden.COMPLETADA);
                System.out.println("Cliente " + clienteId + " termina de comer. Orden " + orden.getId() + " completada");
            }
            mesasOcupadas[numeroMesa] = false;
            mesasDisponibles++;
            ordenesActivas.remove(clienteId);
            System.out.println("Cliente " + clienteId + " libera mesa " + numeroMesa);
            lock.notifyAll();
        }
    }
}