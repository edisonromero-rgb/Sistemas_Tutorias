package edu.uees.tutorias.notification;

import edu.uees.tutorias.domain.Usuario;

/**
 * Implementacion de {@link Notificador} que simula el envio de un
 * correo electronico. En un entorno real delegaria en un cliente SMTP
 * o en un proveedor externo; para esta actividad se deja como una
 * simulacion simple que deja evidencia por consola.
 */
public class NotificadorCorreo implements Notificador {

    @Override
    public void notificar(Usuario destinatario, String mensaje) {
        System.out.println("[correo -> " + destinatario.getCorreo() + "] " + mensaje);
    }
}
