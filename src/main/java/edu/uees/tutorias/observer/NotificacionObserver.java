package edu.uees.tutorias.observer;

import edu.uees.tutorias.domain.Reserva;
import edu.uees.tutorias.domain.ReservaEvento;
import edu.uees.tutorias.notification.NotificadorFactory;
import edu.uees.tutorias.notification.NotificadorFactoryProvider;

import java.util.Objects;

/**
 * ConcreteObserver que traduce eventos de reserva en notificaciones.
 *
 * Aqui se conectan dos patrones: Observer decide QUE reacciona a un
 * evento; Factory Method (via {@link NotificadorFactoryProvider})
 * decide COMO se envia el mensaje segun el canal preferido de la
 * reserva. ServicioReservas no conoce ninguno de los dos.
 */
public class NotificacionObserver implements ReservaObserver {

    private final NotificadorFactoryProvider factoryProvider;

    public NotificacionObserver(NotificadorFactoryProvider factoryProvider) {
        this.factoryProvider = Objects.requireNonNull(factoryProvider);
    }

    @Override
    public void actualizar(ReservaEvento evento) {
        Reserva reserva = evento.getReserva();
        NotificadorFactory factory = factoryProvider.factoryPara(reserva.getCanalNotificacion());

        switch (evento.getTipo()) {
            case CREADA -> {
                factory.enviar(reserva.getEstudiante(),
                        "Reserva " + reserva.getId() + " creada, pendiente de confirmacion.");
                factory.enviar(reserva.getDocente(),
                        "Nueva solicitud de tutoria de " + reserva.getEstudiante().getNombre() + ".");
            }
            case CONFIRMADA -> factory.enviar(reserva.getEstudiante(),
                    "Tu reserva " + reserva.getId() + " fue confirmada.");
            case CANCELADA -> {
                factory.enviar(reserva.getEstudiante(),
                        "Tu reserva " + reserva.getId() + " fue cancelada.");
                factory.enviar(reserva.getDocente(),
                        "La reserva " + reserva.getId() + " fue cancelada.");
            }
            case REPROGRAMADA -> factory.enviar(reserva.getEstudiante(),
                    "Tu reserva " + reserva.getId() + " fue reprogramada.");
            case FINALIZADA -> factory.enviar(reserva.getEstudiante(),
                    "Tu tutoria " + reserva.getId() + " ha finalizado.");
        }
    }
}
