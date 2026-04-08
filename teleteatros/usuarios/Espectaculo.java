package teleteatros.usuarios;
import java.time.LocalDate;


public class Espectaculo {
    private String nombre;
    private String grupo;
    private LocalDate fecha;
    private int prTicket;

    public Espectaculo(String nombre, String grupo, LocalDate fecha, int prTicket) {
        this.nombre = nombre;
        this.grupo = grupo;
        this.fecha = fecha;
        this.prTicket = prTicket;

    }

    public String getNombre() {
        return nombre;
    }

    public String getGrupo() {
        return grupo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getPrecioTicket() {
        return Integer.toString(prTicket);
    }

    public String disponibilidad(){
        // TODO Auto-generated method stub
        return null;
    }

    public String toString(){
        return "Espectaculo: " + nombre + " del grupo " + grupo + " el dia " + fecha.toString() + " con precio de ticket: " + prTicket;
    }
}