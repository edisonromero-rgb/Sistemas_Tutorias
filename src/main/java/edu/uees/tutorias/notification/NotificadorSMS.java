package edu.uees.tutorias.notification;

import edu.uees.tutorias.domain.Usuario;

/**
 * Implementacion de {@link Notificador} que simula el envio de un SMS.
 */
public class NotificadorSMS implements Notificador {

    @Override
    public void notificar(Usuario destinatario, String mensaje) {
        System.out.println("[sms -> " + destinatario.getNombre() + "] " + mensaje);
    }
}
