package Compilador2;

import java.util.HashMap;
import java.util.Stack;

public class Analisis_Semantico {

    Nodo p;
    boolean errorSemantico = false;
    HashMap<String, String> tablaSimbolos = new HashMap<>();
    Stack<String> pilaOperandos = new Stack<>();
    Stack<String> pilaOperadores = new Stack<>();

    // Tabla para operaciones aritméticas (+, -, *)
    int[][] tablaAritmetica = {
        //          int    bool  char   double
        /*int*/     {222,  605,  224,    212},
        /*bool*/    {605,  605,  605,    605},
        /*char*/    {222,  605,  605,    212},
        /*double*/  {212,  605,  605,    212}
    };

    // Tabla para operaciones de división (/)
    int[][] tablaDivision = {
        //          int    bool   char   double
        /*int*/     {212,  605,   605,   212},
        /*bool*/    {605,  605,   605,   605},
        /*char*/    {605,  605,   605,   212},
        /*double*/  {212,  605,   605,   212}
    };

    // Tabla para operaciones relacionales (>, <, >=, <=)
    int[][] tablaRelacional = {
        //          int    bool   char   double
        /*int*/     {206,  605,   605,   206},
        /*bool*/    {605,  605,   605,   605},
        /*char*/    {206,  605,   605,   206},
        /*double*/  {206,  605,   605,   206}
    };

    // Tabla para operaciones de asignación (=)
    int[][] tablaAsignacion = {
        //          int    bool   char   double
        /*int*/     {222,  605,   605,   605},
        /*bool*/    {605,  206,   605,   605},
        /*char*/    {605,  605,   224,   605},
        /*double*/  {212,  605,   605,   212}
    };

    HashMap<Integer, String> mapaTipos = new HashMap<>();

    public Analisis_Semantico() {
        mapaTipos.put(222, "int");
        mapaTipos.put(224, "char");
        mapaTipos.put(206, "bool");
        mapaTipos.put(212, "double");
    }

    public void Semantica() {
        while (p != null) {
            // Ignorar llaves '{' y '}'
            if (p.token == 123 || p.token == 124) {
                p = p.sig; // Saltar la llave
                continue;
            }

            if (esDeclaracion()) {
                if (esVariableMultideclarada()) {
                    imprimirErrorSemantico("603: Variable multideclarada", p.renglon);
                } else {
                    agregarVariable();
                }
            } else if (esAsignacion()) {
                if (esVariableNoDeclarada()) {
                    imprimirErrorSemantico("602: Variable no declarada", p.renglon);
                } else if (!validarAsignacion()) {
                    imprimirErrorSemantico("605: Incompatibilidad de tipos en asignacion", p.renglon);
                }
            } else if (esOperacionAritmetica() || esOperacionDivision() || esOperacionRelacional()) {
                evaluarExpresion();
            }
            p = p.sig;
        }
    }

    private String obtenerTipo(int token) {
        return mapaTipos.get(token);
    }

    // Agrega una variable a la tabla de símbolos si no está previamente declarada
    private void agregarVariable() {
        String tipo = obtenerTipo(p.token);
        p = p.sig; // Moverse al primer nombre de variable

        while (p != null && !p.lexema.equals(";")) { 
            String nombreVariable = p.lexema;

            if (!tablaSimbolos.containsKey(nombreVariable)) {
                tablaSimbolos.put(nombreVariable, tipo);
            } else {
                imprimirErrorSemantico("603: Variable multideclarada", p.renglon);
            }

            p = p.sig; // Moverse al siguiente token

            if (p != null && p.token == 116) { // Asumiendo que 116 es el token de la coma
                p = p.sig; // Saltar la coma
            }
        }
    }

    private boolean esVariableNoDeclarada() {
        return !tablaSimbolos.containsKey(p.lexema);
    }

    private boolean esVariableMultideclarada() {
        return tablaSimbolos.containsKey(p.sig.lexema);
    }

    private boolean esDeclaracion() {
        return p.token == 222 || p.token == 224 || p.token == 206 || p.token == 212; // int, char, bool, double
    }

    private boolean esAsignacion() {
        return p.token == 100; // Token para una variable ya declarada
    }

