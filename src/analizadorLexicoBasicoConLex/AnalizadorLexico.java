
import java.util.Scanner;

public class AnalizadorLexico {
    public static boolean validadorFuncion (String cadena) {
        int estado = 0;
        boolean TieneNombre = false;

        for (int i = 0; i < cadena.length(); i++) {
            char c = cadena.charAt(i);

            switch (estado) {
                case 0:
                    if (c == 'v') estado = 1;
                    else if (c == ' ') estado = 0;
                    else return false;
                    break;

                case 1:
                    if (c == 'o') estado = 2;
                    else return false;
                    break;

                case 2:
                    if (c == 'i') estado = 3;
                    else return false;
                    break;

                case 3:
                    if (c == 'd') estado = 4;
                    else return false;
                    break;

                case 4:
                    if (c == '_') estado = 5;
                    else return false;
                    break;

                case 5:
                    if (Character.isLetterOrDigit(c) || c == '_') {
                        estado = 5;
                        TieneNombre = true;
                    } else if (c == ' ' && !TieneNombre) {
                        estado = 5;
                    } else if (c == '(' && TieneNombre) {
                        estado = 6;
                    } else {
                        return false;
                    }
                    break;
            }
        }

        return estado == 9;

        }
    }

    public static void main(String[] args) {


    }

