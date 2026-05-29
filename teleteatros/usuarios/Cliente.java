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

    /**
     * Comprueba si el cliente tiene actualmente alguna reserva sin pagar.
     * @return true si tiene al menos un ticket en estado reservado.
     */
    public boolean tieneReservaEnCurso() {
        // Recorremos la lista de tickets del cliente
        for (teleteatros.teatros.Ticket t : misTickets) {
            // Suponemos que 1 significa RESERVADO (o Ticket.ESTADO_RESERVADO si usaste constantes)
            if (t.getEstado() == 1) { 
                return true; // ¡Alarma! Encontramos una reserva
            }
        }
        return false; // Está limpio, no tiene reservas
    }

    /**
     * Cuenta cuántos tickets (comprados o reservados) tiene el cliente para un evento concreto.
     * @param esp El espectáculo que queremos consultar.
     * @return El número de tickets que ya posee.
     */
    public int contarTicketsDeEspectaculo(teleteatros.teatros.Espectaculo esp) {
        int contador = 0;
        
        for (teleteatros.teatros.Ticket t : misTickets) {
            // Comprobamos si el ticket pertenece al espectáculo que nos preguntan
            if (t.getEspectaculo().equals(esp)) {
                contador++;
            }
        }
        return contador;
    }

}