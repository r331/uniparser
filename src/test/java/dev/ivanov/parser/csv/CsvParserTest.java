package dev.ivanov.parser.csv;

import dev.ivanov.parser.AnnotationProcessorImpl;
import dev.ivanov.parser.Car;
import dev.ivanov.parser.Parser;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static java.util.List.of;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static org.junit.Assert.assertEquals;

public class CsvParserTest {
    @Test
    public void testCSV() {
        File file = new File(getClass().getClassLoader().getResource("cars.csv").getFile());
        Parser<Car, String> parser = Parser.<Car, String>builder()
                .withRawEntityReader(new RowEntityReaderImpl(file))
                .withAnnotationProcessor(new AnnotationProcessorImpl<>(List.of()))
                .withFieldHandler(of(
                        new IntegerFieldHandler(),
                        new StringFieldHandler()))
                .to(Car.class)
                .build();
        List<Car> cars = stream(parser.spliterator(), false).collect(toList());
        List<Car> expected = List.of(
                new Car("Ford", "E350", 1997),
                new Car("Mercury", "Cougar", 2000));
        assertEquals(expected, cars);
    }
}
