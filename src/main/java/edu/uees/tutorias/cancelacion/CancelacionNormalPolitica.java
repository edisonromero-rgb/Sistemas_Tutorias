package edu.uees.tutorias.cancelacion;

import edu.uees.tutorias.domain.Reserva;

/** Reservas NORMAL: exigen al menos 24h de antelacion para cancelarse. */
public class CancelacionNormalPolitica implements PoliticaCancelacion {

    private static final int HORAS_MINIMAS = 24;

    @Override
    public void validar(Reserva reserva) {
        Antelacion.exigirHorasMinimas(reserva, HORAS_MINIMAS, "normal");
    }
}
