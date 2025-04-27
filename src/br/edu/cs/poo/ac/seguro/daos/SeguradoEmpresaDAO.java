package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cesarschool.next.oo.persistenciaobjetos.CadastroObjetos;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;

public class SeguradoEmpresaDAO extends DAOGenerico{

    public SeguradoEmpresaDAO() {
        cadastro = new CadastroObjetos(SeguradoEmpresa.class);
    }

    public SeguradoEmpresa buscar(String cnpj) {
        return (SeguradoEmpresa) cadastro.buscar(cnpj);
    }

    public boolean incluir(SeguradoEmpresa seguradoEmpresa) {
        if (buscar(seguradoEmpresa.getCnpj()) != null){
            return false;
        }
        else {
            cadastro.incluir(seguradoEmpresa, seguradoEmpresa.getCnpj());
            return true;
        }
    }

    public boolean alterar(SeguradoEmpresa seguradoEmpresa) {
        if (buscar(seguradoEmpresa.getCnpj()) == null) {
            return false;
        }
        else {
            cadastro.alterar(seguradoEmpresa, seguradoEmpresa.getCnpj());
            return true;
        }
    }

    public boolean excluir(String cnpj) {
        if (buscar(cnpj) == null) {
            return false;
        }
        else {
            cadastro.excluir(cnpj);
            return true;
        }
    }
}
