package edu.uees.tutorias.notification;

import edu.uees.tutorias.domain.Usuario;

/** ConcreteProduct: notificacion por mensaje de texto (SMS). */
public class NotificadorSMS implements Notificador {

    @Override
    public void notificar(Usuario destinatario, String mensaje) {
        System.out.println("[sms -> " + destinatario.getNombre() + "] " + mensaje);
    }
}
