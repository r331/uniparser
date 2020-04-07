package dev.ivanov.parser;

import dev.ivanov.parser.annotation.Parsed;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Car {
    @Parsed(index = 1)
    private String company;
    @Parsed(index = 2)
    private String model;
    @Parsed(index = 0)
    private Integer year;
}
