package edu.uees.patrones.factory;

/**
 * Creator del patron Factory Method.
 *
 * Declara el metodo fabrica {@link #crearNotificador()}, que cada
 * subclase concreta implementa para decidir que {@link Notificador}
 * instanciar. El metodo plantilla {@link #enviar(String, String)} usa
 * esa fabrica sin conocer la clase concreta que finalmente se crea: el
 * codigo cliente solo depende de esta abstraccion.
 */
public abstract class NotificadorFactory {

    /**
     * Factory Method: cada ConcreteCreator decide la clase concreta de
     * Notificador que se debe instanciar.
     */
    protected abstract Notificador crearNotificador();

    /**
     * Operacion del Creator que utiliza el producto creado por el factory
     * method. No cambia cuando se agregan nuevos canales de notificacion.
     */
    public void enviar(String destinatario, String mensaje) {
        Notificador notificador = crearNotificador();
        notificador.notificar(destinatario, mensaje);
    }
}
