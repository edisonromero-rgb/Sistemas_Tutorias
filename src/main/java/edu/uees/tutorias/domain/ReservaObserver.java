package edu.uees.tutorias.domain;

/**
 * Observer del patron Observer.
 *
 * Problema que resuelve: cuando una {@link Reserva} cambia de estado,
 * varios componentes independientes deben reaccionar (enviar una
 * notificacion al estudiante/docente, sincronizar un calendario,
 * dejar evidencia en un panel administrativo). Antes de este patron,
 * ServicioReservas conocia a todos esos componentes y llamaba a cada
 * uno explicitamente despues de cada operacion, lo que lo acoplaba a
 * la logica de notificacion y obligaba a modificarlo cada vez que se
 * agregaba un nuevo interesado en los cambios de estado.
 *
 * Con Observer, Reserva (Subject) no conoce a sus observadores
 * concretos, solo a este contrato; agregar un nuevo interesado no
 * requiere tocar ni Reserva ni ServicioReservas.
 */
public interface ReservaObserver {

    /**
     * Notificado por {@link Reserva} despues de una transicion de estado
     * valida. {@code anterior} es {@code null} cuando el evento
     * corresponde a la creacion de la reserva (transicion inicial hacia
     * PENDIENTE).
     */
    void onCambioEstado(Reserva reserva, EstadoReserva anterior, EstadoReserva nuevo);
}
