package dev.ivanov.parser.xls;

import dev.ivanov.parser.RawEntityReader;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.Collections.emptyList;

public class RawEntityReaderImpl implements RawEntityReader<XSSFCell> {
    private final XSSFWorkbook myExcelBook;
    private final XSSFSheet sheet;
    private final Iterator<Row> xssfRowIterator;

    @SneakyThrows
    public RawEntityReaderImpl(File file, int sheet) {
        myExcelBook = new XSSFWorkbook(new FileInputStream(file));
        this.sheet = myExcelBook.getSheetAt(sheet);
        this.xssfRowIterator = this.sheet.rowIterator();
    }

    @Override
    public void close() throws Exception {
        myExcelBook.close();
    }

    @Override
    public List<XSSFCell> next() {
        XSSFRow row = sheet.getRow(xssfRowIterator.next().getRowNum());
        int len = row.getLastCellNum();
        List<XSSFCell> cells = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            cells.add(row.getCell(i));
        }
        return cells;
    }

    @Override
    public boolean hasNext() {
        return xssfRowIterator.hasNext();
    }
}
