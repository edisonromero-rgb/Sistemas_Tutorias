package edu.uees.tutorias.domain;

import java.util.Objects;

/**
 * Abstraccion comun a todos los usuarios del sistema.
 *
 * Encapsulacion: los atributos son privados y solo se exponen mediante
 * getters de solo lectura; ningun objeto externo puede alterar la
 * identidad de un usuario una vez creado.
 *
 * Herencia: Usuario se utiliza como superclase de {@link Estudiante} y
 * {@link Docente} porque ambos comparten identidad y datos de contacto,
 * no para reutilizar codigo por conveniencia.
 */
public abstract class Usuario {

    private final String id;
    private final String nombre;
    private final String correo;

    protected Usuario(String id, String nombre, String correo) {
        this.id = Objects.requireNonNull(id, "id no puede ser nulo");
        this.nombre = Objects.requireNonNull(nombre, "nombre no puede ser nulo");
        this.correo = Objects.requireNonNull(correo, "correo no puede ser nulo");
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario usuario)) return false;
        return id.equals(usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
