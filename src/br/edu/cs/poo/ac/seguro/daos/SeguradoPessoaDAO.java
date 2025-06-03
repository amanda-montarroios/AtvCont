package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;

public class SeguradoPessoaDAO extends SeguradoDAO {

    public SeguradoPessoa buscar(String numero) {
        return (SeguradoPessoa) buscar(numero);
    }
}