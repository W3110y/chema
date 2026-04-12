package teleteatros.teatros;

public class Ticket {

    private int estado;

    public Ticket(int estado, Butaca butaca, Espectaculo espectaculo) {
        this.estado = estado;
    }

    public int getEstado() {
        return estado;
    }
}