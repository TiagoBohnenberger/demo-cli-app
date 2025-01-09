package io.github.tiagobohnenberger.cli.domain;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;

public class TipoPetConverter extends AbstractBeanField<TipoPet, Integer> {
    @Override
    protected TipoPet convert(String tipoPet) throws CsvConstraintViolationException {
        return TipoPet.parse(tipoPet).orElseThrow(() -> new CsvConstraintViolationException(tipoPet, "Tipo de pet " + tipoPet + " não existente"));
    }
}