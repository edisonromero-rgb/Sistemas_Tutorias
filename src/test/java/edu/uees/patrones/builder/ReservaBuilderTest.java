package edu.uees.patrones.builder;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReservaBuilderTest {

    @Test
    void construyeReservaSoloConCamposObligatorios() {
        Reserva reserva = new ReservaBuilder()
                .estudiante("Ana Perez")
                .docente("Jaime Sayago")
                .horario(LocalDate.now().plusDays(1), LocalTime.of(9, 0), LocalTime.of(10, 0))
                .build();

        assertEquals("Ana Perez", reserva.getEstudiante());
        assertEquals("Jaime Sayago", reserva.getDocente());
    }

    @Test
    void aplicaValoresPorDefectoParaCamposOpcionales() {
        Reserva reserva = new ReservaBuilder()
                .estudiante("Ana Perez")
                .docente("Jaime Sayago")
                .horario(LocalDate.now().plusDays(1), LocalTime.of(9, 0), LocalTime.of(10, 0))
                .build();

        assertEquals(Modalidad.PRESENCIAL, reserva.getModalidad());
        assertEquals(CanalNotificacion.EMAIL, reserva.getCanalNotificacion());
        assertEquals("", reserva.getNotas());
        assertFalse(reserva.isRecordatorioActivado());
    }

    @Test
    void sobrescribeValoresPorDefectoCuandoSeIndicanExplicitamente() {
        Reserva reserva = new ReservaBuilder()
                .estudiante("Luis Diaz")
                .docente("Jaime Sayago")
                .horario(LocalDate.now().plusDays(1), LocalTime.of(9, 0), LocalTime.of(10, 0))
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
        ReservaBuilder builder = new ReservaBuilder()
                .estudiante("Ana Perez"); // faltan docente y horario

        IllegalStateException ex = assertThrows(IllegalStateException.class, builder::build);
        assertTrue(ex.getMessage().contains("docente"));
        assertTrue(ex.getMessage().contains("horario"));
    }

    @Test
    void lanzaExcepcionSiHoraInicioNoEsAnteriorAHoraFin() {
        ReservaBuilder builder = new ReservaBuilder()
                .estudiante("Ana Perez")
                .docente("Jaime Sayago")
                .horario(LocalDate.now().plusDays(1), LocalTime.of(11, 0), LocalTime.of(10, 0));

        assertThrows(IllegalStateException.class, builder::build);
    }
}
