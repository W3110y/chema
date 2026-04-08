package teleteatros.excepciones;

public class UsuarioException extends Exception {

	/** Las clases que derivan de Exception deben tener un atributo como éste */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor para crear una excepción relativa a usuarios
	 * @param causa de la excepción
	 */
	public UsuarioException(String causa) {
		super(causa);
	}
}
