package edu.uees.tutorias.strategy;

import edu.uees.tutorias.domain.Reserva;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * ConcreteStrategy: exige una antelacion minima respecto al inicio del
 * horario reservado. Por ejemplo, "no se puede cancelar con menos de 2
 * horas de anticipacion".
 */
public class CancelacionConAntelacionMinima implements PoliticaCancelacion {

    private final Duration antelacionMinima;

    public CancelacionConAntelacionMinima(Duration antelacionMinima) {
        this.antelacionMinima = Objects.requireNonNull(antelacionMinima);
    }

    @Override
    public void validar(Reserva reserva, LocalDateTime ahora) {
        LocalDateTime inicioHorario = LocalDateTime.of(
                reserva.getHorario().getFecha(), reserva.getHorario().getHoraInicio());
        Duration restante = Duration.between(ahora, inicioHorario);
        if (restante.compareTo(antelacionMinima) < 0) {
            throw new CancelacionNoPermitidaException(
                    "La reserva " + reserva.getId() + " no puede cancelarse: faltan " + restante.toMinutes()
                            + " min. para el horario y la antelacion minima exigida es "
                            + antelacionMinima.toMinutes() + " min.");
        }
    }
}
