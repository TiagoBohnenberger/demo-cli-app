package br.com.alura.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import br.com.alura.util.Try;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

import static br.com.alura.util.Functions.println;

@Log4j2
@UtilityClass
public class CsvReader {
    public static <T> List<T> read(String nomeArquivo, Class<T> type) {
        try {
            FileReader fileReader = new FileReader(nomeArquivo);
            return read(fileReader, type);

        } catch (IOException e) {
            println("Erro ao carregar o arquivo: " + nomeArquivo);
            return Collections.emptyList();
        }
    }

    public static <T> List<T> read(FileReader fileReader, Class<T> type) {
        try {
            BufferedReader reader = new BufferedReader(fileReader);

            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                    .withType(type)
                    .build();

            return csvToBean.parse();
        } finally {
            Try.of(fileReader::close).orElse(ex -> log.error("Error on auto closing", ex));
        }
    }
}