package Cliente;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * Clase Cliente muestra los jugadores retados y los retos aceptados o rechazados
 * y los jugadores conectados
 * @author david
 * @version 1.0
 */

public class Cliente extends JFrame implements ActionListener,Runnable{
    
    /**
     *Definimoms todos los objetos que usamos en la clase 
     */
    
    private Socket sckCliente;
    private InputStream flujoLectura;
    private OutputStream flujoEscritura;
    private String usuario;
    private String contraseña;
    
    private JLabel RetadoPor;
    private String Retador;
    private JButton AceptarReto;
    private JButton RechazarReto;
    
    private JLabel RetarA;
    private JTextField RetarAText;
    private JButton RetarAButton;
    
    private JLabel UsuariosInfo;
    private JList UsuariosConectados;
    
    private DefaultListModel<String>model=new DefaultListModel<>();
    private ArrayList<String> otrosUsuarios=new ArrayList<String>();
    
    /**
     * Metodo cliente se conecta con el servidor
     * @param sck
     * @param user usuario concetado
     * @param passw contraseña del usuario
     */
    
    public Cliente(Socket sck,String user,String passw){
        try{
        this.sckCliente=sck;
        this.flujoLectura=this.sckCliente.getInputStream();
        this.flujoEscritura=this.sckCliente.getOutputStream();
        this.usuario=user;
        this.contraseña=passw;
        Thread hiloCliente = new Thread(this);
        hiloCliente.start();
        }catch(IOException ex){
            System.out.println("No se pudo conectar con el servidor");
        }
    }
    
    @Override
    public void run(){
        initComponents();
    }
    
    /**
     * Metodo que nos permite retar a otro jugador y aceptar o no los retos
     * @param e 
     */
    
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == RetarAButton){
            if(CampoVacio(RetarAText.getText())==false && UsuarioEnJList(RetarAText.getText())==true && !RetarAText.getText().equals(usuario)){
                Servidor.RetarA(usuario,RetarAText.getText());
                JOptionPane.showMessageDialog(this, "Has enviado un reto a "+RetarAText.getText(), "Reto enviado", JOptionPane.INFORMATION_MESSAGE, null);
            }
            else{
                JOptionPane.showMessageDialog(this, "Usuario no valido", "Error", JOptionPane.INFORMATION_MESSAGE, null);
            }
        }
        else if(e.getSource()==AceptarReto){
            EsconderBotonesReto();
            Servidor.RetoAceptado(usuario,Retador);
        }
        else if(e.getSource()==RechazarReto){
            Servidor.RetoRechazado(usuario, Retador);
            EsconderBotonesReto();
        }
    }
    
    /**
     * Metodo que inicializa los componentes antes de una partida
     */
    
    public void initComponents(){
        
        this.setTitle(getUsuario());
        this.setSize(380,450);
        this.setLayout(null);
        this.setAlwaysOnTop(true);
        RetarA=new JLabel("Indica el jugador que retar");
        RetarAText=new JTextField(20);
        RetarAButton=new JButton("Enviar Reto");
        UsuariosInfo=new JLabel("Usuarios conectados");
        UsuariosConectados=new JList(model);
        RetadoPor=new JLabel("No tienes retos pendientes");
        AceptarReto= new JButton("Aceptar");
        RechazarReto= new JButton("Rechazar");
        
        add(RetarA);
        RetarA.setBounds(15,20,150,30);
        add(RetarAText);
        RetarAText.setBounds(15, 55, 150, 30);
        add(RetarAButton);
        RetarAButton.setBounds(30, 90, 100, 30);
        RetarAButton.addActionListener(this);
        add(UsuariosInfo);
        UsuariosInfo.setBounds(212, 20, 150, 30);
        add(UsuariosConectados);
        UsuariosConectados.setBounds(200, 55, 150, 300);
        add(RetadoPor);
        RetadoPor.setBounds(15, 220, 175, 30);
        add(AceptarReto);
        AceptarReto.setBounds(5, 260, 90, 30);
        AceptarReto.addActionListener(this);
        add(RechazarReto);
        RechazarReto.setBounds(110,260,90,30);
        RechazarReto.addActionListener(this);
        EsconderBotonesReto();
        
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
    
    public String getUsuario() {
        return usuario;
    }
    
    /**
     * Metodo que actualiza la lista de clientes
     */
    
    public void ActualizarJList(){
        otrosUsuarios=Servidor.ListarClientes();
        String[] aux = otrosUsuarios.toArray(new String[otrosUsuarios.size()]);
        model.clear();
        
        for(String user:aux){
            model.addElement(user);
        }
    }
    
    public boolean CampoVacio(String text){
        return text.equals(""); 
    }
    
    public boolean UsuarioEnJList(String text){
        for (String user : otrosUsuarios) {
            if(user.equals(text)){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Metodo que muestra por quien hemos sido retado
     * @param RetadoP jugador que reta 
     */
    
    public void RetadoPor(String RetadoP){
        Retador=RetadoP;
        RetadoPor.setText("Tienes un reto de "+Retador);
        RetadoPor.setVisible(true);
        AceptarReto.setVisible(true);
        RechazarReto.setVisible(true);
    }
    
    /**
     * Metodo que esconde los botones que se utilizan para retar
     */
    
    public void EsconderBotonesReto(){
        RetadoPor.setVisible(false);
        AceptarReto.setVisible(false);
        RechazarReto.setVisible(false);
    }
   
    /**
     * Metodo que se usa cuando rechazamos un reto
     * @param Retado jugador que ha recibido un reto
     */
    
    public void RechazaronReto(String Retado){
        JOptionPane.showMessageDialog(this, "El usuario "+Retado+" no acepto tu reto", "Reto rechazado", JOptionPane.INFORMATION_MESSAGE, null);
    }
    
}