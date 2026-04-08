public class GestorTeatros {
    
    // mapa de teatros del sistema, indexados por su nombre (único)
    private Map<String, Teatro> mapaTeatros;
    
    public GestorTeatros() {
        mapaTeatros = new HashMap<String, Teatro>();
    }
    
    /**
     * Método que crea un nuevo teatro en el sistema
     * 
     * @param nombre del teatro (ÚNICO)
     * @param direccion del teatro
     * @throws ExcepcionTeatro si ya existe un teatro con ese nombre, 
     * 			si hubo un error interno en la creación del teatro
     */
    public void crearTeatro(String nombre, String direccion) 
            throws TeatroException {
        if (mapaTeatros.containsKey(nombre)) // existe el nombre?
            throw new TeatroException("Teatro \"" + nombre + "\" ya existe");
        else {
            Teatro t = new Teatro(nombre, direccion);
            mapaTeatros.put(nombre, t);			
        }
    }	

}
