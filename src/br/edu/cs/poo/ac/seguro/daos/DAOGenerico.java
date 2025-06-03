package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cesarschool.next.oo.persistenciaobjetos.CadastroObjetos;
import br.edu.cs.poo.ac.seguro.entidades.Registro;

public abstract class DAOGenerico<T extends Registro> {

    private CadastroObjetos cadastro;

    public DAOGenerico() {
        this.cadastro = new CadastroObjetos(getClasseEntidade());
    }

    public abstract Class<T> getClasseEntidade();

    public T buscar(String id) {
        return (T) cadastro.buscar(id);
    }

    public boolean incluir(T entidade) {
        if (buscar(entidade.getIdUnico()) != null) {
            return false;
        } else {
            cadastro.incluir(entidade, entidade.getIdUnico());
            return true;
        }
    }

    public boolean alterar(T entidade) {
        if (buscar(entidade.getIdUnico()) == null) {
            return false;
        } else {
            cadastro.alterar(entidade, entidade.getIdUnico());
            return true;
        }
    }

    public boolean excluir(String id) {
        if (buscar(id) == null) {
            return false;
        } else {
            cadastro.excluir(id);
            return true;
        }
    }

    @SuppressWarnings("unchecked")
    public T[] buscarTodos() {
        Object[] objs = cadastro.buscarTodos();  // Retorna Serializable[] ou Object[]
        T[] array = (T[]) java.lang.reflect.Array.newInstance(getClasseEntidade(), objs.length);
        for (int i = 0; i < objs.length; i++) {
            array[i] = (T) objs[i];
        }
        return array;
    }
}