package dev.ivanov.parser.xls;

import dev.ivanov.parser.FieldHandler;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;

public class StringFieldHandler implements FieldHandler<String, XSSFCell> {
    @Override
    public String process(XSSFCell value) {
        if (value.getCellType() == CellType.STRING)
            return value.getStringCellValue();
        return null;
    }

    @Override
    public Class<String> getFieldType() {
        return String.class;
    }
}
