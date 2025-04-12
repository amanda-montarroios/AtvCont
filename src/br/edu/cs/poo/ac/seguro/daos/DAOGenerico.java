/*0. Incluir no projeto a dependência PersistenciaObjetos.jar, as classes DAOGenerico e
SeguradoPessoaDAO, ambas no pacote br.edu.cs.poo.ac.seguro.daos.

1. Incluir o atributo privado String numero na classe Sinistro, com seus respectivos
métodos get/set. Este atributo identifica unicamente um sinistro. Não alterar o
construtor de sinistro para incluir este atributo!

2. Incluir o atributo privado String numero na classe Apolice, com seus respectivos
métodos get/set. Este atributo identifica unicamente uma apólice. Não alterar o
construtor de apólice para incluir este atributo!

3. Implementar as classes dadas abaixo seguindo o modelo da classe SeguradoPessoaDAO. Tais
classes devem estar todas no pacote br.edu.cs.poo.ac.seguro.daos. Uma ou outra coisa do
código da classe SeguradoPessoaDAO pode não ser completamente entendido, mas será devidamente
explicado no próximo assunto de POO - polimorfismo. Uma observação: os códigos das classes
DAO são muito parecidos, e serão generalizados na 2a unidade, quando o polimorfismo for
devidamente compreendido. Por enquanto, vamos conviver com estas "N-plicações" de código.*/

package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cesarschool.next.oo.persistenciaobjetos.CadastroObjetos;

public class DAOGenerico {
    protected CadastroObjetos cadastro;
}