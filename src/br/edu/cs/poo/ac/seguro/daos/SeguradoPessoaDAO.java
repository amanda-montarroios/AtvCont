package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cesarschool.next.oo.persistenciaobjetos.CadastroObjetos;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;

/*
 * As classes Segurado e SeguradoPessoa devem implementar Serializable.
 */
public class SeguradoPessoaDAO extends DAOGenerico {
    public SeguradoPessoaDAO() {
        cadastro = new CadastroObjetos(SeguradoPessoa.class);
    }
    public SeguradoPessoa buscar(String cpf) {
        return (SeguradoPessoa)cadastro.buscar(cpf);
    }
    public boolean incluir(SeguradoPessoa segurado) {
        if (buscar(segurado.getCpf()) != null) {
            return false;
        } else {
            cadastro.incluir(segurado, segurado.getCpf());
            return true;
        }
    }
    public boolean alterar(SeguradoPessoa segurado) {
        if (buscar(segurado.getCpf()) == null) {
            return false;
        } else {
            cadastro.alterar(segurado, segurado.getCpf());
            return true;
        }
    }
    public boolean excluir(String cpf) {
        if (buscar(cpf) == null) {
            return false;
        } else {
            cadastro.excluir(cpf);
            return true;
        }
    }

}