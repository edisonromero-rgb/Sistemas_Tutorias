package edu.uees.tutorias.observer;

import edu.uees.tutorias.domain.ReservaEvento;

import java.util.ArrayList;
import java.util.List;

/**
 * Subject/Publisher del patron Observer: mantiene la lista de
 * suscriptores y les publica cada {@link ReservaEvento}.
 *
 * {@link edu.uees.tutorias.service.ServicioReservas} depende de esta
 * clase (no de los observers concretos) para emitir eventos; agregar
 * un observer nuevo es responsabilidad de quien compone la aplicacion
 * (ver {@code App.java}), no del servicio.
 */
public class ReservaPublisher {

    private final List<ReservaObserver> observers = new ArrayList<>();

    public void suscribir(ReservaObserver observer) {
        observers.add(observer);
    }

    public void desuscribir(ReservaObserver observer) {
        observers.remove(observer);
    }

    public void publicar(ReservaEvento evento) {
        for (ReservaObserver observer : observers) {
            observer.actualizar(evento);
        }
    }
}
