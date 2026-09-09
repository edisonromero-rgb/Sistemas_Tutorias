package edu.uees.tutorias.service;

import edu.uees.tutorias.domain.Docente;
import edu.uees.tutorias.domain.EstadoReserva;
import edu.uees.tutorias.domain.Estudiante;
import edu.uees.tutorias.domain.HorarioDisponible;
import edu.uees.tutorias.domain.Reserva;
import edu.uees.tutorias.notification.Notificador;
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
     * Notificador de prueba (test double) que registra los mensajes en
     * lugar de enviarlos. Es posible gracias a que Notificador es una
     * abstraccion (DIP/OCP): el servicio no sabe que esta usando una
     * implementacion distinta a NotificadorCorreo.
     */
    private static class NotificadorEnMemoria implements Notificador {
        final List<String> mensajes = new ArrayList<>();

        @Override
        public void notificar(edu.uees.tutorias.domain.Usuario destinatario, String mensaje) {
            mensajes.add(destinatario.getId() + ": " + mensaje);
        }
    }

    private ReservaRepository repository;
    private NotificadorEnMemoria notificador;
    private ServicioReservas servicioReservas;
    private Estudiante estudiante;
    private Docente docente;
    private HorarioDisponible horario;

    @BeforeEach
    void setUp() {
        repository = new ReservaRepositoryMemoria();
        notificador = new NotificadorEnMemoria();
        servicioReservas = new ServicioReservas(repository, notificador);

        estudiante = new Estudiante("EST-1", "Ana Perez", "ana@uees.edu.ec", "Software");
        docente = new Docente("DOC-1", "Jaime Sayago", "jsayago@uees.edu.ec", "POO");
        horario = docente.publicarHorario("HOR-1", LocalDate.now().plusDays(1),
                LocalTime.of(9, 0), LocalTime.of(10, 0));
    }

    @Test
    void crearReservaQuedaPendienteYOcupaElHorario() {
        Reserva reserva = servicioReservas.crearReserva(estudiante, docente, horario);

        assertEquals(EstadoReserva.PENDIENTE, reserva.getEstado());
        assertFalse(horario.isDisponible());
        assertFalse(notificador.mensajes.isEmpty());
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
    void cancelarLiberaElHorario() {
        Reserva reserva = servicioReservas.crearReserva(estudiante, docente, horario);

        servicioReservas.cancelarReserva(reserva.getId());

        assertEquals(EstadoReserva.CANCELADA, reserva.getEstado());
        assertTrue(horario.isDisponible());
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
        HorarioDisponible nuevoHorario = docente.publicarHorario("HOR-2", LocalDate.now().plusDays(2),
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
