package edu.uees.tutorias.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Builder de {@link Reserva}: la construye de forma progresiva mediante
 * una API fluida (cada metodo devuelve {@code this}).
 *
 * Problema que resuelve (constructor telescopico): Reserva tiene 3
 * datos obligatorios (estudiante, docente, horario) y varios opcionales
 * (modalidad, notas, canal de notificacion preferido, recordatorio,
 * tipo). Un constructor con los 8 parametros posicionales seria dificil
 * de leer, obligaria a repetir valores por defecto para usar solo los
 * obligatorios, y agregar un campo opcional nuevo rompería todas las
 * llamadas existentes o exigiria otro constructor sobrecargado.
 *
 * Campos obligatorios: estudiante, docente y horario. Campos
 * opcionales, con valor por defecto si no se indican: modalidad
 * (PRESENCIAL), canalNotificacionPreferido (EMAIL), notas (""),
 * recordatorioActivado (false), tipo (NORMAL).
 *
 * No se usa un Director separado porque la construccion de Reserva no
 * tiene una secuencia fija que valga la pena encapsular aparte del
 * propio ReservaBuilder; el orden de las llamadas fluidas es libre.
 */
public class ReservaBuilder {

    String id = UUID.randomUUID().toString();
    Estudiante estudiante;
    Docente docente;
    HorarioDisponible horario;
    Modalidad modalidad = Modalidad.PRESENCIAL;
    String notas = "";
    CanalNotificacion canalNotificacionPreferido = CanalNotificacion.EMAIL;
    boolean recordatorioActivado = false;
    TipoReserva tipo = TipoReserva.NORMAL;
    final List<ReservaObserver> observadores = new ArrayList<>();

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

    public ReservaBuilder canalNotificacionPreferido(CanalNotificacion canal) {
        this.canalNotificacionPreferido = canal;
        return this;
    }

    public ReservaBuilder conRecordatorio() {
        this.recordatorioActivado = true;
        return this;
    }

    public ReservaBuilder tipo(TipoReserva tipo) {
        this.tipo = tipo;
        return this;
    }

    /**
     * Registra un observador que se agregara a la Reserva antes de que
     * se dispare la notificacion de creacion, de modo que tambien
     * reciba ese primer evento.
     */
    public ReservaBuilder observador(ReservaObserver observador) {
        this.observadores.add(Objects.requireNonNull(observador));
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
