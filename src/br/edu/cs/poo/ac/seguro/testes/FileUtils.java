package br.edu.cs.poo.ac.seguro.testes;

import java.io.File;

public class FileUtils {

    public static void limparDiretorio(String diretorio) {
        File dir = new File(diretorio);
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                file.delete();
            }
        }
    }
}