package dev.ivanov.parser.xls;

import dev.ivanov.parser.FieldHandler;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;

public class IntegerFieldHandler implements FieldHandler<Integer, XSSFCell> {
    @Override
    public Integer process(XSSFCell value) {
        if (value.getCellType() == CellType.NUMERIC)
            return (int) value.getNumericCellValue();
        return null;
    }

    @Override
    public Class<Integer> getFieldType() {
        return Integer.class;
    }
}