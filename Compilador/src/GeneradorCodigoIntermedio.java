import java.io.*;
import java.util.regex.*;
import java.util.*;

public class GeneradorCodigoIntermedio {

    public static void main(String[] args) {
        try {
            generarCodigoEnsambladorDesdeArchivo("C:\\Users\\Min\\Documents\\NetBeansProjects\\COMPILADOR3\\src\\Compilador2\\Codigo_optimizado.txt");
            System.out.println("Codigo ensamblador generado exitosamente.");
        } catch (IOException e) {
            System.err.println("Error al generar el código ensamblador: " + e.getMessage());
        }
    }

    // Método para generar código ensamblador desde un archivo de código C++
    public static void generarCodigoEnsambladorDesdeArchivo(String nombreArchivo) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo));
        BufferedWriter writer = new BufferedWriter(new FileWriter("codigo.asm"));

        // Escribimos la sección de datos
        writer.write("section .data\n");
        writer.write("    newline db 0x0D, 0x0A, 0\n");
        writer.write("\n");

        // Escribimos la sección de código
        writer.write("section .text\n");
        writer.write("    global _start\n");
        writer.write("_start:\n");

        Map<String, String> variables = new HashMap<>();
        String line;
        boolean insideMain = false;

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("int main()")) {
                insideMain = true;
                continue; // Ignorar la primera línea
            }

            if (insideMain && !line.equals("return 0;")) {
                if (!line.isEmpty()) {
                    procesarLineaC(line, writer, variables);
                }
            }
        }

        // Código para finalizar el programa
        writer.write("\n    ; Fin del programa\n");
        writer.write("    mov ax, 4C00h  ; Terminar el programa\n");
        writer.write("    int 21h\n");

        writer.close();
        reader.close();
    }

    // Procesar cada línea de código C++ y traducirla a ensamblador
    public static void procesarLineaC(String line, BufferedWriter writer, Map<String, String> variables) throws IOException {
        // Detectar declaraciones de variables (int)
        if (line.matches("^int\\s+[a-zA-Z_][a-zA-Z0-9_]*\\s*=\\s*\\d+;$")) {
            Pattern pattern = Pattern.compile("int\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s*=\\s*(\\d+);");
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                String varName = matcher.group(1);
                String varValue = matcher.group(2);
                variables.put(varName, varValue);
                writer.write("    mov ax, " + varValue + "  ; Asignación de " + varName + "\n");
                writer.write("    mov [" + varName + "], ax  ; Guardar en la variable\n");
            }
        } 
        // Detectar estructuras IF
        else if (line.startsWith("if")) {
            Pattern pattern = Pattern.compile("if\\s*\\((.*)\\)\\s*\\{");
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                String condition = matcher.group(1).trim();
                if (condition.contains(">")) {
                    String[] parts = condition.split(">");
                    String leftOperand = parts[0].trim();
                    String rightOperand = parts[1].trim();
                    String etiquetaInicio = "IF_START";
                    String etiquetaFin = "IF_END";

                    writer.write(etiquetaInicio + ":\n");
                    writer.write("    mov ax, [" + leftOperand + "]\n");
                    writer.write("    cmp ax, " + rightOperand + "  ; Comparar " + leftOperand + " con " + rightOperand + "\n");
                    writer.write("    jle " + etiquetaFin + "  ; Si la condición es falsa, saltar a IF_END\n");
                    writer.write("    lea dx, msgGreater\n");
                    writer.write("    mov ah, 09h  ; Imprimir mensaje\n");
                    writer.write("    int 21h\n");
                    writer.write("    jmp " + etiquetaFin + "\n");
                    writer.write(etiquetaFin + ":\n");
                }
            }
        } 
        // Detectar estructuras ELSE
        else if (line.startsWith("else")) {
            writer.write("    lea dx, msgSmaller\n");
            writer.write("    mov ah, 09h  ; Imprimir mensaje\n");
            writer.write("    int 21h\n");
        }
        // Detectar estructuras WHILE
        else if (line.startsWith("while")) {
            Pattern pattern = Pattern.compile("while\\s*\\((.*)\\)\\s*\\{");
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                String condition = matcher.group(1).trim();
                if (condition.contains(">")) {
                    String[] parts = condition.split(">");
                    String leftOperand = parts[0].trim();
                    String rightOperand = parts[1].trim();
                    String etiquetaInicio = "WHILE_START";
                    String etiquetaFin = "WHILE_END";

                    writer.write(etiquetaInicio + ":\n");
                    writer.write("    mov ax, [" + leftOperand + "]\n");
                    writer.write("    cmp ax, " + rightOperand + "  ; Comparar " + leftOperand + " con " + rightOperand + "\n");
                    writer.write("    jle " + etiquetaFin + "  ; Si la condición es falsa, salir del while\n");
                    writer.write("    lea dx, number\n");
                    writer.write("    mov ah, 09h  ; Imprimir número\n");
                    writer.write("    int 21h\n");
                    writer.write("    dec word [" + leftOperand + "]  ; Decrementar número\n");
                    writer.write("    jmp " + etiquetaInicio + "  ; Volver al inicio del while\n");
                    writer.write(etiquetaFin + ":\n");
                }
            }
        } 
    }
}
