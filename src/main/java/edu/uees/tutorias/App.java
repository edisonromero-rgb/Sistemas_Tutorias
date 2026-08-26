package edu.uees.tutorias;

import edu.uees.tutorias.domain.Docente;
import edu.uees.tutorias.domain.Estudiante;
import edu.uees.tutorias.domain.HorarioDisponible;
import edu.uees.tutorias.domain.Reserva;
import edu.uees.tutorias.notification.Notificador;
import edu.uees.tutorias.notification.NotificadorCorreo;
import edu.uees.tutorias.repository.ReservaRepository;
import edu.uees.tutorias.repository.ReservaRepositoryMemoria;
import edu.uees.tutorias.service.ServicioReservas;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Punto de entrada de demostracion. Muestra el flujo principal del
 * sistema: publicar un horario, crear una reserva, confirmarla y
 * finalmente finalizarla.
 *
 * No forma parte del dominio: es una capa de composicion que arma
 * las dependencias concretas (ReservaRepositoryMemoria,
 * NotificadorCorreo) y las inyecta en ServicioReservas.
 */
public class App {

    public static void main(String[] args) {
        ReservaRepository repository = new ReservaRepositoryMemoria();
        Notificador notificador = new NotificadorCorreo();
        ServicioReservas servicioReservas = new ServicioReservas(repository, notificador);

        Estudiante estudiante = new Estudiante("EST-1", "Ana Perez", "ana.perez@uees.edu.ec", "Ingenieria de Software");
        Docente docente = new Docente("DOC-1", "Jaime Sayago", "jsayago@uees.edu.ec", "Programacion Orientada a Objetos");

        HorarioDisponible horario = docente.publicarHorario(
                "HOR-1", LocalDate.now().plusDays(1), LocalTime.of(10, 0), LocalTime.of(11, 0));

        Reserva reserva = servicioReservas.crearReserva(estudiante, docente, horario);
        System.out.println("Estado tras crear: " + reserva.getEstado());

        servicioReservas.confirmarReserva(reserva.getId());
        System.out.println("Estado tras confirmar: " + reserva.getEstado());

        servicioReservas.finalizarReserva(reserva.getId());
        System.out.println("Estado tras finalizar: " + reserva.getEstado());
    }
}
