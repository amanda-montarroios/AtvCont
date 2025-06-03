package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;

public class SeguradoPessoaDAO extends DAOGenerico<SeguradoPessoa> {

    public SeguradoPessoaDAO() {
        super();
    }

    @Override
    public Class<SeguradoPessoa> getClasseEntidade() {
        return SeguradoPessoa.class;
    }
}