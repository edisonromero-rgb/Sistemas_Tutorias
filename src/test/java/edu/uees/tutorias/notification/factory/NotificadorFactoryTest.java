package edu.uees.tutorias.notification.factory;

import edu.uees.tutorias.domain.CanalNotificacion;
import edu.uees.tutorias.domain.Estudiante;
import edu.uees.tutorias.notification.NotificadorCorreo;
import edu.uees.tutorias.notification.NotificadorPush;
import edu.uees.tutorias.notification.NotificadorSMS;
import edu.uees.tutorias.notification.NotificadorWhatsApp;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NotificadorFactoryTest {

    private final Estudiante estudiante = new Estudiante("EST-1", "Ana Perez", "ana@uees.edu.ec", "Software");

    @Test
    void correoFactoryCreaNotificadorCorreo() {
        NotificadorFactory factory = new NotificadorCorreoFactory();
        assertInstanceOf(NotificadorCorreo.class, factory.crearNotificador());
    }

    @Test
    void smsFactoryCreaNotificadorSMS() {
        NotificadorFactory factory = new NotificadorSMSFactory();
        assertInstanceOf(NotificadorSMS.class, factory.crearNotificador());
    }

    @Test
    void pushFactoryCreaNotificadorPush() {
        NotificadorFactory factory = new NotificadorPushFactory();
        assertInstanceOf(NotificadorPush.class, factory.crearNotificador());
    }

    @Test
    void whatsAppFactoryCreaNotificadorWhatsApp() {
        NotificadorFactory factory = new NotificadorWhatsAppFactory();
        assertInstanceOf(NotificadorWhatsApp.class, factory.crearNotificador());
    }

    @Test
    void enviarDelegaEnElNotificadorCreadoSinLanzarExcepcion() {
        NotificadorFactory factory = new NotificadorCorreoFactory();
        assertDoesNotThrow(() -> factory.enviar(estudiante, "mensaje de prueba"));
    }

    @Test
    void providerResuelveLaFactorySegunElCanal() {
        assertInstanceOf(NotificadorSMSFactory.class,
                NotificadorFactoryProvider.factoryPara(CanalNotificacion.SMS));
        assertInstanceOf(NotificadorWhatsAppFactory.class,
                NotificadorFactoryProvider.factoryPara(CanalNotificacion.WHATSAPP));
    }

    @Test
    void providerLanzaExcepcionSiElCanalEsNulo() {
        assertThrows(NullPointerException.class, () -> NotificadorFactoryProvider.factoryPara(null));
    }
}
