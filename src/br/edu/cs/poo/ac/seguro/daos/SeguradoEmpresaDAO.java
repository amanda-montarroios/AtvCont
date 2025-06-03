package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;

public class SeguradoEmpresaDAO extends SeguradoDAO {

    public SeguradoEmpresa buscar(String numero) {
        return (SeguradoEmpresa) buscar(numero);
    }
}