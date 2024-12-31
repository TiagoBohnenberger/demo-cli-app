package br.com.alura.domain;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByPosition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pet {
    private Long id;

    @CsvCustomBindByPosition(position = 0, converter = TipoPetConverter.class, required = true)
    private TipoPet tipo;

    @CsvBindByPosition(position = 1)
    private String nome;

    @CsvBindByPosition(position = 2)
    private String raca;

    @CsvBindByPosition(position = 3)
    private Integer idade;

    @CsvBindByPosition(position = 4)
    private String cor;

    @CsvBindByPosition(position = 5)
    private Float peso;

    private Abrigo abrigo;
}