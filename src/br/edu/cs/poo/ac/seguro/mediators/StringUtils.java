package br.edu.cs.poo.ac.seguro.mediators;

public class StringUtils {

    private StringUtils() {}

    public static boolean ehNuloOuBranco(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean temSomenteNumeros(String input) {
        return input != null && input.matches("\\d+");
    }
}