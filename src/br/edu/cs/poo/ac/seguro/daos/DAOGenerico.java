package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cesarschool.next.oo.persistenciaobjetos.CadastroObjetos;
import br.edu.cs.poo.ac.seguro.entidades.Registro;

public abstract class DAOGenerico<T extends Registro> {
    private CadastroObjetos cadastro;

    protected DAOGenerico() {
        Class<T> classeEntidade = getClasseEntidade();
        if (classeEntidade == null) {
            throw new IllegalStateException("getClasseEntidade() retornou null durante a construção do DAOGenerico. Verifique a implementação na subclasse.");
        }
        this.cadastro = new CadastroObjetos(classeEntidade);
    }

    public abstract Class<T> getClasseEntidade();

    public boolean incluir(T reg) {
        if (reg == null || reg.getIdUnico() == null || reg.getIdUnico().isEmpty()) {
            return false;
        }
        try {
            if (buscar(reg.getIdUnico()) != null) {
                return false;
            }
            this.cadastro.incluir(reg, reg.getIdUnico());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean alterar(T reg) {
        if (reg == null || reg.getIdUnico() == null || reg.getIdUnico().isEmpty()) {
            return false;
        }
        if (buscar(reg.getIdUnico()) == null) {
            return false;
        }
        try {
            this.cadastro.alterar(reg, reg.getIdUnico());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public T buscar(String idUnico) {
        if (idUnico == null || idUnico.isEmpty()) {
            return null;
        }
        Object obj = this.cadastro.buscar(idUnico);
        if (obj != null && getClasseEntidade().isInstance(obj)) {
            return (T) obj;
        }
        return null;
    }

    public Registro[] buscarTodos() {
        Object[] objetos = this.cadastro.buscarTodos(getClasseEntidade());
        if (objetos == null || objetos.length == 0) {
            return new Registro[0];
        }
        Registro[] registros = new Registro[objetos.length];
        for (int i = 0; i < objetos.length; i++) {
            registros[i] = (Registro) objetos[i];
        }
        return registros;
    }

    public boolean excluir(String idUnico) {
        if (idUnico == null || idUnico.isEmpty()) {
            return false;
        }
        if (buscar(idUnico) == null) {
            return false;
        }
        try {
            this.cadastro.excluir(idUnico);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}