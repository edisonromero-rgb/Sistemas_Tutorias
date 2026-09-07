package edu.uees.tutorias.service;

import edu.uees.tutorias.cancelacion.PoliticaCancelacion;
import edu.uees.tutorias.cancelacion.PoliticaCancelacionProvider;
import edu.uees.tutorias.domain.Docente;
import edu.uees.tutorias.domain.Estudiante;
import edu.uees.tutorias.domain.HorarioDisponible;
import edu.uees.tutorias.domain.Reserva;
import edu.uees.tutorias.domain.ReservaBuilder;
import edu.uees.tutorias.domain.ReservaObserver;
import edu.uees.tutorias.repository.ReservaRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

/**
 * Coordina el proceso de reserva de tutorias.
 *
 * SRP: ServicioReservas no valida reglas del ciclo de vida de una
 * reserva (eso lo hace {@link Reserva}), no persiste datos (eso lo
 * hace {@link ReservaRepository}), no decide como se comunica un
 * evento (eso lo hacen los {@link ReservaObserver} que la reserva
 * notifica) y no decide si una cancelacion es oportuna (eso lo hace la
 * {@link PoliticaCancelacion} correspondiente, Strategy). Su unica
 * responsabilidad es orquestar la colaboracion entre esos objetos.
 *
 * Cambio respecto a Ae1/Ae2: este servicio ya no recibe un
 * {@code Notificador} por constructor. Con el patron Observer, Reserva
 * notifica directamente a sus observadores en cada transicion, y
 * ServicioReservas solo decide con que observadores por defecto se
 * arma cada reserva nueva.
 *
 * DIP: recibe ReservaRepository y la lista de observadores por defecto
 * como abstracciones, por lo que no conoce ni depende de una
 * tecnologia de persistencia o de comunicacion concreta.
 */
public class ServicioReservas {

    private final ReservaRepository repository;
    private final List<ReservaObserver> observadoresPorDefecto;

    public ServicioReservas(ReservaRepository repository, List<ReservaObserver> observadoresPorDefecto) {
        this.repository = Objects.requireNonNull(repository);
        this.observadoresPorDefecto = List.copyOf(Objects.requireNonNull(observadoresPorDefecto));
    }

    /** Crea una reserva solo con los datos obligatorios (resto con valores por defecto). */
    public Reserva crearReserva(Estudiante estudiante, Docente docente, HorarioDisponible horario) {
        return crearReserva(new ReservaBuilder()
                .estudiante(estudiante)
                .docente(docente)
                .horario(horario));
    }

    /**
     * Crea una reserva a partir de un {@link ReservaBuilder} ya
     * configurado (estudiante, docente, horario y, opcionalmente,
     * modalidad, notas, canal preferido, recordatorio y tipo). El
     * servicio agrega los observadores por defecto antes de construir
     * la reserva y la persiste.
     */
    public Reserva crearReserva(ReservaBuilder builder) {
        Objects.requireNonNull(builder, "builder no puede ser nulo");
        for (ReservaObserver observador : observadoresPorDefecto) {
            builder.observador(observador);
        }
        Reserva reserva = builder.build();
        repository.guardar(reserva);
        return reserva;
    }

    public Reserva confirmarReserva(String idReserva) {
        Reserva reserva = obtenerReservaOFallar(idReserva);
        reserva.confirmar();
        return reserva;
    }

    /**
     * Cancela una reserva solo si la {@link PoliticaCancelacion} de su
     * {@link edu.uees.tutorias.domain.TipoReserva} lo permite en este
     * momento (antelacion minima). Lanza
     * {@link edu.uees.tutorias.cancelacion.CancelacionNoPermitidaException}
     * si no se cumple.
     */
    public Reserva cancelarReserva(String idReserva) {
        Reserva reserva = obtenerReservaOFallar(idReserva);
        PoliticaCancelacionProvider.politicaPara(reserva.getTipo()).validar(reserva);
        reserva.cancelar();
        return reserva;
    }

    public Reserva reprogramarReserva(String idReserva, HorarioDisponible nuevoHorario) {
        Reserva reserva = obtenerReservaOFallar(idReserva);
        reserva.reprogramar(nuevoHorario);
        return reserva;
    }

    public Reserva finalizarReserva(String idReserva) {
        Reserva reserva = obtenerReservaOFallar(idReserva);
        reserva.finalizar();
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
