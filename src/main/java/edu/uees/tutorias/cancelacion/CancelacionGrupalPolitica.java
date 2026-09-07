package edu.uees.tutorias.cancelacion;

import edu.uees.tutorias.domain.Reserva;

/**
 * Reservas GRUPAL: al afectar a varios estudiantes a la vez, exigen al
 * menos 48h de antelacion para cancelarse.
 */
public class CancelacionGrupalPolitica implements PoliticaCancelacion {

    private static final int HORAS_MINIMAS = 48;

    @Override
    public void validar(Reserva reserva) {
        Antelacion.exigirHorasMinimas(reserva, HORAS_MINIMAS, "grupal");
    }
}
