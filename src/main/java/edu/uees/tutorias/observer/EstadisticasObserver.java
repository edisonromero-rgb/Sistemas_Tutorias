package edu.uees.tutorias.observer;

import edu.uees.tutorias.domain.ReservaEvento;
import edu.uees.tutorias.domain.TipoEvento;

import java.util.EnumMap;
import java.util.Map;

/**
 * ConcreteObserver que lleva un conteo de eventos por tipo. Simula un
 * panel/dashboard que reacciona a los mismos eventos que el correo y la
 * auditoria, sin que ninguno de los tres se conozca entre si.
 */
public class EstadisticasObserver implements ReservaObserver {

    private final Map<TipoEvento, Integer> conteo = new EnumMap<>(TipoEvento.class);

    @Override
    public void actualizar(ReservaEvento evento) {
        conteo.merge(evento.getTipo(), 1, Integer::sum);
    }

    public int getConteo(TipoEvento tipo) {
        return conteo.getOrDefault(tipo, 0);
    }
}
