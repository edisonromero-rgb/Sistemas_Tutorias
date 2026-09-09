package edu.uees.tutorias.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReservaBuilderTest {

    private final Estudiante estudiante = new Estudiante("EST-1", "Ana Perez", "ana@uees.edu.ec", "Software");
    private final Docente docente = new Docente("DOC-1", "Jaime Sayago", "jsayago@uees.edu.ec", "POO");

    @Test
    void construyeReservaSoloConCamposObligatorios() {
        HorarioDisponible horario = docente.publicarHorario("H1", LocalDate.now().plusDays(1),
                LocalTime.of(9, 0), LocalTime.of(10, 0));

        Reserva reserva = new ReservaBuilder()
                .estudiante(estudiante).docente(docente).horario(horario)
                .build();

        assertEquals(estudiante, reserva.getEstudiante());
        assertEquals(docente, reserva.getDocente());
    }

    @Test
    void aplicaValoresPorDefectoParaCamposOpcionales() {
        HorarioDisponible horario = docente.publicarHorario("H2", LocalDate.now().plusDays(1),
                LocalTime.of(9, 0), LocalTime.of(10, 0));

        Reserva reserva = new ReservaBuilder()
                .estudiante(estudiante).docente(docente).horario(horario)
                .build();

        assertEquals(Modalidad.PRESENCIAL, reserva.getModalidad());
        assertEquals(CanalNotificacion.EMAIL, reserva.getCanalNotificacion());
        assertEquals("", reserva.getNotas());
        assertFalse(reserva.isRecordatorioActivado());
    }

    @Test
    void sobrescribeValoresPorDefectoCuandoSeIndicanExplicitamente() {
        HorarioDisponible horario = docente.publicarHorario("H3", LocalDate.now().plusDays(1),
                LocalTime.of(9, 0), LocalTime.of(10, 0));

        Reserva reserva = new ReservaBuilder()
                .estudiante(estudiante).docente(docente).horario(horario)
                .modalidad(Modalidad.VIRTUAL)
                .canalNotificacion(CanalNotificacion.SMS)
                .conRecordatorio()
                .build();

        assertEquals(Modalidad.VIRTUAL, reserva.getModalidad());
        assertEquals(CanalNotificacion.SMS, reserva.getCanalNotificacion());
        assertTrue(reserva.isRecordatorioActivado());
    }

    @Test
    void lanzaExcepcionSiFaltaUnCampoObligatorio() {
        ReservaBuilder builder = new ReservaBuilder().estudiante(estudiante); // faltan docente y horario

        IllegalStateException ex = assertThrows(IllegalStateException.class, builder::build);
        assertTrue(ex.getMessage().contains("docente"));
        assertTrue(ex.getMessage().contains("horario"));
    }
}