    private boolean validarAsignacion() {
        // Si es una llave, saltarla
        if (p.token == 123 || p.token == 124) {
            return true;  // Considera que no es un error semántico
        }

        String tipoVariable = tablaSimbolos.get(p.lexema);  // p.lexema contiene el nombre de la variable
        p = p.sig; // Saltar el '='
        p = p.sig; // Ir al valor asignado

        // Si el valor asignado es una llave, saltarla
        if (p.token == 123 || p.token == 124) {
            return true; 
        }

        // Si el valor asignado es otra variable, busca su tipo en la tabla de símbolos
        String tipoValor;
        if (p.token == 100) { // Es una variable, busca su tipo en la tabla de símbolos
            tipoValor = tablaSimbolos.get(p.lexema);
        } else {
            // Es un valor literal, obtener tipo basado en el token
            tipoValor = obtenerTipoPorValor(p.token);
        }

        int tipoVariableIdx = obtenerIndicePorToken(obtenerTokenPorTipo(tipoVariable));
        int tipoValorIdx = obtenerIndicePorToken(obtenerTokenPorTipo(tipoValor));

        if (tipoVariableIdx == -1 || tipoValorIdx == -1) {
            System.out.println("Error: tipo de variable o valor no valido. " + p.token + " " + p.lexema);
            return false;
        }

        int resultado = tablaAsignacion[tipoVariableIdx][tipoValorIdx];

        if (resultado == 605) {
            imprimirErrorSemantico("Error 605: incompatibilidad de tipos en la asignacion", p.renglon);
            return false;
        }

        return resultado != 0;
    }

    private boolean esOperacionAritmetica() {
        return p.token == 103 || p.token == 104 || p.token == 105; // +, -, *
    }

    private boolean esOperacionDivision() {
        return p.token == 106; // /
    }

    private boolean esOperacionRelacional() {
        return p.token == 107 || p.token == 108 || p.token == 109 || p.token == 110; // >, <, >=, <=
    }

    // Evalúa expresiones usando pilas (Polish inverso)
    private void evaluarExpresion() {
        while (p != null && p.token != 117) {
            // Ignorar llaves '{' y '}'
            if (p.token == 123 || p.token == 124) {
                p = p.sig;  // Saltar la llave
                continue;
            }

            if (esOperando()) {
                pilaOperandos.push(obtenerTipo(p.token));
            } else if (esOperador()) {
                pilaOperadores.push(p.lexema);
                realizarOperacion();
            }
            p = p.sig;
        }

        pilaOperandos.clear();
        pilaOperadores.clear();
    }

    private boolean esOperando() {
        return p.token == 101 || p.token == 102 || p.token == 100; // Variables y valores
    }

    private boolean esOperador() {
        return p.token == 103 || p.token == 104 || p.token == 105 || p.token == 106 || p.token == 107 || p.token == 108 || p.token == 109 || p.token == 110; // +, -, *, /, >, <
    }

    private void realizarOperacion() {
        if (pilaOperandos.size() < 2) {
            imprimirErrorSemantico("Operación inválida", p.renglon);
            return;
        }

        String operando2 = pilaOperandos.pop();
        String operando1 = pilaOperandos.pop();
        String operador = pilaOperadores.pop();

        int tipoOperando1Idx = obtenerIndicePorToken(obtenerTokenPorTipo(operando1));
        int tipoOperando2Idx = obtenerIndicePorToken(obtenerTokenPorTipo(operando2));

        int resultado = 0;

        switch (operador) {
            case "+":
            case "-":
            case "*":
                resultado = tablaAritmetica[tipoOperando1Idx][tipoOperando2Idx];
                break;
            case "/":
                resultado = tablaDivision[tipoOperando1Idx][tipoOperando2Idx];
                break;
            case ">":
            case "<":
            case ">=":
            case "<=":
                resultado = tablaRelacional[tipoOperando1Idx][tipoOperando2Idx];
                break;
        }

        if (resultado == 605) {
            imprimirErrorSemantico("Error 605: incompatibilidad de tipos en operacion", p.renglon);
        } else if (resultado == 0) {
            imprimirErrorSemantico("Error 605: Incompatibilidad de tipos en operacion", p.renglon);
        } else {
            pilaOperandos.push(mapaTipos.get(resultado)); // Empuja el tipo resultante de la operación
        }
    }

    private String obtenerTipoPorValor(int token) {
        switch (token) {
            case 101:
                return "int";
            case 102://hace referencia al tipo real (double)
                return "double";
            case 206:
                return "bool";
            case 224:
                return "char";
            case 212:
                return "double";
            default:
                return "desconocido";
        }
    }

    private int obtenerTokenPorTipo(String tipo) {
        for (int token : mapaTipos.keySet()) {
            if (mapaTipos.get(token).equals(tipo)) {
                return token;
            }
        }
        return -1;
    }

    private int obtenerIndicePorToken(int token) {
        switch (token) {
            case 222:
                return 0; // int
            case 206:
                return 1; // bool
            case 224:
                return 2; // char
            case 212:
                return 3; // double
            default:
                return -1;
        }
    }

    private void imprimirErrorSemantico(String mensaje, int renglon) {
        System.out.println("Error semantico en la linea " + renglon + ": " + mensaje);
        errorSemantico = true;
    }

}
