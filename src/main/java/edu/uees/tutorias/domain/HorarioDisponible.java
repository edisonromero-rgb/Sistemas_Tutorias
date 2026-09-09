package edu.uees.tutorias.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Representa una franja horaria publicada por un docente.
 *
 * Encapsulacion: el estado "disponible" solo cambia a traves de
 * {@link #reservar()} y {@link #liberar()}. Ningun otro objeto puede
 * marcar el horario como ocupado directamente asignando un booleano;
 * esto concentra en un unico lugar la regla "no se puede reservar un
 * horario ocupado".
 */
public class HorarioDisponible {

    private final String id;
    private final Docente docente;
    private final LocalDate fecha;
    private final LocalTime horaInicio;
    private final LocalTime horaFin;
    private boolean disponible;

    public HorarioDisponible(String id, Docente docente, LocalDate fecha,
                              LocalTime horaInicio, LocalTime horaFin) {
        if (!horaInicio.isBefore(horaFin)) {
            throw new IllegalArgumentException("horaInicio debe ser anterior a horaFin");
        }
        this.id = Objects.requireNonNull(id);
        this.docente = Objects.requireNonNull(docente);
        this.fecha = Objects.requireNonNull(fecha);
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.disponible = true;
    }

    /**
     * Ocupa el horario. Lanza excepcion si ya estaba reservado, evitando
     * que dos reservas distintas terminen apuntando al mismo horario.
     */
    public void reservar() {
        if (!disponible) {
            throw new IllegalStateException("El horario " + id + " ya no esta disponible");
        }
        disponible = false;
    }

    /**
     * Libera el horario (por ejemplo, al cancelar o reprogramar una reserva).
     */
    public void liberar() {
        disponible = true;
    }

    public String getId() {
        return id;
    }

    public Docente getDocente() {
        return docente;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public boolean isDisponible() {
        return disponible;
    }

    @Override
    public String toString() {
        return id + " (" + fecha + " " + horaInicio + "-" + horaFin + ")";
    }
}
