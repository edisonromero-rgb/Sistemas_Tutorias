package edu.uees.tutorias.notification.factory;

import edu.uees.tutorias.domain.Usuario;
import edu.uees.tutorias.notification.Notificador;

/**
 * Creator del patron Factory Method.
 *
 * Declara el metodo fabrica {@link #crearNotificador()}, que cada
 * subclase concreta implementa para decidir que {@link Notificador}
 * instanciar. Esto separa "que se notifica" (contrato Notificador) de
 * "como se decide y crea la implementacion concreta" segun el canal
 * preferido de una reserva (ver {@link NotificadorFactoryProvider}).
 */
public abstract class NotificadorFactory {

    /**
     * Factory Method: cada ConcreteCreator decide la clase concreta de
     * Notificador que se debe instanciar.
     */
    public abstract Notificador crearNotificador();

    /**
     * Metodo plantilla que usa el producto creado por el factory method.
     * No cambia cuando se agregan nuevos canales de notificacion.
     */
    public void enviar(Usuario destinatario, String mensaje) {
        crearNotificador().notificar(destinatario, mensaje);
    }
}
