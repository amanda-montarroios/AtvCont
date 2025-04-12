// Source code is decompiled from a .class file using FernFlower decompiler.
package br.edu.cesarschool.next.oo.persistenciaobjetos;

import java.io.Serializable;

public class EntidadeTesteCadastroObjetos implements Serializable {
    private static final long serialVersionUID = 1L;
    private String codigo;
    private String nome;

    public EntidadeTesteCadastroObjetos(String codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
