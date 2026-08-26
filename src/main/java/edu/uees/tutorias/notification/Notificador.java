package edu.uees.tutorias.notification;

import edu.uees.tutorias.domain.Usuario;

/**
 * Contrato para comunicar eventos relevantes a un usuario.
 *
 * Polimorfismo / OCP: nuevas formas de notificar (SMS, push, etc.)
 * se agregan implementando esta interfaz, sin modificar
 * ServicioReservas ni las clases del dominio.
 */
public interface Notificador {

    void notificar(Usuario destinatario, String mensaje);
}
