package br.edu.cs.poo.ac.seguro.daos;

import java.util.ArrayList;
import java.util.List;

import br.edu.cs.poo.ac.seguro.entidades.Sinistro;

public class SinistroDAO extends DAOGenerico<Sinistro> {

    @Override
    public Class<Sinistro> getClasseEntidade() {
        return Sinistro.class;
    }

    public Sinistro[] buscarPorNumeroApolice(String numeroApolice) {
        Sinistro[] todos = buscarTodos();
        List<Sinistro> filtrados = new ArrayList<>();
        for (Sinistro s : todos) {
            if (s.getNumeroApolice().equals(numeroApolice)) {
                filtrados.add(s);
            }
        }
        return filtrados.toArray(new Sinistro[0]);
    }
}