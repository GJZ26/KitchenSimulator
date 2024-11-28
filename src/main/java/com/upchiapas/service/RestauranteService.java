package com.upchiapas.service;

import com.upchiapas.config.RestauranteConfig;
import com.upchiapas.model.EstadoOrden;
import com.upchiapas.model.Orden;
import com.upchiapas.model.RenderData;
import com.upchiapas.render.collection.RenderEntityCollection;

public class RestauranteService {
    private final Object lock = new Object();
    private MesaManager mesaManager;
    private ColaEsperaManager colaEsperaManager;
    private OrdenManager ordenManager;

    public RestauranteService(RenderEntityCollection dishes) {
        this.mesaManager = new MesaManager(RestauranteConfig.CAPACIDAD);
        this.colaEsperaManager = new ColaEsperaManager();
        this.ordenManager = new OrdenManager(dishes);
    }

    public RenderData intentarSentarse() throws InterruptedException {
        synchronized (lock) {
            Thread cliente = Thread.currentThread();
            colaEsperaManager.agregarACola(cliente);

            while (!mesaManager.hayMesasDisponibles() ||
                    !colaEsperaManager.colaVacia() &&
                            colaEsperaManager.siguienteEnCola() != cliente) {
                System.out.println("Cliente " + cliente.threadId() + " esperando mesas");
                lock.wait();
            }

            if (!colaEsperaManager.colaVacia() && colaEsperaManager.siguienteEnCola() == cliente) {
                RenderData mesaAsignada = mesaManager.asignarMesa();
                colaEsperaManager.siguienteSalirCola();
                ordenManager.agregarOrdenActiva(cliente.threadId());
                System.out.println("Cliente " + cliente.threadId() + " sentado en la mesa: " + mesaAsignada);
                lock.notifyAll();
                return mesaAsignada;
            }
            throw new RuntimeException("Error al intentar sentarse");
        }
    }

    public Orden atenderCliente() throws InterruptedException {
        synchronized (lock) {
            while (true) {
                for (Long clienteId : ordenManager.getOrdenesActivas().keySet()) {
                    if (ordenManager.obtenerOrdenActiva(clienteId) == null) {
                        Orden nuevaOrden = ordenManager.crearOrden(clienteId);
                        nuevaOrden.setEstado(EstadoOrden.ATENDIDA);
                        nuevaOrden.setMeseroAsignado(Thread.currentThread().threadId());
                        System.out.println("Mesero: " + Thread.currentThread().threadId() + " atiende la orden: " + nuevaOrden.getId() + " del cliente: " + clienteId);
                        return nuevaOrden;
                    }
                }
                lock.wait(RestauranteConfig.DELAY_BASE);
            }
        }
    }

    public void ordenEnCola(Orden orden) {
        synchronized (lock) {
            ordenManager.agregarOrdenACola(orden);
            System.out.println("Mesero: " + Thread.currentThread().threadId() + " coloca la orden: " + orden.getId() + " del cliente: " + orden.getIdCliente() + " en cola de cocina. - Pos: " + orden.getPosicionCola());
            lock.notifyAll();
        }
    }

    public Orden prepararOrden() throws InterruptedException {
        synchronized (lock) {
            while (ordenManager.colaOrdenesVacia()) {
                lock.wait(RestauranteConfig.DELAY_BASE);
            }
            Orden orden = ordenManager.obtenerSiguienteOrdenEnCola();
            if (orden != null) {
                orden.setEstado(EstadoOrden.EN_PREPARACION);
                System.out.println("Cocinero: " + Thread.currentThread().threadId() + " esta cocinando la orden: " + orden.getId() + " del cliente: " + orden.getIdCliente());
            }
            return orden;
        }
    }

    public void terminarOrden(Orden orden) {
        synchronized (lock) {
            if (orden.getEstado() != EstadoOrden.LISTA) {
                orden.setEstado(EstadoOrden.LISTA);
                System.out.println("Cocinero: " + Thread.currentThread().threadId() + " termina la orden: " + orden.getId() + " del cliente: " + orden.getIdCliente() + " - Mesero: " + orden.getMeseroAsignado());
                lock.notifyAll();
            }
        }
    }

    public Orden servirOrden() throws InterruptedException {
        synchronized (lock) {
            long meseroId = Thread.currentThread().threadId();
            while (true) {
                for (Orden orden : ordenManager.getOrdenesActivas().values()) {
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
        synchronized (lock) {
            Orden orden = ordenManager.obtenerOrdenActiva(clienteId);
            return orden != null && orden.isOrdenServida();
        }
    }

    public void liberarMesa(int numeroMesa) {
        synchronized (lock) {
            long clienteId = Thread.currentThread().threadId();
            Orden orden = ordenManager.obtenerOrdenActiva(clienteId);

            if (orden != null && orden.isOrdenServida()) {
                orden.setEstado(EstadoOrden.COMPLETADA);
                System.out.println("Cliente " + clienteId + " termina de comer. Orden " + orden.getId() + " completada");
            }
            mesaManager.liberarMesa(numeroMesa);
            ordenManager.eliminarOrdenActiva(clienteId);
            System.out.println("Cliente " + clienteId + " libera mesa " + numeroMesa);
            lock.notifyAll();
        }
    }

    public RenderData[] obtenerMesasRenderizables() {
        return mesaManager.getMesasDisponibles();
    }

}