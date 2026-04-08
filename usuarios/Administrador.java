package teleteatros.usuarios;

public class Administrador extends Usuario {
	
	public Administrador(String l,String p, String n) {
		super(l, p, n);
	}

	/**
	 * Método que devuelve una descripción del administrador
	 * 
	 * @return descripción
	 */
	@Override
	public String toString() {
		// Compone una cadena con todos los campos y la retorna
		String cadena = super.toString();
		cadena += "\n Tipo: Administrador";
		return cadena;
	}
}
