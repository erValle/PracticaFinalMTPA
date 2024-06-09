package Cliente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.List;
import java.util.ArrayList;

public class IU_Menu extends JPanel {

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String username;
    private String[] usuarios;
    private JPanel usuariosPanel;
    private JScrollPane scrollPane;

    public IU_Menu(Socket socket, String username, String[] usuarios) {
        this.socket = socket;
        this.username = username;
        this.usuarios = usuarios;
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        initComponents();
        startListening();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(60, 63, 65));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("TIC-TAC-TOE", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);

        JLabel labelBienvenida = new JLabel("Bienvenido, " + username, JLabel.CENTER);
        labelBienvenida.setFont(new Font("Arial", Font.BOLD, 24));
        labelBienvenida.setForeground(Color.WHITE);

        JButton btnSalir = new JButton("Salir");
        btnSalir.setFont(new Font("Arial", Font.PLAIN, 14));
        btnSalir.setBackground(new Color(220, 53, 69));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFocusPainted(false);
        btnSalir.addActionListener(e -> salir());

        JLabel usuariosLabel = new JLabel("Usuarios conectados");
        usuariosLabel.setFont(new Font("Arial", Font.BOLD, 18));
        usuariosLabel.setForeground(Color.WHITE);

        // Panel para lista de usuarios
        usuariosPanel = new JPanel();
        usuariosPanel.setLayout(new BoxLayout(usuariosPanel, BoxLayout.Y_AXIS));
        usuariosPanel.setBackground(new Color(60, 63, 65));

        // Añadir cada usuario como un panel clickable
        actualizarListaUsuarios();

        // Scroll pane para la lista de usuarios
        scrollPane = new JScrollPane(usuariosPanel);
        scrollPane.setPreferredSize(new Dimension(250, 300));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 63, 65)));

        JPanel usuariosContainer = new JPanel(new BorderLayout());
        usuariosContainer.setBackground(new Color(60, 63, 65));
        usuariosContainer.add(usuariosLabel, BorderLayout.NORTH);
        usuariosContainer.add(scrollPane, BorderLayout.CENTER);

        add(titleLabel, BorderLayout.NORTH);
        add(labelBienvenida, BorderLayout.CENTER);
        add(usuariosContainer, BorderLayout.WEST);
        add(btnSalir, BorderLayout.SOUTH);
    }

    private void actualizarListaUsuarios() {
        usuariosPanel.removeAll();
        for (String usuario : usuarios) {
            JPanel usuarioPanel = crearPanelUsuario(usuario);
            usuariosPanel.add(usuarioPanel);
            usuariosPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Espacio entre paneles
        }
        usuariosPanel.revalidate();
        usuariosPanel.repaint();
    }

    private JPanel crearPanelUsuario(String usuario) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(75, 75, 75));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.setPreferredSize(new Dimension(230, 60));

        JLabel label = new JLabel(usuario);
        label.setFont(new Font("Arial", Font.PLAIN, 18));
        label.setForeground(Color.WHITE);

        JButton retarButton = new JButton("Retar");
        retarButton.setFont(new Font("Arial", Font.PLAIN, 14));
        retarButton.setBackground(new Color(70, 130, 180));
        retarButton.setForeground(Color.WHITE);
        retarButton.setFocusPainted(false);
        retarButton.addActionListener(e -> retarUsuario(usuario));

        JPanel labelPanel = new JPanel(new BorderLayout());
        labelPanel.setBackground(new Color(75, 75, 75));
        labelPanel.add(label, BorderLayout.CENTER);

        panel.add(labelPanel, BorderLayout.CENTER);
        panel.add(retarButton, BorderLayout.EAST);

        // Hacer el panel clickable
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(null, "Usuario: " + usuario + " clickeado!");
                // Aquí puedes añadir la funcionalidad que desees al hacer click en el usuario
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(100, 100, 100));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(new Color(75, 75, 75));
            }
        });

        return panel;
    }

    private void retarUsuario(String usuario) {
        JOptionPane.showMessageDialog(null, "Has retado a: " + usuario);
        // Aquí puedes añadir la funcionalidad de retar al usuario
    }

    private void salir() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    private void startListening() {
        Thread listenerThread = new Thread(() -> extracted());
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    private void extracted() {
        try {
            while (true) {
                String message = in.readUTF();
                if (!message.equals("Fin")) {
                    String[] userArray = message.split(",");
                    List<String> newUsers = new ArrayList<>();
                    for (String user : userArray) {
                        newUsers.add(user.trim());
                    }
                    SwingUtilities.invokeLater(() -> {
                        usuarios = newUsers.toArray(new String[0]);
                        actualizarListaUsuarios();
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
