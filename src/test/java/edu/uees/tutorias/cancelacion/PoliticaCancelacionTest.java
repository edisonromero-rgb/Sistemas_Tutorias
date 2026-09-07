package edu.uees.tutorias.cancelacion;

import edu.uees.tutorias.domain.Docente;
import edu.uees.tutorias.domain.Estudiante;
import edu.uees.tutorias.domain.HorarioDisponible;
import edu.uees.tutorias.domain.Reserva;
import edu.uees.tutorias.domain.ReservaBuilder;
import edu.uees.tutorias.domain.TipoReserva;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PoliticaCancelacionTest {

    private final Estudiante estudiante = new Estudiante("EST-1", "Ana Perez", "ana@uees.edu.ec", "Software");
    private final Docente docente = new Docente("DOC-1", "Jaime Sayago", "jsayago@uees.edu.ec", "POO");

    private Reserva reservaConHorarioEnHoras(String idHorario, TipoReserva tipo, long horasDesdeAhora) {
        LocalDateTime inicio = LocalDateTime.now().plusHours(horasDesdeAhora);
        HorarioDisponible horario = docente.publicarHorario(idHorario, inicio.toLocalDate(),
                inicio.toLocalTime(), inicio.toLocalTime().plusHours(1));
        return new ReservaBuilder()
                .estudiante(estudiante)
                .docente(docente)
                .horario(horario)
                .tipo(tipo)
                .build();
    }

    @Test
    void politicaNormalRechazaCancelacionConMenosDe24h() {
        Reserva reserva = reservaConHorarioEnHoras("HOR-1", TipoReserva.NORMAL, 5);

        assertThrows(CancelacionNoPermitidaException.class,
                () -> new CancelacionNormalPolitica().validar(reserva));
    }

    @Test
    void politicaPrioritariaPermiteCancelarConSoloDosHoras() {
        Reserva reserva = reservaConHorarioEnHoras("HOR-2", TipoReserva.PRIORITARIA, 3);

        assertDoesNotThrow(() -> new CancelacionPrioritariaPolitica().validar(reserva));
    }

    @Test
    void politicaGrupalExigeAlMenos48h() {
        Reserva reserva = reservaConHorarioEnHoras("HOR-3", TipoReserva.GRUPAL, 5);

        assertThrows(CancelacionNoPermitidaException.class,
                () -> new CancelacionGrupalPolitica().validar(reserva));
    }

    @Test
    void providerResuelveLaPoliticaSegunElTipo() {
        assertDoesNotThrow(() -> PoliticaCancelacionProvider.politicaPara(TipoReserva.NORMAL));
        assertDoesNotThrow(() -> PoliticaCancelacionProvider.politicaPara(TipoReserva.GRUPAL));
    }
}
