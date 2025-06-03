package br.edu.cs.poo.ac.seguro.mediators;

public class StringUtils {
    private StringUtils() {}
    public static boolean ehNuloOuBranco(String str) {
        if (str == null || str.trim().isEmpty()) return true;
        else return false;
    }
    public static boolean temSomenteNumeros(String input) {
        if (input != null && input.matches("\\d+")) return true;
        else return false;
    }
}