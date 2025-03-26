package Compilador2;

public class Compilador {

    Nodo p;

    public static void main(String[] args) {
        Analisis_Lexico Lexico = new Analisis_Lexico();
        /* if (!Lexico.errorEncontrado) {
            System.out.println("\nAnalizador Lexico Terminado");
            System.out.println("---------------------------");
        }
        Analisis_Sintactico Sintactico = new Analisis_Sintactico();
        Sintactico.p = Lexico.cabeza;
        Sintactico.Sintaxis();
        if (!Sintactico.error_Sintaxis) {
            System.out.println("No se encontraron Errores en el Analizador Sintactico");
            System.out.println("Analizador Sintactico Terminado");
            System.out.println("---------------------------");
        }
        Analisis_Semantico Semantico = new Analisis_Semantico();
        Semantico.p = Lexico.cabeza;
        Semantico.Semantica();
        if (!Semantico.errorSemantico) {
            System.out.println("No se encontraron errores en el analisis semantico.");
            System.out.println("---------------------------");
        }
         */
        Analisis_Lexico lexico = new Analisis_Lexico();
        GeneradorPolish generador = new GeneradorPolish(lexico.cabeza);
        generador.generar();
        
        Optimizador optimizador = new Optimizador(Lexico.cabeza);
        optimizador.optimizar();
        optimizador.escribirCodigoOptimizado("Codigo_optimizado.txt");
        System.out.println("Optimizacion completa. Codigo guardado en Codigo_optimizado.txt");

    }

}
