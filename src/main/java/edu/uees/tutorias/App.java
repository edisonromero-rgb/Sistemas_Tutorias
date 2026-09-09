package edu.uees.tutorias;

import edu.uees.tutorias.domain.CanalNotificacion;
import edu.uees.tutorias.domain.Docente;
import edu.uees.tutorias.domain.Estudiante;
import edu.uees.tutorias.domain.HorarioDisponible;
import edu.uees.tutorias.domain.Modalidad;
import edu.uees.tutorias.domain.Reserva;
import edu.uees.tutorias.domain.ReservaBuilder;
import edu.uees.tutorias.domain.TipoEvento;
import edu.uees.tutorias.notification.NotificadorFactoryProvider;
import edu.uees.tutorias.observer.AuditoriaObserver;
import edu.uees.tutorias.observer.EstadisticasObserver;
import edu.uees.tutorias.observer.NotificacionObserver;
import edu.uees.tutorias.observer.ReservaPublisher;
import edu.uees.tutorias.repository.ReservaRepository;
import edu.uees.tutorias.repository.ReservaRepositoryMemoria;
import edu.uees.tutorias.service.ServicioReservas;
import edu.uees.tutorias.strategy.CancelacionConAntelacionMinima;
import edu.uees.tutorias.strategy.CancelacionNoPermitidaException;
import edu.uees.tutorias.strategy.PoliticaCancelacion;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Punto de entrada de demostracion del incremento 1 (Ae3).
 *
 * No forma parte del dominio: es la capa de composicion que arma las
 * dependencias concretas -repositorio, publisher con sus observers,
 * proveedor de fabricas de notificacion y politica de cancelacion- y
 * las inyecta en ServicioReservas. Es el unico lugar del proyecto que
 * conoce todas las implementaciones concretas a la vez.
 */
public class App {

    public static void main(String[] args) {
        ReservaRepository repository = new ReservaRepositoryMemoria();

        // Factory Method: registro de canales de notificacion disponibles.
        NotificadorFactoryProvider factoryProvider = NotificadorFactoryProvider.conCanalesPorDefecto();

        // Observer: el publisher no conoce a ServicioReservas ni viceversa;
        // solo se conocen a traves de ReservaEvento.
        AuditoriaObserver auditoria = new AuditoriaObserver();
        EstadisticasObserver estadisticas = new EstadisticasObserver();
        ReservaPublisher publisher = new ReservaPublisher();
        publisher.suscribir(new NotificacionObserver(factoryProvider));
        publisher.suscribir(auditoria);
        publisher.suscribir(estadisticas);

        // Strategy: exige 2 horas de antelacion para cancelar.
        PoliticaCancelacion politicaCancelacion = new CancelacionConAntelacionMinima(Duration.ofHours(2));

        ServicioReservas servicioReservas = new ServicioReservas(repository, publisher, politicaCancelacion);

        Estudiante ana = new Estudiante("EST-1", "Ana Perez", "ana.perez@uees.edu.ec", "Ingenieria de Software");
        Estudiante luis = new Estudiante("EST-2", "Luis Diaz", "luis.diaz@uees.edu.ec", "Ingenieria de Software");
        Docente docente = new Docente("DOC-1", "Jaime Sayago", "jsayago@uees.edu.ec", "Programacion Orientada a Objetos");

        System.out.println("== Reserva 1: campos obligatorios (Builder con valores por defecto) ==");
        HorarioDisponible horario1 = docente.publicarHorario(
                "HOR-1", LocalDate.now().plusDays(1), LocalTime.of(10, 0), LocalTime.of(11, 0));
        Reserva reserva1 = servicioReservas.crearReserva(ana, docente, horario1);
        System.out.println("Estado tras crear: " + reserva1.getEstado()
                + " | canal=" + reserva1.getCanalNotificacion() + " | modalidad=" + reserva1.getModalidad());

        servicioReservas.confirmarReserva(reserva1.getId());
        System.out.println("Estado tras confirmar: " + reserva1.getEstado());

        System.out.println("\n== Reserva 2: Builder con canal SMS, modalidad virtual y recordatorio ==");
        HorarioDisponible horario2 = docente.publicarHorario(
                "HOR-2", LocalDate.now().plusDays(2), LocalTime.of(9, 0), LocalTime.of(10, 0));
        Reserva reserva2 = servicioReservas.crearReserva(new ReservaBuilder()
                .estudiante(luis)
                .docente(docente)
                .horario(horario2)
                .modalidad(Modalidad.VIRTUAL)
                .canalNotificacion(CanalNotificacion.SMS)
                .notas("Traer avance del proyecto")
                .conRecordatorio());
        System.out.println("Estado tras crear: " + reserva2.getEstado()
                + " | canal=" + reserva2.getCanalNotificacion() + " | modalidad=" + reserva2.getModalidad());

        System.out.println("\n== Strategy: intentar cancelar reserva1 sin antelacion suficiente ==");
        try {
            LocalDateTime muyCercaDelHorario = LocalDateTime.of(horario1.getFecha(), horario1.getHoraInicio())
                    .minusMinutes(30);
            servicioReservas.cancelarReserva(reserva1.getId(), muyCercaDelHorario);
        } catch (CancelacionNoPermitidaException ex) {
            System.out.println("Cancelacion rechazada (esperado): " + ex.getMessage());
        }

        System.out.println("\n== Reprogramar y finalizar reserva1 ==");
        HorarioDisponible horario3 = docente.publicarHorario(
                "HOR-3", LocalDate.now().plusDays(3), LocalTime.of(11, 0), LocalTime.of(12, 0));
        servicioReservas.reprogramarReserva(reserva1.getId(), horario3);
        servicioReservas.confirmarReserva(reserva1.getId());
        servicioReservas.finalizarReserva(reserva1.getId());
        System.out.println("Estado final reserva1: " + reserva1.getEstado());

        System.out.println("\n== Observer: estadisticas acumuladas ==");
        for (TipoEvento tipo : TipoEvento.values()) {
            System.out.println(tipo + " -> " + estadisticas.getConteo(tipo));
        }

        System.out.println("\n== Observer: bitacora de auditoria ==");
        auditoria.getBitacora().forEach(System.out::println);
    }
}
