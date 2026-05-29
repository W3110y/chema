package teleteatros.usuarios;

import java.util.List;
import java.util.ArrayList;
import teleteatros.teatros.Ticket;

private List<Ticket> misTickets;

public class Cliente extends Usuario{
	
    private int saldo;
    List<Ticket> misTickets; 

    public Cliente(String l, String p, String n) {
        super(l, p, n);
        saldo = 0;
        misTickets = new ArrayList<>();
    }
    /**
    * Método que devuelve una descripción del cliente
    * 
    * @return descripción
    */
    @Override

    public String toString() {
        // Compone una cadena con todos los campos y la retorna
        String cadena = super.toString();
        cadena += "\n Tipo: Cliente";
        cadena += "\n Saldo: " + saldo;
        return cadena;
    }

    public void incrementarSaldo(int i) {
        saldo += i;
    }

    public void addTicket(Ticket t) {
        this.misTickets.add(t);
    }
    
    public List<Ticket> getMisTickets() {
        return this.misTickets;
    }

}