package teleteatros.teatros;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import teleteatros.excepciones.TeatroException;

/**
 * Clase gestora que actúa como controlador principal para el subsistema de teatros.
 * Administra la colección completa de teatros registrados en el sistema y delega
 * en ellos las operaciones relacionadas con sus espectáculos.
 */
public class GestorTeatros {
    
    /**
     * Mapa de teatros registrados en el sistema.
     * Indexado utilizando el identificador numérico (idt) del teatro como clave.
     */
    private Map<Integer, Teatro> teatros;
    
    /**
     * Contador interno utilizado para generar identificadores únicos (idt) 
     * de forma automática y secuencial cada vez que se registra un nuevo teatro.
     */
    private int contadorId;
    
    /**
     * Constructor de GestorTeatros.
     * Inicializa el mapa de teatros vacío y establece el contador de IDs a 0.
     */
    public GestorTeatros() {
        teatros = new HashMap<>();
        contadorId = 0;
    }

    /**
     * Crea una nueva instancia de Teatro y la agrega al sistema, garantizando 
     * que no haya duplicados (mismo nombre en la misma ciudad).
     * * @param nom      Nombre comercial del teatro.
     * @param ciudad   Ciudad donde se encuentra el teatro.
     * @param numFilas Número de filas de su patio de butacas.
     * @param numCols  Número de columnas de su patio de butacas.
     * @throws TeatroException Si ya existe un teatro registrado con ese mismo nombre en esa ciudad.
     */
    public void crearTeatro(String nom, String ciudad, int numFilas, int numCols)
            throws TeatroException {
        // 1. Validación: Recorremos los teatros existentes para evitar duplicados
        for (Teatro t : teatros.values()) {
            if (t.getNombre().equals(nom) && t.getCiudad().equals(ciudad)) {
                // Lanzamos la excepción con el mensaje exacto esperado por el sistema
                throw new TeatroException("Ya existe un teatro con nombre " + nom + " en " + ciudad);
            }
        }
        
        // 2. Creación: Instanciamos el teatro asignándole el ID numérico actual
        Teatro nuevoTeatro = new Teatro(contadorId, nom, ciudad, numFilas, numCols);
        
        // 3. Guardado: Lo metemos en el mapa indexado por su ID y preparamos el siguiente ID
        teatros.put(contadorId, nuevoTeatro);
        contadorId++; 
    }

    /**
     * Registra un nuevo espectáculo delegando la tarea al teatro correspondiente.
     * * @param idt      Identificador numérico del teatro donde se hará el espectáculo.
     * @param nomEsp   Nombre del espectáculo.
     * @param grupo    Grupo o artista.
     * @param fecha    Fecha del evento.
     * @param prTicket Precio base de la entrada.
     * @throws TeatroException Si no existe ningún teatro con ese ID o si el teatro 
     * rechaza la fecha (ej. por estar ocupada).
     */
    public void nuevoEspectaculo(int idt, String nomEsp, String grupo, LocalDate fecha, int prTicket) throws TeatroException {
        // 1. Buscamos el teatro por su clave en el mapa
        Teatro t = teatros.get(idt);
        
        // 2. Si no existe, rechazamos la petición
        if (t == null) {
            throw new TeatroException("No existe un teatro con el identificador #" + idt);
        }
        
        // 3. Si existe, le pasamos la responsabilidad al teatro para que gestione sus propias fechas
        t.nuevoEspectaculo(nomEsp, grupo, fecha, prTicket);
    }

    /**
     * Busca espectáculos en todo el sistema aplicando filtros opcionales de ciudad y fecha.
     * Devuelve los resultados ordenados de forma estructurada: primero por ciudad, 
     * luego por fecha y finalmente por nombre.
     * * @param ciudad Ciudad exacta para filtrar (si es null, busca en todas).
     * @param fIni   Límite inferior del rango de fechas (opcional).
     * @param fFin   Límite superior del rango de fechas (opcional).
     * @return Un array de cadenas de texto (String) con la información formateada de cada espectáculo encontrado.
     */
    public String[] buscarEspectaculos(String ciudad, LocalDate fIni, LocalDate fFin) {
        // 1. Extraer los teatros del mapa a una lista para poder ordenarlos
        List<Teatro> listaTeatros = new ArrayList<>(teatros.values());
        
        // 2. Ordenar los teatros alfabéticamente por ciudad para cumplir la jerarquía de salida
        listaTeatros.sort((t1, t2) -> t1.getCiudad().compareToIgnoreCase(t2.getCiudad()));

        // 3. Lista temporal para ir guardando los textos finales generados
        List<String> resultados = new ArrayList<>();

        // 4. Recorrer los teatros ya ordenados
        for (Teatro t : listaTeatros) {
            
            // Pedimos al teatro sus espectáculos válidos (nos llegan ya ordenados por fecha)
            Espectaculo[] espDelTeatro = t.buscarEspectaculos(ciudad, fIni, fFin);
            
            // 5. Construimos el bloque de texto final uniendo la información
            for (Espectaculo e : espDelTeatro) {
                String info = "Espectáculo \"" + e.getNombre() + "\"\n" +
                              "Teatro #" + t.getIdt() + " " + t.getNombre() + " (" + t.getCiudad() + ")\n" +
                              "Grupo: " + e.getGrupo() + "\n" +
                              "Fecha: " + e.getFecha() + "\n" +
                              "Precio ticket: " + e.getPrecioTicket() + "\n";
                
                resultados.add(info);
            }
        }

        // 6. Convertir la lista dinámica a un array estático para cumplir con la firma del método
        return resultados.toArray(new String[0]);
    }

