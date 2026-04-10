package teleteatros.usuarios;

public class Ticket {

    private int estado;
    private Espectaculo espectaculo;
    private Butaca butaca;

    public Ticket(int estado, Butaca butaca) {
        this.estado = estado;
        this.butaca = butaca;
    }

    public int getEstado() {
        return estado;
    }
}