package edu.uees.patrones.factory;

/**
 * ConcreteProduct agregado despues, para evidenciar extensibilidad
 * (ver {@link NotificadorWhatsAppFactory}): notificacion por WhatsApp.
 */
public class NotificadorWhatsApp implements Notificador {

    @Override
    public void notificar(String destinatario, String mensaje) {
        System.out.println("[whatsapp -> " + destinatario + "] " + mensaje);
    }
}
