package edu.uees.tutorias.notification;

import edu.uees.tutorias.domain.Usuario;

/**
 * Implementacion de {@link Notificador} que simula el envio de una
 * notificacion push.
 */
public class NotificadorPush implements Notificador {

    @Override
    public void notificar(Usuario destinatario, String mensaje) {
        System.out.println("[push -> " + destinatario.getNombre() + "] " + mensaje);
    }
}
