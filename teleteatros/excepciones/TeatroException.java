package teleteatros.excepciones;

public class TeatroException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public TeatroException(String causa) {
		super(causa);
	}
}
