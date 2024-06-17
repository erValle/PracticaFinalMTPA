package servidor;

import cliente.MainMenu;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * La clase Servidor gestiona las conexiones de clientes y las partidas de Tres en Raya.
 * Proporciona métodos para registrar y autenticar usuarios, iniciar partidas, y manejar
 * los comandos de la consola para finalizar el servidor.
 * 
 * @author Pablo
 * @author Daniel
 */
public class Servidor {

    private static ArrayList<MainMenu> usuariosConectados = new ArrayList<>();
    private static ArrayList<TresEnRaya> partidasEnCurso = new ArrayList<>();
    static Printer printer = new Printer();
    private static boolean finalizando = false;
    private static ServerSocket srv = null;

    /**
     * Método principal que inicia el servidor y escucha conexiones de clientes.
     * @param args Argumentos de línea de comando.
     * @throws Exception si ocurre un error durante la ejecución del servidor.
     */
    public static void main(String[] args) throws Exception {
        Thread commandThread = new Thread(() -> {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                try {
                    String command = reader.readLine();
                    if (command.equals("finalizar")) {
                        
                        for (TresEnRaya partida : partidasEnCurso) {
                            partida.dispose();
                        }
                        
                        for (MainMenu aux : usuariosConectados) {
                            aux.dispose();
                        }
                        
                        finalizando = true;
                        System.out.println("Todas las partidas activas han sido finalizadas.");
                        try {
                            if (srv != null && !srv.isClosed()) {
                                srv.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        commandThread.start();

        srv = new ServerSocket(9998);
        System.out.println("Servidor Arrancado");
        while (!finalizando) {
            try {
                Socket sck = srv.accept();
                InputStream is = sck.getInputStream();
                OutputStream os = sck.getOutputStream();
                byte[] buffer = new byte[16];
                int nb;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                do {
                    nb = is.read(buffer);
                    baos.write(buffer, 0, nb);
                } while (nb > 0 && is.available() > 0);
                Mensaje msg = Mensaje.CrearMensaje(baos.toByteArray());
                String permiso = LlamarAccion(msg, sck);
                Mensaje mensajeCliente = Mensaje.CrearMensajeConParametros(permiso, msg.getCampo1(), msg.getCampo2());
                os.write(mensajeCliente.toString().getBytes("UTF-8"));
            } catch (IOException e) {
                if (finalizando) {
                    System.out.println("Servidor cerrado.");
                } else {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Llama a la acción correspondiente según el mensaje recibido.
     * @param msg El mensaje recibido del cliente.
     * @param socket El socket del cliente.
     * @return El resultado de la acción ejecutada.
     */
    public static String LlamarAccion(Mensaje msg, Socket socket) {
        if (finalizando) {
            return "Servidor en modo finalización. No se permiten nuevas acciones.";
        }
        try {
            switch (msg.getAccion()) {
                case "Acceso": {
                    try {
                        if (IniciarSesion(msg.getCampo1(), msg.getCampo2()) && !UsuarioYaConectado(msg.getCampo1())) {
                            AceptarCliente(socket, msg.getCampo1(), msg.getCampo2());
                            return "Aceptado";
                        } else {
                            return "Denegado";
                        }
                    } catch (IOException ex) {
                        System.out.println("No se encontró el archivo");
                    }
                }
                break;
                case "Registro": {
                    try {
                        if (RegistrarUsuario(msg.getCampo1(), msg.getCampo2())) {
                            AceptarCliente(socket, msg.getCampo1(), msg.getCampo2());
                            return "Aceptado";
                        } else {
                            return "DenegadoE";
                        }
                    } catch (IOException ex) {
                        System.out.println("No se encontró el archivo");
                    }
                }
                break;
            }
        } catch (Exception ex) {
            System.out.println("Error");
        }
        return "Error inesperado";
    }

    /**
     * Inicia sesión de un usuario comprobando sus credenciales.
     * @param user El nombre de usuario.
     * @param passw La contraseña.
     * @return true si las credenciales son correctas, false en caso contrario.
     * @throws FileNotFoundException si no se encuentra el archivo de credenciales.
     * @throws IOException si ocurre un error de entrada/salida.
     */
    public static boolean IniciarSesion(String user, String passw) throws FileNotFoundException, IOException {
        String linea;
        FileReader ficheroLeer = new FileReader("db_Credenciales.txt");
        BufferedReader buff = new BufferedReader(ficheroLeer);
        while ((linea = buff.readLine()) != null) {
            String[] parte = linea.split(";");
            String usuario = parte[0];
            String contraseña = parte[1];
            if (user.equals(usuario) && passw.equals(contraseña)) {
                ficheroLeer.close();
                return true;
            }
        }
        ficheroLeer.close();
        return false;
    }

    /**
     * Verifica si un usuario ya está conectado.
     * @param user El nombre de usuario.
     * @return true si el usuario ya está conectado, false en caso contrario.
     */
    public static boolean UsuarioYaConectado(String user) {
        for (MainMenu aux : usuariosConectados) {
            if (aux.getUsuario().equals(user)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Registra un nuevo usuario si no existe previamente.
     * @param user El nombre de usuario.
     * @param passw La contraseña.
     * @return true si el usuario fue registrado exitosamente, false si el usuario ya existe.
     * @throws FileNotFoundException si no se encuentra el archivo de credenciales.
     * @throws IOException si ocurre un error de entrada/salida.
     */
    public static boolean RegistrarUsuario(String user, String passw) throws FileNotFoundException, IOException {
        String linea;
        FileReader ficheroLeer = new FileReader("db_Credenciales.txt");
        BufferedReader buff = new BufferedReader(ficheroLeer);
        while ((linea = buff.readLine()) != null) {
            String[] parte = linea.split(";");
            String usuario = parte[0];
            if (user.equals(usuario)) {
                ficheroLeer.close();
                return false;
            }
        }
        ficheroLeer.close();
        printer.printRegistro(user, passw);
        return true;
    }

    /**
     * Acepta la conexión de un cliente y lo agrega a la lista de usuarios conectados.
     * @param sck El socket del cliente.
     * @param user El nombre de usuario.
     * @param passw La contraseña.
     */
    public static void AceptarCliente(Socket sck, String user, String passw) {
        MainMenu cliente = new MainMenu(sck, user, passw);
        usuariosConectados.add(cliente);
        usuariosConectados.forEach((actualizar) -> {
            actualizar.ActualizarJList();
        });
    }

    /**
     * Elimina un cliente de la lista de usuarios conectados.
     * @param cli El nombre de usuario del cliente a eliminar.
     */
    public static void EliminarCliente(String cli) {
        ArrayList<MainMenu> listaAux = new ArrayList<>();
        for (MainMenu user : usuariosConectados) {
                if (user.getUsuario().equals(cli)) {
                    listaAux.add(user);
                }
            }
            usuariosConectados.removeAll(listaAux);
        usuariosConectados.forEach((user) -> {
            user.ActualizarJList();
        });
    }

    /**
     * Lista todos los clientes conectados.
     * @return Una lista de nombres de usuario de los clientes conectados.
     */
    public static ArrayList<String> ListarClientes() {
        ArrayList<String> users = new ArrayList<>();
        for (MainMenu unusuario : usuariosConectados) {
            users.add(unusuario.getUsuario());
        }
        return users;
    }

    /**
     * Reta a un usuario a una partida de Tres en Raya.
     * @param Retador El nombre de usuario del retador.
     * @param Retado El nombre de usuario del retado.
     */
    public static void RetarA(String Retador, String Retado) {
        for (MainMenu unusuario : usuariosConectados) {
            if (unusuario.getUsuario().equals(Retado)) {
                unusuario.RetadoPor(Retador);
            }
        }
    }

    /**
     * Notifica a un usuario que su reto ha sido rechazado.
     * @param Retado El nombre de usuario del retado.
     * @param Retador El nombre de usuario del retador.
     */
    public static void RetoRechazado(String Retado, String Retador) {
        for (MainMenu unusuario : usuariosConectados) {
            if (unusuario.getUsuario().equals(Retador)) {
                unusuario.RechazaronReto(Retado);
            }
        }
    }

    /**
     * Inicia una nueva partida de Tres en Raya.
     * @param Retado El nombre de usuario del retado.
     * @param Retador El nombre de usuario del retador.
     */
    public static void RetoAceptado(String Retado, String Retador) {
        partidasEnCurso.add(new TresEnRaya(Retado, Retador, "X", true));
        partidasEnCurso.add(new TresEnRaya(Retador, Retado, "O", false));
    }

    /**
     * Obtiene la acción del contrincante en una partida de Tres en Raya.
     * @param usuario El nombre de usuario del jugador.
     * @param contrincante El nombre de usuario del contrincante.
     * @return Una matriz representando el tablero de juego.
     */
    public static String[][] AccionDeContrincante(String usuario, String contrincante) {
        
        for (TresEnRaya aux : partidasEnCurso) {
            if (aux.getUsuario().equals(contrincante) && aux.getContrincante().equals(usuario)) {
                return aux.getAccion();
            }
        }
        
        String tableroVacio[][] = new String[3][3];
        return tableroVacio;
    }

    /**
     * Registra la victoria de un jugador en una partida de Tres en Raya.
     * @param usuario El nombre de usuario del jugador ganador.
     * @param contrincante El nombre de usuario del jugador perdedor.
     * @throws IOException si ocurre un error de entrada/salida.
     */
    public static void Victoria(String usuario, String contrincante) throws IOException {
        
        for (TresEnRaya aux : partidasEnCurso) {
            if (aux.getUsuario().equals(contrincante) && aux.getContrincante().equals(usuario)) {
                aux.derrota();
                actualizarLista(usuario, contrincante);
                actualizarHistorial(usuario, contrincante);
            }
        }
        
    }

    /**
     * Actualiza la lista de puntos de los usuarios después de una partida.
     * @param usuario El nombre de usuario del jugador ganador.
     * @param contrincante El nombre de usuario del jugador perdedor.
     */
    public static void actualizarLista(String usuario, String contrincante) {
        String filePath = "db_UsuariosPuntos.txt";
        StringBuilder fileContent = new StringBuilder();
        boolean usuarioFound = false;
        boolean contrincanteFound = false;
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                String existingUser = parts[0];
                int partidasGanadas = Integer.parseInt(parts[1]);
                int partidasPerdidas = Integer.parseInt(parts[2]);
                if (existingUser.equals(usuario)) {
                    partidasGanadas++;
                    usuarioFound = true;
                } else if (existingUser.equals(contrincante)) {
                    partidasPerdidas++;
                    contrincanteFound = true;
                }
                fileContent.append(existingUser).append(";")
                        .append(partidasGanadas).append(";")
                        .append(partidasPerdidas).append("\n");
            }
            br.close();
            if (!usuarioFound) {
                fileContent.append(usuario).append(";1;0\n");
            }
            if (!contrincanteFound) {
                fileContent.append(contrincante).append(";0;1\n");
            }
            printer.printUsuariosPuntos(fileContent.toString());
        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error de entrada/salida: " + e.getMessage());
        }
    }

    /**
     * Elimina una partida en curso.
     * @param usuario El nombre de usuario de un jugador.
     * @param contrincante El nombre de usuario del contrincante.
     */
    public static void EliminarPartida(String usuario, String contrincante) {
        ArrayList<TresEnRaya> listaAux = new ArrayList<>();
        for (TresEnRaya p : partidasEnCurso) {
            if (p.getUsuario().equals(usuario) && p.getContrincante().equals(contrincante)) {
                listaAux.add(p);
            }
        }
        partidasEnCurso.removeAll(listaAux);
        for (TresEnRaya p : partidasEnCurso) {
            if (p.getUsuario().equals(contrincante) && p.getContrincante().equals(usuario)) {
                p.GanadaPorRendicion();
            }
        }
    }

    /**
     * Actualiza el historial de partidas después de una victoria.
     * @param ganador El nombre de usuario del jugador ganador.
     * @param perdedor El nombre de usuario del jugador perdedor.
     * @throws IOException si ocurre un error de entrada/salida.
     */
    public static void actualizarHistorial(String ganador, String perdedor) throws IOException {
        printer.printHistorial(ganador, perdedor);
    }

    /**
     * Finaliza todas las partidas en curso.
     * @param usuario El nombre de usuario de un jugador.
     * @param contrincante El nombre de usuario del contrincante.
     */
    public static void PartidasFinalizadas(String usuario, String contrincante) {
        for (TresEnRaya p : partidasEnCurso) {
            System.out.println("Partida entre " + p.getContrincante() + " y " + p.getUsuario() + " Finalizada");
        }
    }

    /**
     * Destruye todas las partidas en curso entre dos jugadores.
     * @param usuario1 El nombre de usuario de un jugador.
     * @param usuario2 El nombre de usuario del otro jugador.
     */
    public static void destroyPartidas(String usuario1, String usuario2) {
        EliminarPartida(usuario1, usuario2);
        EliminarPartida(usuario2, usuario1);
    }
}