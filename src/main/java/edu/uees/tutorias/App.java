package edu.uees.tutorias;

import edu.uees.tutorias.cancelacion.CancelacionNoPermitidaException;
import edu.uees.tutorias.domain.CanalNotificacion;
import edu.uees.tutorias.domain.Docente;
import edu.uees.tutorias.domain.Estudiante;
import edu.uees.tutorias.domain.HorarioDisponible;
import edu.uees.tutorias.domain.Reserva;
import edu.uees.tutorias.domain.ReservaBuilder;
import edu.uees.tutorias.domain.ReservaObserver;
import edu.uees.tutorias.domain.TipoReserva;
import edu.uees.tutorias.observer.ObservadorCalendario;
import edu.uees.tutorias.observer.ObservadorNotificaciones;
import edu.uees.tutorias.observer.ObservadorPanelAdministrativo;
import edu.uees.tutorias.repository.ReservaRepository;
import edu.uees.tutorias.repository.ReservaRepositoryMemoria;
import edu.uees.tutorias.service.ServicioReservas;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Punto de entrada de demostracion del incremento 1. Muestra el flujo
 * completo: publicar un horario, construir una reserva con
 * ReservaBuilder (Builder), confirmarla, intentar cancelarla
 * incumpliendo la politica de su tipo (Strategy) y finalmente
 * cancelarla dentro del plazo permitido; a lo largo del flujo, los
 * observadores (Observer) reaccionan a cada cambio de estado sin que
 * ServicioReservas los invoque explicitamente.
 *
 * No forma parte del dominio: es una capa de composicion que arma las
 * dependencias concretas (repositorio en memoria, observadores) y las
 * inyecta en ServicioReservas.
 */
public class App {

    public static void main(String[] args) {
        ReservaRepository repository = new ReservaRepositoryMemoria();
        ObservadorPanelAdministrativo panel = new ObservadorPanelAdministrativo();
        List<ReservaObserver> observadoresPorDefecto = List.of(
                new ObservadorNotificaciones(),
                new ObservadorCalendario(),
                panel);

        ServicioReservas servicioReservas = new ServicioReservas(repository, observadoresPorDefecto);

        Estudiante estudiante = new Estudiante("EST-1", "Ana Perez", "ana.perez@uees.edu.ec", "Ingenieria de Software");
        Docente docente = new Docente("DOC-1", "Jaime Sayago", "jsayago@uees.edu.ec", "Programacion Orientada a Objetos");

        HorarioDisponible horarioGrupal = docente.publicarHorario(
                "HOR-1", LocalDate.now().plusDays(1), LocalTime.of(10, 0), LocalTime.of(11, 0));

        // Builder: reserva grupal con canal de notificacion preferido SMS.
        Reserva reserva = servicioReservas.crearReserva(new ReservaBuilder()
                .estudiante(estudiante)
                .docente(docente)
                .horario(horarioGrupal)
                .tipo(TipoReserva.GRUPAL)
                .canalNotificacionPreferido(CanalNotificacion.SMS)
                .conRecordatorio());
        System.out.println("Estado tras crear: " + reserva.getEstado());

        servicioReservas.confirmarReserva(reserva.getId());
        System.out.println("Estado tras confirmar: " + reserva.getEstado());

        // Strategy: una reserva GRUPAL exige 48h de antelacion; el horario
        // es en 1 dia, asi que la politica debe rechazar la cancelacion.
        try {
            servicioReservas.cancelarReserva(reserva.getId());
        } catch (CancelacionNoPermitidaException ex) {
            System.out.println("Cancelacion rechazada: " + ex.getMessage());
        }

        // Una reserva NORMAL con horario lejano si cumple la antelacion minima.
        HorarioDisponible horarioNormal = docente.publicarHorario(
                "HOR-2", LocalDate.now().plusDays(10), LocalTime.of(15, 0), LocalTime.of(16, 0));
        Reserva reservaNormal = servicioReservas.crearReserva(new ReservaBuilder()
                .estudiante(estudiante)
                .docente(docente)
                .horario(horarioNormal)
                .tipo(TipoReserva.NORMAL));
        servicioReservas.cancelarReserva(reservaNormal.getId());
        System.out.println("Estado reserva normal tras cancelar: " + reservaNormal.getEstado());

        System.out.println("Bitacora del panel administrativo:");
        panel.getBitacora().forEach(linea -> System.out.println("  " + linea));
    }
}
