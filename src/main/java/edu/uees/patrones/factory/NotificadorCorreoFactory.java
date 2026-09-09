package edu.uees.patrones.factory;

/** ConcreteCreator: fabrica un {@link NotificadorCorreo}. */
public class NotificadorCorreoFactory extends NotificadorFactory {

    @Override
    protected Notificador crearNotificador() {
        return new NotificadorCorreo();
    }
}
