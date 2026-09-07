package edu.uees.tutorias.observer;

import edu.uees.tutorias.domain.EstadoReserva;
import edu.uees.tutorias.domain.Reserva;
import edu.uees.tutorias.domain.ReservaObserver;
import edu.uees.tutorias.notification.factory.NotificadorFactory;
import edu.uees.tutorias.notification.factory.NotificadorFactoryProvider;

/**
 * ConcreteObserver que traduce cada cambio de estado en un mensaje y lo
 * envia por el canal preferido de la reserva.
 *
 * Reutiliza el Factory Method de notificaciones: no decide "como" se
 * envia un mensaje, solo "cuando" y "que" mensaje corresponde a cada
 * estado; {@link NotificadorFactoryProvider} decide la implementacion
 * concreta segun {@link Reserva#getCanalNotificacionPreferido()}. Esto
 * reemplaza las llamadas directas a Notificador que antes hacia
 * ServicioReservas despues de cada operacion.
 */
public class ObservadorNotificaciones implements ReservaObserver {

    @Override
    public void onCambioEstado(Reserva reserva, EstadoReserva anterior, EstadoReserva nuevo) {
        NotificadorFactory factory = NotificadorFactoryProvider.factoryPara(reserva.getCanalNotificacionPreferido());

        if (anterior == null) {
            factory.enviar(reserva.getEstudiante(),
                    "Reserva " + reserva.getId() + " creada, pendiente de confirmacion.");
            factory.enviar(reserva.getDocente(),
                    "Nueva solicitud de tutoria de " + reserva.getEstudiante().getNombre() + ".");
            return;
        }

        switch (nuevo) {
            case CONFIRMADA -> factory.enviar(reserva.getEstudiante(),
                    "Tu reserva " + reserva.getId() + " fue confirmada.");
            case CANCELADA -> {
                factory.enviar(reserva.getEstudiante(), "Tu reserva " + reserva.getId() + " fue cancelada.");
                factory.enviar(reserva.getDocente(), "La reserva " + reserva.getId() + " fue cancelada.");
            }
            case REPROGRAMADA -> factory.enviar(reserva.getEstudiante(),
                    "Tu reserva " + reserva.getId() + " fue reprogramada.");
            case FINALIZADA -> factory.enviar(reserva.getEstudiante(),
                    "Tu tutoria " + reserva.getId() + " ha finalizado.");
            default -> { /* PENDIENTE ya se maneja en la creacion */ }
        }
    }
}
