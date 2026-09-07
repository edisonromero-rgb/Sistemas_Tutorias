package edu.uees.tutorias.cancelacion;

import edu.uees.tutorias.domain.Reserva;

/**
 * Strategy del patron Strategy.
 *
 * Problema que resuelve: la antelacion minima para cancelar una
 * tutoria sin penalizacion varia segun el {@link edu.uees.tutorias.domain.TipoReserva}
 * (normal, prioritaria, grupal), y se espera que esas reglas cambien
 * con el tiempo (por ejemplo, si cambia la politica comercial). Antes
 * de este patron, esa regla tendria que vivir en un if/else dentro de
 * ServicioReservas o de Reserva, mezclando una politica de negocio
 * variable con la coordinacion del caso de uso o con el ciclo de vida
 * de la reserva.
 *
 * Con Strategy, {@link edu.uees.tutorias.service.ServicioReservas}
 * delega la validacion en la politica correspondiente
 * (ver {@link PoliticaCancelacionProvider}) sin conocer sus reglas
 * concretas; agregar un tipo de reserva nuevo no obliga a modificar el
 * servicio ni las politicas existentes.
 */
public interface PoliticaCancelacion {

    /**
     * Valida si {@code reserva} puede cancelarse en este momento segun
     * esta politica. Lanza {@link CancelacionNoPermitidaException} si no
     * cumple la antelacion minima exigida.
     */
    void validar(Reserva reserva);
}
