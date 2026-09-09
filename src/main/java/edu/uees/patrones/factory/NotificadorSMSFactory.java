package edu.uees.patrones.factory;

/** ConcreteCreator: fabrica un {@link NotificadorSMS}. */
public class NotificadorSMSFactory extends NotificadorFactory {

    @Override
    protected Notificador crearNotificador() {
        return new NotificadorSMS();
    }
}
