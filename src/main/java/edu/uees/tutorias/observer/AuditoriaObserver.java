package edu.uees.tutorias.observer;

import edu.uees.tutorias.domain.ReservaEvento;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ConcreteObserver que deja un registro de auditoria simple (en
 * memoria) de cada evento de reserva. En un entorno real escribiria a
 * una tabla de auditoria o a un log persistente.
 */
public class AuditoriaObserver implements ReservaObserver {

    private final List<String> bitacora = new ArrayList<>();

    @Override
    public void actualizar(ReservaEvento evento) {
        String entrada = evento.getOcurridoEn() + " | " + evento.getTipo() + " | reserva="
                + evento.getReserva().getId();
        bitacora.add(entrada);
        System.out.println("[auditoria] " + entrada);
    }

    public List<String> getBitacora() {
        return Collections.unmodifiableList(bitacora);
    }
}
