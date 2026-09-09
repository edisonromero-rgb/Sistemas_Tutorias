package edu.uees.patrones.factory;

/**
 * Product del patron Factory Method.
 *
 * Problema inicial: el Sistema de gestion de tutorias necesita avisar a
 * estudiantes y docentes por distintos canales (correo, SMS, push y, mas
 * adelante, WhatsApp). Si el codigo cliente decide con un
 * "if / else if / switch" que clase concreta instanciar segun el canal,
 * cada canal nuevo obliga a modificar ese mismo bloque de codigo en todos
 * los lugares donde se crea un notificador, violando el principio
 * abierto/cerrado.
 *
 * Factory Method resuelve esto separando "que se notifica" (este
 * contrato) de "como se decide y crea la implementacion concreta"
 * (ver {@link NotificadorFactory} y sus subclases).
 */
public interface Notificador {

    void notificar(String destinatario, String mensaje);
}
