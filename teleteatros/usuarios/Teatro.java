package teleteatros.usuarios;

import java.time.LocalDate;
import java.util.ArrayList;

import teleteatros.excepciones.TeatroException;

public class Teatro{
    private int idt;
    private String nombre;
    private String ciudad;
    private int numFilas;
    private int numCols;
    private ArrayList<Espectaculo> espectaculos;
    private Butaca[][] butacas;

    /**
     * Constructor del Teatro. 
     * Inicializa los datos básicos y construye físicamente el patio de butacas.
     */
    public Teatro(int idt, String nombre, String ciudad, int numFilas, int numCols) {
        this.idt = idt;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.numFilas = numFilas;
        this.numCols = numCols;
        this.espectaculos = new ArrayList<>();
        this.butacas = new Butaca[numFilas][numCols];
        for (int f = 0; f < numFilas; f++) {
            for (int c = 0; c < numCols; c++) {
                this.butacas[f][c] = new Butaca(f, c);
            }
        }
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

    /**
     * Registra un nuevo espectáculo en el teatro si la fecha está libre.
     */
    public void nuevoEspectaculo(String nomEsp, String grupo, LocalDate fecha, int prTicket) throws TeatroException {
        // 1. Validar que la fecha esté libre (Regla de negocio del enunciado)
        for (Espectaculo e : espectaculos) {
            if (e.getFecha().equals(fecha)) {
                throw new TeatroException("Ya existe un espectáculo programado en esa fecha en este teatro.");
            }
        }
        
        // 2. Crear el espectáculo
        // Fíjate que le pasamos la matriz de butacas al constructor de Espectaculo
        // para que él mismo se encargue de generar todos sus tickets.
        Espectaculo nuevoEsp = new Espectaculo(nomEsp, grupo, fecha, prTicket, this.butacas);
        
        // 3. Añadir el nuevo espectáculo a nuestra lista
        espectaculos.add(nuevoEsp);
    }
    public Espectaculo[] buscarEspectaculos(String ciudad, LocalDate fechaIni, LocalDate fechaFin) {
        if(ciudad == null || !this.ciudad.equalsIgnoreCase(ciudad)) {
            return new Espectaculo[0]; // No es el teatro que buscamos
        }
        ArrayList<Espectaculo> encontrados = new ArrayList<>();
        for(Espectaculo e : espectaculos) {
            if((fechaIni == null || !e.getFecha().isBefore(fechaIni)) && 
               (fechaFin == null || !e.getFecha().isAfter(fechaFin))) {
                encontrados.add(e);
            }
        }
        encontrados.sort((e1, e2) -> {
            int cmp = e1.getFecha().compareTo(e2.getFecha());
            if (cmp != 0) return cmp;
            return e1.getNombre().compareTo(e2.getNombre());
        });
        return encontrados.toArray(new Espectaculo[0]);
    }
    public String disponibilidadEspectaculo(LocalDate fecha) throws TeatroException {
        Espectaculo espEncontrado = null;
        
        // 1. Buscamos el espectáculo que coincida exactamente con la fecha
        for (Espectaculo e : this.espectaculos) {
            if (e.getFecha().equals(fecha)) {
                espEncontrado = e;
                break; // Lo encontramos, dejamos de buscar
            }
        }
        
        // 2. Si no lo encontramos, lanzamos la excepción con el formato del PDF
        if (espEncontrado == null) {
            throw new TeatroException("No hay espectáculos programados en el teatro #" + this.idt + " el " + fecha);
        }
        
        // 3. Montamos la información del teatro + la información del espectáculo + el dibujo
        String info = "Espectáculo \"" + espEncontrado.getNombre() + "\"\n" +
                      "Teatro #" + this.idt + " " + this.nombre + " (" + this.ciudad + ")\n" +
                      "Grupo: " + espEncontrado.getGrupo() + "\n" +
                      "Fecha: " + espEncontrado.getFecha() + "\n" +
                      "Precio ticket: " + espEncontrado.getPrecioTicket() + "\n" +
                      espEncontrado.disponibilidad(); // Llamamos al método que hicimos antes
                      
        return info;
    }
}