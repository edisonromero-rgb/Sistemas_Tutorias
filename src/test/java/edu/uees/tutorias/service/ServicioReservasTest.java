package edu.uees.tutorias.service;

import edu.uees.tutorias.cancelacion.CancelacionNoPermitidaException;
import edu.uees.tutorias.domain.Docente;
import edu.uees.tutorias.domain.EstadoReserva;
import edu.uees.tutorias.domain.Estudiante;
import edu.uees.tutorias.domain.HorarioDisponible;
import edu.uees.tutorias.domain.Reserva;
import edu.uees.tutorias.domain.ReservaBuilder;
import edu.uees.tutorias.domain.ReservaObserver;
import edu.uees.tutorias.domain.TipoReserva;
import edu.uees.tutorias.repository.ReservaRepository;
import edu.uees.tutorias.repository.ReservaRepositoryMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ServicioReservasTest {

    /**
     * Observer de prueba (test double) que registra los eventos en lugar
     * de notificar de verdad. Es posible gracias a que ReservaObserver es
     * una abstraccion (DIP/OCP): ni Reserva ni ServicioReservas saben que
     * estan usando una implementacion distinta a las de produccion.
     */
    private static class ObservadorEnMemoria implements ReservaObserver {
        final List<String> eventos = new ArrayList<>();

        @Override
        public void onCambioEstado(Reserva reserva, EstadoReserva anterior, EstadoReserva nuevo) {
            eventos.add(reserva.getId() + ": " + anterior + " -> " + nuevo);
        }
    }

    private ReservaRepository repository;
    private ObservadorEnMemoria observador;
    private ServicioReservas servicioReservas;
    private Estudiante estudiante;
    private Docente docente;
    private HorarioDisponible horario;

    @BeforeEach
    void setUp() {
        repository = new ReservaRepositoryMemoria();
        observador = new ObservadorEnMemoria();
        servicioReservas = new ServicioReservas(repository, List.of(observador));

        estudiante = new Estudiante("EST-1", "Ana Perez", "ana@uees.edu.ec", "Software");
        docente = new Docente("DOC-1", "Jaime Sayago", "jsayago@uees.edu.ec", "POO");
        horario = docente.publicarHorario("HOR-1", LocalDate.now().plusDays(10),
                LocalTime.of(9, 0), LocalTime.of(10, 0));
    }

    @Test
    void crearReservaQuedaPendienteYOcupaElHorarioYNotificaAlObservador() {
        Reserva reserva = servicioReservas.crearReserva(estudiante, docente, horario);

        assertEquals(EstadoReserva.PENDIENTE, reserva.getEstado());
        assertFalse(horario.isDisponible());
        assertFalse(observador.eventos.isEmpty());
        assertEquals(TipoReserva.NORMAL, reserva.getTipo());
    }

    @Test
    void noSePuedeReservarUnHorarioYaOcupado() {
        servicioReservas.crearReserva(estudiante, docente, horario);

        Estudiante otroEstudiante = new Estudiante("EST-2", "Luis Diaz", "luis@uees.edu.ec", "Software");

        assertThrows(IllegalStateException.class,
                () -> servicioReservas.crearReserva(otroEstudiante, docente, horario));
    }

    @Test
    void confirmarCambiaEstadoAConfirmada() {
        Reserva reserva = servicioReservas.crearReserva(estudiante, docente, horario);

        servicioReservas.confirmarReserva(reserva.getId());

        assertEquals(EstadoReserva.CONFIRMADA, reserva.getEstado());
    }

    @Test
    void cancelarUnaReservaNormalConAntelacionSuficienteLiberaElHorario() {
        Reserva reserva = servicioReservas.crearReserva(estudiante, docente, horario);

        servicioReservas.cancelarReserva(reserva.getId());

        assertEquals(EstadoReserva.CANCELADA, reserva.getEstado());
        assertTrue(horario.isDisponible());
    }

    @Test
    void cancelarUnaReservaGrupalSinAntelacionSuficienteLanzaExcepcion() {
        HorarioDisponible horarioCercano = docente.publicarHorario("HOR-2",
                LocalDate.now(), LocalTime.now().plusHours(3), LocalTime.now().plusHours(4));
        Reserva reserva = servicioReservas.crearReserva(new ReservaBuilder()
                .estudiante(estudiante)
                .docente(docente)
                .horario(horarioCercano)
                .tipo(TipoReserva.GRUPAL));

        assertThrows(CancelacionNoPermitidaException.class,
                () -> servicioReservas.cancelarReserva(reserva.getId()));
        assertEquals(EstadoReserva.PENDIENTE, reserva.getEstado());
    }

    @Test
    void noSePuedeFinalizarUnaReservaPendiente() {
        Reserva reserva = servicioReservas.crearReserva(estudiante, docente, horario);

        assertThrows(IllegalStateException.class,
                () -> servicioReservas.finalizarReserva(reserva.getId()));
    }

    @Test
    void reprogramarMueveLaReservaAOtroHorarioYLiberaElAnterior() {
        Reserva reserva = servicioReservas.crearReserva(estudiante, docente, horario);
        HorarioDisponible nuevoHorario = docente.publicarHorario("HOR-3", LocalDate.now().plusDays(11),
                LocalTime.of(11, 0), LocalTime.of(12, 0));

        servicioReservas.reprogramarReserva(reserva.getId(), nuevoHorario);

        assertEquals(EstadoReserva.REPROGRAMADA, reserva.getEstado());
        assertTrue(horario.isDisponible());
        assertFalse(nuevoHorario.isDisponible());
        assertEquals(nuevoHorario, reserva.getHorario());
    }

    @Test
    void listarReservasPorEstudianteDelegaEnElRepositorio() {
        servicioReservas.crearReserva(estudiante, docente, horario);

        List<Reserva> reservas = servicioReservas.listarReservasPorEstudiante(estudiante);

        assertEquals(1, reservas.size());
    }
}
