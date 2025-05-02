package br.edu.cs.poo.ac.seguro.testes;

import br.edu.cs.poo.ac.seguro.mediators.SeguradoMediator;

public class TesteMediator {
    public static void main(String[] args) {
        SeguradoMediator mediator = SeguradoMediator.getInstancia();
        System.out.println("Mediator criado: " + mediator);
    }

    protected void setUp() {
    }
}