import java.util.Scanner;

public class AnalizadorFuncionAFD {

    
    enum Estado {
        q0_INICIO, q1_TIPO_RETORNO, q2_NOMBRE_FUNC, q3_PAREN_ABIERTO,
        q4_TIPO_PARAM, q5_NOMBRE_PARAM, q6_COMA, q7_PAREN_CERRADO,
        q8_LLAVE_ABIERTA, q9_ACEPTACION, qERROR
    }

    
    enum Token {
        TIPO_DATO, IDENTIFICADOR, PAREN_ABIERTO, PAREN_CERRADO,
        COMA, LLAVE_ABIERTA, LLAVE_CERRADA, DESCONOCIDO
    }

    public static void main(String[] args) {
        String[] pruebas = {
            "int sumar ( int a , int b ) { }",
            "void imprimir ( ) { }",
            "String obtenerNombre ( String id ) { }",
            "int calcular ( int a , ) { }",
            "void 123hola ( ) { }",
            "float dividir ( float x float y ) { }"
        };

        for (String funcion : pruebas) {
            System.out.println("Analizando: " + funcion);
            boolean esValida = analizarFuncion(funcion);
            System.out.println("Resultado: " + (esValida ? "CORRECTA " : "INCORRECTA "));
            System.out.println("-------------------------------------------------");
        }
    }

    public static boolean analizarFuncion(String entrada) {
        
        entrada = entrada.replaceAll("\\(", " ( ")
                         .replaceAll("\\)", " ) ")
                         .replaceAll(",", " , ")
                         .replaceAll("\\{", " { ")
                         .replaceAll("\\}", " } ");
                         
        
        String[] palabras = entrada.trim().split("\\s+");
        
        Estado estadoActual = Estado.q0_INICIO;

        for (String palabra : palabras) {
            if (palabra.isEmpty()) continue;
            
            Token token = identificarToken(palabra);
            estadoActual = transicion(estadoActual, token);
            
            if (estadoActual == Estado.qERROR) {
                return false;
            }
        }

        
        return estadoActual == Estado.q9_ACEPTACION;
    }

   
    private static Estado transicion(Estado actual, Token entrada) {
        switch (actual) {
            case q0_INICIO:
                return (entrada == Token.TIPO_DATO) ? Estado.q1_TIPO_RETORNO : Estado.qERROR;
            case q1_TIPO_RETORNO:
                return (entrada == Token.IDENTIFICADOR) ? Estado.q2_NOMBRE_FUNC : Estado.qERROR;
            case q2_NOMBRE_FUNC:
                return (entrada == Token.PAREN_ABIERTO) ? Estado.q3_PAREN_ABIERTO : Estado.qERROR;
            case q3_PAREN_ABIERTO:
                if (entrada == Token.TIPO_DATO) return Estado.q4_TIPO_PARAM;
                if (entrada == Token.PAREN_CERRADO) return Estado.q7_PAREN_CERRADO;
                return Estado.qERROR;
            case q4_TIPO_PARAM:
                return (entrada == Token.IDENTIFICADOR) ? Estado.q5_NOMBRE_PARAM : Estado.qERROR;
            case q5_NOMBRE_PARAM:
                if (entrada == Token.COMA) return Estado.q6_COMA;
                if (entrada == Token.PAREN_CERRADO) return Estado.q7_PAREN_CERRADO;
                return Estado.qERROR;
            case q6_COMA:
                return (entrada == Token.TIPO_DATO) ? Estado.q4_TIPO_PARAM : Estado.qERROR;
            case q7_PAREN_CERRADO:
                return (entrada == Token.LLAVE_ABIERTA) ? Estado.q8_LLAVE_ABIERTA : Estado.qERROR;
            case q8_LLAVE_ABIERTA:
                return (entrada == Token.LLAVE_CERRADA) ? Estado.q9_ACEPTACION : Estado.qERROR;
            default:
                return Estado.qERROR;
        }
    }

   
    private static Token identificarToken(String palabra) {
        if (palabra.equals("(")) return Token.PAREN_ABIERTO;
        if (palabra.equals(")")) return Token.PAREN_CERRADO;
        if (palabra.equals(",")) return Token.COMA;
        if (palabra.equals("{")) return Token.LLAVE_ABIERTA;
        if (palabra.equals("}")) return Token.LLAVE_CERRADA;
        
        
        if (palabra.matches("int|float|double|char|void|boolean|String|long")) {
            return Token.TIPO_DATO;
        }
        
      
        if (palabra.matches("^[a-zA-Z_$][a-zA-Z_$0-9]*$")) {
            return Token.IDENTIFICADOR;
        }
        
        return Token.DESCONOCIDO;
    }
}