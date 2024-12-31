package br.com.alura.service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.IntStream;

import br.com.alura.domain.Pet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CsvReaderTest {

    @Test
    void read() throws FileNotFoundException {
        Path path = Path.of("pets.csv");

        FileReader fileReader = new FileReader(path.toFile());

        List<Pet> pets = CsvReader.read(fileReader, Pet.class);

        IntStream.rangeClosed(0, pets.size() - 1)
                .forEach(index -> System.out.println((index + 1) + " -> " + pets.get(index)));

        assertEquals(8, pets.size());
    }
}