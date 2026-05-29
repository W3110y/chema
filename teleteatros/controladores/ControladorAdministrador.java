package teleteatros.controladores;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import teleteatros.excepciones.TeatroException;
import teleteatros.excepciones.UsuarioException;
import teleteatros.teatros.GestorTeatros;
import teleteatros.usuarios.Administrador;
import teleteatros.usuarios.GestorUsuarios;
import teleteatros.usuarios.Usuario;

public class ControladorAdministrador {
		
	private GestorUsuarios gu; /** el gestor de usuarios */
	private GestorTeatros gt; /** el gestor de teatros */
	private Administrador admin; /** el administrador de la sesión */
	
	
	/**
	 * Constructor con parámetros
	 * @param gu es el gestor de usuarios 
	 */	
	public ControladorAdministrador(GestorUsuarios gu) {
		this.gu = gu;
	}
	
	/**
	 * Constructor con parámetros
	 * @param gu es el gestor de usuarios 
	 * @param gt es el gestor de teatros 
	 */	
	public ControladorAdministrador(GestorUsuarios gu, GestorTeatros gt) {
		this.gu = gu;
		this.gt = gt;
	}
	
	/**
	 * Metodo para identificar a un administrador en el sistema, guardando una referencia en el atributo admin
	 * @param login del administrador
	 * @param clave del administrador
	 * @throws ExcepcionUsuario si las credenciales de usuario no son válidas
	 * 			o si las credenciales no son de un administrador
	 */
	public void identificarAdministrador(String login, String clave) throws UsuarioException {
		if (gu.validarUsuario(login, clave)) {
			// admin válido
			try {
				admin = (Administrador) gu.getUsuario(login);
			} catch (ClassCastException e) {
				throw new UsuarioException("Usuario "+login+" no es Administrador");
			}
		}
		else // admin no válido
			throw new UsuarioException("Credenciales de usuario no válidas");
	}

	/**
	 * Metodo para crear un usuario
	 * @param login del usuario a crear
	 * @param clave del usuario a crear
	 * @param nombre del usuario a crear
	 * @param tipo de usuario a crear
	 * @throws ExcepcionUsuario si el administrador no se ha identificado en el sistema,
	 * 			si ya existe un usuario con el login indicado  		
	 * 			o si el tipo de usuario solicitado no existe
	 */
	public void crearUsuario(String login, String clave, String nombre, String tipoUsuario) throws UsuarioException {
		if (admin== null)
			throw new UsuarioException("Autenticación requerida");
		gu.crearUsuario(login, clave, nombre, tipoUsuario);
	}
	
	/**
	 * Metodo para recuperar una lista de descripciones de usuario de cierto tipo
	 * @param tipo de usuario de interés
	 * @return lista de descripciones de usuarios del tipo de interés
	 * @throws ExcepcionUsuario si el administrador no se ha identificado en el sistema,  		
	 * 			o si el tipo de usuario solicitado no existe
	 */
	public List<String> listarUsuariosTipo(String tipo) throws UsuarioException {
		if (admin== null)
			throw new UsuarioException("Autenticación requerida");
		// obtengo lista de descripciones de ususarios y la devuelvo
		List<String> descUsuarios = new ArrayList<>();
		for (Usuario us : gu.listarUsuariosTipo(tipo)) 
			descUsuarios.add(us.toString());
		return descUsuarios;
	}
	
	/**
	 * Método para crear un teatro
	 * @param nom nombre del teatro
	 * @param ciu ciudad
	 * @param nf número de filas
	 * @param nc número de columnas
	 * @throws ExcepcionUsuario si el administrador no se ha identificado en el sistema
	 * @throws TeatroException si ya existe un teatro con ese nombre y en esa ciudad
	 */
	public void crearTeatro(String nom, String ciu, int nf, int nc) throws UsuarioException, TeatroException {
		if (admin== null)
			throw new UsuarioException("Autenticación requerida");
		gt.crearTeatro(nom, ciu, nf, nc);
	}

	/**
	 * Método para registrar un nuevo espectáculo
	 * @param idt identificador del teatro
	 * @param nomEs nombre del espectáculo
	 * @param grupo grupo del espectáculo
	 * @param fe fecha de celebración
	 * @param pr precio del ticket
	 * @throws ExcepcionUsuario si el administrador no se ha identificado en el sistema
	 * @throws TeatroException si no existe el teatro o si ya hay otro espectáculo en esa fecha en el teatro eligido
	 */
	public void nuevoEspectaculo(int idt, String nomEs, String grupo, LocalDate fe, int prTicket) throws UsuarioException, TeatroException {
		if (admin== null)
			throw new UsuarioException("Autenticación requerida");
		gt.nuevoEspectaculo(idt, nomEs, grupo, fe, prTicket);
	}
	

	/**
	 * Método para obtener la información de ventas de un espectáculo
	 * @param idt identificador del teatro
	 * @param fe fecha de celebración
	 * @throws ExcepcionUsuario si el administrador no se ha identificado en el sistema
	 * @throws TeatroException si no existe el teatro o si no hay un espectáculo programado en esa fecha
	 */
	public String infoVentasEspectaculo(int idt, LocalDate fe) throws UsuarioException, TeatroException {
		if (admin== null)
			throw new UsuarioException("Autenticación requerida");
		return gt.infoVentasEspectaculo(idt, fe);
	}
	
	/**
	 * Metodo para cerrar sesión que elimina la referencia a admin
	 */
	public void cerrarSesion() {
		admin = null;
	}
}
