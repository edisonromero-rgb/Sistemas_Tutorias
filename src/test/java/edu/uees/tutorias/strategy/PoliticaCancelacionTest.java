package edu.uees.tutorias.strategy;

import edu.uees.tutorias.domain.Docente;
import edu.uees.tutorias.domain.Estudiante;
import edu.uees.tutorias.domain.HorarioDisponible;
import edu.uees.tutorias.domain.Reserva;
import edu.uees.tutorias.domain.ReservaBuilder;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PoliticaCancelacionTest {

    private final Estudiante estudiante = new Estudiante("EST-1", "Ana Perez", "ana@uees.edu.ec", "Software");
    private final Docente docente = new Docente("DOC-1", "Jaime Sayago", "jsayago@uees.edu.ec", "POO");

    private Reserva nuevaReserva(LocalDate fecha, LocalTime inicio, LocalTime fin) {
        HorarioDisponible horario = docente.publicarHorario("H-" + fecha + inicio, fecha, inicio, fin);
        return new ReservaBuilder().estudiante(estudiante).docente(docente).horario(horario).build();
    }

    @Test
    void cancelacionLibreNuncaRechaza() {
        Reserva reserva = nuevaReserva(LocalDate.now().plusDays(1), LocalTime.of(9, 0), LocalTime.of(10, 0));

        assertDoesNotThrow(() -> new CancelacionLibre().validar(reserva, LocalDateTime.now()));
    }

    @Test
    void antelacionMinimaPermiteCancelarConTiempoSuficiente() {
        LocalDate fecha = LocalDate.now().plusDays(1);
        Reserva reserva = nuevaReserva(fecha, LocalTime.of(9, 0), LocalTime.of(10, 0));
        PoliticaCancelacion politica = new CancelacionConAntelacionMinima(Duration.ofHours(2));

        LocalDateTime conTiempoDeSobra = LocalDateTime.of(fecha, LocalTime.of(9, 0)).minusHours(3);

        assertDoesNotThrow(() -> politica.validar(reserva, conTiempoDeSobra));
    }

    @Test
    void antelacionMinimaRechazaCancelacionTardia() {
        LocalDate fecha = LocalDate.now().plusDays(1);
        Reserva reserva = nuevaReserva(fecha, LocalTime.of(9, 0), LocalTime.of(10, 0));
        PoliticaCancelacion politica = new CancelacionConAntelacionMinima(Duration.ofHours(2));

        LocalDateTime muyCerca = LocalDateTime.of(fecha, LocalTime.of(9, 0)).minusMinutes(10);

        assertThrows(CancelacionNoPermitidaException.class, () -> politica.validar(reserva, muyCerca));
    }
}
