package edu.uees.patrones.factory;

/** ConcreteCreator: fabrica un {@link NotificadorPush}. */
public class NotificadorPushFactory extends NotificadorFactory {

    @Override
    protected Notificador crearNotificador() {
        return new NotificadorPush();
    }
}
