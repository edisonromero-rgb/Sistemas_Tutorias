package edu.uees.tutorias.notification;

import edu.uees.tutorias.domain.CanalNotificacion;

import java.util.EnumMap;
import java.util.Map;

/**
 * Registro que resuelve la {@link NotificadorFactory} correspondiente a
 * un {@link CanalNotificacion}. Es el punto donde se conecta el dominio
 * (canal preferido de una Reserva) con el patron Factory Method: quien
 * necesita notificar no decide con un switch que clase concreta usar,
 * solo pide "la fabrica para este canal".
 *
 * Agregar un canal nuevo significa registrar una entrada mas aqui (en
 * la composicion de la aplicacion) y crear su Product/Creator; no
 * implica modificar a quienes consumen este proveedor (observer
 * NotificacionObserver, por ejemplo).
 */
public class NotificadorFactoryProvider {

    private final Map<CanalNotificacion, NotificadorFactory> fabricas = new EnumMap<>(CanalNotificacion.class);

    public NotificadorFactoryProvider registrar(CanalNotificacion canal, NotificadorFactory fabrica) {
        fabricas.put(canal, fabrica);
        return this;
    }

    public NotificadorFactory factoryPara(CanalNotificacion canal) {
        NotificadorFactory fabrica = fabricas.get(canal);
        if (fabrica == null) {
            throw new IllegalStateException("No hay NotificadorFactory registrada para el canal " + canal);
        }
        return fabrica;
    }

    /** Registro por defecto con los tres canales soportados hoy. */
    public static NotificadorFactoryProvider conCanalesPorDefecto() {
        return new NotificadorFactoryProvider()
                .registrar(CanalNotificacion.EMAIL, new NotificadorCorreoFactory())
                .registrar(CanalNotificacion.SMS, new NotificadorSMSFactory())
                .registrar(CanalNotificacion.PUSH, new NotificadorPushFactory());
    }
}
