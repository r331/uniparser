package dev.ivanov.parser;

import java.util.List;

public interface RawEntityReader<F> extends AutoCloseable {
    List<F> next();

    boolean hasNext();
}
