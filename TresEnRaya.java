package servidor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.Semaphore;

/**
 * La clase TresEnRaya representa el juego del Tres en Raya.
 * 
 * @author Pablo
 * @author Daniel
 */
public class TresEnRaya extends JFrame implements Runnable, ActionListener {

    private final Semaphore semaphore = new Semaphore(0);
    JButton[][] buttons = new JButton[3][3];
    private String ficha;

    private String usuario;
    private String contrincante;

    private String tablero[][] = new String[3][3];

    boolean partidaPerdida = false;
    boolean partidaGanada = false;

    public String AccionDeContrincante[][] = new String[3][3];

    private boolean miTurno;

    /**
     * Constructor de la clase TresEnRaya.
     *
     * @param usuario El nombre del usuario.
     * @param contrincante El nombre del contrincante.
     * @param ficha La ficha del usuario (X o O).
     * @param miTurno Indica si es el turno del usuario.
     */
    public TresEnRaya(String usuario, String contrincante, String ficha, boolean miTurno) {
        this.usuario = usuario;
        this.contrincante = contrincante;
        this.ficha = ficha;
        this.miTurno = miTurno; 

        initComponents(); 

        Thread hiloPartida = new Thread(this);
        hiloPartida.start();
    }

    /**
     * Método principal de ejecución del juego.
     */
    @Override
    public void run() {
        resetBoard();

        while (!partidaPerdida && !partidaGanada) {

            if (!miTurno) {
                
                AccionDeContrincante = Servidor.AccionDeContrincante(usuario, contrincante);

                if (hayJugada(AccionDeContrincante)) {
                    jugadaContrincante(AccionDeContrincante);
                    miTurno = true;
                }
            } else {
                try {
                    semaphore.acquire();
                } catch (InterruptedException ex) {
                    Logger.getLogger(TresEnRaya.class.getName()).log(Level.SEVERE, null, ex);
                }
                miTurno = false;
            }

            if (checkForWin()) {
                try {
                    Servidor.Victoria(usuario, contrincante);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
                JOptionPane.showMessageDialog(this, "Enhorabuena, has ganado", "Victoria", JOptionPane.INFORMATION_MESSAGE, null);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(TresEnRaya.class.getName()).log(Level.SEVERE, null, ex);
                }
                dispose();
                break;
            } else if (isBoardFull()) {
                JOptionPane.showMessageDialog(this, "Ninguno ha ganado, es un empate", "Empate", JOptionPane.INFORMATION_MESSAGE, null);
                 try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(TresEnRaya.class.getName()).log(Level.SEVERE, null, ex);
                }
                dispose();
                break;
            } else if (partidaPerdida) {
                JOptionPane.showMessageDialog(this, "Has perdido, mejor suerte la próxima vez", "Derrota", JOptionPane.INFORMATION_MESSAGE, null);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(TresEnRaya.class.getName()).log(Level.SEVERE, null, ex);
                }
                dispose();
                break;
            }
            
        }
        Servidor.EliminarPartida(usuario, contrincante);
        Servidor.EliminarPartida(contrincante, usuario);
    }

    /**
     * Maneja las acciones del usuario en la interfaz gráfica.
     *
     * @param e El evento de acción.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!miTurno) {
            return; 
        }

        JButton buttonClicked = (JButton) e.getSource();

        if (buttonClicked.getText().equals("")) {

            buttonClicked.setText(String.valueOf(ficha));
            int i = (int) buttonClicked.getClientProperty("fila");
            int j = (int) buttonClicked.getClientProperty("col");
            setAccion(i, j, ficha);
            buttonClicked.setEnabled(false);

            semaphore.release();
        }
    }

    /**
     * Inicializa los componentes de la interfaz gráfica.
     */
    public void initComponents() {
        setTitle("Partida contra " + contrincante);
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 3));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].putClientProperty("fila", i);
                buttons[i][j].putClientProperty("col", j);
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 60));
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].addActionListener(this);
                add(buttons[i][j]);
            }
        }

        this.setAlwaysOnTop(true);
        this.setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Servidor.EliminarPartida(usuario, contrincante);
                Servidor.PartidasFinalizadas(usuario, contrincante);
                partidaPerdida = true;
                dispose();
            }
        });
    }

    /**
     * Verifica si el usuario ha ganado.
     *
     * @return Verdadero si el usuario ha ganado, falso en caso contrario.
     */
    private boolean checkForWin() {
        for (int i = 0; i < 3; i++) {
            if (checkThree(buttons[i][0], buttons[i][1], buttons[i][2], ficha)) return true;
            if (checkThree(buttons[0][i], buttons[1][i], buttons[2][i], ficha)) return true;
        }
        if (checkThree(buttons[0][0], buttons[1][1], buttons[2][2], ficha)) return true;
        if (checkThree(buttons[0][2], buttons[1][1], buttons[2][0], ficha)) return true;
        return false;
    }

    /**
     * Verifica si tres botones tienen la misma ficha.
     *
     * @param b1 El primer botón.
     * @param b2 El segundo botón.
     * @param b3 El tercer botón.
     * @param ficha La ficha a verificar.
     * @return Verdadero si los tres botones tienen la misma ficha, falso en caso contrario.
     */
    private boolean checkThree(JButton b1, JButton b2, JButton b3, String ficha) {
        return b1.getText().equals(ficha) && b1.getText().equals(b2.getText()) && b2.getText().equals(b3.getText());
    }

    /**
     * Verifica si el tablero está lleno.
     *
     * @return Verdadero si el tablero está lleno, falso en caso contrario.
     */
    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Reinicia el tablero.
     */
    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tablero[i][j] = "";
                AccionDeContrincante[i][j] = "";
            }
        }
    }

    /**
     * Establece una acción en el tablero.
     *
     * @param fila La fila de la acción.
     * @param col La columna de la acción.
     * @param ficha La ficha a establecer.
     */
    public void setAccion(int fila, int col, String ficha) {
        resetBoard();
        tablero[fila][col] = ficha;
    }

    /**
     * Obtiene las acciones del tablero.
     *
     * @return Las acciones del tablero.
     */
    public String[][] getAccion() {
        return tablero;
    }

    /**
     * Ejecuta la jugada del contrincante.
     *
     * @param AccionDeContrincante Las acciones del contrincante.
     */
    private void jugadaContrincante(String[][] AccionDeContrincante) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!AccionDeContrincante[i][j].equals("")) {
                    buttons[i][j].setText(String.valueOf(AccionDeContrincante[i][j]));
                    buttons[i][j].setEnabled(false);
                }
            }
        }
    }

    /**
     * Verifica si hay una jugada del contrincante.
     *
     * @param AccionDeContrincante Las acciones del contrincante.
     * @return Verdadero si hay una jugada, falso en caso contrario.
     */
    private boolean hayJugada(String[][] AccionDeContrincante) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!AccionDeContrincante[i][j].equals("")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Marca la partida como perdida.
     */
    public void derrota() {
        partidaPerdida = true;
    }

    /**
     * Obtiene el nombre del usuario.
     *
     * @return El nombre del usuario.
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * Establece el nombre del usuario.
     *
     * @param usuario El nombre del usuario.
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * Obtiene el nombre del contrincante.
     *
     * @return El nombre del contrincante.
     */
    public String getContrincante() {
        return contrincante;
    }

    /**
     * Establece el nombre del contrincante.
     *
     * @param contrincante El nombre del contrincante.
     */
    public void setContrincante(String contrincante) {
        this.contrincante = contrincante;
    }

    /**
     * Marca la partida como ganada por rendición.
     */
    public void GanadaPorRendicion() {
        partidaGanada = true;
    }
}