package Compilador2;

import java.io.FileWriter;
import java.io.IOException;

public class Optimizador {

    Nodo cabeza;

    public Optimizador(Nodo cabeza) {
        this.cabeza = cabeza;
    }

    public void optimizar() {
        Nodo p = cabeza;

        while (p != null) {
            if (esExpresionConstante(p)) {
                simplificarExpresionConstante(p);
            }
            p = p.sig;
        }
    }

    public void escribirCodigoOptimizado(String rutaArchivo) {
        try (FileWriter writer = new FileWriter(rutaArchivo)) {
            Nodo temp = cabeza;
            int nivelIndentacion = 0;

            while (temp != null) {
                if (!esNodoValido(temp)) {
                    temp = temp.sig;
                    continue;
                }

                switch (temp.lexema) {
                    case "{":
                        writer.write("\n" + "\t".repeat(nivelIndentacion) + temp.lexema + "\n");
                        nivelIndentacion++;
                        break;
                    case "}":
                        nivelIndentacion--;
                        writer.write("\t".repeat(nivelIndentacion) + temp.lexema + "\n");
                        break;
                    case ";":
                        writer.write(temp.lexema + "\n" + "\t".repeat(nivelIndentacion));
                        break;
                    case "else":
                        writer.write(temp.lexema + "\n" + "\t".repeat(nivelIndentacion));
                        break;
                    default:
                        if (temp.ant != null && "{;".contains(temp.ant.lexema)) {
                            writer.write("\t".repeat(nivelIndentacion) + temp.lexema + " ");
                        } else {
                            writer.write(temp.lexema + " ");
                        }
                        break;
                }

                temp = temp.sig;
            }

            writer.write("\n");
        } catch (IOException e) {
            System.out.println("Error al escribir el archivo optimizado: " + e.getMessage());
        }
    }

    private boolean esExpresionConstante(Nodo nodo) {
        return nodo != null
                && nodo.sig != null
                && nodo.sig.sig != null
                && nodo.token == 101 // Constante
                && nodo.sig.token == 103 // Operador (+, -, *, /)
                && nodo.sig.sig.token == 101; // Otra constante
    }

    private void simplificarExpresionConstante(Nodo nodo) {
        try {
            int valor1 = Integer.parseInt(nodo.lexema);
            int valor2 = Integer.parseInt(nodo.sig.sig.lexema);
            int resultado = 0;

            switch (nodo.sig.lexema) {
                case "+":
                    resultado = valor1 + valor2;
                    break;
                case "-":
                    resultado = valor1 - valor2;
                    break;
                case "*":
                    resultado = valor1 * valor2;
                    break;
                case "/":
                    if (valor2 != 0) {
                        resultado = valor1 / valor2;
                    } else {
                        System.out.println("Error: División por cero en la expresión.");
                        return;
                    }
                    break;
                default:
                    System.out.println("Operador desconocido: " + nodo.sig.lexema);
                    return;
            }

            nodo.lexema = String.valueOf(resultado);
            nodo.token = 101;

            nodo.sig = nodo.sig.sig.sig;
            if (nodo.sig != null) {
                nodo.sig.ant = nodo;
            }
        } catch (NumberFormatException e) {
            System.out.println("Error al simplificar expresión constante: " + e.getMessage());
        }
    }

    private boolean esNodoValido(Nodo nodo) {
        return nodo != null && nodo.lexema != null && !nodo.lexema.trim().isEmpty();
    }
}
