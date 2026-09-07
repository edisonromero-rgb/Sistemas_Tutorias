package edu.uees.tutorias.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReservaBuilderTest {

    private final Estudiante estudiante = new Estudiante("EST-1", "Ana Perez", "ana@uees.edu.ec", "Software");
    private final Docente docente = new Docente("DOC-1", "Jaime Sayago", "jsayago@uees.edu.ec", "POO");

    private HorarioDisponible horario(String id) {
        return docente.publicarHorario(id, LocalDate.now().plusDays(5), LocalTime.of(9, 0), LocalTime.of(10, 0));
    }

    @Test
    void construyeReservaSoloConCamposObligatorios() {
        Reserva reserva = new ReservaBuilder()
                .estudiante(estudiante)
                .docente(docente)
                .horario(horario("HOR-1"))
                .build();

        assertEquals(estudiante, reserva.getEstudiante());
        assertEquals(docente, reserva.getDocente());
    }

    @Test
    void aplicaValoresPorDefectoParaCamposOpcionales() {
        Reserva reserva = new ReservaBuilder()
                .estudiante(estudiante)
                .docente(docente)
                .horario(horario("HOR-2"))
                .build();

        assertEquals(Modalidad.PRESENCIAL, reserva.getModalidad());
        assertEquals(CanalNotificacion.EMAIL, reserva.getCanalNotificacionPreferido());
        assertEquals("", reserva.getNotas());
        assertFalse(reserva.isRecordatorioActivado());
        assertEquals(TipoReserva.NORMAL, reserva.getTipo());
    }

    @Test
    void sobrescribeValoresPorDefectoCuandoSeIndicanExplicitamente() {
        Reserva reserva = new ReservaBuilder()
                .estudiante(estudiante)
                .docente(docente)
                .horario(horario("HOR-3"))
                .modalidad(Modalidad.VIRTUAL)
                .canalNotificacionPreferido(CanalNotificacion.SMS)
                .tipo(TipoReserva.PRIORITARIA)
                .conRecordatorio()
                .build();

        assertEquals(Modalidad.VIRTUAL, reserva.getModalidad());
        assertEquals(CanalNotificacion.SMS, reserva.getCanalNotificacionPreferido());
        assertEquals(TipoReserva.PRIORITARIA, reserva.getTipo());
        assertTrue(reserva.isRecordatorioActivado());
    }

    @Test
    void lanzaExcepcionSiFaltaUnCampoObligatorio() {
        ReservaBuilder builder = new ReservaBuilder()
                .estudiante(estudiante); // faltan docente y horario

        IllegalStateException ex = assertThrows(IllegalStateException.class, builder::build);
        assertTrue(ex.getMessage().contains("docente"));
        assertTrue(ex.getMessage().contains("horario"));
    }

    @Test
    void registraLosObservadoresIndicadosAntesDeConstruir() {
        List<String> eventos = new ArrayList<>();
        ReservaObserver observador = (reserva, anterior, nuevo) -> eventos.add(anterior + "->" + nuevo);

        Reserva reserva = new ReservaBuilder()
                .estudiante(estudiante)
                .docente(docente)
                .horario(horario("HOR-4"))
                .observador(observador)
                .build();

        assertEquals(1, reserva.getObservadores().size());
        assertEquals(List.of("null->PENDIENTE"), eventos);
    }
}
