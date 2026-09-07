package edu.uees.tutorias.cancelacion;

import edu.uees.tutorias.domain.HorarioDisponible;
import edu.uees.tutorias.domain.Reserva;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Utilidad de paquete que calcula la antelacion entre ahora y el inicio
 * del horario de una reserva. Se comparte entre las politicas para no
 * repetir el mismo calculo (no es una jerarquia de herencia porque las
 * politicas no comparten estado ni comportamiento mas alla de esta
 * cuenta puntual).
 */
final class Antelacion {

    private Antelacion() {
    }

    static void exigirHorasMinimas(Reserva reserva, int horasMinimas, String etiquetaPolitica) {
        HorarioDisponible horario = reserva.getHorario();
        LocalDateTime inicio = LocalDateTime.of(horario.getFecha(), horario.getHoraInicio());
        Duration antelacion = Duration.between(LocalDateTime.now(), inicio);
        if (antelacion.toHours() < horasMinimas) {
            throw new CancelacionNoPermitidaException(
                    "Politica " + etiquetaPolitica + ": la reserva " + reserva.getId()
                            + " requiere al menos " + horasMinimas + "h de antelacion para cancelarse; "
                            + "faltan " + antelacion.toHours() + "h.");
        }
    }
}
