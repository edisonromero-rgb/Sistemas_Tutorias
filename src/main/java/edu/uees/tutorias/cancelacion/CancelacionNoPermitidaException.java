package edu.uees.tutorias.cancelacion;

/** Se lanza cuando una {@link PoliticaCancelacion} rechaza una cancelacion. */
public class CancelacionNoPermitidaException extends RuntimeException {

    public CancelacionNoPermitidaException(String mensaje) {
        super(mensaje);
    }
}
