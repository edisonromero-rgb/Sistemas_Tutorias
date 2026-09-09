package edu.uees.tutorias.domain;

/**
 * Tipos de evento que puede publicar una {@link Reserva} a lo largo de
 * su ciclo de vida. Es el "hecho estable" del patron Observer: los
 * interesados (correo, auditoria, estadisticas, ...) pueden variar,
 * pero el catalogo de eventos que ocurren en una reserva no cambia con
 * cada canal o receptor nuevo.
 */
public enum TipoEvento {
    CREADA,
    CONFIRMADA,
    CANCELADA,
    REPROGRAMADA,
    FINALIZADA
}
