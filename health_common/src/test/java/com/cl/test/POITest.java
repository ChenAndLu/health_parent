package com.cl.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author: ChenLu
 * @date: Created in 2023/3/25
 * @description:
 * @version:1.0
 */


public class POITest {

    //从EXCEL文件读取数据
    public void test1() throws IOException {
        //构造一个EXCEL对象
        XSSFWorkbook excel = new XSSFWorkbook("D:\\java学习资料\\04-360期-课件\\10_健康医疗\\day55_健康医疗\\资源\\poi.xlsx");
        //获取第一个工作表对象
        XSSFSheet sheet = excel.getSheetAt(0);
        for (Row row : sheet){
            for (Cell cell : row){
                System.out.print(cell.getStringCellValue()+" ");
            }
            System.out.println();
        }
        excel.close();
    }


    public void test2() throws IOException {
        //构造一个EXCEL对象
        XSSFWorkbook excel = new XSSFWorkbook("D:\\java学习资料\\04-360期-课件\\10_健康医疗\\day55_健康医疗\\资源\\poi.xlsx");
        //获取第一个工作表对象
        XSSFSheet sheet = excel.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum(); //获取最后一个行的行号
        for (int i = 0; i <= lastRowNum; i++){
            XSSFRow row = sheet.getRow(i);
            short lastCellNum = row.getLastCellNum();
            for (int j=0; j < lastCellNum;j++){
                XSSFCell cell = row.getCell(j);
                System.out.print(cell.getStringCellValue()+" ");
            }
            System.out.println();
        }
        excel.close();
    }


    //向EXCEL文件写入数据，并提供客户端下载
    public void test3() throws IOException {
        XSSFWorkbook excel = new XSSFWorkbook();
        //创建一个工作表对象
        XSSFSheet sheet = excel.createSheet("chenLu");
        //在啊工作表中创建行
        XSSFRow title = sheet.createRow(0);

        title.createCell(0).setCellValue("编号");
        title.createCell(1).setCellValue("姓名");
        title.createCell(2).setCellValue("地址");

        XSSFRow row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue("u001");
        row1.createCell(1).setCellValue("小明");
        row1.createCell(2).setCellValue("北京");

        XSSFRow row2 = sheet.createRow(2);
        row2.createCell(0).setCellValue("u002");
        row2.createCell(1).setCellValue("小李");
        row2.createCell(2).setCellValue("南京");

        FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\晨露\\Desktop\\chenLu.xlsx");
        excel.write(fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
        excel.close();
    }

}
