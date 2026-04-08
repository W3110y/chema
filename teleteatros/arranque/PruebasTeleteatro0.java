package teleteatros.arranque;

import java.util.List;

import teleteatros.excepciones.UsuarioException;
import teleteatros.usuarios.GestorUsuarios;
import teleteatros.usuarios.Usuario;


public class PruebasTeleteatro0 {

	/**
	 * Método main(). No se esperan parámetros.
	 * @param args parámetros por línea de comandos que no se tratan.
	 */
	public static void main(String[] args) {

		//**********************************
		//**INICIALIZACION GESTOR USUARIOS**
		//**********************************
		// Instancio el gestor de usuarios
		GestorUsuarios gu = new GestorUsuarios();

		System.out.println("///////////////////////////");
		System.out.println("// CASOS DE USO ITERACIÓN 0");
		System.out.println("///////////////////////////\n");
		casosUsoIter0(gu);
	}


	/**
	 * Método en el que se crean varios usuarios y se listan
	 * @param gu el gestor de usuarios
	 */
	private static void casosUsoIter0(GestorUsuarios gu) {
		System.out.println("/// CREAR USUARIOS ///\n");
		// Creo usuarios
		try {
			// dos administradores
			gu.crearUsuario("root", "root", "Root Lancaster", "Administrador");
			gu.crearUsuario("admin", "admin", "Laura Admin", "Administrador");
			// varios clientes
			gu.crearUsuario("homero", "clave", "Homero Aedo", "Cliente");
			gu.crearUsuario("juliet", "clave", "Juliet Capuleto", "Cliente");
			gu.crearUsuario("bernarda", "clave", "Bernarda Alba", "Cliente");
			System.out.println("(éste fallará...)");
			gu.crearUsuario("homero", "clave", "Homero Simpson", "Cliente");
		} catch (UsuarioException e) {
			System.out.println(e.getMessage());
		}

		System.out.println("\n\n/// LISTAR USUARIOS ///\n");

		System.out.println("El listado de clientes...");
		try {
			List<Usuario> socios = gu.listarUsuariosTipo("Cliente");
			System.out.println("\nHay "+socios.size()+" usuarios de tipo \"Cliente\"\n");
			for (Usuario soc :socios)
				System.out.println(soc.toString()+"\n");
		} catch (UsuarioException e) {
			System.out.println(e.getMessage());
		}

		System.out.println("El listado de administradores...");
		try {
			List<Usuario> mons = gu.listarUsuariosTipo("Administrador");
			System.out.println("\nHay "+mons.size()+" usuarios de tipo \"Administrador\"\n");
			for (Usuario mon : mons)
				System.out.println(mon.toString()+"\n");
		} catch (UsuarioException e) {
			System.out.println(e.getMessage());
		}	
	}
}
