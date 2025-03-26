package Compilador2;

public class Nodo {
    public String lexema;
    public int token;
    public int renglon;
    public Nodo sig; // Apunta al siguiente nodo
    public Nodo ant; // Apunta al nodo anterior

    public Nodo(String lexema, int token, int renglon) {
        this.lexema = lexema;
        this.token = token;
        this.renglon = renglon;
        this.sig = null;
        this.ant = null; // Inicializar en null
    }
}
