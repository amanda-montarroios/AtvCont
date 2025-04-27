package br.edu.cs.poo.ac.seguro.mediators;

import java.time.LocalDate;
import br.edu.cs.poo.ac.seguro.daos.SeguradoPessoaDAO;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;

@SuppressWarnings("unused")
public class SeguradoPessoaMediator {

    private SeguradoMediator seguradoMediator = SeguradoMediator.getInstancia();
    private SeguradoPessoaDAO dao = new SeguradoPessoaDAO();

    private static SeguradoPessoaMediator instancia = new SeguradoPessoaMediator();

    private SeguradoPessoaMediator() {
    }

    public static SeguradoPessoaMediator getInstancia() {
        return instancia;
    }

    public String validarCpf(String cpf) {
        if (StringUtils.ehNuloOuBranco(cpf))
            return "CPF deve ser informado";
        if (cpf.length() != 11)
            return "CPF deve ter 11 caracteres";
        if (!ValidadorCpfCnpj.ehCpfValido(cpf))
            return "CPF com dígito inválido";

        return null;
    }

    public String validarRenda(double renda) {
        if (renda < 0)
            return "Renda deve ser maior ou igual à zero";
        return null;
    }

    public String validarSeguradoPessoa(SeguradoPessoa seg) {
        if (seg == null) return "Segurado não pode ser nulo";
        if (StringUtils.ehNuloOuBranco(seg.getNome()))
            return "Nome deve ser informado";
        if (seg.getEndereco() == null)
            return "Endereço deve ser informado";
        if (seg.getDataNascimento() == null)
            return "Data do nascimento deve ser informada";
        String erroCpf = validarCpf(seg.getCpf());
        if (erroCpf != null)
            return erroCpf;
        String erroRenda = validarRenda(seg.getRenda());
        if (erroRenda != null)
            return erroRenda;
        return null;
    }

    public String incluirSeguradoPessoa(SeguradoPessoa seg) {
        String erro = validarSeguradoPessoa(seg);
        if (erro != null) return erro;

        boolean sucesso = dao.incluir(seg);
        if (!sucesso) {
            return "CPF do segurado pessoa já existente";
        }
        return null;
    }

    public String alterarSeguradoPessoa(SeguradoPessoa seg) {
        String erro = validarSeguradoPessoa(seg);
        if (erro != null) return erro;

        boolean sucesso = dao.alterar(seg);
        if (!sucesso) {
            return "CPF do segurado pessoa não existente";
        }
        return null;
    }

    public String excluirSeguradoPessoa(String cpf) {
        boolean sucesso = dao.excluir(cpf);
        if (!sucesso) {
            return "CPF do segurado pessoa não existente";
        }
        return null;
    }

    public SeguradoPessoa buscarSeguradoPessoa(String cpf) {
        return dao.buscar(cpf);
    }
}
