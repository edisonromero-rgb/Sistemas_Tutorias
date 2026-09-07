package edu.uees.tutorias.observer;

import edu.uees.tutorias.domain.EstadoReserva;
import edu.uees.tutorias.domain.Reserva;
import edu.uees.tutorias.domain.ReservaObserver;

/**
 * ConcreteObserver que simula la sincronizacion de una reserva con un
 * calendario externo: crea el evento cuando se confirma y lo retira
 * cuando se cancela o reprograma. No conoce a los demas observadores
 * (por ejemplo {@link ObservadorNotificaciones}); Reserva los notifica
 * a todos por igual.
 */
public class ObservadorCalendario implements ReservaObserver {

    @Override
    public void onCambioEstado(Reserva reserva, EstadoReserva anterior, EstadoReserva nuevo) {
        switch (nuevo) {
            case CONFIRMADA -> System.out.println(
                    "[calendario] evento creado para " + reserva.getId() + " (" + reserva.getHorario() + ")");
            case CANCELADA -> System.out.println(
                    "[calendario] evento retirado para " + reserva.getId());
            case REPROGRAMADA -> System.out.println(
                    "[calendario] evento movido para " + reserva.getId() + " (" + reserva.getHorario() + ")");
            default -> { /* sin accion de calendario para otros estados */ }
        }
    }
}
