package edu.uees.tutorias.domain;

/**
 * Tipo de reserva. Determina que politica de cancelacion (Strategy,
 * ver {@link edu.uees.tutorias.cancelacion.PoliticaCancelacion}) se le
 * aplica: cada tipo exige una antelacion minima distinta para cancelar
 * sin penalizacion.
 */
public enum TipoReserva {
    NORMAL,
    PRIORITARIA,
    GRUPAL
}
