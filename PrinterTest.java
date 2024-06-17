/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package servidor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author pablo
 */
public class PrinterTest {
    
    private Printer instance;
    
    public PrinterTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() throws IOException {
        instance = new Printer();
        // Limpia los archivos antes de cada prueba
        new FileWriter("db_Credenciales.txt", false).close();
        new FileWriter("db_UsuariosPuntos.txt", false).close();
        new FileWriter("db_Historial.txt", false).close();
    }
    
    @AfterEach
    public void tearDown() throws IOException {
        // Limpia los archivos después de cada prueba
        new FileWriter("db_Credenciales.txt", false).close();
        new FileWriter("db_UsuariosPuntos.txt", false).close();
        new FileWriter("db_Historial.txt", false).close();
    }

    /**
     * Test of printRegistro method, of class Printer.
     */
    @Test
    public void testPrintRegistro() throws Exception {
        System.out.println("printRegistro");
        String User = "testUser";
        String Password = "testPassword";
        instance.printRegistro(User, Password);

        BufferedReader br = new BufferedReader(new FileReader("db_Credenciales.txt"));
        String line = br.readLine();
        br.close();

        assertEquals(User + ";" + Password, line);
    }

    /**
     * Test of printUsuariosPuntos method, of class Printer.
     */
    @Test
    public void testPrintUsuariosPuntos() throws Exception {
        System.out.println("printUsuariosPuntos");
        String Puntuacion = "testPuntuacion";
        instance.printUsuariosPuntos(Puntuacion);

        BufferedReader br = new BufferedReader(new FileReader("db_UsuariosPuntos.txt"));
        String line = br.readLine();
        br.close();

        assertEquals(Puntuacion, line);
    }

    /**
     * Test of printHistorial method, of class Printer.
     */
    @Test
    public void testPrintHistorial() throws Exception {
        System.out.println("printHistorial");
        String Ganador = "testGanador";
        String Perdedor = "testPerdedor";
        instance.printHistorial(Ganador, Perdedor);

        BufferedReader br = new BufferedReader(new FileReader("db_Historial.txt"));
        String line = br.readLine();
        br.close();
        
        assertEquals( Ganador + " vs " + Perdedor + "; Ganador: " + Ganador ,line);
    }
    
}
