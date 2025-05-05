package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cesarschool.next.oo.persistenciaobjetos.CadastroObjetos;
import br.edu.cs.poo.ac.seguro.entidades.Apolice;

public class ApoliceDAO extends DAOGenerico {

    public ApoliceDAO() {
        cadastro = new CadastroObjetos(Apolice.class);
    }

    public Apolice buscar(String numero) {
        return (Apolice)cadastro.buscar(numero);
    }

    public boolean incluir(Apolice apolice) {
        if (buscar(apolice.getNumero()) != null) {
            return false;
        } else {
            cadastro.incluir(apolice, apolice.getNumero());
            return true;
        }
    }

    public boolean alterar(Apolice apolice) {
        if (buscar(apolice.getNumero()) == null) {
            return false;
        } else {
            cadastro.alterar(apolice, apolice.getNumero());
            return true;
        }
    }

    public boolean excluir(String numero) {
        if (buscar(numero) == null) {
            return false;
        } else {
            cadastro.excluir(numero);
            return true;
        }
    }
}
