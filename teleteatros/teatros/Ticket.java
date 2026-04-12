package teleteatros.teatros;

public class Ticket {

    private int estado;
    private Espectaculo espectaculo;
    private Butaca butaca;

    public Ticket(int estado, Butaca butaca, Espectaculo espectaculo) {
        this.estado = estado;
        this.butaca = butaca;
        this.espectaculo = espectaculo;
    }

    public int getEstado() {
        return estado;
    }
}