package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;

public class SeguradoEmpresaDAO extends DAOGenerico<SeguradoEmpresa> {

    public SeguradoEmpresaDAO() {
        super();
    }

    @Override
    public Class<SeguradoEmpresa> getClasseEntidade() {
        return SeguradoEmpresa.class;
    }
}