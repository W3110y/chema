package teleteatros.teatros;

/**
 * Clase que representa una butaca física dentro del patio de butacas de un teatro.
 * Define la posición exacta (coordenadas) de un asiento.
 */
public class Butaca {
    
    /**
     * Número de fila en la que se ubica la butaca.
     */
    private int fila;
    
    /**
     * Número de columna en la que se ubica la butaca.
     */
    private int col;

    /**
     * Constructor de la clase Butaca.
     * Inicializa la butaca con sus coordenadas físicas dentro del recinto.
     * * @param fila El número de fila asignado a esta butaca.
     * @param col  El número de columna asignado a esta butaca.
     */
    public Butaca(int fila, int col) {
        this.fila = fila;
        this.col = col;
    }

    /**
     * Devuelve el número de fila de la butaca.
     * * @return El entero que representa la fila.
     */
    public int getFila() {
        return fila;
    }

    /**
     * Devuelve el número de columna de la butaca.
     * * @return El entero que representa la columna.
     */
    public int getCol() {
        return col;
    }
}