    /**
     * Solicita la disponibilidad de asientos para un espectáculo concreto en un teatro específico.
     * * @param idt   Identificador del teatro a consultar.
     * @param fecha Fecha exacta del espectáculo.
     * @return Cadena de texto con los datos del espectáculo y el mapa visual de asientos.
     * @throws TeatroException Si el teatro no existe o no tiene función ese día.
     */
    public String disponibilidadEspectaculo(int idt, LocalDate fecha) throws TeatroException {
        // 1. Buscamos el teatro solicitado
        Teatro t = teatros.get(idt);
        
        // 2. Validamos su existencia en el sistema
        if (t == null) {
            throw new TeatroException("No existe un teatro con el identificador #" + idt);
        }
        
        // 3. Delegamos el trabajo de búsqueda y dibujo en la propia clase Teatro
        return t.disponibilidadEspectaculo(fecha);
    }

    /**
     * Delega la petición de reserva de tickets al teatro correspondiente.
     * @param idt Identificador del teatro.
     * @param fecha Fecha del espectáculo.
     * @param butacas Array con los números de las butacas a reservar.
     * @param cliente El cliente que solicita la reserva.
     * @throws TeatroException Si el teatro no existe o falla alguna validación posterior.
     */
    public void reservarTickets(int idt, LocalDate fecha, int[] butacas, teleteatros.usuarios.Cliente cliente) throws TeatroException {
        
        // 1. Buscamos el teatro en nuestro mapa usando su ID
        Teatro t = teatros.get(idt);
        
        // 2. Comprobamos si realmente existe
        if (t == null) {
            throw new TeatroException("No existe un teatro con el identificador #" + idt);
        }
        
        // 3. Si existe, le pasamos la patata caliente al teatro
        t.reservarTickets(fecha, butacas, cliente);
    }

    public void comprarTickets(int idt, LocalDate fecha, int[] butacas, teleteatros.usuarios.Cliente cliente) throws TeatroException {
        Teatro t = teatros.get(idt);
        if (t == null) {
            throw new TeatroException("No existe un teatro con el identificador #" + idt);
        }
        t.comprarTickets(fecha, butacas, cliente);
    }

    /**
     * Genera un informe en texto con todos los tickets de un cliente.
     */
    public String informacionTickets(teleteatros.usuarios.Cliente cliente) {
        StringBuilder sb = new StringBuilder();
        
        // 1. Extraer los espectáculos ÚNICOS para los que el cliente tiene entrada
        java.util.List<Teatro> listaTeatros = new java.util.ArrayList<>(teatros.values());
        java.util.List<teleteatros.teatros.Espectaculo> espectaculosDelCliente = new java.util.ArrayList<>();
        
        for (teleteatros.teatros.Ticket t : cliente.getMisTickets()) {
            if (!espectaculosDelCliente.contains(t.getEspectaculo())) {
                espectaculosDelCliente.add(t.getEspectaculo());
            }
        }
        
        // 2. Por cada espectáculo, buscamos su teatro y generamos el bloque de texto
        for (teleteatros.teatros.Espectaculo e : espectaculosDelCliente) {
            
            // Buscamos el teatro al que pertenece este espectáculo
            Teatro teatroDelEsp = null;
            for (Teatro t : listaTeatros) {
                if (t.getEspectaculos().contains(e)) {
                    teatroDelEsp = t;
                    break;
                }
            }
            
            // Imprimimos la cabecera (Igual que en buscarEspectaculos)
            sb.append("Espectáculo \"").append(e.getNombre()).append("\"\n");
            if (teatroDelEsp != null) {
                sb.append("Teatro #").append(teatroDelEsp.getIdt()).append(" ")
                  .append(teatroDelEsp.getNombre()).append(" (")
                  .append(teatroDelEsp.getCiudad()).append(")\n");
            }
            sb.append("Grupo: ").append(e.getGrupo()).append("\n");
            sb.append("Fecha: ").append(e.getFecha()).append("\n");
            sb.append("Precio ticket: ").append(e.getPrecioTicket()).append("\n");
            
            // 3. Reconstruir los números de butaca
            java.util.List<String> comprados = new java.util.ArrayList<>();
            java.util.List<String> reservados = new java.util.ArrayList<>();
            int numCols = e.getTickets()[0].length; // Rescatamos las columnas para la fórmula
            
            for (teleteatros.teatros.Ticket t : cliente.getMisTickets()) {
                if (t.getEspectaculo().equals(e)) {
                    // FÓRMULA INVERSA: (fila * columnas) + columna = ID original
                    int idButaca = (t.getButaca().getFila() * numCols) + t.getButaca().getCol();
                    
                    if (t.getEstado() == 2) {
                        comprados.add(String.valueOf(idButaca));
                    } else if (t.getEstado() == 1) {
                        reservados.add(String.valueOf(idButaca));
                    }
                }
            }
            
            // 4. Formatear con comas
            if (!comprados.isEmpty()) {
                sb.append("Tickets comprados: ").append(String.join(",", comprados)).append("\n");
            }
            if (!reservados.isEmpty()) {
                sb.append("Tickets reservados: ").append(String.join(",", reservados)).append("\n");
            }
        }
        
        return sb.toString();
    }
}