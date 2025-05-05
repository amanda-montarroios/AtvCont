package br.edu.cs.poo.ac.seguro.entidades;
import static br.edu.cs.poo.ac.seguro.entidades.PrecosAnosCategoria.*;

public enum CategoriaVeiculo {

    BASICO(1,"Veículo econômico", PA_BASICO),
    INTERMEDIARIO(2,"Veículo de categoria média", PA_INTERMEDIARIO),
    LUXO(3, "Veículo de luxo", PA_LUXO),
    SUPER_LUXO(4, "Veículo exclusivo", PA_SUPER_LUXO),
    ESPORTIVO(5, "Veículo esportivo", PA_ESPORTIVO);

    private int codigo;
    private String nome;
    private PrecoAno[] precosAnos;

    private CategoriaVeiculo(int codigo, String nome, PrecoAno[] precosAnos) {
        this.codigo = codigo;
        this.nome = nome;
        this.precosAnos = precosAnos;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public PrecoAno[] getPrecosAnos() {
        return precosAnos;
    }

    public static CategoriaVeiculo obter(int codigo) {
        for (CategoriaVeiculo c : CategoriaVeiculo.values()) {
            if (c.getCodigo() == codigo) {
                return c;
            }
        }
        return null;
    }
}