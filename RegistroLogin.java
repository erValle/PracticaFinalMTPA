package cliente;

import servidor.Mensaje;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 * La clase IULogin muestra la interfaz de usuario para que los usuarios se registren o inicien sesión.
 * Esta clase extiende JFrame e implementa ActionListener y Runnable.
 * Gestiona la interacción del usuario con la interfaz gráfica y la comunicación con el servidor.
 * 
 * @author Pablo
 * @author Daniel
 */
public class RegistroLogin extends JFrame implements ActionListener, Runnable {

    private JButton AccesoBoton;
    private JButton RegistroBoton;
    private JTextField usuarioText;
    private JTextField contraseñaText;
    private JLabel usuarioLabel;
    private JLabel contraseñaLabel;

    private Socket cliente;
    private InputStream flujoLectura;
    private OutputStream flujoEscritura;

    private String accion;
    
    /**
     * Método principal que inicia la aplicación.
     * 
     * @param args argumentos de la línea de comandos
     */
    public static void main(String args[]) {
        RegistroLogin prueba = new RegistroLogin();
    }

    /**
     * Constructor de la clase IULogin.
     * Inicializa los componentes de la interfaz gráfica.
     */
    public RegistroLogin() {
        initComponents();
    }

    /**
     * Inicializa y configura los componentes de la interfaz gráfica.
     */
    public void initComponents() {
        this.setTitle("Login Y Registro");
        this.setSize(600, 250);
        this.setLayout(null);
        AccesoBoton = new JButton("Iniciar Sesion");
        RegistroBoton = new JButton("Registrarse");
        usuarioLabel = new JLabel("Usuario");
        contraseñaLabel = new JLabel("Contraseña");
        usuarioText = new JTextField(20);
        contraseñaText = new JTextField(20);

        AccesoBoton.setBackground(Color.GREEN);
        RegistroBoton.setBackground(Color.YELLOW);

        AccesoBoton.addActionListener(this);
        RegistroBoton.addActionListener(this);

        add(usuarioLabel);
        usuarioLabel.setBounds(50, 40, 100, 25);
        add(usuarioText);
        usuarioText.setBounds(150, 40, 200, 25);
        add(contraseñaLabel);
        contraseñaLabel.setBounds(50, 80, 100, 25);
        add(contraseñaText);
        contraseñaText.setBounds(150, 80, 200, 25);

        add(AccesoBoton);
        AccesoBoton.setBounds(370, 40, 125, 50);
        add(RegistroBoton);
        RegistroBoton.setBounds(370, 100, 125, 50);

        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    /**
     * Establece la comunicación con el servidor.
     * Inicializa el socket y los flujos de entrada y salida.
     */
    public void initComunication() {
        try {
            cliente = new Socket("127.0.0.1", 9998);
            this.flujoLectura = cliente.getInputStream();
            this.flujoEscritura = cliente.getOutputStream();
            Thread hilo = new Thread(this);
            hilo.start();
        } catch (IOException ex) {
            System.out.println("No se pudo conectar con el servidor");
        }
    }

    /**
     * Envía los datos del usuario al servidor y espera la respuesta.
     * Ejecuta las acciones correspondientes según el mensaje recibido del servidor.
     */
    @Override
    public void run() {
        try {
            this.flujoEscritura.write(toString(usuarioText.getText(), contraseñaText.getText(), accion).getBytes("UTF-8"));
            byte[] buffer = new byte[16];
            int nb;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            do {
                nb = flujoLectura.read(buffer);
                baos.write(buffer, 0, nb);
            } while (nb > 0 && flujoLectura.available() > 0);
            Mensaje msg = Mensaje.CrearMensaje(baos.toByteArray());
            LlamarAccionLogin(msg);
        } catch (IOException ex) {
            System.out.println("No se pudo enviar el mensaje");
        }
    }

    /**
     * Maneja los eventos de los botones de la interfaz gráfica.
     * 
     * @param e el evento de acción
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == AccesoBoton) {
            if (CamposVacios(usuarioText.getText(), contraseñaText.getText()) == true) {
                JOptionPane.showMessageDialog(this, "Campos no completados", "Error", JOptionPane.INFORMATION_MESSAGE, null);
            } else {
                accion = "Acceso";
                initComunication();
            }
        } else if (e.getSource() == RegistroBoton) {
            if (CamposVacios(usuarioText.getText(), contraseñaText.getText()) == true) {
                JOptionPane.showMessageDialog(this, "Campos no completados", "Error", JOptionPane.INFORMATION_MESSAGE, null);
            } else {
                accion = "Registro";
                initComunication();
            }
        }
    }

    /**
     * Verifica si los campos de usuario y contraseña están vacíos.
     * 
     * @param user el nombre de usuario
     * @param passw la contraseña
     * @return true si algún campo está vacío, false en caso contrario
     */
    public boolean CamposVacios(String user, String passw) {
        return "".equals(user) || "".equals(passw);
    }

    /**
     * Crea una cadena con los datos del usuario y la acción a realizar.
     * 
     * @param user el nombre de usuario
     * @param passw la contraseña
     * @param accion la acción a realizar (Acceso o Registro)
     * @return una cadena con el formato "accion@user@passw"
     */
    public String toString(String user, String passw, String accion) {
        return accion + "@" + user + "@" + passw;
    }

    /**
     * Maneja las acciones a realizar según el mensaje recibido del servidor.
     * 
     * @param msg el mensaje recibido del servidor
     */
    public void LlamarAccionLogin(Mensaje msg) {
        switch (msg.getAccion()) {
            case "Aceptado":
                dispose();
                break;
            case "Denegado":
                try {
                    cliente.close();
                } catch (IOException ex) {
                    System.out.println("No se pudo cerrar el socket");
                }
                System.out.println("Usuario o contraseña no válidos");
                JOptionPane.showMessageDialog(this, "Usuario o contraseña no válidos", "Error", JOptionPane.INFORMATION_MESSAGE, null);
                break;
            case "DenegadoE":
                try {
                    cliente.close();
                } catch (IOException ex) {
                    System.out.println("No se pudo cerrar el socket");
                }
                JOptionPane.showMessageDialog(this, "Usuario ya existe", "Error", JOptionPane.INFORMATION_MESSAGE, null);
                break;
        }
    }
}
