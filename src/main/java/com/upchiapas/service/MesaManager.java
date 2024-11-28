package com.upchiapas.service;

import com.upchiapas.model.Direction;
import com.upchiapas.model.RenderData;
import com.upchiapas.render.Render;
import com.upchiapas.render.RenderResource;

public class MesaManager {
    private final int MAX_MESAS;
    private boolean[] mesasOcupadas;
    private RenderData[] mesas;
    private int mesasDisponibles;


    public MesaManager(int capacidad) {
        this.MAX_MESAS = capacidad;
        this.mesas = new RenderData[MAX_MESAS];
        this.mesasOcupadas = new boolean[MAX_MESAS];
        this.mesasDisponibles = MAX_MESAS;

        for (int i = 0; i < MAX_MESAS; i++) {
            mesas[i] = new RenderData(10 + (i * 70), 250, 0, Direction.UP, i);
            mesasOcupadas[i] = false;
        }
    }


    public synchronized RenderData asignarMesa() {
        for (int i = 0; i < MAX_MESAS; i++) {
            if (!mesasOcupadas[i]) {
                mesasOcupadas[i] = true;
                mesasDisponibles--;
                return mesas[i];
            }
        }
        throw new RuntimeException("No se puede asignar la mesa");
    }

    public synchronized void liberarMesa(int numeroMesa) {
        if (numeroMesa >= 0 && numeroMesa < MAX_MESAS) {
            mesasOcupadas[numeroMesa] = false;
            mesasDisponibles++;
        }
    }

    public synchronized boolean hayMesasDisponibles() {
        return mesasDisponibles > 0;
    }

    public RenderData[] getMesasDisponibles() {
        return mesas;
    }
}