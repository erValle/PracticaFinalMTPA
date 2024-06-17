package servidor;

import java.nio.charset.Charset;

/**
 * La clase Mensaje crea un mensaje con una acción y dos campos.
 * 
 * @author Pablo
 * @author Daniel
 */
public class Mensaje {

    private String accion;
    private String campo1;
    private String campo2;

    /**
     * Constructor por defecto de la clase Mensaje.
     */
    public Mensaje() {}

    /**
     * Crea un mensaje con los parámetros especificados.
     *
     * @param _accion La acción del mensaje.
     * @param _campo1 El primer campo del mensaje.
     * @param _campo2 El segundo campo del mensaje.
     * @return Un nuevo objeto Mensaje con los parámetros dados.
     */
    public static Mensaje CrearMensajeConParametros(String _accion, String _campo1, String _campo2) {
        Mensaje msg = new Mensaje();
        msg.setAccion(_accion);
        msg.setCampo1(_campo1);
        msg.setCampo2(_campo2);
        return msg;
    }

    /**
     * Crea un mensaje a partir de un buffer de bytes.
     *
     * @param buffer El buffer de bytes que contiene los datos del mensaje.
     * @return Un nuevo objeto Mensaje creado a partir del buffer.
     */
    public static Mensaje CrearMensaje(byte[] buffer) {
        Mensaje msg = null;
        String aux = new String(buffer, Charset.forName("UTF-8"));
        String tokens[] = aux.split("@");
        try {
            msg = new Mensaje();
            msg.setAccion(tokens[0]);
            msg.setCampo1(tokens[1]);
            msg.setCampo2(tokens[2]);
        } catch (Exception ex) {
            System.out.println("Error");
        }
        return msg;
    }

    /**
     * Convierte el mensaje en una cadena de texto.
     *
     * @return Una cadena de texto que representa el mensaje en el formato "accion@campo1@campo2".
     */
    @Override
    public String toString() {
        return this.accion + "@" + this.campo1 + "@" + this.campo2;
    }

    /**
     * Establece la acción del mensaje.
     *
     * @param accion La acción a establecer.
     */
    public void setAccion(String accion) {
        this.accion = accion;
    }

    /**
     * Obtiene la acción del mensaje.
     *
     * @return La acción del mensaje.
     */
    public String getAccion() {
        return accion;
    }

    /**
     * Obtiene el primer campo del mensaje.
     *
     * @return El primer campo del mensaje.
     */
    public String getCampo1() {
        return campo1;
    }

    /**
     * Establece el primer campo del mensaje.
     *
     * @param campo1 El primer campo a establecer.
     */
    public void setCampo1(String campo1) {
        this.campo1 = campo1;
    }

    /**
     * Obtiene el segundo campo del mensaje.
     *
     * @return El segundo campo del mensaje.
     */
    public String getCampo2() {
        return campo2;
    }

    /**
     * Establece el segundo campo del mensaje.
     *
     * @param campo2 El segundo campo a establecer.
     */
    public void setCampo2(String campo2) {
        this.campo2 = campo2;
    }
}