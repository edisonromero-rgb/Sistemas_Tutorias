package edu.uees.tutorias.notification;

import edu.uees.tutorias.domain.Usuario;

/**
 * Creator del patron Factory Method (recuperado y re-justificado de
 * Ae2). Declara el metodo fabrica {@link #crearNotificador()}, que
 * cada subclase concreta implementa para decidir que {@link Notificador}
 * instanciar. El metodo plantilla {@link #enviar(Usuario, String)} usa
 * esa fabrica sin conocer la clase concreta que finalmente se crea.
 *
 * Por que sigue justificado en el incremento 1: en Ae1 el sistema solo
 * enviaba correos y ServicioReservas dependia de un unico Notificador
 * inyectado. Ahora cada Reserva puede tener un canal de notificacion
 * preferido distinto (ver {@link edu.uees.tutorias.domain.CanalNotificacion}),
 * y el numero de canales seguira creciendo. {@link NotificadorFactoryProvider}
 * elige la fabrica correcta segun el canal, y agregar un canal nuevo
 * solo requiere una clase Product + una clase Creator, sin tocar
 * ServicioReservas ni los observers que ya usan esta abstraccion.
 */
public abstract class NotificadorFactory {

    /**
     * Factory Method: cada ConcreteCreator decide la clase concreta de
     * Notificador que se debe instanciar.
     */
    protected abstract Notificador crearNotificador();

    /**
     * Operacion del Creator que utiliza el producto creado por el
     * factory method. No cambia cuando se agregan nuevos canales.
     */
    public void enviar(Usuario destinatario, String mensaje) {
        Notificador notificador = crearNotificador();
        notificador.notificar(destinatario, mensaje);
    }
}
