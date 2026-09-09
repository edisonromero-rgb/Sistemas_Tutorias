package edu.uees.tutorias.domain;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Registra el encuentro entre un {@link Estudiante} y un {@link Docente}
 * en un {@link HorarioDisponible} concreto, y gestiona su ciclo de vida.
 *
 * Alta cohesion: toda la logica de transicion de estados vive aqui.
 * Reserva no envia correos, no persiste datos ni genera reportes;
 * esas responsabilidades pertenecen a Notificador, ReservaRepository
 * y a otros componentes que colaboran con ServicioReservas, no con
 * Reserva directamente (ver decision de diseno en la seccion 4 del
 * documento de analisis).
 *
 * Incremento 1 (Ae3): Reserva ya no se construye con un constructor
 * publico de varios parametros. Ademas de los tres datos obligatorios
 * originales (estudiante, docente, horario), el proyecto necesita
 * ahora datos opcionales (modalidad, notas, canal de notificacion
 * preferido, recordatorio) que antes no existian. Un constructor con
 * 7-8 parametros posicionales, varios de ellos opcionales, es dificil
 * de leer y propenso a errores; por eso Reserva solo se crea a traves
 * de {@link ReservaBuilder} (patron Builder), que valida los campos
 * obligatorios y aplica valores por defecto a los opcionales.
 */
public class Reserva {

    private static final Map<EstadoReserva, Set<EstadoReserva>> TRANSICIONES_VALIDAS = new EnumMap<>(EstadoReserva.class);

    static {
        TRANSICIONES_VALIDAS.put(EstadoReserva.PENDIENTE,
                EnumSet.of(EstadoReserva.CONFIRMADA, EstadoReserva.CANCELADA, EstadoReserva.REPROGRAMADA));
        TRANSICIONES_VALIDAS.put(EstadoReserva.CONFIRMADA,
                EnumSet.of(EstadoReserva.CANCELADA, EstadoReserva.REPROGRAMADA, EstadoReserva.FINALIZADA));
        TRANSICIONES_VALIDAS.put(EstadoReserva.REPROGRAMADA,
                EnumSet.of(EstadoReserva.CONFIRMADA, EstadoReserva.CANCELADA));
        TRANSICIONES_VALIDAS.put(EstadoReserva.CANCELADA, EnumSet.noneOf(EstadoReserva.class));
        TRANSICIONES_VALIDAS.put(EstadoReserva.FINALIZADA, EnumSet.noneOf(EstadoReserva.class));
    }

    private final String id;
    private final Estudiante estudiante;
    private final Docente docente;
    private HorarioDisponible horario;
    private EstadoReserva estado;
    private final Modalidad modalidad;
    private final String notas;
    private final CanalNotificacion canalNotificacion;
    private final boolean recordatorioActivado;

    /** Solo ReservaBuilder puede construir una Reserva (mismo paquete). */
    Reserva(ReservaBuilder builder) {
        this.id = Objects.requireNonNull(builder.id);
        this.estudiante = Objects.requireNonNull(builder.estudiante);
        this.docente = Objects.requireNonNull(builder.docente);
        this.horario = Objects.requireNonNull(builder.horario);
        // Regla: no se puede reservar un horario ocupado. La propia
        // franja horaria valida y protege esta condicion.
        this.horario.reservar();
        this.estado = EstadoReserva.PENDIENTE;
        this.modalidad = builder.modalidad;
        this.notas = builder.notas;
        this.canalNotificacion = builder.canalNotificacion;
        this.recordatorioActivado = builder.recordatorioActivado;
    }

    public void confirmar() {
        transicionarA(EstadoReserva.CONFIRMADA);
    }

    public void cancelar() {
        transicionarA(EstadoReserva.CANCELADA);
        horario.liberar();
    }

    public void reprogramar(HorarioDisponible nuevoHorario) {
        Objects.requireNonNull(nuevoHorario, "nuevoHorario no puede ser nulo");
        transicionarA(EstadoReserva.REPROGRAMADA);
        horario.liberar();
        nuevoHorario.reservar();
        this.horario = nuevoHorario;
    }

    public void finalizar() {
        transicionarA(EstadoReserva.FINALIZADA);
    }

    private void transicionarA(EstadoReserva nuevoEstado) {
        Set<EstadoReserva> permitidos = TRANSICIONES_VALIDAS.get(estado);
        if (!permitidos.contains(nuevoEstado)) {
            throw new IllegalStateException(
                    "Transicion invalida: " + estado + " -> " + nuevoEstado + " (reserva " + id + ")");
        }
        this.estado = nuevoEstado;
    }

    public String getId() {
        return id;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public Docente getDocente() {
        return docente;
    }

    public HorarioDisponible getHorario() {
        return horario;
    }

    public EstadoReserva getEstado() {
        return estado;
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
}
