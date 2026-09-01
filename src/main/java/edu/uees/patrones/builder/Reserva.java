package edu.uees.patrones.builder;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Representa una reserva de tutoria con datos obligatorios (estudiante,
 * docente y horario) y datos opcionales (modalidad, notas, canal de
 * notificacion preferido y recordatorio).
 *
 * Problema que resuelve el patron Builder (constructor inicial):
 * <pre>
 *   // Antes de aplicar Builder, un constructor "telescopico" mezcla
 *   // parametros obligatorios y opcionales, todos posicionales:
 *   new Reserva("R-1", "Ana Perez", "Jaime Sayago",
 *               LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(11, 0),
 *               Modalidad.VIRTUAL, "recordar traer material",
 *               CanalNotificacion.SMS, true);
 *
 *   // Para omitir los opcionales hay que repetir valores por defecto
 *   // en cada llamada, y dos parametros del mismo tipo (por ejemplo,
 *   // horaInicio y horaFin, ambos LocalTime) se pueden intercambiar
 *   // por error sin que el compilador lo detecte.
 * </pre>
 * {@link ReservaBuilder} reemplaza esa llamada por una API fluida,
 * auto-descriptiva, con valores por defecto para los campos opcionales
 * y validacion explicita de los campos obligatorios antes de construir
 * el objeto (ver {@link ReservaBuilder#build()}).
 *
 * Reserva es inmutable: una vez construida, ningun campo puede
 * modificarse; solo existen getters.
 */
public final class Reserva {

    private final String id;
    private final String estudiante;
    private final String docente;
    private final LocalDate fecha;
    private final LocalTime horaInicio;
    private final LocalTime horaFin;
    private final Modalidad modalidad;
    private final String notas;
    private final CanalNotificacion canalNotificacion;
    private final boolean recordatorioActivado;

    /** Solo ReservaBuilder puede construir una Reserva. */
    Reserva(ReservaBuilder builder) {
        this.id = builder.id;
        this.estudiante = builder.estudiante;
        this.docente = builder.docente;
        this.fecha = builder.fecha;
        this.horaInicio = builder.horaInicio;
        this.horaFin = builder.horaFin;
        this.modalidad = builder.modalidad;
        this.notas = builder.notas;
        this.canalNotificacion = builder.canalNotificacion;
        this.recordatorioActivado = builder.recordatorioActivado;
    }

    public String getId() {
        return id;
    }

    public String getEstudiante() {
        return estudiante;
    }

    public String getDocente() {
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

    public Modalidad getModalidad() {
        return modalidad;
    }

    public String getNotas() {
        return notas;
    }

    public CanalNotificacion getCanalNotificacion() {
        return canalNotificacion;
    }

    public boolean isRecordatorioActivado() {
        return recordatorioActivado;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "id='" + id + '\'' +
                ", estudiante='" + estudiante + '\'' +
                ", docente='" + docente + '\'' +
                ", fecha=" + fecha +
                ", horaInicio=" + horaInicio +
                ", horaFin=" + horaFin +
                ", modalidad=" + modalidad +
                ", notas='" + notas + '\'' +
                ", canalNotificacion=" + canalNotificacion +
                ", recordatorioActivado=" + recordatorioActivado +
                '}';
    }
}
