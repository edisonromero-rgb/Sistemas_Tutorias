package edu.uees.tutorias.domain;

/**
 * Estados posibles del ciclo de vida de una {@link Reserva}.
 *
 * El propio enum documenta las transiciones permitidas, de modo que
 * la logica de validacion en Reserva se apoye en un contrato explicito
 * en lugar de comparar cadenas de texto.
 */
public enum EstadoReserva {
    PENDIENTE,
    CONFIRMADA,
    CANCELADA,
    REPROGRAMADA,
    FINALIZADA
}
