package servidor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
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
public class TresEnRayaTest {
    
    private TresEnRaya instance;
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
        instance = new TresEnRaya("usuario", "contrincante", "X", true);
    }
    
    @AfterEach
    public void tearDown() {
        instance.dispose(); // Cerrar la ventana después de cada prueba
    }
    
    /**
     * Test of run method, of class TresEnRaya.
     */
    @Test
    public void testRun() {
        System.out.println("run");
        assertFalse(instance.partidaPerdida);
        assertFalse(instance.partidaGanada);
    }

    /**
     * Test of actionPerformed method, of class TresEnRaya.
     */
    @Test
    public void testActionPerformed() {
        System.out.println("actionPerformed");

        JButton button = new JButton();
        button.putClientProperty("fila", 0);
        button.putClientProperty("col", 0);
        ActionEvent e = new ActionEvent(button, ActionEvent.ACTION_PERFORMED, null);
        
        instance.actionPerformed(e);
        
        assertEquals("X", button.getText());
        assertFalse(button.isEnabled());
    }

    /**
     * Test of initComponents method, of class TresEnRaya.
     */
    @Test
    public void testInitComponents() {
        System.out.println("initComponents");
        
        instance.initComponents();
        
        // Verificar que los componentes se han inicializado correctamente
        assertNotNull(instance.buttons);
        assertEquals(3, instance.buttons.length);
        for (int i = 0; i < 3; i++) {
            assertEquals(3, instance.buttons[i].length);
            for (int j = 0; j < 3; j++) {
                assertNotNull(instance.buttons[i][j]);
                assertEquals("", instance.buttons[i][j].getText());
            }
        }
    }

    /**
     * Test of setAccion method, of class TresEnRaya.
     */
    @Test
    public void testSetAccion() {
        System.out.println("setAccion");
        int fila = 0;
        int col = 0;
        String ficha = "X";
        
        instance.setAccion(fila, col, ficha);
        
        assertEquals(ficha, instance.getAccion()[fila][col]);
    }

    /**
     * Test of getAccion method, of class TresEnRaya.
     */
    @Test
    public void testGetAccion() {
        System.out.println("getAccion");
        instance.setAccion(0, 0, "X");
        String[][] result = instance.getAccion();
        assertEquals("X", result[0][0]);
    }

    /**
     * Test of derrota method, of class TresEnRaya.
     */
    @Test
    public void testDerrota() {
        System.out.println("derrota");
        instance.derrota();
        assertTrue(instance.partidaPerdida);
    }

    /**
     * Test of getUsuario method, of class TresEnRaya.
     */
    @Test
    public void testGetUsuario() {
        System.out.println("getUsuario");
        String expResult = "usuario";
        String result = instance.getUsuario();
        assertEquals(expResult, result);
    }

    /**
     * Test of setUsuario method, of class TresEnRaya.
     */
    @Test
    public void testSetUsuario() {
        System.out.println("setUsuario");
        String usuario = "nuevoUsuario";
        instance.setUsuario(usuario);
        assertEquals(usuario, instance.getUsuario());
    }

    /**
     * Test of getContrincante method, of class TresEnRaya.
     */
    @Test
    public void testGetContrincante() {
        System.out.println("getContrincante");
        String expResult = "contrincante";
        String result = instance.getContrincante();
        assertEquals(expResult, result);
    }

    /**
     * Test of setContrincante method, of class TresEnRaya.
     */
    @Test
    public void testSetContrincante() {
        System.out.println("setContrincante");
        String contrincante = "nuevoContrincante";
        instance.setContrincante(contrincante);
        assertEquals(contrincante, instance.getContrincante());
    }

    /**
     * Test of GanadaPorRendicion method, of class TresEnRaya.
     */
    @Test
    public void testGanadaPorRendicion() {
        System.out.println("GanadaPorRendicion");
        instance.GanadaPorRendicion();
        assertTrue(instance.partidaGanada);
    }
}