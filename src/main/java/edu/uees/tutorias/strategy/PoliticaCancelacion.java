package edu.uees.tutorias.strategy;

import edu.uees.tutorias.domain.Reserva;

import java.time.LocalDateTime;

/**
 * Strategy: politica que decide si una {@link Reserva} puede cancelarse
 * en un momento dado.
 *
 * Problema real que resuelve: en Ae1/Ae2 cancelar una reserva solo
 * validaba el estado (PENDIENTE/CONFIRMADA -> CANCELADA). El proyecto
 * ahora necesita reglas de negocio adicionales sobre CUANDO se puede
 * cancelar (por ejemplo, con una antelacion minima), y esas reglas
 * cambian con mas frecuencia que el ciclo de vida de la reserva (se
 * ajustan por politica institucional, campana, tipo de curso, etc.).
 * Encapsular la politica detras de este contrato permite intercambiarla
 * sin modificar {@link Reserva} ni
 * {@link edu.uees.tutorias.service.ServicioReservas}.
 */
public interface PoliticaCancelacion {

    /**
     * Valida si {@code reserva} puede cancelarse en el instante
     * {@code ahora}. Lanza {@link CancelacionNoPermitidaException} si la
     * politica no lo permite; no hace nada si lo permite.
     */
    void validar(Reserva reserva, LocalDateTime ahora);
}
