package tictactoe.practicafinalmtpa;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Servidor {

    private ServerSocket listenSocket;
    private List<ClientHandler> clients;
    private List<String> connectedUsers; // Lista de usuarios conectados

    public Servidor(int port) {
        try {
            listenSocket = new ServerSocket(port);
            clients = new CopyOnWriteArrayList<>();
            connectedUsers = new CopyOnWriteArrayList<>();
        } catch (IOException e) {
            System.out.println("Error initializing server: " + e.getMessage());
        }
    }

    public void start() {
        System.out.println("Server started...");
        while (true) {
            try {
                Socket clientSocket = listenSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                clients.add(clientHandler);
                clientHandler.start();
            } catch (IOException e) {
                System.out.println("Error accepting client connection: " + e.getMessage());
            }
        }
    }

    public synchronized void broadcast(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    public synchronized void addClient(String username) {
        connectedUsers.add(username);
        broadcastUserList();
    }

    public synchronized void removeClient(ClientHandler clientHandler, String username) {
        clients.remove(clientHandler);
        connectedUsers.remove(username);
        broadcastUserList();
    }

    private void broadcastUserList() {
        String userList = String.join(",", connectedUsers);
        broadcast(userList);
        broadcast("Fin");
    }

    public static void main(String[] args) {
        final int SERVERPORT = 6789;
        Servidor server = new Servidor(SERVERPORT);
        server.start();
    }
}

class ClientHandler extends Thread {

    private Socket clientSocket;
    private DataInputStream in;
    private DataOutputStream out;
    private Servidor server;
    private String username;
    private String db_credenciales = "db_Credenciales.txt";

    public ClientHandler(Socket socket, Servidor server) {
        this.clientSocket = socket;
        this.server = server;
        try {
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Error setting up client handler: " + e.getMessage());
        }
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    @Override
    public void run() {
        try {
            out.writeUTF("comunciacion_establecida");
            String opcion = in.readUTF();

            switch (opcion) {
                case "Registro":
                    handleRegistration();
                    break;
                case "Login":
                    handleLogin();
                    break;
                default:
                    out.writeUTF("opcion_invalida");
                    break;
            }
        } catch (IOException e) {
            System.out.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Error closing client socket: " + e.getMessage());
            }
            if (username != null) {
                server.removeClient(this, username);
            }
        }
    }

    private void handleRegistration() throws IOException {
        String usuario_registro = in.readUTF();
        String contrasena_registro = in.readUTF();

        try (BufferedReader br = new BufferedReader(new FileReader(db_credenciales))) {
            String linea;
            boolean usuarioExistente = false;

            while ((linea = br.readLine()) != null) {
                String[] credenciales = linea.split(";");
                if (credenciales.length > 0 && credenciales[0].equals(usuario_registro)) {
                    usuarioExistente = true;
                    break;
                }
            }

            if (usuarioExistente) {
                out.writeUTF("fallo_registro");
            } else {
                try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(db_credenciales, true)))) {
                    pw.println(usuario_registro + ";" + contrasena_registro);
                    out.writeUTF("login_correcto");
                    username = usuario_registro;
                    server.addClient(username);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading credentials file: " + e.getMessage());
            out.writeUTF("fallo_registro");
        }
    }

    private void handleLogin() throws IOException {
        String usuario_login = in.readUTF();
        String contrasena_login = in.readUTF();

        try (BufferedReader br = new BufferedReader(new FileReader(db_credenciales))) {
            String linea;
            boolean encontrado = false;

            while ((linea = br.readLine()) != null) {
                String[] credenciales = linea.split(";");
                if (credenciales.length > 1 && credenciales[0].equals(usuario_login) && credenciales[1].equals(contrasena_login)) {
                    encontrado = true;
                    break;
                }
            }

            if (encontrado) {
                out.writeUTF("login_correcto");
                username = usuario_login;
                server.addClient(username);
            } else {
                out.writeUTF("fallo_login");
            }
        } catch (IOException e) {
            System.out.println("Error reading credentials file: " + e.getMessage());
            out.writeUTF("fallo_login");
        }
    }

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            System.out.println("Error sending message to client: " + e.getMessage());
        }
    }
}
