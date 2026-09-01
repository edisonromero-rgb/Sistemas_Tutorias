package edu.uees.patrones.factory;

/** ConcreteProduct: notificacion por correo electronico. */
public class NotificadorCorreo implements Notificador {

    @Override
    public void notificar(String destinatario, String mensaje) {
        System.out.println("[correo -> " + destinatario + "] " + mensaje);
    }
}
