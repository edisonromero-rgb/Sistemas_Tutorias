package edu.uees.tutorias.observer;

import edu.uees.tutorias.domain.Docente;
import edu.uees.tutorias.domain.Estudiante;
import edu.uees.tutorias.domain.HorarioDisponible;
import edu.uees.tutorias.domain.Reserva;
import edu.uees.tutorias.domain.ReservaBuilder;
import edu.uees.tutorias.domain.ReservaEvento;
import edu.uees.tutorias.domain.TipoEvento;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas del Subject/Publisher del patron Observer. No dependen de
 * ServicioReservas: publican eventos directamente para verificar que
 * el publisher reparte cada evento a todos sus suscriptores y que
 * desuscribir() detiene la entrega, sin que los observers se conozcan
 * entre si.
 */
class ReservaPublisherTest {

    private static class ObserverDePrueba implements ReservaObserver {
        final List<ReservaEvento> recibidos = new ArrayList<>();

        @Override
        public void actualizar(ReservaEvento evento) {
            recibidos.add(evento);
        }
    }

    private Reserva nuevaReserva() {
        Estudiante estudiante = new Estudiante("EST-1", "Ana Perez", "ana@uees.edu.ec", "Software");
        Docente docente = new Docente("DOC-1", "Jaime Sayago", "jsayago@uees.edu.ec", "POO");
        HorarioDisponible horario = docente.publicarHorario("H1", LocalDate.now().plusDays(1),
                LocalTime.of(9, 0), LocalTime.of(10, 0));
        return new ReservaBuilder().estudiante(estudiante).docente(docente).horario(horario).build();
    }

    @Test
    void publicarEntregaElEventoATodosLosSuscriptores() {
        ReservaPublisher publisher = new ReservaPublisher();
        ObserverDePrueba observer1 = new ObserverDePrueba();
        ObserverDePrueba observer2 = new ObserverDePrueba();
        publisher.suscribir(observer1);
        publisher.suscribir(observer2);

        ReservaEvento evento = new ReservaEvento(TipoEvento.CREADA, nuevaReserva());
        publisher.publicar(evento);

        assertEquals(List.of(evento), observer1.recibidos);
        assertEquals(List.of(evento), observer2.recibidos);
    }

    @Test
    void desuscribirDetieneLaEntregaDeNuevosEventos() {
        ReservaPublisher publisher = new ReservaPublisher();
        ObserverDePrueba observer = new ObserverDePrueba();
        publisher.suscribir(observer);

        publisher.publicar(new ReservaEvento(TipoEvento.CREADA, nuevaReserva()));
        publisher.desuscribir(observer);
        publisher.publicar(new ReservaEvento(TipoEvento.CONFIRMADA, nuevaReserva()));

        assertEquals(1, observer.recibidos.size());
        assertEquals(TipoEvento.CREADA, observer.recibidos.get(0).getTipo());
    }

    @Test
    void auditoriaYEstadisticasReaccionanAlMismoEventoSinConocerseEntreSi() {
        ReservaPublisher publisher = new ReservaPublisher();
        AuditoriaObserver auditoria = new AuditoriaObserver();
        EstadisticasObserver estadisticas = new EstadisticasObserver();
        publisher.suscribir(auditoria);
        publisher.suscribir(estadisticas);

        Reserva reserva = nuevaReserva();
        publisher.publicar(new ReservaEvento(TipoEvento.CREADA, reserva));
        publisher.publicar(new ReservaEvento(TipoEvento.CREADA, reserva));
        publisher.publicar(new ReservaEvento(TipoEvento.CANCELADA, reserva));

        assertEquals(2, estadisticas.getConteo(TipoEvento.CREADA));
        assertEquals(1, estadisticas.getConteo(TipoEvento.CANCELADA));
        assertEquals(3, auditoria.getBitacora().size());
        assertTrue(auditoria.getBitacora().get(0).contains("CREADA"));
    }
}
