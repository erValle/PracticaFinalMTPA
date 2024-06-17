/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package cliente;

import java.awt.event.ActionEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import servidor.Mensaje;

/**
 *
 * @author pablo
 */
public class RegistroLoginTest {
    
    public RegistroLoginTest() {
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
     * Test of main method, of class RegistroLogin.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        RegistroLogin.main(args);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of initComponents method, of class RegistroLogin.
     */
    @Test
    public void testInitComponents() {
        System.out.println("initComponents");
        RegistroLogin instance = new RegistroLogin();
        instance.initComponents();
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of initComunication method, of class RegistroLogin.
     */
    @Test
    public void testInitComunication() {
        System.out.println("initComunication");
        RegistroLogin instance = new RegistroLogin();
        instance.initComunication();
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of run method, of class RegistroLogin.
     */
    @Test
    public void testRun() {
        System.out.println("run");
        RegistroLogin instance = new RegistroLogin();
        instance.run();
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of actionPerformed method, of class RegistroLogin.
     */
    @Test
    public void testActionPerformed() {
        System.out.println("actionPerformed");
        ActionEvent e = null;
        RegistroLogin instance = new RegistroLogin();
        instance.actionPerformed(e);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of CamposVacios method, of class RegistroLogin.
     */
    @Test
    public void testCamposVacios() {
        System.out.println("CamposVacios");
        String user = "";
        String passw = "";
        RegistroLogin instance = new RegistroLogin();
        boolean result = instance.CamposVacios(user, passw);
        assertTrue(result);

        user = "usuario";
        passw = "";
        result = instance.CamposVacios(user, passw);
        assertTrue(result);

        user = "";
        passw = "contraseña";
        result = instance.CamposVacios(user, passw);
        assertTrue(result);

        user = "usuario";
        passw = "contraseña";
        result = instance.CamposVacios(user, passw);
        assertFalse(result);
    }

    /**
     * Test of toString method, of class RegistroLogin.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        String user = "";
        String passw = "";
        String accion = "";
        RegistroLogin instance = new RegistroLogin();
        String expResult = "";
        String result = instance.toString(user, passw, accion);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of LlamarAccionLogin method, of class RegistroLogin.
     */
    @Test
    public void testLlamarAccionLogin() {
        System.out.println("LlamarAccionLogin");
        Mensaje msg = null;
        RegistroLogin instance = new RegistroLogin();
        instance.LlamarAccionLogin(msg);
        // TODO review the generated test code and remove the default call to fail.
        
    }
    
}
