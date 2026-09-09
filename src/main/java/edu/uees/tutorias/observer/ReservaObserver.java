package edu.uees.tutorias.observer;

import edu.uees.tutorias.domain.ReservaEvento;

/**
 * Observer: contrato comun para reaccionar a un {@link ReservaEvento}.
 *
 * Problema real que resuelve: en Ae1/Ae2, ServicioReservas llamaba
 * directamente al Notificador tras cada operacion. Este incremento
 * necesita que, ademas de notificar, se registre auditoria y se
 * actualicen estadisticas cuando una reserva cambia de estado -y ese
 * conjunto de interesados seguira creciendo (un panel, un webhook,
 * etc.)-. Si ServicioReservas siguiera llamando a cada uno por su
 * nombre, cada interesado nuevo obligaria a modificarlo. Observer
 * desacopla al publisher (quien emite el evento) de los suscriptores
 * (quienes reaccionan).
 */
public interface ReservaObserver {

    void actualizar(ReservaEvento evento);
}
