package br.com.alura.domain;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Abrigo {

    private Long id;
    private String nome;
    private String telefone;
    private String email;
    private List<Pet> pets = new ArrayList<>();
}