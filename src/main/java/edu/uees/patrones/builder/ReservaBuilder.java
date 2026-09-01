package edu.uees.patrones.builder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Builder de {@link Reserva}: construye el objeto de forma progresiva
 * mediante una API fluida (cada metodo devuelve {@code this}).
 *
 * Campos obligatorios: estudiante, docente y horario (fecha, horaInicio,
 * horaFin). Campos opcionales, con valor por defecto si no se indican:
 * modalidad (PRESENCIAL), canalNotificacion (EMAIL), notas (""),
 * recordatorioActivado (false).
 */
public class ReservaBuilder {

    String id = UUID.randomUUID().toString();
    String estudiante;
    String docente;
    LocalDate fecha;
    LocalTime horaInicio;
    LocalTime horaFin;
    Modalidad modalidad = Modalidad.PRESENCIAL;
    String notas = "";
    CanalNotificacion canalNotificacion = CanalNotificacion.EMAIL;
    boolean recordatorioActivado = false;

    public ReservaBuilder id(String id) {
        this.id = id;
        return this;
    }

    public ReservaBuilder estudiante(String estudiante) {
        this.estudiante = estudiante;
        return this;
    }

    public ReservaBuilder docente(String docente) {
        this.docente = docente;
        return this;
    }

    public ReservaBuilder horario(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        return this;
    }

    public ReservaBuilder modalidad(Modalidad modalidad) {
        this.modalidad = modalidad;
        return this;
    }

    public ReservaBuilder notas(String notas) {
        this.notas = notas;
        return this;
    }

    public ReservaBuilder canalNotificacion(CanalNotificacion canalNotificacion) {
        this.canalNotificacion = canalNotificacion;
        return this;
    }

    public ReservaBuilder conRecordatorio() {
        this.recordatorioActivado = true;
        return this;
    }

    /**
     * Valida los campos obligatorios antes de construir la Reserva.
     * Lanza IllegalStateException si falta alguno, o si el horario es
     * incoherente (horaInicio no anterior a horaFin).
     */
    public Reserva build() {
        List<String> faltantes = new ArrayList<>();
        if (estudiante == null || estudiante.isBlank()) faltantes.add("estudiante");
        if (docente == null || docente.isBlank()) faltantes.add("docente");
        if (fecha == null || horaInicio == null || horaFin == null) {
            faltantes.add("horario (fecha/horaInicio/horaFin)");
        }
        if (!faltantes.isEmpty()) {
            throw new IllegalStateException(
                    "Faltan campos obligatorios para construir la Reserva: " + faltantes);
        }
        if (!horaInicio.isBefore(horaFin)) {
            throw new IllegalStateException("horaInicio debe ser anterior a horaFin");
        }
        return new Reserva(this);
    }
}
