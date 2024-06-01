

package tictactoe.practicafinalmtpa;

import java.io.*;
import java.net.*;
<<<<<<< HEAD
import java.util.*;
=======
import java.util.logging.Level;
import java.util.logging.Logger;
>>>>>>> e28981b61e2190f5fbe6943ba54b06e908385fb2

/**
 *
 * @author pablo
 */
public class Servidor {
    public static void main(String args[]){
        ServerSocket listenSocket = null;
        final int SERVERPORT = 6789;
        String db_credenciales = "db_Credenciales.txt";
        
        try{
            listenSocket = new ServerSocket(SERVERPORT);
            while(true){
                Socket clientSocket = listenSocket.accept();
                
                Connection c = new Connection(clientSocket, db_credenciales);
                
            }
        }catch(IOException ex){
            
        }
    }
}

class Connection extends Thread{
    
    DataInputStream in;
    DataOutputStream out;
    String db_credenciales;
    
<<<<<<< HEAD
    Socket clientSocket;    
=======
    Socket clientSocket;

    
>>>>>>> e28981b61e2190f5fbe6943ba54b06e908385fb2
    
    public Connection(Socket aClientSocket, String db_credenciales){
        try {
            this.db_credenciales = db_credenciales;
            clientSocket = aClientSocket;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            
            this.start();
<<<<<<< HEAD
            
=======
>>>>>>> e28981b61e2190f5fbe6943ba54b06e908385fb2
            out.writeUTF("comunciacion_establecida");
        } catch (IOException ex) {
            System.out.println("Connection:" + ex.getMessage());
        }
        
    }
    public void run(){
        try {
            String opcion = in.readUTF();
<<<<<<< HEAD
=======
            
>>>>>>> e28981b61e2190f5fbe6943ba54b06e908385fb2
            switch(opcion){
                case "Registro":
                    //Cuando recibe esta opcion, obtiene el usuario y la contraseña y la escribe en un archivo cuyo nombre está en una variable "db_credenciales"
                    
                    String usuario_registro = in.readUTF();
                    String contrasena_registro = in.readUTF();
                    try{
                        FileReader fr = new FileReader(db_credenciales);
                        BufferedReader br = new BufferedReader(fr);
                        String linea;
                        boolean usuarioExistente = false;
                        while((linea = br.readLine()) != null){
                            String[] credenciales = linea.split(";");
                            if(credenciales[0].equals(usuario_registro)){
                                usuarioExistente = true;
                                break;
                            }
                        }
                        br.close();
                        fr.close();
                        
                        if(usuarioExistente){
                            out.writeUTF("fallo_registro");
                        }else{
                            FileWriter fw = new FileWriter(db_credenciales, true);
                            BufferedWriter bw = new BufferedWriter(fw);
                            PrintWriter pw = new PrintWriter(bw);
                            pw.println(usuario_registro + ";" + contrasena_registro);
                            
                            out.writeUTF("login_correcto");
                            
                            pw.close();
                            bw.close();
                            fw.close();
                               
                        }
                    }catch(IOException ex){
                        System.out.println("Error al leer el archivo de credenciales");
                    }
                    break;
                case "Login":
                    //Cuando recibe esta opcion, obtiene el usuario y la contraseña y las compara con las que están en el archivo cuyo nombre está en una variable "db_credenciales"
                    String usuario_login = in.readUTF();
                    String contrasena_login = in.readUTF();
                    
                    try{
                        FileReader fr = new FileReader(db_credenciales);
                        BufferedReader br = new BufferedReader(fr);
                        String linea;
                        boolean encontrado = false;
                        while((linea = br.readLine()) != null){
                            String[] credenciales = linea.split(";");
                            if(credenciales[0].equals(usuario_login) && credenciales[1].equals(contrasena_login)){
                                encontrado = true;
                                break;
                            }
                        }
                        if(encontrado){
                            out.writeUTF("login_correcto");
<<<<<<< HEAD
                            //Crea un cliente
                            //Le dice al cliente que instancie el menu
=======
>>>>>>> e28981b61e2190f5fbe6943ba54b06e908385fb2
                        }else{
                            out.writeUTF("fallo_login");
                        }
                        br.close();
                        fr.close();
                    }catch(IOException ex){
                        System.out.println("Error al leer el archivo de credenciales");
                    }
                    
                    break;
                default:
                    break;
            }
        } catch (IOException ex) {
            System.out.println("Error al recibir la operacion ");
        }
    }
}