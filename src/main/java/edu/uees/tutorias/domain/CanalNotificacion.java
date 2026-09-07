package edu.uees.tutorias.domain;

/**
 * Canal de notificacion preferido para una {@link Reserva}.
 *
 * Lo usa {@link edu.uees.tutorias.notification.factory.NotificadorFactoryProvider}
 * (Factory Method) para decidir que implementacion de
 * {@link edu.uees.tutorias.notification.Notificador} debe crearse cuando un
 * observador reacciona a un cambio de estado de la reserva.
 */
public enum CanalNotificacion {
    EMAIL,
    SMS,
    PUSH,
    WHATSAPP
}
