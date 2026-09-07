package edu.uees.tutorias.notification.factory;

import edu.uees.tutorias.notification.Notificador;
import edu.uees.tutorias.notification.NotificadorCorreo;

public class NotificadorCorreoFactory extends NotificadorFactory {

    @Override
    public Notificador crearNotificador() {
        return new NotificadorCorreo();
    }
}
