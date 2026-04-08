package teleteatros.usuarios;

public class Butaca {
    private int fila;
    private int columna;

    public Butaca(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }
}