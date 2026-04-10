package teleteatros.usuarios;

import java.time.LocalDate;

public class Espectaculo {
    private String nombre;
    private String grupo;
    private LocalDate fecha;
    private int prTicket;
    
    private Ticket[][] tickets;

    // Constructor: Inicializa los atributos y las asociaciones obligatorias
    public Espectaculo(String nombre, String grupo, LocalDate fecha, int prTicket, Butaca[][] butacas) {
        this.nombre = nombre;
        this.grupo = grupo;
        this.fecha = fecha;
        this.prTicket = prTicket;
        
        this.tickets = new Ticket[butacas.length][butacas[0].length];
        
        // CORRECCIÓN: Iteramos correctamente por filas y columnas
        for (int f = 0; f < butacas.length; f++) {
            for (int c = 0; c < butacas[f].length; c++) {
                // Instanciamos el ticket asociándole su butaca individual exacta
                this.tickets[f][c] = new Ticket(0, butacas[f][c]);
            }
        }
    }

    public String getNombre() { return nombre; }
    public String getGrupo() { return grupo; }
    public LocalDate getFecha() { return fecha; }
    public String getPrecioTicket() { return String.valueOf(prTicket); }

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

    public String toString() {
        return "Espectáculo \"" + nombre + "\"\n" +
               "Grupo: " + grupo + "\n" +
               "Fecha: " + fecha + "\n" +
               "Precio ticket: " + prTicket;
    }
}