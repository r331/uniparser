package dev.ivanov.parser.csv;

import dev.ivanov.parser.RawEntityReader;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import static java.util.List.of;

public class CSVEntityReaderImpl implements RawEntityReader<String> {
    private final BufferedReader br;
    private String nextLine;

    @SneakyThrows
    public CSVEntityReaderImpl(File file) {
        this.br = new BufferedReader(new FileReader(file));
        this.nextLine = br.readLine();
    }

    @Override
    public List<String> next() {
        return of(nextLine.split(","));
    }

    @Override
    @SneakyThrows
    public boolean hasNext() {
        nextLine = br.readLine();
        if (nextLine != null && nextLine.isBlank()) return hasNext();
        return nextLine != null;
    }

    @Override
    public void close() throws Exception {
        br.close();
    }
}