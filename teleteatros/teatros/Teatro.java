package teleteatros.teatros;

import java.time.LocalDate;
import java.util.ArrayList;

import teleteatros.excepciones.TeatroException;

/**
 * Clase que representa un teatro físico dentro de la red Teleteatros.
 * Gestiona su propia disposición de asientos (butacas) y el calendario 
 * de espectáculos programados en él.
 */
public class Teatro {
    
    /** Identificador numérico único asignado al teatro por el sistema. */
    private int idt;
    
    /** Nombre del teatro (ej. "Calderón"). */
    private String nombre;
    
    /** Ciudad donde está ubicado el teatro (ej. "Valladolid"). */
    private String ciudad;
    
    /** Número de filas que componen el patio de butacas. */
    private int numFilas;
    
    /** Número de columnas que componen el patio de butacas. */
    private int numCols;
    
    /** * Colección dinámica que almacena todos los espectáculos programados en este teatro.
     * (Asociación de multiplicidad * en el diagrama UML).
     */
    private ArrayList<Espectaculo> espectaculos;
    
    /** * Matriz bidimensional que representa físicamente los asientos del teatro.
     * (Asociación de multiplicidad * en el diagrama UML).
     */
    private Butaca[][] butacas;

    /**
     * Constructor principal de la clase Teatro. 
     * Inicializa los datos básicos y construye físicamente la matriz del patio de butacas.
     * * @param idt      Identificador único del teatro.
     * @param nombre   Nombre comercial del teatro.
     * @param ciudad   Ciudad de ubicación.
     * @param numFilas Cantidad de filas de butacas.
     * @param numCols  Cantidad de columnas de butacas.
     */
    public Teatro(int idt, String nombre, String ciudad, int numFilas, int numCols) {
        this.idt = idt;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.numFilas = numFilas;
        this.numCols = numCols;
        this.espectaculos = new ArrayList<>();
        this.butacas = new Butaca[numFilas][numCols];
        
        // Generamos todas las butacas físicas basándonos en las dimensiones dadas
        for (int f = 0; f < numFilas; f++) {
            for (int c = 0; c < numCols; c++) {
                this.butacas[f][c] = new Butaca(f, c);
            }
        }
    }

    /** @return El identificador numérico del teatro. */
    public int getIdt() {
        return idt;
    }

    /** @return El nombre del teatro. */
    public String getNombre() {
        return nombre;
    }

    /** @return La ciudad donde se ubica el teatro. */
    public String getCiudad() {
        return ciudad;
    }

    /** @return El total de filas del patio de butacas. */
    public int getNumFilas() {
        return numFilas;
    }

    /** @return El total de columnas del patio de butacas. */
    public int getNumCols() {
        return numCols;
    }

    /**
     * Registra un nuevo espectáculo en el calendario del teatro, asegurando
     * que la fecha solicitada esté completamente libre.
     * * @param nomEsp   Nombre del nuevo espectáculo.
     * @param grupo    Grupo o compañía artística.
     * @param fecha    Fecha exacta de la representación.
     * @param prTicket Precio fijado para la entrada.
     * @throws TeatroException Si ya existe otro espectáculo programado ese mismo día.
     */
    public void nuevoEspectaculo(String nomEsp, String grupo, LocalDate fecha, int prTicket) throws TeatroException {
        // 1. Validar que la fecha esté libre (Regla de negocio)
        for (Espectaculo e : espectaculos) {
            if (e.getFecha().equals(fecha)) {
                throw new TeatroException("Ya existe un espectáculo programado en esa fecha en este teatro.");
            }
        }
        
        // 2. Crear el espectáculo pasándole la matriz de butacas para que genere sus tickets
        Espectaculo nuevoEsp = new Espectaculo(nomEsp, grupo, fecha, prTicket, this.butacas);
        
        // 3. Añadir el nuevo espectáculo al calendario
        espectaculos.add(nuevoEsp);
    }

    /**
     * Busca los espectáculos de este teatro que coincidan con la ciudad y el rango de fechas.
     * Los parámetros de fecha son opcionales (pueden ser null).
     * * @param ciudad   La ciudad de interés (se usa para descartar el teatro si no coincide).
     * @param fechaIni Límite inferior de la búsqueda (opcional).
     * @param fechaFin Límite superior de la búsqueda (opcional).
     * @return Un array con los espectáculos encontrados y ordenados por fecha.
     */
    public Espectaculo[] buscarEspectaculos(String ciudad, LocalDate fechaIni, LocalDate fechaFin) {
        // Filtro rápido: si buscan en otra ciudad, devolvemos un array vacío enseguida
        if(ciudad != null && !this.ciudad.equalsIgnoreCase(ciudad)) {
            return new Espectaculo[0]; 
        }
        
        ArrayList<Espectaculo> encontrados = new ArrayList<>();
        
        // Aplicamos los filtros de fecha usando evaluación de cortocircuito para evitar NullPointerException
        for(Espectaculo e : espectaculos) {
            if((fechaIni == null || !e.getFecha().isBefore(fechaIni)) && 
               (fechaFin == null || !e.getFecha().isAfter(fechaFin))) {
                encontrados.add(e);
            }
        }
        
        // Ordenación de la lista usando una expresión Lambda de Java 8.
        // Ordena cronológicamente y, en caso de empate, alfabéticamente por nombre.
        encontrados.sort((e1, e2) -> {
            int cmp = e1.getFecha().compareTo(e2.getFecha());
            if (cmp != 0) return cmp;
            return e1.getNombre().compareTo(e2.getNombre());
        });
        
        return encontrados.toArray(new Espectaculo[0]);
    }

    /**
     * Obtiene la disponibilidad visual de los asientos para un día concreto.
     * * @param fecha La fecha del espectáculo del que queremos ver el mapa de asientos.
     * @return Una cadena de texto formateada con los detalles de la función y la matriz de butacas.
     * @throws TeatroException Si no existe ningún espectáculo programado en la fecha solicitada.
     */
    public String disponibilidadEspectaculo(LocalDate fecha) throws TeatroException {
        Espectaculo espEncontrado = null;
        
        // 1. Buscamos el espectáculo programado para esa fecha exacta
        for (Espectaculo e : this.espectaculos) {
            if (e.getFecha().equals(fecha)) {
                espEncontrado = e;
                break; // Lo encontramos, optimizamos saliendo del bucle
            }
        }
        
        // 2. Si no hay función ese día, lanzamos excepción
        if (espEncontrado == null) {
            throw new TeatroException("No hay espectáculos programados en el teatro #" + this.idt + " el " + fecha);
        }
        
        // 3. Montamos el String final concatenando la información del Teatro y del Espectáculo
        String info = "Espectáculo \"" + espEncontrado.getNombre() + "\"\n" +
                      "Teatro #" + this.idt + " " + this.nombre + " (" + this.ciudad + ")\n" +
                      "Grupo: " + espEncontrado.getGrupo() + "\n" +
                      "Fecha: " + espEncontrado.getFecha() + "\n" +
                      "Precio ticket: " + espEncontrado.getPrecioTicket() + "\n" +
                      espEncontrado.disponibilidad(); // Delega el dibujo al propio espectáculo
                      
        return info;
    }
}