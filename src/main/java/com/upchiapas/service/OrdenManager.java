package service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import model.EstadoOrden;
import model.Orden;

public class OrdenManager {
    private Queue<Orden> colaOrdenes;
    private Map<Long, Orden> ordenesActivas;
    private int ordenCounter;

    public OrdenManager() {
        this.colaOrdenes = new LinkedList<>();
        this.ordenesActivas = new HashMap<>();
        this.ordenCounter = 0;
    }

    public synchronized void agregarOrdenActiva(long clienteId) {
        ordenesActivas.put(clienteId, null);
    }

    public synchronized Orden crearOrden(long clienteId) {
        Orden nuevaOrden = new Orden(ordenCounter++, clienteId);
        nuevaOrden.setEstado(EstadoOrden.ATENDIDA);
        ordenesActivas.put(clienteId, nuevaOrden);
        return nuevaOrden;
    }

    public synchronized Map<Long, Orden> getOrdenesActivas() {
        return new HashMap<>(ordenesActivas);
    }

    public synchronized void agregarOrdenACola(Orden orden) {
        if (!colaOrdenes.contains(orden)) {
            orden.setEstado(EstadoOrden.EN_COLA_COCINA);
            orden.setPosicionCola(colaOrdenes.size() + 1);
            colaOrdenes.add(orden);
        }
        actualizarCola();
    }

    private void actualizarCola() {
        int posicion = 1;
        for (Orden orden : colaOrdenes) {
            if (orden.getPosicionCola() != posicion) {
                orden.setPosicionCola(posicion);
            }
            posicion++;
        }
    }

    public synchronized Orden obtenerSiguienteOrdenEnCola() {
        return colaOrdenes.poll();
    }

    public synchronized boolean colaOrdenesVacia() {
        return colaOrdenes.isEmpty();
    }

    public synchronized Orden obtenerOrdenActiva(long clienteId) {
        return ordenesActivas.get(clienteId);
    }

    public synchronized void eliminarOrdenActiva(long clienteId) {
        ordenesActivas.remove(clienteId);
    }
}