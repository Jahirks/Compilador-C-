package Compilador2;

import java.io.RandomAccessFile;

class Analisis_Lexico {
    Nodo cabeza = null;
    Nodo p;
    int estado = 0, columna, valorMT, numRenglon = 1;
    int caracter = 0;
    String lexema = "";
    boolean errorEncontrado = false;

String archivo = "C:\\Users\\Min\\Documents\\NetBeansProjects\\COMPILADOR3\\src\\Compilador2\\codigo.txt";

    int matriz[][] = {
        //       L    D    .    +    -    *    /    >    <    =    (    )    ,    ;    '    "   EB  TAB  EOL  EOF   OC    {    }    :
        //       0    1    2    3    4    5    6    7    8    9   10   11   12   13   14   15   16   17   18   19   20   21   22   23
        /*0*/{   1,   2, 115, 103, 104, 105,   5,   9,  10,  11, 113, 114, 116, 117, 118,  12,   0,   0,   0,   0, 505, 122, 123, 124},
        /*1*/{   1,   1, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100},
        /*2*/{ 101,   2,   3, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101},
        /*3*/{ 500,   4, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500},
        /*4*/{ 102,   4, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102},
        /*5*/{ 106, 106, 106, 106, 106,   7,   6, 106, 106, 106, 106, 106, 106, 106, 106, 106, 106, 106, 106, 106, 106, 106, 106, 106},
        /*6*/{   6,   6,   6,   6,   6,   6,   6,   6,   6,   6,   6,   6,   6,   6,   6,   6,   6,   6,   0,   0,   6,   6,   6,   6},
        /*7*/{   7,   7,   7,   7,   7,   8,   7,   7,   7,   7,   7,   7,   7,   7,   7,   7,   7,   7,   7, 503, 503,   7,   7,   7},
        /*8*/{ 503, 503, 503, 503, 503,   8,   0, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503, 503},
        /*9*/{ 109, 109, 109, 109, 109, 109, 109, 126, 109, 110, 109, 109, 109, 109, 109, 109, 109, 109, 109, 109, 109, 109, 109, 109},
        /*10*/{107, 107, 107, 107, 107, 107, 107, 112, 125, 108, 107, 107, 107, 107, 107, 107, 107, 107, 107, 107, 107, 107, 107, 107},
        /*11*/{120, 120, 120, 120, 120, 120, 120, 120, 120, 111, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120},
        /*12*/{ 12,  12,  12,  12,  12,  12,  12,  12,  12,  12,  12,  12,  12,  12,  12, 121,  12,  12, 504, 504, 12,  12,  12,  12},};

    String palabrasReservadas[][]  = {
        //      0               1
        /*0*/ {"asm",           "200"},
        /*1*/ {"default",       "201"},
        /*2*/ {"for",           "202"},
        /*3*/ {"new",           "205"},
        /*4*/ {"delete",        "204"},
        /*5*/ {"static",        "205"},
        /*6*/ {"bool",          "206"},
        /*7*/ {"do",            "207"},
        /*8*/ {"goto",          "208"},
        /*9*/ {"private",       "209"},
        /*10*/ {"struct",       "210"},
        /*11*/ {"break",        "211"},
        /*12*/ {"double",       "212"},
        /*13*/ {"if",           "213"},
        /*14*/ {"protected",    "214"},
        /*15*/ {"switch",       "215"},
        /*16*/ {"unsigned",     "216"},
        /*17*/ {"case",         "217"},
        /*18*/ {"else",         "218"},
        /*19*/ {"public",       "219"},
        /*20*/ {"using",        "220"},
        /*21*/ {"catch",        "221"},
        /*22*/ {"int",          "222"},
        /*23*/ {"class",        "223"},
        /*24*/ {"char",         "224"},
        /*25*/ {"string",       "225"},
        /*26*/ {"return",       "226"},
        /*27*/ {"void",         "227"},
        /*28*/ {"short",        "228"},
        /*29*/ {"true",         "229"},
        /*30*/ {"false",        "230"},
        /*31*/ {"while",        "231"},
        /*32*/ {"continue",     "232"},
        /*33*/ {"float",        "233"},
        /*34*/ {"printf",       "234"},
        /*35*/ {"scanf",        "235"},
        /*37*/ {"cin",          "237"},
        /*38*/ {"cout",         "238"},
        /*39*/ {"main",         "239"}};

    String errores[][] = {
        //      0                                   1
        /*0*/ {"Se espera digito",                  "500"},
        /*1*/ {"Se espera un =",                    "501"},
        /*2*/ {"Se espera un = o un >",             "502"},
        /*3*/ {"Se espera cierre de comentario",    "503"},
        /*4*/ {"Se espera cierre de cadena",        "504"},
        /*5*/ {"Error Inesperado",                  "505"},};
    RandomAccessFile file = null;

