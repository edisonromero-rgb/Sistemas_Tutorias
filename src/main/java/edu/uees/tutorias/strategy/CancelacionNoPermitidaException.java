package edu.uees.tutorias.strategy;

/** Se lanza cuando una {@link PoliticaCancelacion} rechaza una cancelacion. */
public class CancelacionNoPermitidaException extends RuntimeException {

    public CancelacionNoPermitidaException(String mensaje) {
        super(mensaje);
    }
}
