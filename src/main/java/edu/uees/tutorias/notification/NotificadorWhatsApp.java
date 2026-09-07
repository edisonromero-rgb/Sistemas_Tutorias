package edu.uees.tutorias.notification;

import edu.uees.tutorias.domain.Usuario;

/**
 * Implementacion de {@link Notificador} que simula el envio de un
 * mensaje de WhatsApp. Se agrego despues de tener correo, SMS y push
 * funcionando, sin modificar ninguna clase existente (evidencia de
 * extensibilidad / OCP del Factory Method, ver
 * {@link edu.uees.tutorias.notification.factory.NotificadorWhatsAppFactory}).
 */
public class NotificadorWhatsApp implements Notificador {

    @Override
    public void notificar(Usuario destinatario, String mensaje) {
        System.out.println("[whatsapp -> " + destinatario.getNombre() + "] " + mensaje);
    }
}
