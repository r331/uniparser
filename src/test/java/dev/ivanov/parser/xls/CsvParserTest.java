package dev.ivanov.parser.xls;

import dev.ivanov.parser.Car;
import dev.ivanov.parser.Parser;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static java.util.List.of;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static org.junit.Assert.assertEquals;

public class CsvParserTest {
    @Test
    public void testXLS() {
        File file = new File(requireNonNull(getClass().getClassLoader().getResource("cars.xlsx")).getFile());
        Parser<Car, XSSFCell> parser = Parser.<Car, XSSFCell>builder()
                .from(new RawEntityReaderImpl(file, 0))
                .withAnnotationHandler(of())
                .withFieldHandler(of(new IntegerFieldHandler(), new StringFieldHandler()))
                .to(Car.class)
                .build();
        List<Car> cars = stream(parser.spliterator(), false).collect(toList());
        List<Car> expected = List.of(
                new Car("Ford", "E350", 1997),
                new Car("Mercury", "Cougar", 2000));
        assertEquals(expected, cars);
    }
}
