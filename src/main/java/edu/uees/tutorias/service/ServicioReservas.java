package edu.uees.tutorias.service;

import edu.uees.tutorias.domain.Docente;
import edu.uees.tutorias.domain.Estudiante;
import edu.uees.tutorias.domain.HorarioDisponible;
import edu.uees.tutorias.domain.Reserva;
import edu.uees.tutorias.notification.Notificador;
import edu.uees.tutorias.repository.ReservaRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Coordina el proceso de reserva de tutorias.
 *
 * SRP: ServicioReservas no valida reglas del ciclo de vida de una
 * reserva (eso lo hace {@link Reserva}), no persiste datos (eso lo
 * hace {@link ReservaRepository}) y no decide como se comunica un
 * evento (eso lo hace {@link Notificador}). Su unica responsabilidad
 * es orquestar la colaboracion entre esos objetos.
 *
 * DIP: recibe ReservaRepository y Notificador como abstracciones por
 * constructor, por lo que no conoce ni depende de una tecnologia de
 * persistencia o de comunicacion concreta.
 */
public class ServicioReservas {

    private final ReservaRepository repository;
    private final Notificador notificador;

    public ServicioReservas(ReservaRepository repository, Notificador notificador) {
        this.repository = Objects.requireNonNull(repository);
        this.notificador = Objects.requireNonNull(notificador);
    }

    public Reserva crearReserva(Estudiante estudiante, Docente docente, HorarioDisponible horario) {
        Reserva reserva = new Reserva(UUID.randomUUID().toString(), estudiante, docente, horario);
        repository.guardar(reserva);
        notificador.notificar(estudiante, "Reserva " + reserva.getId() + " creada, pendiente de confirmacion.");
        notificador.notificar(docente, "Nueva solicitud de tutoria de " + estudiante.getNombre() + ".");
        return reserva;
    }

    public Reserva confirmarReserva(String idReserva) {
        Reserva reserva = obtenerReservaOFallar(idReserva);
        reserva.confirmar();
        notificador.notificar(reserva.getEstudiante(), "Tu reserva " + idReserva + " fue confirmada.");
        return reserva;
    }

    public Reserva cancelarReserva(String idReserva) {
        Reserva reserva = obtenerReservaOFallar(idReserva);
        reserva.cancelar();
        notificador.notificar(reserva.getEstudiante(), "Tu reserva " + idReserva + " fue cancelada.");
        notificador.notificar(reserva.getDocente(), "La reserva " + idReserva + " fue cancelada.");
        return reserva;
    }

    public Reserva reprogramarReserva(String idReserva, HorarioDisponible nuevoHorario) {
        Reserva reserva = obtenerReservaOFallar(idReserva);
        reserva.reprogramar(nuevoHorario);
        notificador.notificar(reserva.getEstudiante(), "Tu reserva " + idReserva + " fue reprogramada.");
        return reserva;
    }

    public Reserva finalizarReserva(String idReserva) {
        Reserva reserva = obtenerReservaOFallar(idReserva);
        reserva.finalizar();
        notificador.notificar(reserva.getEstudiante(), "Tu tutoria " + idReserva + " ha finalizado.");
        return reserva;
    }

    public List<Reserva> listarReservasPorEstudiante(Estudiante estudiante) {
        return repository.listarPorEstudiante(estudiante);
    }

    public List<Reserva> listarTodas() {
        return repository.listarTodas();
    }

    private Reserva obtenerReservaOFallar(String idReserva) {
        Optional<Reserva> reserva = repository.buscarPorId(idReserva);
        return reserva.orElseThrow(() ->
                new java.util.NoSuchElementException("No existe una reserva con id " + idReserva));
    }
}
