package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cs.poo.ac.seguro.entidades.Segurado;
import br.edu.cs.poo.ac.seguro.daos.DAOGenerico;

public abstract class SeguradoDAO extends DAOGenerico<Segurado> {

    @Override
    public Class<Segurado> getClasseEntidade() {
        return Segurado.class;
    }
}