package edu.uees.tutorias.strategy;

import edu.uees.tutorias.domain.Reserva;

import java.time.LocalDateTime;

/**
 * ConcreteStrategy: no impone restricciones de tiempo. Es la politica
 * equivalente al comportamiento que tenia el sistema en Ae1/Ae2 (solo
 * el estado de la reserva decide si se puede cancelar).
 */
public class CancelacionLibre implements PoliticaCancelacion {

    @Override
    public void validar(Reserva reserva, LocalDateTime ahora) {
        // Sin restriccion adicional: la propia Reserva ya valida el estado.
    }
}
