package edu.uees.patrones.factory;

/** ConcreteProduct: notificacion por mensaje de texto (SMS). */
public class NotificadorSMS implements Notificador {

    @Override
    public void notificar(String destinatario, String mensaje) {
        System.out.println("[sms -> " + destinatario + "] " + mensaje);
    }
}