    public Analisis_Lexico() {
        try {
            file = new RandomAccessFile(archivo, "r");
            while (caracter != -1) {
                caracter = file.read();
                if (Character.isLetter((char) caracter)) {
                    columna = 0;
                } else if (Character.isDigit((char) caracter)) {
                    columna = 1;
                } else {
                    switch ((char) caracter) {
                        case '.':
                            columna = 2;
                            break;
                        case '+':
                            columna = 3;
                            break;
                        case '-':
                            columna = 4;
                            break;
                        case '*':
                            columna = 5;
                            break;
                        case '/':
                            columna = 6;
                            break;
                        case '>':
                            columna = 7;
                            break;
                        case '<':
                            columna = 8;
                            break;
                        case '=':
                            columna = 9;
                            break;
                        case '(':
                            columna = 10;
                            break;
                        case ')':
                            columna = 11;
                            break;
                        case ',':
                            columna = 12;
                            break;
                        case ';':
                            columna = 13;
                            break;
                        case '\'':
                            columna = 14;
                            break;
                        case '"':
                            columna = 15;
                            break;
                        case ' ':
                            columna = 16;
                            break;
                        case 9:
                            columna = 17; 
                            break;
                        case 10: {
                            columna = 18;
                            numRenglon++;
                        }
                        break;
                        case 13: {
                            columna = 18;
                        }
                        break;
                        case '{': {
                            columna = 21;
                        }
                        break;
                        case '}': {
                            columna = 22;
                        }
                        break;
                        case ':': {
                            columna = 23;
                        }
                        break;
                        default:
                            columna = 20;
                            break;
                    }
                }
                valorMT = matriz[estado][columna];
                if (valorMT < 100) {
                    estado = valorMT;

                    if (estado == 0) {
                        lexema = "";
                    } else {
                        lexema = lexema + (char) caracter;
                    }
                } else if (valorMT >= 100 && valorMT < 500) {
                    if (valorMT == 100) {
                        validarSiEsPalabraReservada();
                    }
                    if (valorMT == 100 || valorMT == 101 || valorMT == 102 || valorMT == 106 || valorMT == 109 || valorMT == 107 || valorMT == 120 || valorMT == 119||(valorMT<300&&valorMT>199)) {
                        file.seek(file.getFilePointer() - 1);
                    } else if (caracter != 13){
                        lexema = lexema + (char) caracter;
                    }
                    insertarNodo();
                    estado = 0;
                    lexema = "";
                } else {
                    imprimir_Mensaje_Error();
                }
            }//fin while
            if (estado == 3 || estado == 7 || estado == 8 || estado == 12) {
                switch (estado) {
                    case 3:
                        valorMT = 500;
                    case 7:
                        valorMT = 503;
                    case 8:
                        valorMT = 503;
                    case 12:
                        valorMT = 504;
                }
                imprimir_Mensaje_Error_Nodo();
                System.out.println("    "+estado+" Nodo no cerrado");
            }
            imprimirNodos();
        } catch (Exception e) {
                System.out.println(e.getMessage());
        } finally {
            try {
                if (file != null) {
                    file.close();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void imprimirNodos() {
        p = cabeza;
        while (p != null) {
            System.out.println( " " +p.lexema + " " + p.token + " " + p.renglon);
            p = p.sig;
        }
    }

    private void validarSiEsPalabraReservada() {
        for (String[] palReservada : palabrasReservadas) {
            if (lexema.equals(palReservada[0])) {
                valorMT = Integer.valueOf(palReservada[1]);
            }
        }
    }

    private void imprimir_Mensaje_Error() {
        if (caracter != -1 && valorMT >= 500) {
            for (String[] errore : errores) {
                if (valorMT == Integer.valueOf(errore[1])) {
                    System.out.println("    El error encontrado es: " + errore[0]
                            + " Error " + valorMT + " Caracter " + caracter
                            + " En El Renglon " + numRenglon);
                }
            }
            errorEncontrado = true;
            estado = 0;
        }
    }

    private void imprimir_Mensaje_Error_Nodo() {
        for (String[] errore : errores) {
            if (valorMT == Integer.valueOf(errore[1])) {
                System.out.println("    El error encontrado es: " + errore[0]
                        + " Error " + valorMT + " Caracter " + caracter
                        + " En el Renglon " + numRenglon);
            }
        }
        errorEncontrado = true;
    }

    private void insertarNodo() {
        Nodo nodo = new Nodo(lexema, valorMT, numRenglon);
        if (cabeza == null) {
            cabeza = nodo;
            p = cabeza;
        } else {
            p.sig = nodo;
            p = nodo;
        }
    }
}

