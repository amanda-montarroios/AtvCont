package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;

public class SeguradoEmpresaDAO extends SeguradoDAO<SeguradoEmpresa> {

    @Override
    public Class<SeguradoEmpresa> getClasseEntidade() {
        return SeguradoEmpresa.class;
    }

    public SeguradoEmpresa buscar(String id) {
        return (SeguradoEmpresa) super.buscar(id);
    }
}