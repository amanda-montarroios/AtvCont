package br.edu.cs.poo.ac.seguro.mediators;

import java.util.Comparator;

import br.edu.cs.poo.ac.seguro.entidades.Sinistro;

public class ComparadorSinistroSequencial implements Comparator<Sinistro> {

    @Override
    public int compare(Sinistro s1, Sinistro s2) {
        return Integer.compare(s1.getSequencial(), s2.getSequencial());
    }
}
