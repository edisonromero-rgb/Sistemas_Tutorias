package edu.uees.patrones.factory;

/** ConcreteProduct: notificacion push (aplicacion movil). */
public class NotificadorPush implements Notificador {

    @Override
    public void notificar(String destinatario, String mensaje) {
        System.out.println("[push -> " + destinatario + "] " + mensaje);
    }
}
