package com.upchiapas.service;

import java.util.LinkedList;
import java.util.Queue;

public class ColaEsperaManager {
    private Queue<Thread> colaEspera;

    public ColaEsperaManager() {
        this.colaEspera = new LinkedList<>();
    }

    public synchronized void agregarACola(Thread cliente) {
        if (!colaEspera.contains(cliente)) {
            colaEspera.add(cliente);
        }
    }

    public synchronized Thread siguienteEnCola() {
        return colaEspera.peek();
    }

    public synchronized Thread siguienteSalirCola() {
        return colaEspera.poll();
    }

    public synchronized boolean estaEnCola(Thread cliente) {
        return colaEspera.contains(cliente);
    }

    public synchronized boolean colaVacia() {
        return colaEspera.isEmpty();
    }
}