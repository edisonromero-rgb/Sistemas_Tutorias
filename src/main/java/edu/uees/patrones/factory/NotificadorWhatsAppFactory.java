package edu.uees.patrones.factory;

/**
 * ConcreteCreator agregado para incorporar el canal WhatsApp sin
 * modificar {@link Notificador}, {@link NotificadorFactory} ni ninguno
 * de los ConcreteCreators existentes (Correo, SMS, Push). Es la
 * evidencia de extensibilidad pedida en la actividad: extender el
 * sistema significa agregar dos clases nuevas, no tocar las que ya
 * funcionan (principio abierto/cerrado).
 */
public class NotificadorWhatsAppFactory extends NotificadorFactory {

    @Override
    protected Notificador crearNotificador() {
        return new NotificadorWhatsApp();
    }
}
