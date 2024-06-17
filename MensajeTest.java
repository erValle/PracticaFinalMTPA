/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package servidor;

import java.nio.charset.Charset;
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
public class MensajeTest {
    
    public MensajeTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of CrearMensajeConParametros method, of class Mensaje.
     */
    @Test
    public void testCrearMensajeConParametros() {
        System.out.println("CrearMensajeConParametros");
        String _accion = "accionTest";
        String _campo1 = "campo1Test";
        String _campo2 = "campo2Test";
        Mensaje result = Mensaje.CrearMensajeConParametros(_accion, _campo1, _campo2);
        assertNotNull(result);
        assertEquals(_accion, result.getAccion());
        assertEquals(_campo1, result.getCampo1());
        assertEquals(_campo2, result.getCampo2());
    }

    /**
     * Test of CrearMensaje method, of class Mensaje.
     */
    @Test
    public void testCrearMensaje() {
        System.out.println("CrearMensaje");
        byte[] buffer = "accionTest@campo1Test@campo2Test".getBytes(Charset.forName("UTF-8"));
        Mensaje result = Mensaje.CrearMensaje(buffer);
        assertNotNull(result);
        assertEquals("accionTest", result.getAccion());
        assertEquals("campo1Test", result.getCampo1());
        assertEquals("campo2Test", result.getCampo2());
    }

    /**
     * Test of toString method, of class Mensaje.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Mensaje instance = Mensaje.CrearMensajeConParametros("accionTest", "campo1Test", "campo2Test");
        String expResult = "accionTest@campo1Test@campo2Test";
        String result = instance.toString();
        assertEquals(expResult, result);
    }

    /**
     * Test of setAccion method, of class Mensaje.
     */
    @Test
    public void testSetAccion() {
        System.out.println("setAccion");
        String accion = "";
        Mensaje instance = new Mensaje();
        instance.setAccion(accion);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of getAccion method, of class Mensaje.
     */
    @Test
    public void testGetAccion() {
        System.out.println("getAccion");
        Mensaje instance = new Mensaje();
        String expResult = "";
        String result = instance.getAccion();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of getCampo1 method, of class Mensaje.
     */
    @Test
    public void testGetcampo1() {
        System.out.println("getCampo1");
        Mensaje instance = new Mensaje();
        String expResult = "";
        String result = instance.getCampo1();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of setCampo1 method, of class Mensaje.
     */
    @Test
    public void testSetcampo1() {
        System.out.println("setCampo1");
        String campo1 = "";
        Mensaje instance = new Mensaje();
        instance.setCampo1(campo1);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of getCampo2 method, of class Mensaje.
     */
    @Test
    public void testGetcampo2() {
        System.out.println("getCampo2");
        Mensaje instance = new Mensaje();
        String expResult = "";
        String result = instance.getCampo2();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of setCampo2 method, of class Mensaje.
     */
    @Test
    public void testSetcampo2() {
        System.out.println("setCampo2");
        String campo2 = "";
        Mensaje instance = new Mensaje();
        instance.setCampo2(campo2);
        // TODO review the generated test code and remove the default call to fail.
    }
    
}
