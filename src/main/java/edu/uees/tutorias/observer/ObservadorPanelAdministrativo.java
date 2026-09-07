package edu.uees.tutorias.observer;

import edu.uees.tutorias.domain.EstadoReserva;
import edu.uees.tutorias.domain.Reserva;
import edu.uees.tutorias.domain.ReservaObserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ConcreteObserver que deja evidencia de cada cambio de estado, como lo
 * haria un panel administrativo. Mantiene su propia bitacora en
 * memoria; ni Reserva ni ServicioReservas conocen esta lista.
 */
public class ObservadorPanelAdministrativo implements ReservaObserver {

    private final List<String> bitacora = new ArrayList<>();

    @Override
    public void onCambioEstado(Reserva reserva, EstadoReserva anterior, EstadoReserva nuevo) {
        String origen = anterior == null ? "creada" : anterior + " -> " + nuevo;
        bitacora.add(reserva.getId() + ": " + origen);
    }

    public List<String> getBitacora() {
        return Collections.unmodifiableList(bitacora);
    }
}
