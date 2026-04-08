package teleteatros.usuarios;

public class Butaca {
    private int fila;
    private int col;

    public Butaca(int fila, int col) {
        this.fila = fila;
        this.col = col;
    }

    public int getFila() {
        return fila;
    }

    public int getCol() {
        return col;
    }
}