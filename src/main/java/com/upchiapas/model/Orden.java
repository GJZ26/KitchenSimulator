package com.upchiapas.model;

public class Orden {
    private final int id;
    private final long idCliente;
    private EstadoOrden estado;
    private long meseroAsignado;
    private int posicionCola;
    private boolean ordenServida;
    
    public Orden(int id, long idCliente) {
        this.id = id;
        this.idCliente = idCliente;
        this.estado = EstadoOrden.ESPERANDO_MESERO;
        this.meseroAsignado = -1;
        this.ordenServida = false;
    }
    
    public int getId() { 
        return id; 
    }

    public long getIdCliente() { 
        return idCliente; 
    }

    public EstadoOrden getEstado() { 
        return estado; 
    }

    public void setEstado(EstadoOrden estado) { 
        this.estado = estado; 
    }

    public long getMeseroAsignado() { 
        return meseroAsignado; 
    }

    public void setMeseroAsignado(long meseroId) { 
        this.meseroAsignado = meseroId; 
    }

    public int getPosicionCola() { 
        return posicionCola; 
    }

    public void setPosicionCola(int posicion) { 
        this.posicionCola = posicion; 
    }

    public boolean isOrdenServida() { 
        return ordenServida; 
    }
    
    public void setOrdenServida(boolean servida) { 
        this.ordenServida = servida; 
    }
}