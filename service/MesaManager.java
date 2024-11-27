package service;

public class MesaManager {
    private final int MAX_MESAS;
    private boolean[] mesasOcupadas;
    private int mesasDisponibles;

    public MesaManager(int capacidad) {
        this.MAX_MESAS = capacidad;
        this.mesasOcupadas = new boolean[MAX_MESAS];
        this.mesasDisponibles = MAX_MESAS;
    }

    public synchronized int asignarMesa() {
        for (int i = 0; i < MAX_MESAS; i++) {
            if (!mesasOcupadas[i]) {
                mesasOcupadas[i] = true;
                mesasDisponibles--;
                return i;
            }
        }
        return -1;
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

    public synchronized int getMesasDisponibles() {
        return mesasDisponibles;
    }
}