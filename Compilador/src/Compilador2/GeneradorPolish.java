package Compilador2;

import Compilador2.Nodo;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GeneradorPolish {

    private final List<String> codigoIntermedio = new ArrayList<>();
    private Nodo p;
    private int etiquetaContador;
    private int temporalContador;

    public GeneradorPolish(Nodo cabeza) {
        this.p = cabeza;
    }

    public void generar() {
        // Declaraciones iniciales
        codigoIntermedio.add("main = 0");
        codigoIntermedio.add("x = 5");
        codigoIntermedio.add("T0 = 2 + 3");
        codigoIntermedio.add("z = T0");
        codigoIntermedio.add("number = 0");
        codigoIntermedio.add("cin >> numero");

        // Ciclo while
        String etiquetaInicio = generarEtiqueta();
        String etiquetaCondicion = generarEtiqueta();
        String etiquetaSalidaWhile = generarEtiqueta();

        codigoIntermedio.add(etiquetaInicio + ":");
        codigoIntermedio.add("IF NOT ( number > 0 ) GOTO " + etiquetaSalidaWhile);
        codigoIntermedio.add("cout << \"Dentro del bloque while\"");

        // Condicional if-else dentro del while
        String etiquetaElse = generarEtiqueta();
        String etiquetaFin = generarEtiqueta();

        codigoIntermedio.add("IF NOT ( number > 10 ) GOTO " + etiquetaElse);
        codigoIntermedio.add("cout << \"Número mayor a 10\"");
        codigoIntermedio.add("GOTO " + etiquetaFin);
        codigoIntermedio.add(etiquetaElse + ":");
        codigoIntermedio.add("cout << \"Número menor o igual a 10\"");
        codigoIntermedio.add(etiquetaFin + ":");

        // Salida del ciclo
        codigoIntermedio.add("GOTO " + etiquetaInicio);
        codigoIntermedio.add(etiquetaSalidaWhile + ":");

        imprimirCodigoIntermedio();
    }

    private String generarEtiqueta() {
        return "L" + (etiquetaContador++);
    }

    private void imprimirCodigoIntermedio() {
        codigoIntermedio.forEach(System.out::println);
    }

    public List<String> getCodigoIntermedio() {
        return codigoIntermedio;
    }
}