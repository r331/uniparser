package dev.ivanov.parser;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@ToString
public class Pair<K, V> {
    final K first;
    final V second;

    public static <K, V> Pair<K, V> pair(K first, V second) {
        return new Pair<>(first, second);
    }
}
