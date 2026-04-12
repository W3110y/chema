package teleteatros.teatros;

import java.time.LocalDate;

/**
 * Clase que representa un evento o función concreta programada en un teatro.
 * Almacena los datos básicos del evento y gestiona la colección completa 
 * de tickets asociados a sus asientos.
 */
public class Espectaculo {
    
    /** Nombre comercial de la función o espectáculo. */
    private String nombre;
    
    /** Grupo, banda o compañía teatral que realiza el espectáculo. */
    private String grupo;
    
    /** Fecha exacta en la que está programada la función. */
    private LocalDate fecha;
    
    /** Precio base fijado para cada ticket de esta función. */
    private int prTicket;
    
    /** * Matriz bidimensional que almacena todos los tickets emitidos para este evento.
     * Su estructura (filas y columnas) es idéntica a la del patio de butacas del teatro.
     * (Asociación de multiplicidad * en el diagrama UML).
     */
    private Ticket[][] tickets;

    /**
     * Constructor de la clase Espectaculo.
     * Inicializa los atributos principales y genera automáticamente un ticket 
     * asociado a cada butaca física disponible en el teatro para esta fecha.
     * * @param nombre   El nombre comercial del espectáculo.
     * @param grupo    El artista o compañía que actúa.
     * @param fecha    La fecha en la que se realizará el evento.
     * @param prTicket El precio en euros de la entrada.
     * @param butacas  La matriz del patio de butacas proporcionada por el Teatro 
     * para generar los tickets correspondientes.
     */
    public Espectaculo(String nombre, String grupo, LocalDate fecha, int prTicket, Butaca[][] butacas) {
        this.nombre = nombre;
        this.grupo = grupo;
        this.fecha = fecha;
        this.prTicket = prTicket;
        
        // Inicializamos la matriz de tickets con las mismas dimensiones que las butacas
        this.tickets = new Ticket[butacas.length][butacas[0].length];
        
        // Iteramos correctamente por filas y columnas para generar los tickets
        for (int f = 0; f < butacas.length; f++) {
            for (int c = 0; c < butacas[f].length; c++) {
                // Instanciamos el ticket asociándole su butaca individual exacta y este espectáculo
                this.tickets[f][c] = new Ticket(0, butacas[f][c], this);
            }
        }
    }

    /**
     * Devuelve el nombre del espectáculo.
     * @return El texto con el nombre.
     */
    public String getNombre() { 
        return nombre; 
    }
    
    /**
     * Devuelve el grupo o compañía que realiza el espectáculo.
     * @return El texto con el nombre del grupo.
     */
    public String getGrupo() { 
        return grupo; 
    }
    
    /**
     * Devuelve la fecha programada para el espectáculo.
     * @return Un objeto LocalDate con la fecha.
     */
    public LocalDate getFecha() { 
        return fecha; 
    }
    
    /**
     * Devuelve el precio del ticket formateado como cadena de texto, 
     * tal y como exige el diagrama de diseño UML.
     * @return El precio convertido a texto.
     */
    public String getPrecioTicket() { 
        return String.valueOf(prTicket); 
    }

    /**
     * Genera una representación visual en formato texto del estado del patio de butacas
     * para este espectáculo, indicando qué asientos están disponibles.
     * * @return Una cadena de texto (String) multilinea con el mapa de asientos dibujado.
     */
    public String disponibilidad() {
        // Usamos StringBuilder porque vamos a concatenar mucho texto (es más eficiente en Java)
        StringBuilder sb = new StringBuilder();
        sb.append("Disponibilidad:\n");

        // Bucle doble para recorrer nuestra matriz de tickets
        for (int f = 0; f < tickets.length; f++) {
            sb.append("> Fila #").append(f).append(":");
            
            for (int c = 0; c < tickets[f].length; c++) {
                // Obtenemos el estado del ticket en esta coordenada
                int estado = tickets[f][c].getEstado();
                
                // Si el estado es 0 (suponiendo que 0 es libre), pintamos una D
                if (estado == 0) {
                    sb.append(" D");
                } 
                // En el futuro, si estado es 1 o 2, pintarías 'R' (Reservado) o 'C' (Comprado)
            }
            sb.append("\n"); // Salto de línea al terminar la fila
        }
        
        return sb.toString();
    }

    /**
     * Sobrescribe el método toString genérico de Java para devolver un bloque 
     * de texto formateado con los detalles básicos de este espectáculo.
     * * @return Una cadena de texto formateada para mostrar por pantalla.
     */
    @Override
    public String toString() {
        return "Espectáculo \"" + nombre + "\"\n" +
               "Grupo: " + grupo + "\n" +
               "Fecha: " + fecha + "\n" +
               "Precio ticket: " + prTicket;
    }
}