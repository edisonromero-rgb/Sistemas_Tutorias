package edu.uees.tutorias.notification.factory;

import edu.uees.tutorias.domain.CanalNotificacion;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * Resuelve que {@link NotificadorFactory} corresponde al
 * {@link CanalNotificacion} preferido de una reserva.
 *
 * No es en si mismo el patron (eso es {@link NotificadorFactory} y sus
 * ConcreteCreator); es el punto unico donde se traduce la preferencia
 * del dominio (un enum) a la fabrica concreta que debe usarse, para que
 * los observadores de notificacion no repitan un switch/if propio.
 */
public final class NotificadorFactoryProvider {

    private static final Map<CanalNotificacion, NotificadorFactory> FACTORIES = new EnumMap<>(CanalNotificacion.class);

    static {
        FACTORIES.put(CanalNotificacion.EMAIL, new NotificadorCorreoFactory());
        FACTORIES.put(CanalNotificacion.SMS, new NotificadorSMSFactory());
        FACTORIES.put(CanalNotificacion.PUSH, new NotificadorPushFactory());
        FACTORIES.put(CanalNotificacion.WHATSAPP, new NotificadorWhatsAppFactory());
    }

    private NotificadorFactoryProvider() {
    }

    public static NotificadorFactory factoryPara(CanalNotificacion canal) {
        Objects.requireNonNull(canal, "canal no puede ser nulo");
        NotificadorFactory factory = FACTORIES.get(canal);
        if (factory == null) {
            throw new IllegalArgumentException("Canal no soportado: " + canal);
        }
        return factory;
    }
}
