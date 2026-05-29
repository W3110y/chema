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

    /**
     * Procesa la reserva de tickets, validando disponibilidad y reglas de negocio.
     * @param fecha Fecha del espectáculo.
     * @param butacasReservar Array con los números de butaca solicitados.
     * @param cliente El cliente que realiza la operación.
     * @throws TeatroException Si se incumple alguna regla o los tickets no están libres.
     */
    public void reservarTickets(LocalDate fecha, int[] butacasReservar, teleteatros.usuarios.Cliente cliente) throws TeatroException {
        Espectaculo espEncontrado = null;
        
        // 1. Buscamos el espectáculo programado para esa fecha
        for (Espectaculo e : this.espectaculos) {
            if (e.getFecha().equals(fecha)) {
                espEncontrado = e;
                break;
            }
        }
        
        // Si no existe el espectáculo, la reserva falla
        if (espEncontrado == null) {
            throw new TeatroException("No hay espectáculos programados en el teatro #" + this.idt + " el " + fecha);
        }

        // 2. Validar que no supera el máximo de 4 tickets por espectáculo (incluyendo previas)
        int ticketsYaComprados = cliente.contarTicketsDeEspectaculo(espEncontrado);
        if (ticketsYaComprados + butacasReservar.length > 4) {
            throw new TeatroException("No puede hacerse la reserva: el cliente superaría el máximo de tickets por espectáculo");
        }

        // Extraemos la matriz de tickets del espectáculo
        teleteatros.teatros.Ticket[][] matrizTickets = espEncontrado.getTickets();

        // 3. Validar disponibilidad de TODAS las butacas pedidas ANTES de modificar nada
        for (int idButaca : butacasReservar) {
            // Conversión matemática de array unidimensional a matriz bidimensional
            int f = idButaca / this.numCols;
            int c = idButaca % this.numCols;
            
            // Verificamos por seguridad que el ID no se salga de los límites físicos del teatro
            if (f < 0 || f >= this.numFilas || c < 0 || c >= this.numCols) {
                 throw new TeatroException("El ticket #" + idButaca + " no existe en este teatro");
            }
            
            // Si el estado no es 0 (Libre), abortamos la operación
            if (matrizTickets[f][c].getEstado() != 0) { 
                throw new TeatroException("El ticket #" + idButaca + " no está disponible");
            }
        }

        // 4. Si llegamos aquí, la reserva es 100% legal. Aplicamos los cambios de estado.
        for (int idButaca : butacasReservar) {
            int f = idButaca / this.numCols;
            int c = idButaca % this.numCols;
            
            teleteatros.teatros.Ticket t = matrizTickets[f][c];
            t.setEstado(1);             // Cambia a estado Reservado (1)
            t.setPropietario(cliente);  // El ticket guarda la referencia de su dueño
            cliente.addTicket(t);       // El cliente guarda el ticket en su historial
        }
    }

    public void comprarTickets(LocalDate fecha, int[] butacasComprar, teleteatros.usuarios.Cliente cliente) throws TeatroException {
        
        // 1. ¡REUTILIZACIÓN! Hacemos la reserva primero. 
        // Si hay algún problema de disponibilidad o límite, lanzará la excepción desde ahí y se cortará la ejecución.
        // Si pasa de esta línea, los tickets YA ESTÁN vinculados al cliente y en estado Reservado (1).
        this.reservarTickets(fecha, butacasComprar, cliente);

        // 2. Recuperamos el espectáculo para saber el precio
        Espectaculo espEncontrado = null;
        for (Espectaculo e : this.espectaculos) {
            if (e.getFecha().equals(fecha)) {
                espEncontrado = e;
                break;
            }
        }

        // 3. Calculamos el coste total
        int precioTotal = espEncontrado.getPrecioBase() * butacasComprar.length;

        // 4. Comprobamos si tiene dinero suficiente
        if (cliente.getSaldo() < precioTotal) {
            // El truco está aquí: lanzamos la excepción con el texto exacto que pide el PDF.
            // Como ya ejecutamos "reservarTickets", los asientos quedan bloqueados para él.
            throw new TeatroException("El cliente no tiene saldo suficiente para la compra => LOS BILLETES HAN SIDO RESERVADOS");
        }

        // 5. Si llegamos aquí, ¡TIENE SALDO! Consolidamos la compra.
        cliente.decrementarSaldo(precioTotal);
        
        teleteatros.teatros.Ticket[][] matrizTickets = espEncontrado.getTickets();
        
        for (int idButaca : butacasComprar) {
            int f = idButaca / this.numCols;
            int c = idButaca % this.numCols;
            
            // Cambiamos el estado a Comprado (suponiendo que es el número 2)
            matrizTickets[f][c].setEstado(2);
        }
    }

    public java.util.ArrayList<Espectaculo> getEspectaculos() {
        return espectaculos;
    }
}