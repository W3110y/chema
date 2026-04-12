package teleteatros.arranque;

import java.time.LocalDate;
import java.util.List;

import teleteatros.excepciones.TeatroException;
import teleteatros.excepciones.UsuarioException;
import teleteatros.teatros.GestorTeatros;
import teleteatros.usuarios.Cliente;
import teleteatros.usuarios.GestorUsuarios;
import teleteatros.usuarios.Usuario;

public class PruebasTeleteatro1 {

    /**
     * Método main(). No se esperan parámetros.
     * @param args parámetros por línea de comandos que no se tratan.
     */
    public static void main(String[] args) {

        // **********************************************
        // **INICIALIZACION GESTORES USUARIOS Y TEATROS**
        // **********************************************
        GestorUsuarios gu = new GestorUsuarios(); // Instancio el gestor de usuarios
        GestorTeatros gt = new GestorTeatros();   // Instancio el gestor de teatros

        System.out.println("///////////////////////////");
        System.out.println("// CASOS DE USO ITERACIÓN 0");
        System.out.println("///////////////////////////\n");
        casosUsoIter0(gu);

        System.out.println("\n\n///////////////////////////");
        System.out.println("// CASOS DE USO ITERACIÓN 1");
        System.out.println("///////////////////////////\n");
        casosUsoIter1(gu, gt);
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
            System.out.println("\nHay " + socios.size() + " usuarios de tipo \"Cliente\"\n");
            for (Usuario soc : socios)
                System.out.println(soc.toString() + "\n");
        } catch (UsuarioException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("El listado de administradores...");
        try {
            List<Usuario> mons = gu.listarUsuariosTipo("Administrador");
            System.out.println("\nHay " + mons.size() + " usuarios de tipo \"Administrador\"\n");
            for (Usuario mon : mons)
                System.out.println(mon.toString() + "\n");
        } catch (UsuarioException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Método en el que se recarga el saldo de clientes,
     * se crean varios teatros y varios espectáculos,
     * se buscan espectáculos por varios criterios y se comprueba la disponibilidad
     * @param gu gestor de usuarios
     * @param gt gestor de teatros
     */
    private static void casosUsoIter1(GestorUsuarios gu, GestorTeatros gt) {

        System.out.println("/// RECARGAR SALDO ///");
        System.out.println("\naumentamos el saldo de homero\n");

        Cliente cli = (Cliente) gu.getUsuario("homero");
        cli.incrementarSaldo(600);
        System.out.println(cli.toString()); // comprobamos saldo

        System.out.println("\ny ahora el de juliet\n");
        cli = (Cliente) gu.getUsuario("juliet");
        cli.incrementarSaldo(1500);
        System.out.println(cli.toString()); // comprobamos saldo

        System.out.println("\n\n/// CREAR TEATROS ///\n");
        System.out.println("creación de varios teatros");
        try {
            gt.crearTeatro("Calderón", "Valladolid", 4, 4); // se asignará id 0
            gt.crearTeatro("Cervantes", "Valladolid", 2, 6); // se asignará id 1
            gt.crearTeatro("Real", "Madrid", 3, 4);          // se asignará id 2
            gt.crearTeatro("Calderón", "Madrid", 1, 8);      // se asignará id 3
            
            System.out.println("(éste fallará...)");
            gt.crearTeatro("Calderón", "Madrid", 5, 8);
        } catch (TeatroException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\n\n/// REGISTRAR ESPECTÁCULOS ///");
        System.out.println("\nregistro de varios espectáculos");
        try {
            // el entero inicial es el id del teatro del paso anterior
            gt.nuevoEspectaculo(0, "CATS", "Pussycats", LocalDate.parse("2026-06-01"), 20);
            gt.nuevoEspectaculo(1, "Eras", "Taylor Swift", LocalDate.parse("2026-06-05"), 100);
            gt.nuevoEspectaculo(2, "Epitaph", "Judas Priest", LocalDate.parse("2026-06-08"), 50);
            gt.nuevoEspectaculo(3, "Eras", "Taylor Swift", LocalDate.parse("2026-06-15"), 120);
            gt.nuevoEspectaculo(1, "Most Wanted", "Bad Bunny", LocalDate.parse("2026-06-25"), 80);
            gt.nuevoEspectaculo(0, "El Rey León", "Disney", LocalDate.parse("2026-07-14"), 30);
            gt.nuevoEspectaculo(2, "El Rey León", "Disney", LocalDate.parse("2026-07-21"), 40);
            gt.nuevoEspectaculo(3, "Most Wanted", "Bad Bunny", LocalDate.parse("2026-07-25"), 180);
            gt.nuevoEspectaculo(1, "El Fantasma de la Ópera", "Lloyd Webber", LocalDate.parse("2026-07-27"), 60);
            gt.nuevoEspectaculo(0, "Cine de autor", "SEMINCI", LocalDate.parse("2026-11-02"), 15);
            
            System.out.println("(éste fallará...)");
            gt.nuevoEspectaculo(0, "Epitaph", "Judas Priest", LocalDate.parse("2026-07-27"), 50);
        } catch (TeatroException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\n/// BUSCAR ESPECTÁCULOS ///\n");
        System.out.println("(ordenación por ciudad y luego por fecha)");

        System.out.println("\n - búsqueda de espectáculos en Valladolid a partir del 10 de junio:");
        for (String s : gt.buscarEspectaculos("Valladolid", LocalDate.parse("2026-06-10"), null))
            System.out.println("\n" + s);

        System.out.println("\n - búsqueda de espectáculos en julio:");
        for (String s : gt.buscarEspectaculos(null, LocalDate.parse("2026-07-01"), LocalDate.parse("2026-07-30")))
            System.out.println("\n" + s);

        System.out.println("\n\n/// VER DISPONIBILIDAD ESPECTÁCULOS ///\n");
        System.out.println("consulta la disponibilidad de varios espectáculos\n");
        try {
            System.out.println(gt.disponibilidadEspectaculo(1, LocalDate.parse("2026-06-05")));
            System.out.println("\n(éste fallará...)");
            System.out.println(gt.disponibilidadEspectaculo(1, LocalDate.parse("2026-06-08")));
        } catch (TeatroException e) {
            System.out.println(e.getMessage());
        }

        try {
            System.out.println("\nmás consultas...\n");
            System.out.println(gt.disponibilidadEspectaculo(2, LocalDate.parse("2026-06-08")));
            System.out.println("\n(ésta fallará...)");
            System.out.println(gt.disponibilidadEspectaculo(0, LocalDate.parse("2026-06-15")));
        } catch (TeatroException e) {
            System.out.println(e.getMessage());
        }
    }
}