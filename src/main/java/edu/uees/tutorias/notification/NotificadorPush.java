package edu.uees.tutorias.notification;

import edu.uees.tutorias.domain.Usuario;

/** ConcreteProduct: notificacion push (aplicacion movil). */
public class NotificadorPush implements Notificador {

    @Override
    public void notificar(Usuario destinatario, String mensaje) {
        System.out.println("[push -> " + destinatario.getNombre() + "] " + mensaje);
    }
}
