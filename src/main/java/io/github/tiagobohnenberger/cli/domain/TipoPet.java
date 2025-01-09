package io.github.tiagobohnenberger.cli.domain;

import java.util.EnumSet;
import java.util.Optional;

import lombok.Getter;

@Getter
public enum TipoPet {
    GATO,
    CACHORRO;

    public static Optional<TipoPet> parse(String tipo) {
        return EnumSet.allOf(TipoPet.class)
                .stream()
                .filter(tipoPetEnum -> tipoPetEnum.name().equalsIgnoreCase(tipo))
                .findFirst();
    }
}