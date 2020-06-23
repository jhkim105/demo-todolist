package com.example.todolist.sample_data;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public final class ExcelToXmlDataSet {

  private static final String DEFAULTDATA_CELL_COLOR_HEX = "FFFFFF00"; // yellow:FFFFFF00,
                                                                       // red:FFFF0000,
                                                                       // green:FF00B050,
                                                                       // blue:FF00B0F0

  public static void generateDataSetXmlToDestinationPath(String destPath, String fileName) throws Exception {
    generateDataSetXml(destPath, fileName, fileName, false);
  }

  public static void generateDataSetXmlToDestinationPath(String destPath, String destFileName, String sourceFileName) throws Exception {
    generateDataSetXml("src/test/resources/", destFileName, sourceFileName, false);
  }

  public static void generateDataSetXml(String fileName) throws Exception {
    generateDataSetXml("src/test/resources/", fileName, fileName, false);
  }

  public static void generateDataSetXml(String destFileName, String sourceFileName) throws Exception {
    generateDataSetXml("src/test/resources/", destFileName, sourceFileName, false);
  }

  public static void generateDataSetXmlIgnoreTestData(String fileName) throws Exception {
    generateDataSetXml("src/test/resources/", "default-data", fileName, true);
  }

  public static void generateDataSetXmlIgnoreTestData(String destPath, String fileName) throws Exception {
    generateDataSetXml(destPath, "default-data", fileName, true);
  }

  private static void generateDataSetXml(String destFilePath, String fileName, String excelFileName, boolean ignoreTestData) throws Exception {
    String excelFilePath = "src/test/resources/".concat(excelFileName).concat(".xlsx");
    InputStream inputStream = new FileInputStream(excelFilePath);
    ZipSecureFile.setMinInflateRatio(-1.0d);
    XSSFWorkbook wb = new XSSFWorkbook(inputStream);
    StringBuilder sb = new StringBuilder();
    sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
      .append("\n<dataset>");
    int sheetCount = wb.getNumberOfSheets();
    if (sheetCount == 0)
      return;

    for (int i = 0; i < sheetCount; i++) {
      String tableXml = getTableXml(wb, i, ignoreTestData);
      sb.append(tableXml);
    }
    sb.append("\n</dataset>");

    File file = new File(destFilePath.concat(fileName).concat(".xml"));
    PrintWriter out = new PrintWriter(file, "UTF-8");
    out.println(sb.toString());
    out.close();
  }

  private static String getTableXml(XSSFWorkbook wb, int i, boolean ignoreTestData) {
    XSSFSheet sheet = null;
    String tableName;
    tableName = wb.getSheetName(i);
    sheet = wb.getSheetAt(i);
    int rowCount = sheet.getPhysicalNumberOfRows();
    if (rowCount == 0)
      return "";

    String tableDataXml = getTableDataXml(sheet, ignoreTestData);
    if (StringUtils.isBlank(tableDataXml))
      return "";

    StringBuilder sb = new StringBuilder();
    sb.append("\n<table name=\"").append(tableName).append("\">")
      .append(tableDataXml)
      .append("\n</table>");
    return sb.toString();
  }

  private static String getTableDataXml(XSSFSheet sheet, boolean ignoreTestData) {
    List<String> columns = new ArrayList<String>();

    String columnString = getColumnXml(sheet, columns, ignoreTestData);
    if (StringUtils.isBlank(columnString))
      return "";

    String rowString = getRowXml(sheet, columns, ignoreTestData);
    if (StringUtils.isBlank(rowString))
      return "";

    StringBuilder sb = new StringBuilder();
    sb.append(columnString).append(rowString);
    return sb.toString();
  }

  private static String getColumnXml(XSSFSheet sheet, List<String> columns, boolean ignoreTestData) {
    String columnName = "";
    XSSFRow headerRow = sheet.getRow(0);
    if (headerRow == null) {
      log.warn("headerRow is null");
    }
    int cellCount = headerRow.getPhysicalNumberOfCells();

    StringBuilder sb = new StringBuilder();
    for (int ii = 0; ii < cellCount; ii++) {
      XSSFCell cell = headerRow.getCell(ii);
      if (ii == 0 && ignoreTestData) {
        if (cell == null)
          return "";
        try {
          if (ii == 0 && ignoreTestData)
            checkIgnoreTestData(cell);
        } catch (NoDefaultDataColorException ex) {
          return "";
        }
      }
      columnName = getStringCellValue(cell);
      if (StringUtils.isBlank(columnName))
        continue;
      columns.add(columnName);
      sb.append("\n    ").append("<column>").append(columnName).append("</column>");
    }
    return sb.toString();
  }

  private static String getRowXml(XSSFSheet sheet, List<String> columns, boolean ignoreTestData) {
    int rowCount = sheet.getPhysicalNumberOfRows();
    StringBuilder sb = new StringBuilder();
    for (int j = 1; j < rowCount; j++) {
      XSSFRow row = sheet.getRow(j);
      if (row == null)
        break;
      StringBuilder rowTag = new StringBuilder("\n    <row>");
      boolean isValidRow = false;
      String value;

      for (int k = 0; k < columns.size(); k++) {
        XSSFCell cell = row.getCell(k);

        if (k == 0 && ignoreTestData) {
          if (cell == null) {
            isValidRow = false;
            break;
          }
          try {
            checkIgnoreTestData(cell);
          } catch (NoDefaultDataColorException ex) {
            isValidRow = false;
            break;
          }
        }
        value = getStringCellValue(cell);
        if (k == 0 && StringUtils.isBlank(value)) {
          isValidRow = false;
          break;
        }
        if (value == null || "NULL".equalsIgnoreCase(value)) {
          rowTag.append("\n         ").append("<null />");
          continue;
        }

        if (!value.startsWith("<![CDATA[") || !value.endsWith("]]>"))
          value = StringEscapeUtils.escapeXml10(value);

        rowTag.append("\n         ").append("<value description=\"").append(columns.get(k)).append("\">").append(value).append("</value>");
        isValidRow = true;
      }
      rowTag.append("\n    </row>");
      if (isValidRow)
        sb.append(rowTag);
    }
    return sb.toString();
  }

  private static void checkIgnoreTestData(XSSFCell cell) throws NoDefaultDataColorException {
    XSSFCellStyle cellStyle = cell.getCellStyle();
    if (cellStyle == null)
      throw new NoDefaultDataColorException();

    XSSFColor color = cellStyle.getFillForegroundXSSFColor();
    if (color == null)
      throw new NoDefaultDataColorException();
    if (!DEFAULTDATA_CELL_COLOR_HEX.equals(color.getARGBHex())) {
      throw new NoDefaultDataColorException();
    }
  }

  public static String getStringCellValue(XSSFCell cell) {
    if (cell == null)
      return null;

    CellType cellType = cell.getCellTypeEnum();
    String result;
    if (cellType == CellType.NUMERIC) {
      result = String.valueOf(cell.getNumericCellValue());
      result = StringUtils.removeEnd(result, ".0");
    } else if (cellType == CellType.STRING) {
      result = cell.getStringCellValue();
    } else if (cellType == CellType.FORMULA) {
      result = String.valueOf((int) cell.getNumericCellValue());
    } else if (cellType == CellType.BOOLEAN) {
      result = String.valueOf(cell.getBooleanCellValue());
    } else {
      result = null;
    }
    return result;
  }

  public static class NoDefaultDataColorException extends Exception {
    private static final long serialVersionUID = -8826410309764387724L;
  }
}
