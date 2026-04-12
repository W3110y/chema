package teleteatros.teatros;

/**
 * Clase que representa un ticket individual para un espectáculo.
 * Asocia un estado de disponibilidad con una butaca física y un evento concreto.
 */
public class Ticket {

    /**
     * Estado actual del ticket. 
     * (Por ejemplo: 0 = Libre, 1 = Reservado, 2 = Comprado).
     */
    private int estado;
    
    /**
     * Referencia a la butaca física del teatro a la que corresponde este ticket.
     * (Asociación de multiplicidad 1 en el diagrama UML).
     */
    private Butaca butaca;
    
    /**
     * Referencia al espectáculo para el cual se ha emitido este ticket.
     * (Asociación de multiplicidad 1 en el diagrama UML).
     */
    private Espectaculo espectaculo;

    /**
     * Constructor de la clase Ticket.
     * Inicializa el estado del ticket y establece sus relaciones obligatorias.
     * * @param estado      El estado inicial del ticket al crearse (normalmente 0, libre).
     * @param butaca      El objeto Butaca exacto al que queda vinculado.
     * @param espectaculo El objeto Espectaculo al que pertenece este ticket.
     */
    public Ticket(int estado, Butaca butaca, Espectaculo espectaculo) {
        this.estado = estado;
        this.butaca = butaca;             // ¡Crucial para guardar la referencia!
        this.espectaculo = espectaculo;   // ¡Crucial para guardar la referencia!
    }

    /**
     * Devuelve el estado actual de este ticket.
     * * @return Un entero que representa si está libre, reservado o comprado.
     */
    public int getEstado() {
        return estado;
    }

}