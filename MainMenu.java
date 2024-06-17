package cliente;

import servidor.Servidor;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.*;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.*;

/**
 * La clase MainMenu representa la interfaz del cliente para el juego.
 * 
 * @author Pablo
 * @author Daniel
 */
public class MainMenu extends JFrame implements ActionListener, Runnable {

    private Socket sckCliente;
    private String usuario;

    private JLabel RetadoPor;
    private String Retador;
    private JButton AceptarReto;
    private JButton RechazarReto;

    private JLabel RetarA;
    private JTextField RetarAText;
    private JButton RetarAButton;

    private JLabel UsuariosInfo;
    private JList<String> UsuariosConectados;

    private DefaultListModel<String> model = new DefaultListModel<>();
    private ArrayList<String> otrosUsuarios = new ArrayList<>();

    private JLabel Bienvenida;

    /**
     * Constructor de la clase Cliente.
     *
     * @param sck El socket del cliente.
     * @param user El nombre del usuario.
     * @param passw La contraseña del usuario.
     */
    public MainMenu(Socket sck, String user, String passw) {
        this.sckCliente = sck;
        this.usuario = user;
        Thread hiloCliente = new Thread(this);
        hiloCliente.start();
    }

    /**
     * Método principal de ejecución del cliente.
     */
    @Override
    public void run() {
        initComponents();
    }

    /**
     * Maneja las acciones del usuario en la interfaz gráfica.
     *
     * @param e El evento de acción.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == RetarAButton) {
            enviarReto(RetarAText.getText());
        } else if (e.getSource() == AceptarReto) {
            EsconderBotonesReto();
            Servidor.RetoAceptado(usuario, Retador);
        } else if (e.getSource() == RechazarReto) {
            Servidor.RetoRechazado(usuario, Retador);
            EsconderBotonesReto();
        }
    }

    /**
     * Envía un reto a otro usuario.
     *
     * @param retado El nombre del usuario a retar.
     */
    private void enviarReto(String retado) {
        if (!CampoVacio(retado) && UsuarioEnJList(retado) && !retado.equals(usuario)) {
            Servidor.RetarA(usuario, retado);
            JOptionPane.showMessageDialog(this, "Has enviado un reto a " + retado, "Reto enviado", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Usuario no válido", "Error", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Inicializa los componentes de la interfaz gráfica.
     */
    public void initComponents() {
        this.setTitle("Menú Principal");
        this.setSize(600, 500);
        this.setLayout(null);
        this.setAlwaysOnTop(true);

        Bienvenida = new JLabel("Bienvenido, " + getUsuario());
        Bienvenida.setFont(new Font("Arial", Font.BOLD, 16));
        Bienvenida.setForeground(Color.BLACK);

        RetarA = new JLabel("Indica el jugador que quieres o clicke sobre su nombre en la lista dos veces");
        RetarA.setForeground(Color.BLUE);
        RetarAText = new JTextField(20);
        RetarAButton = new JButton("Enviar Reto");
        RetarAButton.setBackground(Color.GREEN);
        UsuariosInfo = new JLabel("Usuarios conectados");
        UsuariosInfo.setForeground(Color.BLUE);
        UsuariosConectados = new JList<>(model);
        UsuariosConectados.setFont(new Font("Arial", Font.PLAIN, 16));
        RetadoPor = new JLabel("No tienes retos pendientes");
        RetadoPor.setForeground(Color.RED);
        AceptarReto = new JButton("Aceptar");
        AceptarReto.setBackground(Color.GREEN);
        RechazarReto = new JButton("Rechazar");
        RechazarReto.setBackground(Color.RED);

        add(Bienvenida);
        Bienvenida.setBounds(20, 10, 300, 30);

        add(RetarA);
        RetarA.setBounds(20, 50, 650, 30);
        add(RetarAText);
        RetarAText.setBounds(20, 90, 200, 30);
        add(RetarAButton);
        RetarAButton.setBounds(240, 90, 120, 30);
        RetarAButton.addActionListener(this);

        add(UsuariosInfo);
        UsuariosInfo.setBounds(20, 130, 200, 30);
        add(UsuariosConectados);
        UsuariosConectados.setBounds(20, 170, 340, 200);

        add(RetadoPor);
        RetadoPor.setBounds(20, 380, 200, 30);
        add(AceptarReto);
        AceptarReto.setBounds(20, 420, 120, 30);
        AceptarReto.addActionListener(this);
        add(RechazarReto);
        RechazarReto.setBounds(160, 420, 120, 30);
        RechazarReto.addActionListener(this);

        EsconderBotonesReto();

        UsuariosConectados.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = UsuariosConectados.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        String selectedUser = model.getElementAt(index);
                        enviarReto(selectedUser);
                    }
                } else if (e.getClickCount() == 1) {
                    int index = UsuariosConectados.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        String selectedUser = model.getElementAt(index);
                        RetarAText.setText(selectedUser);
                    }
                }
            }
        });

        this.setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    Servidor.EliminarCliente(usuario);
                    sckCliente.close();
                } catch (IOException ex) {
                    System.out.println("No se pudo cerrar el socket");
                }
            }
        });
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
     * Actualiza la lista de usuarios conectados.
     */
    public void ActualizarJList() {
        otrosUsuarios = Servidor.ListarClientes();
        model.clear();

        for (String user : otrosUsuarios) {
            if (!user.equals(usuario)) {
                model.addElement(user);
            }
        }
    }

    /**
     * Verifica si un campo de texto está vacío.
     *
     * @param text El texto a verificar.
     * @return Verdadero si el campo está vacío, falso en caso contrario.
     */
    public boolean CampoVacio(String text) {
        return text.equals("");
    }

    /**
     * Verifica si un usuario está en la lista de usuarios conectados.
     *
     * @param text El nombre del usuario a verificar.
     * @return Verdadero si el usuario está en la lista, falso en caso contrario.
     */
    public boolean UsuarioEnJList(String text) {
        return otrosUsuarios.contains(text);
    }

    /**
     * Establece el usuario que ha retado al cliente.
     *
     * @param RetadoP El nombre del usuario que ha retado.
     */
    public void RetadoPor(String RetadoP) {
        Retador = RetadoP;
        RetadoPor.setText("Tienes un reto de " + Retador);
        RetadoPor.setVisible(true);
        AceptarReto.setVisible(true);
        RechazarReto.setVisible(true);
    }

    /**
     * Esconde los botones de aceptar y rechazar reto.
     */
    public void EsconderBotonesReto() {
        RetadoPor.setVisible(false);
        AceptarReto.setVisible(false);
        RechazarReto.setVisible(false);
    }

    /**
     * Notifica al usuario que su reto ha sido rechazado.
     *
     * @param Retado El nombre del usuario que ha rechazado el reto.
     */
    public void RechazaronReto(String Retado) {
        JOptionPane.showMessageDialog(this, "El usuario " + Retado + " no aceptó tu reto", "Reto rechazado", JOptionPane.INFORMATION_MESSAGE);
    }
}