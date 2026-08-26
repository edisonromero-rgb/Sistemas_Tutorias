package edu.uees.tutorias.repository;

import edu.uees.tutorias.domain.Estudiante;
import edu.uees.tutorias.domain.Reserva;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementacion de referencia de {@link ReservaRepository} basada en
 * un mapa en memoria. Sirve para pruebas y para ejecutar el proyecto
 * sin depender de una base de datos externa.
 *
 * OCP: es una implementacion mas de la interfaz; puede convivir o ser
 * reemplazada por una implementacion con JDBC/JPA sin tocar
 * ServicioReservas.
 */
public class ReservaRepositoryMemoria implements ReservaRepository {

    private final Map<String, Reserva> reservas = new LinkedHashMap<>();

    @Override
    public void guardar(Reserva reserva) {
        reservas.put(reserva.getId(), reserva);
    }

    @Override
    public Optional<Reserva> buscarPorId(String id) {
        return Optional.ofNullable(reservas.get(id));
    }

    @Override
    public List<Reserva> listarTodas() {
        return new ArrayList<>(reservas.values());
    }

    @Override
    public List<Reserva> listarPorEstudiante(Estudiante estudiante) {
        List<Reserva> resultado = new ArrayList<>();
        for (Reserva reserva : reservas.values()) {
            if (reserva.getEstudiante().equals(estudiante)) {
                resultado.add(reserva);
            }
        }
        return resultado;
    }
}
