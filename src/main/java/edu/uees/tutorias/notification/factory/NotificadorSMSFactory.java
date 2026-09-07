package edu.uees.tutorias.notification.factory;

import edu.uees.tutorias.notification.Notificador;
import edu.uees.tutorias.notification.NotificadorSMS;

public class NotificadorSMSFactory extends NotificadorFactory {

    @Override
    public Notificador crearNotificador() {
        return new NotificadorSMS();
    }
}
