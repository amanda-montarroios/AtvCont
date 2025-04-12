package br.edu.cs.poo.ac.seguro.entidades;

public class CategoriaVeiculo{
    private String nome;

    public CategoriaVeiculo(String nome){
        this.nome = nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public String getNome(){
        return nome;
    }
}