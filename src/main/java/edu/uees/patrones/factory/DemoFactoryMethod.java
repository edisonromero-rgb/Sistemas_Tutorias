package edu.uees.patrones.factory;

import java.util.List;

/**
 * Demuestra la creacion y uso de las tres variantes originales
 * (correo, SMS, push) y, despues, la incorporacion de una variante
 * adicional (WhatsApp) para evidenciar extensibilidad sin modificar
 * el codigo ya existente.
 */
public class DemoFactoryMethod {

    public static void main(String[] args) {
        System.out.println("== Variantes originales ==");
        List<NotificadorFactory> factories = List.of(
                new NotificadorCorreoFactory(),
                new NotificadorSMSFactory(),
                new NotificadorPushFactory()
        );

        for (NotificadorFactory factory : factories) {
            // El cliente solo conoce NotificadorFactory; no sabe (ni le
            // importa) que clase concreta de Notificador se crea.
            factory.enviar("EST-1", "Tu reserva fue confirmada.");
        }

        System.out.println("\n== Variante adicional (extensibilidad) ==");
        NotificadorFactory whatsapp = new NotificadorWhatsAppFactory();
        whatsapp.enviar("+593999999999", "Tu tutoria empieza en 15 minutos.");
    }
}
