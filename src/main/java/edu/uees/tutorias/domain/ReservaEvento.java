package edu.uees.tutorias.domain;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Evento inmutable publicado cuando una {@link Reserva} cambia de
 * estado. Es el objeto que viaja entre el publisher y los observers en
 * el patron Observer (ver paquete {@code observer}).
 */
public final class ReservaEvento {

    private final TipoEvento tipo;
    private final Reserva reserva;
    private final LocalDateTime ocurridoEn;

    public ReservaEvento(TipoEvento tipo, Reserva reserva) {
        this.tipo = Objects.requireNonNull(tipo);
        this.reserva = Objects.requireNonNull(reserva);
        this.ocurridoEn = LocalDateTime.now();
    }

    public TipoEvento getTipo() {
        return tipo;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public LocalDateTime getOcurridoEn() {
        return ocurridoEn;
    }
}
