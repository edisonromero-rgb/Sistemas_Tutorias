package edu.uees.patrones;

import edu.uees.patrones.builder.DemoBuilder;
import edu.uees.patrones.factory.DemoFactoryMethod;

/** Punto de entrada que ejecuta las demostraciones de ambos patrones. */
public class App {

    public static void main(String[] args) {
        System.out.println("############ FACTORY METHOD ############");
        DemoFactoryMethod.main(args);

        System.out.println("\n############ BUILDER ############");
        DemoBuilder.main(args);
    }
}
