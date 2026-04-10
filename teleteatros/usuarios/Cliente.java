package teleteatros.usuarios;

public class Cliente extends Usuario{
	
    private int saldo;
    public Cliente(String l, String p, String n) {
        super(l, p, n);
        saldo = 0;
    }
    /**
    * Método que devuelve una descripción del cliente
    * 
    * @return descripción
    */
    @Override

    public String toString() {
        // Compone una cadena con todos los campos y la retorna
        String cadena = super.toString();
        cadena += "\n Tipo: Cliente";
        cadena += "\n Saldo: " + saldo;
        return cadena;
    }

    public void incrementarSaldo(int i) {
        saldo += i;
    }

}