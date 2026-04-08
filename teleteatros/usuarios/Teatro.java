import java.time.LocalDate;

public abstract class Teatro{
    private int idt;
    private String nombre;
    private String ciudad;
    private int numFilas;
    private int numCols;

    public Teatro(int idt, String nombre, String ciudad, int numFilas, int numCols) {
        this.idt = idt;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.numFilas = numFilas;
        this.numCols = numCols;
    }
    public int getIdt() {
        return idt;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public int getNumFilas() {
        return numFilas;
    }

    public int getNumCols() {
        return numCols;
    }

    public void nuevoEspectaculo(String nomEsp, String grupo, LocalDate fecha, int prTicket) {
        // TODO Auto-generated method stub
        
    }
    public Espectaculo[] buscarEspectaculos(String ciudad, LocalDate fechaIni, LocalDate fechaFin) {
        // TODO Auto-generated method stub
        return null;
    }
    public String disponibilidadEspectaculo(LocalDate fecha) {
        // TODO Auto-generated method stub
        return null;
    }
}