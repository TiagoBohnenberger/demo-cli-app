package br.com.alura.domain;

import lombok.Data;

@Data
public class Pet {
    private Long id;
    private TipoPet tipo;
    private String nome;
    private String raca;
    private Integer idade;
    private String cor;
    private Float peso;
    private Abrigo abrigo;
}