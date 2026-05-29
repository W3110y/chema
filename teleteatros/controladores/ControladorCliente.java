package teleteatros.controladores;

import java.time.LocalDate;
import teleteatros.excepciones.TeatroException;
import teleteatros.excepciones.UsuarioException;
import teleteatros.teatros.GestorTeatros;
import teleteatros.usuarios.Cliente;
import teleteatros.usuarios.GestorUsuarios;

public class ControladorCliente {
    
    private GestorUsuarios gu;
    private GestorTeatros gt;
    private Cliente cliente; // Guarda la sesión del cliente activo
    
    public ControladorCliente(GestorUsuarios gu, GestorTeatros gt) {
        this.gu = gu;
        this.gt = gt;
    }
    
    public void identificarCliente(String login, String clave) throws UsuarioException {
        if (gu.validarUsuario(login, clave)) {
            try {
                cliente = (Cliente) gu.getUsuario(login);
            } catch (ClassCastException e) {
                throw new UsuarioException("Usuario " + login + " no es Cliente");
            }
        } else {
            throw new UsuarioException("Credenciales de usuario no válidas");
        }
    }

    public void cerrarSesion() {
        cliente = null;
    }

    // ====================================================================
    // ESQUELETOS DE LOS NUEVOS CASOS DE USO (Iteración 2)
    // ====================================================================

    /**
     * Inicia el proceso de reserva de tickets para un cliente.
     */
    public void reservarTickets(int idt, LocalDate fecha, int[] tickets) throws UsuarioException, TeatroException {
        // 1. Validar que hay un cliente logueado
        if (cliente == null) {
            throw new UsuarioException("Autenticación requerida");
        }
        
        // 2. Validar que no tenga ya reservas en curso (Usando el método que creamos antes)
        if (cliente.tieneReservaEnCurso()) {
            throw new TeatroException("El cliente ya tiene una reserva en curso");
        }
        
        // 3. Si todo está en orden, delegamos el trabajo duro al GestorTeatros,
        // pasándole también el objeto 'cliente' para que el teatro sepa a quién asignarle los tickets.
        gt.reservarTickets(idt, fecha, tickets, cliente);
    }

    public void comprarTickets(int idt, LocalDate fecha, int[] tickets) throws UsuarioException, TeatroException {
        if (cliente == null) throw new UsuarioException("Autenticación requerida");
        // TODO: Delegar compra en GestorTeatros
    }

    public void comprarReserva() throws UsuarioException, TeatroException {
        if (cliente == null) throw new UsuarioException("Autenticación requerida");
        // TODO: Delegar compra de reserva en GestorTeatros
    }

    public void anularReserva() throws UsuarioException, TeatroException {
        if (cliente == null) throw new UsuarioException("Autenticación requerida");
        // TODO: Delegar anulación en GestorTeatros
    }

    public String informacionTickets() throws UsuarioException {
        if (cliente == null) throw new UsuarioException("Autenticación requerida");
        return ""; // TODO: Recuperar y formatear la lista de tickets del cliente
    }
}