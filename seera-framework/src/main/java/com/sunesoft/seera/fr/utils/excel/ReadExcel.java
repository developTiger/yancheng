package com.sunesoft.seera.fr.utils.excel;

import com.sunesoft.seera.fr.ddd.infrastructure.repo.hibernate.GenericHibernateFinder;
import com.sunesoft.seera.fr.results.ListResult;
import com.sunesoft.seera.fr.results.UniqueResult;
import com.sunesoft.seera.fr.utils.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zhouz on 2016/8/15.
 */
public abstract class ReadExcel<T> extends GenericHibernateFinder {


    private POIFSFileSystem fs;
    private HSSFWorkbook workbook;
    private HSSFSheet sheet;
    private HSSFRow row;


    /**
     * 导入excel
     * @param is 导入excel 的数据流
     * @return 数据集合 Dto
     */
    public ListResult<T> readExcel(InputStream is) {
        List<T> collection = new ArrayList<>();
        try {
            fs = new POIFSFileSystem(is);
            workbook = new HSSFWorkbook(fs);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {//获取每个Sheet表
            sheet=workbook.getSheetAt(i);
            ListResult<T> result= readExcelSheet(sheet);
            if(result.getIsSuccess()) {
                if (result.getItems() != null && result.getItems().size() > 0)
                    collection.addAll(result.getItems());
            }
            else
                return new ListResult<T>(result.getMsg());
        }
        return new ListResult<T>(collection);
    }



    protected ListResult<T> readExcelSheet(HSSFSheet sheet) {

        List<T> collection = new ArrayList<>();
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        row = sheet.getRow(0);
        if(!checkExcelCol(row)){
            return new ListResult("excel 格式错误，请调整为模板格式！");
        }
        int colNum = row.getPhysicalNumberOfCells();
        // 正文内容应该从第二行开始,第一行为表头的标题
        for (int i = 1; i <= rowNum; i++) {
            row = sheet.getRow(i);
            if(!StringUtils.isNullOrWhiteSpace(getCellFormatValue(row.getCell(0)))){
                UniqueResult result = convertRow(row, i,colNum);
                if(result.getIsSuccess()) {
                    T t =(T) result.getT();
                    if (t != null)
                        collection.add(t);
                }
                else
                    return new ListResult<T>(result.getMsg());
            }
        }
        return new ListResult<T>(collection);
    }

    /**
     * 将一行转化为数据对象
     * 需要重写
     * @param row excel 行
     * @param colNum 列的数量
     * @return 数据对象
     */
    protected UniqueResult<T> convertRow(HSSFRow row,int rowNum,int colNum){
        String value;
        int j = 0;

        while (j < colNum) {
            // 每个单元格的数据内容用"-"分割开，以后需要时用String类的replace()方法还原数据
            // 也可以将每个单元格的数据设置到一个javabean的属性中，此时需要新建一个javabean
            // str += getStringCellValue(row.getCell((short) j)).trim()
            value= getCellFormatValue(row.getCell(j));
            j++;
        }
        return null;
    }
    /**
     * 读取Excel数据内容
     * @param is
     * @return Map 包含单元格数据内容的Map对象
     */
    public Map<Integer, String> readExcelMapped(InputStream is) {
        Map<Integer, String> content = new HashMap<Integer, String>();
        String str = "";
        try {
            fs = new POIFSFileSystem(is);
            workbook = new HSSFWorkbook(fs);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sheet = workbook.getSheetAt(0);
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        row = sheet.getRow(0);
        checkExcelCol(row);
        int colNum = row.getPhysicalNumberOfCells();
        // 正文内容应该从第二行开始,第一行为表头的标题
        for (int i = 1; i <= rowNum; i++) {
            row = sheet.getRow(i);
            int j = 0;
            while (j < colNum) {
                // 每个单元格的数据内容用"-"分割开，以后需要时用String类的replace()方法还原数据
                // 也可以将每个单元格的数据设置到一个javabean的属性中，此时需要新建一个javabean
                // str += getStringCellValue(row.getCell((short) j)).trim() +
                // "-";
                str += getCellFormatValue(row.getCell(j)).trim() + "    ";
                j++;
            }
            content.put(i, str);
            str = "";
        }
        return content;
    }


    protected Boolean checkExcelCol(HSSFRow row){
        return true;
    }

    /**
     * 读取Excel表格表头的内容
     * @param workbook
     * @return String 表头内容的数组
     */
    public String[] readExcelTitle(HSSFWorkbook workbook) {

        sheet = workbook.getSheetAt(0);
        row = sheet.getRow(0);
        // 标题总列数
        int colNum = row.getPhysicalNumberOfCells();
        System.out.println("colNum:" + colNum);
        String[] title = new String[colNum];
        for (int i = 0; i < colNum; i++) {
            //title[i] = getStringCellValue(row.getCell((short) i));
            title[i] = getCellFormatValue(row.getCell((short) i));
        }
        return title;
    }
    /**
     * 获取单元格数据内容为字符串类型的数据
     *
     * @param cell Excel单元格
     * @return String 单元格数据内容
     */
    private String getStringCellValue(HSSFCell cell) {
        String strCell = "";
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING:
                strCell = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                strCell = String.valueOf(cell.getNumericCellValue());
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                strCell = String.valueOf(cell.getBooleanCellValue());
                break;
            case HSSFCell.CELL_TYPE_BLANK:
                strCell = "";
                break;
            default:
                strCell = "";
                break;
        }
        if (strCell.equals("") || strCell == null) {
            return "";
        }
        if (cell == null) {
            return "";
        }
        return strCell;
    }



    /**
     * 根据HSSFCell类型设置数据
     * @param cell
     * @return
     */
    protected String getCellFormatValue(HSSFCell cell) {
        String cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
                // 如果当前Cell的Type为NUMERIC
                case HSSFCell.CELL_TYPE_NUMERIC:
                case HSSFCell.CELL_TYPE_FORMULA: {
                    // 判断当前的cell是否为Date
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        // 如果是Date类型则，转化为Data格式

                        //方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
                        //cellvalue = cell.getDateCellValue().toLocaleString();

                        //方法2：这样子的data格式是不带带时分秒的：2011-10-12
                        Date date = cell.getDateCellValue();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        cellvalue = sdf.format(date);

                    }
                    // 如果是纯数字
                    else {
                        // 取得当前Cell的数值
                        cellvalue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                // 如果当前Cell的Type为STRIN
                case HSSFCell.CELL_TYPE_STRING:
                    // 取得当前的Cell字符串
                    cellvalue = cell.getRichStringCellValue().getString();
                    break;
                // 默认的Cell值
                default:
                    cellvalue = " ";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;

    }
}
