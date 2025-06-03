package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cs.poo.ac.seguro.entidades.Veiculo;

public class VeiculoDAO extends DAOGenerico<Veiculo> {

    public VeiculoDAO() {
        super();
    }

    @Override
    public Class<Veiculo> getClasseEntidade() {
        return Veiculo.class;
    }
}