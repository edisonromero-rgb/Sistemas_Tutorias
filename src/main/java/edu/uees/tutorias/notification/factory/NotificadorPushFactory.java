package edu.uees.tutorias.notification.factory;

import edu.uees.tutorias.notification.Notificador;
import edu.uees.tutorias.notification.NotificadorPush;

public class NotificadorPushFactory extends NotificadorFactory {

    @Override
    public Notificador crearNotificador() {
        return new NotificadorPush();
    }
}
