package Compilador2;

 class Analisis_Sintactico {

    boolean error_Sintaxis = false;
    Nodo p = null;
    
    public void Sintaxis() {
            if (p != null && (p.token == 222 || p.token == 224 || p.token == 206 || p.token == 233 || p.token == 212)) { //Tipo de dato
                p = p.sig;
                if (p != null && p.token == 239) { //Main
                    p = p.sig;
                    if (p != null && p.token == 113) {// (
                        p = p.sig;
                        if (p != null && p.token == 114) {//)
                            p = p.sig;
                            if (p != null && p.token == 122) {// {
                                p = p.sig;
                                if (p != null && TipoDato()) {//Bloque
                                    while (p != null && !error_Sintaxis && p.token != 226) {
                                        Bloque();
                                    }
                                    if (p != null && p.token == 226) { //Return
                                        p = p.sig;
                                        if (p != null && p.token == 101) {//Digito
                                            p = p.sig;
                                            if (p != null && p.token == 117) {// ;
                                                p = p.sig;
                                                if (p != null && p.token == 123) {// }
                                                    p=p.sig;
                                                }else {
                                                    Mensaje_Error("Se espera }");
                                                }if(p!=null){
                                                    Mensaje_Error("CODIGO NO ADMITIDO");
                                                }
                                            } else {
                                                Mensaje_Error("Se espera ;");
                                            }
                                        } else {
                                            Mensaje_Error("Se espera un digito");
                                        }
                                    } else {
                                        if (!error_Sintaxis) {
                                            Mensaje_Error("Se espera return");
                                        }
                                    }
                                } else {
                                    Mensaje_Error("Se espera una sentencia valida");
                                }
                            } else {
                                Mensaje_Error("Se espera {");
                            }
                        } else {
                            Mensaje_Error("Se espera )");
                        }
                    } else {
                        Mensaje_Error("Se espera (");
                    }
                } else {
                    Mensaje_Error("Se espera main");
                }
            } else {
                Mensaje_Error("Se espera tipo de dato");
            }
    }

    private boolean TipoDato() {
        if (p.token == 237 || p.token == 238 || p.token == 222 || p.token == 224 || p.token == 206 || p.token == 233 || p.token == 212 || p.token == 100 || p.token == 213 || p.token == 231 || p.token == 207) {
            return true;
        } else {
            return false;
        }
    }

    private void Bloque() {
        if (TipoDato()) {
            switch (p.token) {
                case 237:
                    p = p.sig;
                    Leer();
                    break;
                case 238:
                    p = p.sig;
                    Imprimir();
                    break;
                case 222:
                    p = p.sig;
                    Declaracion();
                    break;
                case 224:
                    p = p.sig;
                    Declaracion();
                    break;
                case 206:
                    p = p.sig;
                    Declaracion();
                    break;
                case 233:
                    p = p.sig;
                    Declaracion();
                    break;
                case 212:
                    p = p.sig;
                    Declaracion();
                    break;
                case 100:
                    p = p.sig;
                    Inicializar();
                    break;
                case 213:
                    p = p.sig;
                    Condicional();
                    break;
                case 231:
                    p = p.sig;
                    While();
                    break;
                case 207:
                    p = p.sig;
                    DoWhile();
                    break;
            }
        } else {
            error_Sintaxis = true;
        }
    }

    private void Leer() {
        if (p != null && p.token == 126) {
            p = p.sig;
            if (p != null && p.token == 100) {
                p = p.sig;
                if (p != null && p.token == 126) {
                    Leer();
                } else if (p != null && p.token == 117) {
                    p = p.sig;
                } else {
                    Mensaje_Error("Se espera ; o >>");
                }
            } else {
                Mensaje_Error("Se espera variable");
            }
        } else {
            Mensaje_Error("Se espera >>");
        }
    }

    private void Imprimir() {
        if (p != null && p.token == 125) {
            p = p.sig;
            if (p != null && (p.token == 121 || p.token == 100)) { 
                p = p.sig;
                if (p != null && p.token == 125) {
                    Imprimir();
                } else if (p != null && p.token == 117) {
                    p = p.sig;
                } else {
                    Mensaje_Error("Se espera ; o <<");
                }
            } else {
                Mensaje_Error("Se espera variable o cadena");
            }
        } else {
            Mensaje_Error("Se espera <<");
        }
    }

    private void Declaracion() {
        if (p != null && p.token == 100) {
            p = p.sig;
            if (p != null && (p.token == 117 || p.token == 116)) {
                if (p != null && p.token == 116) {
                    p = p.sig;
                    Declaracion();
                } else if (p != null && p.token == 117) {
                    p = p.sig;
                } else {
                    Mensaje_Error("Se espera ;");
                }
            } else {
                Mensaje_Error("Se espera , o ;");
            }
        } else {
            Mensaje_Error("Se espera variable");
        }
    }

    private void Inicializar() {
        if (p != null && p.token == 120) {
            p = p.sig;
            if (p != null && (p.token == 121 || p.token == 101 || p.token == 102 || p.token == 229 || p.token == 230 ||p.token==100)) {
                p = p.sig;
                if (p != null && (p.token == 103 || p.token == 104 || p.token == 105 || p.token == 106 || p.token == 113)) {
                    p = p.sig;
                    Operacion();
                } else if (p != null && p.token == 117) {
                    p = p.sig;
                } else {
                    Mensaje_Error("Se espera ;");
                }
            } else {
                Mensaje_Error("Se espera tipo de variable");
            }
        } else {
            Mensaje_Error("Se espera tipo de dato o igualar el dato");
        }
    }

    private void Operacion() {
        if (p != null && (p.token == 121 || p.token == 101 || p.token == 102 || p.token == 229 || p.token == 230 || p.token == 100)) {
            p = p.sig;
            if (p != null && (p.token == 103 || p.token == 104 || p.token == 105 || p.token == 106)) {
                p = p.sig;
                Operacion();
            } else if (p != null && p.token == 117) {
                p = p.sig;
            } else {   
                Mensaje_Error("Se espera ;");
            }
        } else {
            Mensaje_Error("Se espera variable o digito");
        }
    }

    private void Condicional() {

        if (p != null && p.token == 113) {
            p = p.sig;
            if (p != null && p.token == 100) {
                p = p.sig;
                if (p != null && (p.token == 107 || p.token == 108 || p.token == 109 || p.token == 110|| p.token == 111 || p.token == 112)) {
                    p = p.sig;
                    if (p != null && (p.token == 100 || p.token == 101 || p.token == 102 || p.token == 121 || p.token == 229 || p.token == 230)) {
                        p = p.sig;
                        if (p != null && p.token == 114) {
                            p = p.sig;
                            if (p != null && p.token == 122) {
                                p = p.sig;
                                if (p != null && TipoDato()) {
                                    Bloque();
                                    if (p != null && p.token == 123) {
                                        p = p.sig;
                                        if (p != null && p.token == 218) {
                                            p = p.sig;
                                            OC();
                                        }
                                    } else {
                                        Mensaje_Error("Se espera }");
                                    }
                                } else {
                                    Mensaje_Error("Sentencia Invalida");
                                }
                            } else {
                                Mensaje_Error("Se espera {");
                            }
                        } else {
                            Mensaje_Error("Se espera )");
                        }
                    } else {
                        Mensaje_Error("Se espera variable");
                    }
                } else {
                    Mensaje_Error("Se espera operador");
                }
            } else {
                Mensaje_Error("Se espera condicion");
            }
        } else {
            Mensaje_Error("Se espera (");
        }
    }

    private void OC() {
        if (p != null && p.token == 122) {
            p = p.sig;
            if (p != null && TipoDato()) {
                Bloque();
                if (p != null && p.token == 123) {
                    p = p.sig;
                } else {
                    if (!error_Sintaxis) {
                        System.out.println("Se espera }");
                    }
                    error_Sintaxis = true;
                }
            } else {
                Mensaje_Error("Sentencia Invalida");
            }
        } else {    
            Mensaje_Error("Se espera {");
        }
    }

    private void While() {
        if (p != null && p.token == 113) {
            p = p.sig;
            if (p != null && p.token == 100) {
                p = p.sig;
                if (p != null && (p.token == 107 || p.token == 108 || p.token == 109 || p.token == 110 || p.token == 111 || p.token == 112)) {
                    p = p.sig;
                    if (p != null && (p.token == 100 || p.token == 101 || p.token == 102 || p.token == 121 || p.token == 229 || p.token == 230)) {
                        p = p.sig;
                        if (p != null && p.token == 114) {
                            p = p.sig;
                            if (p != null && p.token == 122) {
                                p = p.sig;
                                if (p != null && TipoDato()) {
                                    Bloque();
                                    if (p != null && p.token == 123) {
                                        p = p.sig;
                                    } else {
                                        Mensaje_Error("Se espera }");
                                    }
                                } else {
                                    Mensaje_Error("Sentencia Invalida");
                                }
                            } else {
                                Mensaje_Error("Se espera {");
                            }
                        } else {
                            Mensaje_Error("Se espera )");
                        }
                    } else {
                        Mensaje_Error("Se espera variable");
                    }
                } else {
                    Mensaje_Error("Se espera operador");
                }
            } else {
                Mensaje_Error("Se espera condicion");
            }
        } else {
            Mensaje_Error("Se espera (");
        }
    }

    private void DoWhile() {
        if (p != null && p.token == 122) {
            p = p.sig;
            if (p != null && TipoDato()) {
                Bloque();
                if (p != null && p.token == 123) {
                    p = p.sig;
                    if (p != null && p.token == 231) {
                        p = p.sig;
                        if (p != null && p.token == 113) {
                            p = p.sig;
                            if (p != null && p.token == 100) {
                                p = p.sig;
                                if (p != null && (p.token == 107 || p.token == 108 || p.token == 109 || p.token == 110 || p.token == 111 || p.token == 112)) {
                                    p = p.sig;
                                    if (p != null && (p.token == 100 || p.token == 101 || p.token == 102 || p.token == 121 || p.token == 229|| p.token == 230)) {
                                        p = p.sig;
                                        if (p != null && p.token == 114) {
                                            p = p.sig;
                                            if (p != null && p.token == 117) {
                                                p = p.sig;
                                            } else {
                                                Mensaje_Error("Se espera ;");
                                            }
                                        } else {
                                            Mensaje_Error("Se espera )");
                                        }
                                    } else {
                                        Mensaje_Error("Se espera variable");
                                    }
                                } else {
                                    Mensaje_Error("Se espera operador");
                                }
                            } else {
                                Mensaje_Error("Se espera condición");
                            }
                        } else {
                            Mensaje_Error("Se espera (");
                        }
                    } else {
                        Mensaje_Error("Se espera While");
                    }
                } else {
                    if (!error_Sintaxis) {
                        System.out.println("Se espera }");
                    }
                    error_Sintaxis = true;
                }
            } else {
                Mensaje_Error("Sentencia Invalida");
            }
        } else {
            Mensaje_Error("Se espera {");
        }
    }
    
    private void Mensaje_Error(String valor){
        
        error_Sintaxis = true;
        if (p==null) {
            System.out.println(valor);
        }else{
            System.out.println(valor + " en renglon " + p.renglon);
        }
    }
    
}
