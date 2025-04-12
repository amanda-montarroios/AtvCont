// Source code is decompiled from a .class file using FernFlower decompiler.
package br.edu.cesarschool.next.oo.persistenciaobjetos;

public class ProgramaTesteCadastroObjetos {
    public ProgramaTesteCadastroObjetos() {
    }

    public static void main(String[] args) {
        String codigo1 = "001";
        testeIncluir(codigo1);
        testeIncluir("002");
        testeIncluir("003");
        testeIncluirFilho("004");
        testeAlterar(codigo1);
        testeExcluir(codigo1);
    }

    private static void testeIncluirFilho(String codigo) {
        CadastroObjetos cadastro = new CadastroObjetos(EntidadeTesteCadastroObjetos.class);
        EntidadeFilha ent = new EntidadeFilha(codigo, "Entidade " + codigo, "Atributo filho");
        cadastro.incluir(ent, ent.getCodigo());
        EntidadeTesteCadastroObjetos entLer = (EntidadeTesteCadastroObjetos)cadastro.buscar(codigo);
        System.out.println(entLer.getCodigo());
        System.out.println(entLer.getNome());
    }

    private static void testeIncluir(String codigo) {
        CadastroObjetos cadastro = new CadastroObjetos(EntidadeTesteCadastroObjetos.class);
        EntidadeTesteCadastroObjetos ent = new EntidadeTesteCadastroObjetos(codigo, "Entidade " + codigo);
        cadastro.incluir(ent, ent.getCodigo());
        EntidadeTesteCadastroObjetos entLer = (EntidadeTesteCadastroObjetos)cadastro.buscar(codigo);
        System.out.println(entLer.getCodigo());
        System.out.println(entLer.getNome());
    }

    private static void testeAlterar(String codigo) {
        CadastroObjetos cadastro = new CadastroObjetos(EntidadeTesteCadastroObjetos.class);
        EntidadeTesteCadastroObjetos ent = new EntidadeTesteCadastroObjetos(codigo, "Entidade " + codigo + " alterado");
        cadastro.alterar(ent, ent.getCodigo());
        EntidadeTesteCadastroObjetos entLer = (EntidadeTesteCadastroObjetos)cadastro.buscar(codigo);
        System.out.println(entLer.getCodigo());
        System.out.println(entLer.getNome());
    }

    private static void testeExcluir(String codigo) {
        CadastroObjetos cadastro = new CadastroObjetos(EntidadeTesteCadastroObjetos.class);
        cadastro.excluir(codigo);
        EntidadeTesteCadastroObjetos entLer = (EntidadeTesteCadastroObjetos)cadastro.buscar(codigo);
        if (entLer == null) {
            System.out.println("Excluído com sucesso!");
        } else {
            System.out.println("Não excluído!");
        }

    }
}

