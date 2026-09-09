package edu.uees.patrones.factory;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class NotificadorFactoryTest {

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
        assertDoesNotThrow(() -> factory.enviar("ana@uees.edu.ec", "mensaje de prueba"));
    }
}
