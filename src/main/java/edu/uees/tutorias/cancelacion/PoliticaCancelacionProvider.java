package edu.uees.tutorias.cancelacion;

import edu.uees.tutorias.domain.TipoReserva;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * Resuelve que {@link PoliticaCancelacion} corresponde al
 * {@link TipoReserva} de una reserva, para que
 * {@link edu.uees.tutorias.service.ServicioReservas} no necesite un
 * switch propio para elegir la estrategia.
 */
public final class PoliticaCancelacionProvider {

    private static final Map<TipoReserva, PoliticaCancelacion> POLITICAS = new EnumMap<>(TipoReserva.class);

    static {
        POLITICAS.put(TipoReserva.NORMAL, new CancelacionNormalPolitica());
        POLITICAS.put(TipoReserva.PRIORITARIA, new CancelacionPrioritariaPolitica());
        POLITICAS.put(TipoReserva.GRUPAL, new CancelacionGrupalPolitica());
    }

    private PoliticaCancelacionProvider() {
    }

    public static PoliticaCancelacion politicaPara(TipoReserva tipo) {
        Objects.requireNonNull(tipo, "tipo no puede ser nulo");
        PoliticaCancelacion politica = POLITICAS.get(tipo);
        if (politica == null) {
            throw new IllegalArgumentException("Tipo de reserva no soportado: " + tipo);
        }
        return politica;
    }
}
