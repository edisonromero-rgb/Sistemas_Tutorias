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

    public Reserva(String id, Estudiante estudiante, Docente docente, HorarioDisponible horario) {
        this.id = Objects.requireNonNull(id);
        this.estudiante = Objects.requireNonNull(estudiante);
        this.docente = Objects.requireNonNull(docente);
        this.horario = Objects.requireNonNull(horario);
        // Regla: no se puede reservar un horario ocupado. La propia
        // franja horaria valida y protege esta condicion.
        this.horario.reservar();
        this.estado = EstadoReserva.PENDIENTE;
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
}
