package com.sunesoft.seera.fr.utils.excel;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhouz on 2016/8/4.
 */
public class ExpotExcel<T> {

    private POIFSFileSystem fs;
    private HSSFWorkbook workbook;
    private HSSFSheet sheet;
    private HSSFRow row;


    /**
     * 多个sheet数据导出
     * @param title 标题
     * @param sheetMap sheet 和数据map
     * @param headers 列名
     * @param datePattern 日期格式化
     * @return workBook
     */
    public HSSFWorkbook exportExcel(String title, Map<String, Collection<T>> sheetMap, String[] headers,
                                    String datePattern) {
        // 声明一个工作薄
         workbook = new HSSFWorkbook();

        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor(HSSFColor.WHITE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);
        for (String key : sheetMap.keySet()){
            workbook= exportExcel(workbook,style,title,key,headers,sheetMap.get(key),datePattern);
        }


        return workbook;
    }


    /**
     * 导出excel 单Sheet
     *
     * @param title       标题（+sheet名称）
     * @param headers     列名
     * @param collection  导出的数据集合
     * @param datePattern 日期转换格式
     * @return
     */
    public HSSFWorkbook exportExcel(String title, String[] headers,
                                    Collection<T> collection, String datePattern) {
        Map<String, Collection<T>> map = new HashMap<>();
        map.put(title, collection);

        return exportExcel(title, map, headers, datePattern);
    }
    /**
     * 导出excel 单Sheet
     *
     * @param title       标题（+sheet名称）
     * @param headers     列名
     * @param collection  导出的数据集合
     * @param datePattern 日期转换格式
     * @return
     */
    public void  doExportExcel(String title, String[] headers,
                                    Collection<T> collection, String datePattern,HttpServletResponse response) {
        Map<String, Collection<T>> map = new HashMap<>();
        map.put(title, collection);
        workbook=exportExcel(title, map, headers, datePattern);
        try {
            response.setHeader("Content-Disposition", "attachment;filename="
                    + new String((title+".xls").getBytes("GB2312"), "ISO8859-1"));

            OutputStream fos = new BufferedOutputStream(
                    response.getOutputStream());
            response.setContentType("application/vnd.ms-excel;charset=gb2312");
            workbook.write(fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    protected HSSFWorkbook exportExcel(HSSFWorkbook workbook,HSSFCellStyle style, String title, String sheetName, String[] headers,
                                       Collection<T> collection, String datePattern) {

        // 生成一个表格
        sheet = workbook.createSheet(sheetName);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth( 15);
        // 声明一个画图的顶级管理器
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();

        // 产生表格标题行
        HSSFRow row = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }

        // 遍历集合数据，产生数据行
        Iterator<T> it = collection.iterator();
        int index = 0;
        while (it.hasNext()) {
            index++;
            row = sheet.createRow(index);
            T t = (T) it.next();
            // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
            Field[] fields = t.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellStyle(style);
                Field field = fields[i];
                String fieldName = field.getName();
                String getMethodName = "get"
                        + fieldName.substring(0, 1).toUpperCase()
                        + fieldName.substring(1);
                try {
                    Class tCls = t.getClass();
                    Method getMethod = tCls.getMethod(getMethodName,
                            new Class[] {});
                    Object value = getMethod.invoke(t, new Object[] {});
                    // 判断值的类型后进行强制类型转换
                    String textValue = null;
                    textValue =getValue(fieldName,value);
                    // 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
                    if (textValue != null) {
                        Pattern p = Pattern.compile("^//d+(//.//d+)?$");
                        Matcher matcher = p.matcher(textValue);
                        if (matcher.matches()) {
                            // 是数字当作double处理
                            cell.setCellValue(Double.parseDouble(textValue));
                        } else {
                            HSSFRichTextString richString = new HSSFRichTextString(
                                    textValue);
                            HSSFFont font3 = workbook.createFont();
                            font3.setColor(HSSFColor.BLACK.index);
                            richString.applyFont(font3);
                            cell.setCellValue(richString);
                        }
                    }
                } catch (SecurityException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

        }
        return workbook;
    }

    /**
     * 将Dto 中对应的值，转换为excel 的展示值
     * 可在factory 中根据实际需求重写
     * @param name 字段名称
     * @param value 字段值
     * @return 字段值
     */
    protected  String getValue(String name,Object value){
        if(value==null){
            return null;
        }
        return value.toString();
    }




    /**
     * 向浏览器端输出 Excel。
     * @param workbook 工作单元格。
     * @param response 当前 Response 上下文。
     * @param fileName 导出的文件名。
     */
    public void exportServletExcel(HSSFWorkbook workbook, HttpServletResponse response, String fileName) {
        try {
            response.setHeader("Content-Disposition", "attachment;filename="
                    + new String((fileName+".xls").getBytes("GB2312"), "ISO8859-1"));

            OutputStream fos = new BufferedOutputStream(
                    response.getOutputStream());
            response.setContentType("application/vnd.ms-excel;charset=gb2312");
            workbook.write(fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
