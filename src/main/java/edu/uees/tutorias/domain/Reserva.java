package edu.uees.tutorias.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Registra el encuentro entre un {@link Estudiante} y un {@link Docente}
 * en un {@link HorarioDisponible} concreto, y gestiona su ciclo de vida.
 *
 * Alta cohesion: toda la logica de transicion de estados vive aqui.
 * Reserva no envia correos, no persiste datos ni genera reportes;
 * esas responsabilidades pertenecen a {@link ReservaObserver}
 * (patron Observer) y a {@link edu.uees.tutorias.repository.ReservaRepository},
 * no a Reserva directamente.
 *
 * Product del patron Builder: el constructor es de paquete y solo
 * {@link ReservaBuilder} puede invocarlo, de modo que los campos
 * obligatorios (estudiante, docente, horario) y opcionales (modalidad,
 * notas, canal de notificacion preferido, recordatorio, tipo) siempre
 * se validan y asignan de forma consistente antes de que exista una
 * Reserva.
 *
 * Subject del patron Observer: mantiene una lista de
 * {@link ReservaObserver} y los notifica despues de cada transicion de
 * estado valida, incluida la creacion. Reserva no sabe que hacen sus
 * observadores (notificar, sincronizar un calendario, auditar); solo
 * conoce el contrato.
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

    // Campos opcionales (Builder), con los valores por defecto documentados en ReservaBuilder.
    private final Modalidad modalidad;
    private final String notas;
    private final CanalNotificacion canalNotificacionPreferido;
    private final boolean recordatorioActivado;
    private final TipoReserva tipo;

    private final List<ReservaObserver> observadores = new ArrayList<>();

    /** Solo ReservaBuilder puede construir una Reserva. */
    Reserva(ReservaBuilder builder) {
        this.id = Objects.requireNonNull(builder.id);
        this.estudiante = Objects.requireNonNull(builder.estudiante);
        this.docente = Objects.requireNonNull(builder.docente);
        this.horario = Objects.requireNonNull(builder.horario);
        this.modalidad = builder.modalidad;
        this.notas = builder.notas;
        this.canalNotificacionPreferido = builder.canalNotificacionPreferido;
        this.recordatorioActivado = builder.recordatorioActivado;
        this.tipo = builder.tipo;
        this.observadores.addAll(builder.observadores);
        // Regla: no se puede reservar un horario ocupado. La propia
        // franja horaria valida y protege esta condicion.
        this.horario.reservar();
        this.estado = EstadoReserva.PENDIENTE;
        notificarObservadores(null, EstadoReserva.PENDIENTE);
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
        EstadoReserva anterior = estado;
        this.estado = nuevoEstado;
        notificarObservadores(anterior, nuevoEstado);
    }

    /** Agrega un observador que reaccionara a los cambios de estado futuros. */
    public void agregarObservador(ReservaObserver observador) {
        observadores.add(Objects.requireNonNull(observador));
    }

    public void removerObservador(ReservaObserver observador) {
        observadores.remove(observador);
    }

    private void notificarObservadores(EstadoReserva anterior, EstadoReserva nuevo) {
        for (ReservaObserver observador : observadores) {
            observador.onCambioEstado(this, anterior, nuevo);
        }
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

    public CanalNotificacion getCanalNotificacionPreferido() {
        return canalNotificacionPreferido;
    }

    public boolean isRecordatorioActivado() {
        return recordatorioActivado;
    }

    public TipoReserva getTipo() {
        return tipo;
    }

    public List<ReservaObserver> getObservadores() {
        return Collections.unmodifiableList(observadores);
    }
}
