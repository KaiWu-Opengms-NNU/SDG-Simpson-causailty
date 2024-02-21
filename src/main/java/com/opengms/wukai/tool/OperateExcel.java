package com.opengms.wukai.tool;

import com.opengms.wukai.pojo.SystemDynamics.SDResultPojo;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class OperateExcel {
    //设置总的输入路径
    private final String Path="G:/WukaiBag/Year2023/sustainableofpaper2023/coding/JavaProgramChangeDataAndRegress_Goal";

    //读入xlsx文件
    public List readExcel(File file) throws IOException {
        InputStream is=null;
        try{
            // 创建输入流，读取Excel
            is = new FileInputStream(file.getAbsolutePath());
            XSSFWorkbook wb=new XSSFWorkbook(is);
            // Excel的页签数量
            int sheet_size = wb.getNumberOfSheets();
            for (int index = 0; index <= sheet_size; index++) {
                List<List> outerList=new ArrayList<List>();
                // 每个页签创建一个Sheet对象
                Sheet sheet = wb.getSheetAt(index);
                // sheet.getRows()返回该页的总行数
                for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
                    System.out.println(i);
                    List innerList=new ArrayList();
                    // sheet.getColumns()返回该页的总列数
                    for (int j = 0; j < sheet.getRow(0).getPhysicalNumberOfCells(); j++) {
                        if(sheet.getRow(i).getCell(j)!=null){
                            sheet.getRow(i).getCell(j).setCellType(Cell.CELL_TYPE_STRING);
                            String cellInfo = sheet.getRow(i).getCell(j).getStringCellValue();
                            innerList.add(cellInfo);
                        }else{
                            innerList.add("null");
                            continue;
                        }
                    }
                    outerList.add(i, innerList);
                }
                return outerList;
            }
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            if(is!=null) is.close();
        }
        return null;
    };

    //将一个文件拆分成两个读入
    public List AddExcel(List<List<String>> OrignalList,File file) throws IOException {
        InputStream is=null;
        try{
            // 创建输入流，读取Excel
            is = new FileInputStream(file.getAbsolutePath());

            XSSFWorkbook wb=new XSSFWorkbook(is);
            // Excel的页签数量
            int sheet_size = wb.getNumberOfSheets();
            for (int index = 0; index <= sheet_size; index++) {
                // 每个页签创建一个Sheet对象
                Sheet sheet = wb.getSheetAt(index);
                // sheet.getRows()返回该页的总行数
                for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
                    System.out.println(i);
                    List innerList=new ArrayList();
                    // sheet.getColumns()返回该页的总列数
                    for (int j = 0; j < sheet.getRow(0).getPhysicalNumberOfCells(); j++) {
                        if(sheet.getRow(i).getCell(j)!=null){
                            sheet.getRow(i).getCell(j).setCellType(Cell.CELL_TYPE_STRING);
                            String cellInfo = sheet.getRow(i).getCell(j).getStringCellValue();
                            innerList.add(cellInfo);
                        }else{
                            innerList.add("null");
                            continue;
                        }
                    }
                    OrignalList.add(innerList);
                }
                return OrignalList;
            }
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            if(is!=null) is.close();
        }
        return null;
    };


    //读入xls文件
    public List readExcel2(File file) {
        try{
            // 创建输入流，读取Excel
            InputStream is = new FileInputStream(file.getAbsolutePath());

            HSSFWorkbook wb=new HSSFWorkbook(is);
            // Excel的页签数量
            int sheet_size = wb.getNumberOfSheets();
            for (int index = 0; index <= sheet_size; index++) {
                List<List> outerList=new ArrayList<List>();
                // 每个页签创建一个Sheet对象
                Sheet sheet = wb.getSheetAt(index);
                // sheet.getRows()返回该页的总行数
                for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
                    List innerList=new ArrayList();
                    // sheet.getColumns()返回该页的总列数
                    for (int j = 0; j < sheet.getRow(0).getPhysicalNumberOfCells(); j++) {
                        if(sheet.getRow(i).getCell(j)!=null){
                            sheet.getRow(i).getCell(j).setCellType(Cell.CELL_TYPE_STRING);
                            String cellInfo = sheet.getRow(i).getCell(j).getStringCellValue();
                            innerList.add(cellInfo);
                        }else{
                            innerList.add("null");
                            continue;
                        }
                    }
                    outerList.add(i, innerList);
                }
                return outerList;
            }
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    };


    public <T> void writeExcel(String excelName,Class<T> classType,List<T> excelData) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 根据 Workbook 获取当前 Sheet 页
        Sheet sheet = workbook.createSheet("sheet1");
        T rowDataZero = excelData.get(0);
        Field[] fields=rowDataZero.getClass().getDeclaredFields();
        //填充表头数据
        HSSFRow row0 = (HSSFRow) sheet.createRow(0);//sheet1
        HSSFCell cell = null;
        for(int j=0;j<fields.length;j++){
            cell=row0.createCell(j);
            cell.setCellValue(fields[j].getName());
        }
        for(int i=0;i<excelData.size();i++){
            HSSFRow row =(HSSFRow)sheet.createRow(i+1);
            T cellData = excelData.get(i);
            for(int j = 0; j < fields.length; j++){
                // 单元格存储数据
                row.createCell(j).setCellValue(String.valueOf(getFieldValueByName(fields[j].getName(),cellData)));
            }
        }
        // 根据 Excel 路径创建 File 文件类
        File excelFile = new File(Path+"/src/test/result/"+excelName+".xls");
        // 文件输出流
        FileOutputStream out=new FileOutputStream(excelFile);
        // 清理
        out.flush();
        // 将 Workbook 中的数据通过流写入
        workbook.write(out);
        // 关闭流
        out.close();
    }



    public void writeExcel(String Path,String excelName,List<String> excelData) throws IOException{
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 根据 Workbook 获取当前 Sheet 页
        Sheet sheet = workbook.createSheet("sheet1");
        for(int i=0;i<excelData.size();i++){
            HSSFRow row =(HSSFRow)sheet.createRow(i);
            String cellData = excelData.get(i);
            row.createCell(0).setCellValue(cellData);
        }
        // 根据 Excel 路径创建 File 文件类
        File excelFile = new File(Path+excelName+".xls");
        // 文件输出流
        FileOutputStream out=new FileOutputStream(excelFile);
        // 清理
        out.flush();
        // 将 Workbook 中的数据通过流写入
        workbook.write(out);
        // 关闭流
        out.close();
    }

    public void writeExcel(String Path,String excelName,double [][] NetData) throws IOException{
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 根据 Workbook 获取当前 Sheet 页
        Sheet sheet = workbook.createSheet("sheet1");
        String[] RowName=new String[]{"SDG1","SDG2","SDG3","SDG4","SDG5","SDG6",
        "SDG7","SDG8","SDG9","SDG10","SDG11","SDG12","SDG13","SDG14","SDG15","SDG16","SDG17"};
        //填充表头
        HSSFRow row0=(HSSFRow)sheet.createRow(0);
        HSSFCell cell=null;
        cell=row0.createCell(0);
        cell.setCellValue(" ");
        for(int j=1;j<RowName.length+1;j++){
            cell=row0.createCell(j);
            cell.setCellValue(RowName[j-1]);
        }
        for(int i=1;i<NetData.length+1;i++){
            HSSFRow row =(HSSFRow)sheet.createRow(i);
            HSSFCell cellData=row.createCell(0);
            cellData.setCellValue(RowName[i-1]);
            double [] eachData=NetData[i-1];
            for(int j=1;j<eachData.length+1;j++){
                cellData=row.createCell(j);
                cellData.setCellValue(Math.abs(eachData[j-1]));
            }
        }
        // 根据 Excel 路径创建 File 文件类
        File excelFile = new File(Path+excelName+".xls");
        // 文件输出流
        FileOutputStream out=new FileOutputStream(excelFile);
        // 清理
        out.flush();
        // 将 Workbook 中的数据通过流写入
        workbook.write(out);
        // 关闭流
        out.close();
    }

    public void writeExcelNoAbs(String Path,String excelName,double [][] NetData) throws IOException{
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 根据 Workbook 获取当前 Sheet 页
        Sheet sheet = workbook.createSheet("sheet1");
        String[] RowName=new String[]{"SDG1","SDG2","SDG3","SDG4","SDG5","SDG6",
                "SDG7","SDG8","SDG9","SDG10","SDG11","SDG12","SDG13","SDG14","SDG15","SDG16","SDG17"};
        //填充表头
        HSSFRow row0=(HSSFRow)sheet.createRow(0);
        HSSFCell cell=null;
        cell=row0.createCell(0);
        cell.setCellValue(" ");
        for(int j=1;j<RowName.length+1;j++){
            cell=row0.createCell(j);
            cell.setCellValue(RowName[j-1]);
        }
        for(int i=1;i<NetData.length+1;i++){
            HSSFRow row =(HSSFRow)sheet.createRow(i);
            HSSFCell cellData=row.createCell(0);
            cellData.setCellValue(RowName[i-1]);
            double [] eachData=NetData[i-1];
            for(int j=1;j<eachData.length+1;j++){
                cellData=row.createCell(j);
                cellData.setCellValue(eachData[j-1]);
            }
        }
        // 根据 Excel 路径创建 File 文件类
        File excelFile = new File(Path+excelName+".xls");
        // 文件输出流
        FileOutputStream out=new FileOutputStream(excelFile);
        // 清理
        out.flush();
        // 将 Workbook 中的数据通过流写入
        workbook.write(out);
        // 关闭流
        out.close();
    }

    public <T> void writeExcel(String Path,String excelName,Class<T> classType,List<T> excelData) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 根据 Workbook 获取当前 Sheet 页
        Sheet sheet = workbook.createSheet("sheet1");
        T rowDataZero = excelData.get(0);
        Field[] fields=rowDataZero.getClass().getDeclaredFields();
        //填充表头数据
        HSSFRow row0 = (HSSFRow) sheet.createRow(0);//sheet1
        HSSFCell cell = null;
        for(int j=0;j<fields.length;j++){
            cell=row0.createCell(j);
            cell.setCellValue(fields[j].getName());
        }
        for(int i=0;i<excelData.size();i++){
            HSSFRow row =(HSSFRow)sheet.createRow(i+1);
            T cellData = excelData.get(i);
            for(int j = 0; j < fields.length; j++){
                // 单元格存储数据
                row.createCell(j).setCellValue(String.valueOf(getFieldValueByName(fields[j].getName(),cellData)));
            }
        }
        // 根据 Excel 路径创建 File 文件类
        File excelFile = new File(Path+excelName+".xls");
        // 文件输出流
        FileOutputStream out=new FileOutputStream(excelFile);
        // 清理
        out.flush();
        // 将 Workbook 中的数据通过流写入
        workbook.write(out);
        // 关闭流
        out.close();
    }

    public void writeExcelString(String Path,String excelName,List<String[]> excelData) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 根据 Workbook 获取当前 Sheet 页
        Sheet sheet = workbook.createSheet("sheet1");
        HSSFRow row0 = (HSSFRow) sheet.createRow(0);//sheet1
        HSSFCell cell = null;
        cell=row0.createCell(0);
        cell.setCellValue("Scale");
        cell=row0.createCell(1);
        cell.setCellValue("Interaction");
        cell=row0.createCell(2);
        cell.setCellValue("NetIndicator");
        cell=row0.createCell(3);
        cell.setCellValue("Top1");
        cell=row0.createCell(4);
        cell.setCellValue("Top1Value");
        cell=row0.createCell(5);
        cell.setCellValue("Top2");
        cell=row0.createCell(6);
        cell.setCellValue("Top2Value");
        cell=row0.createCell(7);
        cell.setCellValue("Top3");
        cell=row0.createCell(8);
        cell.setCellValue("Top3Value");
        cell=row0.createCell(9);
        cell.setCellValue("Top4");
        cell=row0.createCell(10);
        cell.setCellValue("Top4Value");
        cell=row0.createCell(11);
        cell.setCellValue("Top5");
        cell=row0.createCell(12);
        cell.setCellValue("Top5Value");
        cell=row0.createCell(13);
        cell.setCellValue("Top6");
        cell=row0.createCell(14);
        cell.setCellValue("Top6Value");
        cell=row0.createCell(15);
        cell.setCellValue("Top7");
        cell=row0.createCell(16);
        cell.setCellValue("Top7Value");
        cell=row0.createCell(17);
        cell.setCellValue("Top8");
        cell=row0.createCell(18);
        cell.setCellValue("Top8Value");
        cell=row0.createCell(19);
        cell.setCellValue("Top9");
        cell=row0.createCell(20);
        cell.setCellValue("Top9Value");
        cell=row0.createCell(21);
        cell.setCellValue("Top10");
        cell=row0.createCell(22);
        cell.setCellValue("Top10Value");
        cell=row0.createCell(23);
        cell.setCellValue("Top11");
        cell=row0.createCell(24);
        cell.setCellValue("Top11Value");
        cell=row0.createCell(25);
        cell.setCellValue("Top12");
        cell=row0.createCell(26);
        cell.setCellValue("Top12Value");
        cell=row0.createCell(27);
        cell.setCellValue("Top13");
        cell=row0.createCell(28);
        cell.setCellValue("Top13Value");
        cell=row0.createCell(29);
        cell.setCellValue("Top14");
        cell=row0.createCell(30);
        cell.setCellValue("Top14Value");
        cell=row0.createCell(31);
        cell.setCellValue("Top15");
        cell=row0.createCell(32);
        cell.setCellValue("Top15Value");
        cell=row0.createCell(33);
        for(int i=0;i<excelData.size();i++){
            HSSFRow row =(HSSFRow)sheet.createRow(i+1);
            for(int j = 0; j < excelData.get(i).length; j++){
                // 单元格存储数据
                row.createCell(j).setCellValue(excelData.get(i)[j]);
            }
        }
        // 根据 Excel 路径创建 File 文件类
        File excelFile = new File(Path+excelName+".xls");
        // 文件输出流
        FileOutputStream out=new FileOutputStream(excelFile);
        // 清理
        out.flush();
        // 将 Workbook 中的数据通过流写入
        workbook.write(out);
        // 关闭流
        out.close();
    }

    private Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(o, new Object[] {});
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    //批量读入文件夹内的文件
    public  List<File> getAllFile(File fileInput) {
        List<File> allFileList =new ArrayList<>();
        // 获取文件列表
        File[] fileList = fileInput.listFiles();
        assert fileList != null;
        for (File file : fileList) {
            if (file.isDirectory()) {
                // 递归处理文件夹
                // 如果不想统计子文件夹则可以将下一行注释掉
            } else {
                // 如果是文件则将其加入到文件数组中
                allFileList.add(file);
            }
        }
        return allFileList;
    }
}


