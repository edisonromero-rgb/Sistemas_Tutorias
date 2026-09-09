package edu.uees.tutorias.service;

import edu.uees.tutorias.domain.Docente;
import edu.uees.tutorias.domain.Estudiante;
import edu.uees.tutorias.domain.HorarioDisponible;
import edu.uees.tutorias.domain.Reserva;
import edu.uees.tutorias.domain.ReservaBuilder;
import edu.uees.tutorias.domain.ReservaEvento;
import edu.uees.tutorias.domain.TipoEvento;
import edu.uees.tutorias.observer.ReservaPublisher;
import edu.uees.tutorias.repository.ReservaRepository;
import edu.uees.tutorias.strategy.PoliticaCancelacion;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

/**
 * Coordina el proceso de reserva de tutorias.
 *
 * SRP: ServicioReservas no valida reglas del ciclo de vida de una
 * reserva (eso lo hace {@link Reserva}), no persiste datos (eso lo
 * hace {@link ReservaRepository}), no decide si una cancelacion es
 * oportuna (eso lo hace {@link PoliticaCancelacion}) y, desde este
 * incremento, tampoco decide quien debe enterarse de un cambio ni como
 * se le notifica (eso lo hace {@link ReservaPublisher} junto a sus
 * observers). Su unica responsabilidad es orquestar la colaboracion
 * entre esos objetos y publicar los eventos correspondientes.
 *
 * DIP: recibe ReservaRepository, ReservaPublisher y PoliticaCancelacion
 * como abstracciones por constructor.
 *
 * Cambio respecto a Ae1/Ae2: este servicio ya NO depende de Notificador
 * ni de NotificadorFactory directamente. Esa responsabilidad se movio a
 * {@code NotificacionObserver} (un suscriptor mas de ReservaPublisher),
 * lo que redujo -no aumento- el numero de dependencias de esta clase.
 */
public class ServicioReservas {

    private final ReservaRepository repository;
    private final ReservaPublisher publisher;
    private final PoliticaCancelacion politicaCancelacion;

    public ServicioReservas(ReservaRepository repository, ReservaPublisher publisher,
                             PoliticaCancelacion politicaCancelacion) {
        this.repository = Objects.requireNonNull(repository);
        this.publisher = Objects.requireNonNull(publisher);
        this.politicaCancelacion = Objects.requireNonNull(politicaCancelacion);
    }

    /** Atajo para el caso simple: solo los campos obligatorios. */
    public Reserva crearReserva(Estudiante estudiante, Docente docente, HorarioDisponible horario) {
        return crearReserva(new ReservaBuilder().estudiante(estudiante).docente(docente).horario(horario));
    }

    /** Permite configurar tambien los campos opcionales (Builder). */
    public Reserva crearReserva(ReservaBuilder builder) {
        Reserva reserva = builder.build();
        repository.guardar(reserva);
        publisher.publicar(new ReservaEvento(TipoEvento.CREADA, reserva));
        return reserva;
    }

    public Reserva confirmarReserva(String idReserva) {
        Reserva reserva = obtenerReservaOFallar(idReserva);
        reserva.confirmar();
        publisher.publicar(new ReservaEvento(TipoEvento.CONFIRMADA, reserva));
        return reserva;
    }

    /** Cancela usando la hora actual real. */
    public Reserva cancelarReserva(String idReserva) {
        return cancelarReserva(idReserva, LocalDateTime.now());
    }

    /** Sobrecarga que permite inyectar "ahora" (pruebas deterministas). */
    public Reserva cancelarReserva(String idReserva, LocalDateTime ahora) {
        Reserva reserva = obtenerReservaOFallar(idReserva);
        politicaCancelacion.validar(reserva, ahora);
        reserva.cancelar();
        publisher.publicar(new ReservaEvento(TipoEvento.CANCELADA, reserva));
        return reserva;
    }

    public Reserva reprogramarReserva(String idReserva, HorarioDisponible nuevoHorario) {
        Reserva reserva = obtenerReservaOFallar(idReserva);
        reserva.reprogramar(nuevoHorario);
        publisher.publicar(new ReservaEvento(TipoEvento.REPROGRAMADA, reserva));
        return reserva;
    }

    public Reserva finalizarReserva(String idReserva) {
        Reserva reserva = obtenerReservaOFallar(idReserva);
        reserva.finalizar();
        publisher.publicar(new ReservaEvento(TipoEvento.FINALIZADA, reserva));
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
                new NoSuchElementException("No existe una reserva con id " + idReserva));
    }
}
