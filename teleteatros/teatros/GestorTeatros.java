package teleteatros.teatros;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import teleteatros.excepciones.TeatroException;

public class GestorTeatros {
    
    /**
     * Mapa de teatros del sistema, indexado por su identificador (idt)
     */
    private Map<Integer, Teatro> teatros;
    
    /**
     * Contador interno para generar IDs únicos automáticamente
     */
    private int contadorId;
    
    public GestorTeatros() {
        teatros = new HashMap<>();
        contadorId = 0;
    }
    /**
     * Método que crea una nueva instancia de teatro y la agrega al mapa, indexada por su nombre
     * 
     * @param nom del teatro (ÚNICO)
     * @param ciudad del teatro
     * @param numFilas del teatro
     * @param numCols del teatro
     * @throws TeatroException si ya existe un teatro con ese nombre, 
     * 			si hubo un error interno en la creación del teatro
     */

    public void crearTeatro(String nom, String ciudad, int numFilas, int numCols)
            throws TeatroException {
        // 1. Validación: Recorremos los teatros existentes
        for (Teatro t : teatros.values()) {
            if (t.getNombre().equals(nom) && t.getCiudad().equals(ciudad)) {
                // Lanzamos la excepción idéntica a la salida esperada del PDF
                throw new TeatroException("Ya existe un teatro con nombre " + nom + " en " + ciudad);
            }
        }
        
        // 2. Creación: Instanciamos el teatro con el ID actual
        Teatro nuevoTeatro = new Teatro(contadorId, nom, ciudad, numFilas, numCols);
        
        // 3. Guardado: Lo metemos en el mapa y actualizamos el contador
        teatros.put(contadorId, nuevoTeatro);
        contadorId++; 
    }
    /**
     * Delega la creación de un espectáculo al teatro correspondiente
     */
    public void nuevoEspectaculo(int idt, String nomEsp, String grupo, LocalDate fecha, int prTicket) throws TeatroException {
        // 1. Buscamos el teatro por su clave en el mapa
        Teatro t = teatros.get(idt);
        
        // 2. Si no existe, lanzamos excepción
        if (t == null) {
            throw new TeatroException("No existe un teatro con el identificador #" + idt);
        }
        
        // 3. Si existe, le pasamos la responsabilidad al teatro
        t.nuevoEspectaculo(nomEsp, grupo, fecha, prTicket);
    }

/**
     * Busca espectáculos aplicando filtros opcionales y los devuelve ordenados.
     */
    public String[] buscarEspectaculos(String ciudad, LocalDate fIni, LocalDate fFin) {
        // 1. Extraer los teatros del mapa a una lista para poder ordenarlos
        List<Teatro> listaTeatros = new ArrayList<>(teatros.values());
        
        // 2. ORDENAR LOS TEATROS POR CIUDAD (Clave para cumplir el UML)
        listaTeatros.sort((t1, t2) -> t1.getCiudad().compareToIgnoreCase(t2.getCiudad()));

        // 3. Lista para ir guardando los textos finales de salida
        List<String> resultados = new ArrayList<>();

        // 4. Recorrer los teatros ya ordenados alfabéticamente
        for (Teatro t : listaTeatros) {
            
            // Le pedimos al teatro que nos dé sus espectáculos filtrados (nos llegan ordenados por fecha)
            Espectaculo[] espDelTeatro = t.buscarEspectaculos(ciudad, fIni, fFin);
            
            // 5. Construimos el texto formateado para cada espectáculo encontrado
            for (Espectaculo e : espDelTeatro) {
                // Montamos el String uniendo los datos del Teatro actual y del Espectaculo
                String info = "Espectáculo \"" + e.getNombre() + "\"\n" +
                              "Teatro #" + t.getIdt() + " " + t.getNombre() + " (" + t.getCiudad() + ")\n" +
                              "Grupo: " + e.getGrupo() + "\n" +
                              "Fecha: " + e.getFecha() + "\n" +
                              "Precio ticket: " + e.getPrecioTicket() + "\n";
                
                resultados.add(info);
            }
        }

        // 6. Convertir a array de String y devolver
        return resultados.toArray(new String[0]);
    }

    public String disponibilidadEspectaculo(int idt, LocalDate fecha) throws TeatroException {
        // 1. Buscamos el teatro
        Teatro t = teatros.get(idt);
        
        // 2. Validamos que exista
        if (t == null) {
            throw new TeatroException("No existe un teatro con el identificador #" + idt);
        }
        
        // 3. Delegamos el trabajo en el teatro
        return t.disponibilidadEspectaculo(fecha);
    }	

}
