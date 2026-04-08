package teleteatros.usuarios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import teleteatros.excepciones.UsuarioException;

public class GestorUsuarios {

	/**
	 * Mapa de usuarios del sistema
	 */
	private Map<String, Usuario> mapaUsuarios;

	/**
	 * Constructor que inicializa el mapa de usuarios
	 */
	public GestorUsuarios() {
		// inicializo mapa de usuarios
		mapaUsuarios = new HashMap<>();
	}	
	
	/**
	 * Método que crea una nueva instancia del tipo de usuario adecuado y la agrega al mapa, indexada por login
	 * 
	 * @param login del usuario (ÚNICO)
	 * @param clave del usuario (en claro)
	 * @param nombre del usuario
	 * @param tipo de usuario a generar
	 * @throws ExcepcionUsuario si ya existe un usuario con ese login, 
	 * 			si el tipo de usuario no existe en el sistema,
	 * 			si hubo un error interno en la creación del usuario
	 */
	public void crearUsuario(String login, String clave, String nombre, String tipoUsuario) 
			throws UsuarioException {
		if (mapaUsuarios.containsKey(login)) // existe el login?
			throw new UsuarioException("Login \"" + login + "\" ya existe");
		else {
			Usuario u; // usuario a crear
			switch(tipoUsuario) {		
			case "Cliente":
				u = new Cliente(login, clave, nombre);
				break;
			case "Administrador":
				u = new Administrador(login, clave, nombre);
				break;
			default: // tipo de usuario incorrecto
				throw new UsuarioException("Tipo de usuario \""+tipoUsuario+"\" incorrecto");
			}
			// guardo usuario en el mapa
			mapaUsuarios.put(login, u);			
		}
	}	

	/**
	 * Método que comprueba las credenciales de un usuario
	 * 
	 * @param login del usuario
	 * @param clave del usuario (en claro)
	 * @return true si existe un usuario con ese login y su clave coincide con la proporcionada
	 * 			false en cualquier otro caso 
	 */
	public boolean validarUsuario(String login, String clave) {
		Usuario u = mapaUsuarios.get(login);
		if (u == null)
			return false;
		else 
			return clave.equals(u.getClave());
	}

	/**
	 * Método que devuelve un usuario a partir de su login
	 * 
	 * @param login del usuario
	 * @return el usuario del mapa con ese login o null si no existe
	 */
	public Usuario getUsuario(String login) {
		return mapaUsuarios.get(login);
	}
	
	/**
	 * Método que devuelve una lista de usuarios en el sistema por tipo
	 * 
	 * @param tipo de usuario de interés
	 * @return lista de usuarios del tipo de interés encontrados
	 * @throws ExcepcionUsuario si el tipo de usuario solicitado no existe 
	 */
	public List<Usuario> listarUsuariosTipo(String tipoUsuario) throws UsuarioException {
		// inicializo lista
		List<Usuario> lus = new ArrayList<>();		
		// preparo lista
		for (Usuario us : mapaUsuarios.values()) {
			// incluyo en la lista según el tipo de usuario	
			switch(tipoUsuario) {		
			case "Cliente":
				if (us instanceof Cliente)
					lus.add(us);
				break;
			case "Administrador":
				if (us instanceof Administrador)
					lus.add(us);
				break;
			default: // tipo de usuario incorrecto
				throw new UsuarioException("Tipo de usuario \""+tipoUsuario+"\" incorrecto");
			}				
		}			
		// y la devuelvo
		return lus;
	}
}