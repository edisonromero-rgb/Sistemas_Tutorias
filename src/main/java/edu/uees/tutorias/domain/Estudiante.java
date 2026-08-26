package edu.uees.tutorias.domain;

/**
 * Especializacion de {@link Usuario} que solicita tutorias.
 *
 * No se agrega una coleccion de reservas dentro de Estudiante: la
 * asociacion se resuelve desde {@link Reserva}, que referencia al
 * estudiante, y desde el repositorio de reservas cuando se necesita
 * una consulta ("listar reservas de un estudiante"). Evitar el
 * atributo bidireccional reduce el acoplamiento entre Estudiante y
 * la coleccion de reservas que en realidad administra el servicio.
 */
public class Estudiante extends Usuario {

    private final String carrera;

    public Estudiante(String id, String nombre, String correo, String carrera) {
        super(id, nombre, correo);
        this.carrera = carrera;
    }

    public String getCarrera() {
        return carrera;
    }
}
