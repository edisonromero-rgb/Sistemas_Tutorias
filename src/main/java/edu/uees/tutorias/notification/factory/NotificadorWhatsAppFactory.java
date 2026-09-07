package edu.uees.tutorias.notification.factory;

import edu.uees.tutorias.notification.Notificador;
import edu.uees.tutorias.notification.NotificadorWhatsApp;

/**
 * ConcreteCreator agregado despues de tener correo, SMS y push
 * funcionando, sin modificar NotificadorFactory ni el codigo cliente
 * que ya dependia de ella (OCP).
 */
public class NotificadorWhatsAppFactory extends NotificadorFactory {

    @Override
    public Notificador crearNotificador() {
        return new NotificadorWhatsApp();
    }
}
