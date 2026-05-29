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

    public double getSaldo() {
        return saldo; // O el nombre que le hayas puesto a tu variable de dinero
    }

    public void decrementarSaldo(double cantidad) {
        this.saldo -= cantidad;
    }

    /**
     * Hace efectiva la compra de todos los tickets que el cliente tiene en estado Reservado.
     * @throws TeatroException Si no tiene reservas o no tiene saldo.
     */
    public void comprarReserva() throws teleteatros.excepciones.TeatroException {
        // 1. Verificamos que tenga algo que comprar
        if (!tieneReservaEnCurso()) {
            throw new teleteatros.excepciones.TeatroException("El cliente NO tiene una reserva en curso");
        }
        
        // 2. Calculamos el coste total de la reserva recopilando los tickets implicados
        int costeTotal = 0;
        List<teleteatros.teatros.Ticket> ticketsReservados = new ArrayList<>();
        
        for (teleteatros.teatros.Ticket t : misTickets) {
            if (t.getEstado() == 1) { // 1 = Reservado
                ticketsReservados.add(t);
                costeTotal += t.getEspectaculo().getPrecioBase();
            }
        }
        
        // 3. Verificamos el saldo
        if (this.saldo < costeTotal) {
            throw new teleteatros.excepciones.TeatroException("El cliente no tiene saldo suficiente para comprar la reserva");
        }
        
        // 4. Ejecutamos la compra: cobramos y cambiamos estados
        this.decrementarSaldo(costeTotal);
        
        for (teleteatros.teatros.Ticket t : ticketsReservados) {
            t.setEstado(2); // 2 = Comprado
        }
    }

    /**
     * Cancela la reserva actual, liberando los tickets para que otros puedan comprarlos.
     * @throws TeatroException Si no tiene reservas activas.
     */
    public void anularReserva() throws teleteatros.excepciones.TeatroException {
        if (!tieneReservaEnCurso()) {
            throw new teleteatros.excepciones.TeatroException("El cliente NO tiene una reserva en curso");
        }
        
        // Lista temporal para guardar los tickets que vamos a quitarle al cliente
        List<teleteatros.teatros.Ticket> aBorrar = new ArrayList<>();
        
        for (teleteatros.teatros.Ticket t : misTickets) {
            if (t.getEstado() == 1) { // 1 = Reservado
                t.setEstado(0);         // Vuelven a estar Libres (0)
                t.setPropietario(null); // Ya no tienen dueño
                aBorrar.add(t);         // Lo marcamos para borrar
            }
        }
        
        // Eliminamos los tickets liberados del historial del cliente
        misTickets.removeAll(aBorrar);
    }
}