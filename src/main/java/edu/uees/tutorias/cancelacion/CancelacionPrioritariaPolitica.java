package edu.uees.tutorias.cancelacion;

import edu.uees.tutorias.domain.Reserva;

/**
 * Reservas PRIORITARIA: al ser un servicio con mayor flexibilidad,
 * solo exigen 2h de antelacion para cancelarse.
 */
public class CancelacionPrioritariaPolitica implements PoliticaCancelacion {

    private static final int HORAS_MINIMAS = 2;

    @Override
    public void validar(Reserva reserva) {
        Antelacion.exigirHorasMinimas(reserva, HORAS_MINIMAS, "prioritaria");
    }
}
