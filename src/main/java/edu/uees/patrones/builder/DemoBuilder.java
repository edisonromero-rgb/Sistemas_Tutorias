package edu.uees.patrones.builder;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Demuestra dos configuraciones distintas de Reserva construidas con
 * ReservaBuilder, y la validacion de campos obligatorios.
 */
public class DemoBuilder {

    public static void main(String[] args) {
        System.out.println("== Configuracion 1: solo campos obligatorios ==");
        Reserva reservaMinima = new ReservaBuilder()
                .estudiante("Ana Perez")
                .docente("Jaime Sayago")
                .horario(LocalDate.now().plusDays(1), LocalTime.of(10, 0), LocalTime.of(11, 0))
                .build();
        System.out.println(reservaMinima);

        System.out.println("\n== Configuracion 2: con campos opcionales ==");
        Reserva reservaCompleta = new ReservaBuilder()
                .estudiante("Luis Diaz")
                .docente("Jaime Sayago")
                .horario(LocalDate.now().plusDays(2), LocalTime.of(15, 0), LocalTime.of(16, 0))
                .modalidad(Modalidad.VIRTUAL)
                .notas("Traer avance del proyecto final")
                .canalNotificacion(CanalNotificacion.SMS)
                .conRecordatorio()
                .build();
        System.out.println(reservaCompleta);

        System.out.println("\n== Validacion: falta un campo obligatorio ==");
        try {
            new ReservaBuilder()
                    .estudiante("Maria Lopez")
                    .build(); // falta docente y horario
        } catch (IllegalStateException ex) {
            System.out.println("Error esperado: " + ex.getMessage());
        }
    }
}
