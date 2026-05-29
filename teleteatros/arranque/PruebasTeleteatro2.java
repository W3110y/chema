
package teleteatros.arranque;

import java.time.LocalDate;
import java.util.List;

import teleteatros.excepciones.TeatroException;
import teleteatros.excepciones.UsuarioException;
import teleteatros.teatros.GestorTeatros;
import teleteatros.usuarios.Cliente;
import teleteatros.usuarios.GestorUsuarios;
import teleteatros.usuarios.Usuario;
import teleteatros.controladores.ControladorAdministrador;
import teleteatros.controladores.ControladorCliente;

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
		System.out.println("// CASOS DE USO ITERACIÓN 2");
		System.out.println("///////////////////////////\n");
		casosUsoIter2(gu);
	}

    /**
     * Método que simula el comportamiento de los usuarios interactuando con los controladores 
     * correspondientes a la Iteración 2.
     * @param ca Controlador del Administrador
     * @param cc Controlador del Cliente
     * @param gt Gestor de Teatros (necesario para ver disponibilidad si no pasa por controlador)
     */
    private static void casosUsoIter2(ControladorAdministrador ca, ControladorCliente cc, GestorTeatros gt) {
        
        System.out.println("/// RESERVA Y COMPRA DE TICKETS ///");
        
        try {
            System.out.println("<<inicio sesión homero>>");
            cc.identificarCliente("homero", "clave");
            
            // Compras previas de Homero para cuadrar su saldo final (Gasta 490 en total, le quedan 110)
            cc.comprarTickets(1, LocalDate.parse("2026-06-05"), new int[]{5, 3, 7}); // Eras (Valladolid)
            cc.comprarTickets(0, LocalDate.parse("2026-07-14"), new int[]{10});      // Rey Leon
            cc.comprarTickets(1, LocalDate.parse("2026-07-27"), new int[]{11, 6});   // Fantasma
            cc.comprarTickets(0, LocalDate.parse("2026-06-01"), new int[]{1, 2});    // CATS
            
            System.out.println("homero compra varios tickets");
            System.out.println("(ésta fallará...)");
            try {
                // Intenta comprar un ticket que ya no está disponible
                cc.comprarTickets(0, LocalDate.parse("2026-06-01"), new int[]{1});
            } catch (TeatroException e) {
                System.out.println(e.getMessage());
            }
            
            System.out.println("homero intenta reservar más tickets de los que puede");
            try {
                // Intenta reservar 5 tickets (el límite es 4)
                cc.reservarTickets(0, LocalDate.parse("2026-07-14"), new int[]{0, 1, 2, 3, 4});
            } catch (TeatroException e) {
                System.out.println(e.getMessage());
            }
            
            System.out.println("homero intenta comprar tickets sin saldo suficiente");
            try {
                // Eras en Madrid cuesta 120. Pide 3 = 360. Homero solo tiene 110 de saldo. 
                // Se genera la reserva automáticamente.
                cc.comprarTickets(3, LocalDate.parse("2026-06-15"), new int[]{1, 3, 5});
            } catch (TeatroException e) {
                System.out.println(e.getMessage());
            }
            
            System.out.println("<<cierre sesión homero>>");
            cc.cerrarSesion();
            
            // ---------------------------------------------------------
            
            System.out.println("<<inicio sesión juliet>>");
            cc.identificarCliente("juliet", "clave");
            
            System.out.println("juliet quiere ver a Taylor Swift en Madrid, consulta la disponibilidad:");
            System.out.println(gt.disponibilidadEspectaculo(3, LocalDate.parse("2026-06-15")));
            
            System.out.println("juliet compra todos los tickets que puede");
            cc.comprarTickets(3, LocalDate.parse("2026-06-15"), new int[]{0, 2, 4, 6});
            
            System.out.println("juliet intenta comprar más...");
            try {
                cc.comprarTickets(3, LocalDate.parse("2026-06-15"), new int[]{8});
            } catch (TeatroException e) {
                System.out.println(e.getMessage());
            }
            
            System.out.println("juliet reserva Judas Priest");
            cc.reservarTickets(2, LocalDate.parse("2026-06-08"), new int[]{0, 1});
            
            System.out.println("juliet intenta otra reserva...");
            try {
                cc.reservarTickets(0, LocalDate.parse("2026-06-01"), new int[]{4});
            } catch (TeatroException e) {
                System.out.println(e.getMessage());
            }
            
            System.out.println("juliet hace la compra de su reserva pendiente");
            cc.comprarReserva();
            
            System.out.println("si intenta comprar de nuevo la reserva...");
            try {
                cc.comprarReserva();
            } catch (TeatroException e) {
                System.out.println(e.getMessage());
            }
            
            System.out.println("juliet intenta una compra de un espectáculo inexistente");
            try {
                cc.comprarTickets(0, LocalDate.parse("2026-08-15"), new int[]{0});
            } catch (TeatroException e) {
                System.out.println(e.getMessage());
            }
            
            System.out.println("juliet hace una última reserva");
            cc.reservarTickets(0, LocalDate.parse("2026-06-01"), new int[]{0, 3});
            
            // ---------------------------------------------------------
            
            System.out.println("/// INFO TICKETS ///");
            System.out.println("juliet consulta sus tickets:");
            System.out.println(cc.informacionTickets());
            
            cc.cerrarSesion();
            
            System.out.println("<<inicio sesión homero>>");
            cc.identificarCliente("homero", "clave");
            System.out.println("homero consulta sus tickets:");
            System.out.println(cc.informacionTickets());
            
            // ---------------------------------------------------------
            
            System.out.println("/// ANULACIÓN RESERVA ///");
            System.out.println("homero anula su reserva");
            cc.anularReserva();
            
            System.out.println("esta vez fallará...");
            try {
                cc.anularReserva();
            } catch (TeatroException e) {
                System.out.println(e.getMessage());
            }
            cc.cerrarSesion();
            
            // ---------------------------------------------------------
            
            System.out.println("/// INFO VENTAS ESPECTÁCULOS ///");
            System.out.println("<<inicio sesión root>>");
            ca.identificarAdministrador("root", "root");
            
            System.out.println("root consulta las ventas de varios espectáculos");
            System.out.println(ca.infoVentasEspectaculo(0, LocalDate.parse("2026-06-01"))); // CATS
            System.out.println(ca.infoVentasEspectaculo(3, LocalDate.parse("2026-06-15"))); // Eras Madrid
            System.out.println(ca.infoVentasEspectaculo(2, LocalDate.parse("2026-06-08"))); // Epitaph
            
            System.out.println("esta vez fallará...");
            try {
                System.out.println(ca.infoVentasEspectaculo(1, LocalDate.parse("2026-07-08")));
            } catch (TeatroException e) {
                System.out.println(e.getMessage());
            }
            
            ca.cerrarSesion();
            
        } catch (UsuarioException e) {
            System.out.println("Error de usuario crítico en pruebas: " + e.getMessage());
        } catch (TeatroException e) {
            System.out.println("Error de teatro no capturado en pruebas: " + e.getMessage());
        }
    }
}