package Cliente;

import java.net.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;


public class PracticaFinalMTPA {


    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                iniciarLogin();
            }
        });
    }

    private static void iniciarLogin() {
        JFrame frame = new JFrame("Login/Registro");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);

        IU_LoginRegistro panel = new IU_LoginRegistro(frame);
        frame.add(panel);
        frame.setVisible(true);
    }

    public static void iniciarMenu(Socket socket, String username, String[] usuarios) {
        JFrame menuFrame = new JFrame("Main Menu");
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setSize(700, 500);
        menuFrame.setLocationRelativeTo(null);

        IU_Menu mainMenu = new IU_Menu(socket, username, usuarios);
        menuFrame.add(mainMenu);
        menuFrame.setVisible(true);
    }
}
