package edu.uees.tutorias.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Especializacion de {@link Usuario} que administra horarios disponibles.
 *
 * Composicion: el docente es dueno del ciclo de vida de los horarios
 * que publica (si el docente desaparece del sistema, sus horarios no
 * publicados dejan de tener sentido), por eso HorarioDisponible se
 * crea a partir de un Docente y este mantiene la coleccion.
 */
public class Docente extends Usuario {

    private final String especialidad;
    private final List<HorarioDisponible> horariosPublicados = new ArrayList<>();

    public Docente(String id, String nombre, String correo, String especialidad) {
        super(id, nombre, correo);
        this.especialidad = especialidad;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    /**
     * Publica un nuevo horario disponible para tutorias.
     */
    public HorarioDisponible publicarHorario(String idHorario, java.time.LocalDate fecha,
                                              java.time.LocalTime horaInicio,
                                              java.time.LocalTime horaFin) {
        HorarioDisponible horario = new HorarioDisponible(idHorario, this, fecha, horaInicio, horaFin);
        horariosPublicados.add(horario);
        return horario;
    }

    public List<HorarioDisponible> getHorariosPublicados() {
        return Collections.unmodifiableList(horariosPublicados);
    }
}
