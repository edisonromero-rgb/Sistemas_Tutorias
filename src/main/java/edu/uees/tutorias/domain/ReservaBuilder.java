package edu.uees.tutorias.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Builder de {@link Reserva} (patron Builder, recuperado y re-justificado
 * de Ae2): construye el objeto de forma progresiva mediante una API
 * fluida (cada metodo devuelve {@code this}).
 *
 * Campos obligatorios: estudiante, docente y horario. Campos opcionales,
 * con valor por defecto si no se indican: modalidad (PRESENCIAL),
 * canalNotificacion (EMAIL), notas (""), recordatorioActivado (false).
 *
 * Por que sigue justificado en el incremento 1: en Ae1, Reserva solo
 * tenia 3 campos, todos obligatorios, y un constructor simple alcanzaba.
 * Este incremento agrega modalidad, notas, canal de notificacion
 * preferido y recordatorio -tres de ellos opcionales-, con lo que un
 * constructor posicional ya mezclaria obligatorios y opcionales y
 * obligaria a repetir valores por defecto en cada llamada. Builder
 * resuelve ese problema real sin tocar el resto del dominio.
 */
public class ReservaBuilder {

    String id = UUID.randomUUID().toString();
    Estudiante estudiante;
    Docente docente;
    HorarioDisponible horario;
    Modalidad modalidad = Modalidad.PRESENCIAL;
    String notas = "";
    CanalNotificacion canalNotificacion = CanalNotificacion.EMAIL;
    boolean recordatorioActivado = false;

    public ReservaBuilder id(String id) {
        this.id = id;
        return this;
    }

    public ReservaBuilder estudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
        return this;
    }

    public ReservaBuilder docente(Docente docente) {
        this.docente = docente;
        return this;
    }

    public ReservaBuilder horario(HorarioDisponible horario) {
        this.horario = horario;
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
     * Lanza IllegalStateException si falta alguno.
     */
    public Reserva build() {
        List<String> faltantes = new ArrayList<>();
        if (estudiante == null) faltantes.add("estudiante");
        if (docente == null) faltantes.add("docente");
        if (horario == null) faltantes.add("horario");
        if (!faltantes.isEmpty()) {
            throw new IllegalStateException(
                    "Faltan campos obligatorios para construir la Reserva: " + faltantes);
        }
        return new Reserva(this);
    }
}
