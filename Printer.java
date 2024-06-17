package servidor;

import java.io.FileWriter;
import java.io.IOException;

/**
 * La clase Printer se encarga de la gestión de archivos para registrar credenciales,
 * puntajes de usuarios y el historial de partidas.
 * 
 * @author Pablo
 * @author Daniel
 */
public class Printer {

    private final String REGISTRO = "db_Credenciales.txt";
    private final String USUARIOS_PUNTOS = "db_UsuariosPuntos.txt";
    private final String HISTORIAL = "db_Historial.txt";

    /**
     * Constructor de la clase Printer.
     */
    public Printer() {
    }

    /**
     * Registra las credenciales de un nuevo usuario en el archivo de credenciales.
     * 
     * @param User el nombre de usuario
     * @param Password la contraseña del usuario
     * @throws IOException si ocurre un error al escribir en el archivo
     */
    public void printRegistro(String User, String Password) throws IOException {
        FileWriter ficheroRegistro = new FileWriter(REGISTRO, true);
        ficheroRegistro.write(User + ";" + Password + "\n");
        ficheroRegistro.close();
    }

    /**
     * Actualiza el archivo de puntajes de usuarios con la información proporcionada.
     * 
     * @param Puntuacion la cadena que contiene los puntajes de los usuarios
     * @throws IOException si ocurre un error al escribir en el archivo
     */
    public void printUsuariosPuntos(String Puntuacion) throws IOException {
        FileWriter ficheroUsuariosPuntos = new FileWriter(USUARIOS_PUNTOS, false);
        ficheroUsuariosPuntos.write(Puntuacion);
        ficheroUsuariosPuntos.close();
    }

    /**
     * Registra el historial de una partida con el ganador y el perdedor.
     * 
     * @param Ganador el nombre del usuario ganador
     * @param Perdedor el nombre del usuario perdedor
     * @throws IOException si ocurre un error al escribir en el archivo
     */
    public void printHistorial(String Ganador, String Perdedor) throws IOException {
        FileWriter ficheroHistorial = new FileWriter(HISTORIAL, true);
        ficheroHistorial.write(Ganador + " vs " + Perdedor + "; Ganador: " + Ganador + "\n");
        ficheroHistorial.close();
    }
}