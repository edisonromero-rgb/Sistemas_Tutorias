package edu.uees.tutorias.domain;

/**
 * Canal de notificacion preferido para una {@link Reserva}. Es un
 * concepto del dominio (una preferencia de contacto); la decision de
 * qué {@code Notificador} concreto corresponde a cada canal es una
 * responsabilidad de infraestructura que vive en el paquete
 * {@code notification} (ver {@code NotificadorFactoryProvider}), de
 * modo que el dominio no depende de esa capa.
 */
public enum CanalNotificacion {
    EMAIL,
    SMS,
    PUSH
}
