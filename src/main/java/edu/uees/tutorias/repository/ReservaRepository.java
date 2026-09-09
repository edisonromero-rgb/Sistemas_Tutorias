package edu.uees.tutorias.repository;

import edu.uees.tutorias.domain.Estudiante;
import edu.uees.tutorias.domain.Reserva;

import java.util.List;
import java.util.Optional;

/**
 * Contrato de persistencia de reservas.
 *
 * DIP: ServicioReservas depende de esta abstraccion y no de una
 * implementacion concreta, de modo que cambiar la tecnologia de
 * almacenamiento (memoria, base de datos relacional, NoSQL, etc.)
 * no obliga a modificar la logica de coordinacion del servicio.
 */
public interface ReservaRepository {

    void guardar(Reserva reserva);

    Optional<Reserva> buscarPorId(String id);

    List<Reserva> listarTodas();

    List<Reserva> listarPorEstudiante(Estudiante estudiante);
}
