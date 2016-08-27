/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm.hiber.service.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author Rohit.Dhalwani (dhalwanirohit@gmail.com)
 */
public class DatabaseUtil {

    private static DatabaseUtil objUtilService = null;

    public static DatabaseUtil self() {
        if (objUtilService == null) {
            objUtilService = new DatabaseUtil();
        }
        return objUtilService;
    }

    public String getExportSheetName() {
        return "StatusAsOn." + new SimpleDateFormat("dd.MMM.yyyy").format(new Date());
    }

    public String getExportWorkbookName() {
        return "StatusAsOn_" + new SimpleDateFormat("dd.MMM.yyyy").format(new Date());
    }

    /**
     *
     * @param objFile
     * @param objJTable
     * @return number of records exported
     */
    public int exportData(File objFile, JTable objJTable) throws FileNotFoundException, IOException {
        int response = 0;

        if (objFile == null) {
            return response;
        }

        DefaultTableModel tm = (DefaultTableModel) objJTable.getModel();        
        Object[] rows = tm.getDataVector().toArray();

        JTableHeader columnNames = objJTable.getTableHeader();
        TableColumnModel columnModel = columnNames.getColumnModel();
        int columnCount = columnModel.getColumnCount();
        Vector<String> vecColumnNames = new Vector<String>();
        for (int c = 0; c < columnCount; c++) {
            vecColumnNames.add(columnModel.getColumn(c).getHeaderValue().toString());
        }
        
        HSSFWorkbook exportReadyWorkbook = new HSSFWorkbook();
        HSSFSheet dataSheet = exportReadyWorkbook.createSheet(getExportSheetName());

        if(vecColumnNames.size() > 0)
        {
            int columnCounter = 0;
            Row objHSSFColumnName = dataSheet.createRow(0);
            
            for(String strColumnName : vecColumnNames)
            {       
                /* Set Header CSS */
                
                Cell objHSSFCell = objHSSFColumnName.createCell(columnCounter);
                objHSSFCell.setCellValue(strColumnName);               
                CellStyle csll = exportReadyWorkbook.createCellStyle();
                Font objFont = exportReadyWorkbook.createFont();
                objFont.setFontName("Calibri");
                objFont.setColor(IndexedColors.BLACK.index);                
                objFont.setBold(true);
                csll.setFont(objFont);
                csll.setFillBackgroundColor(HSSFColor.YELLOW.index);
                csll.setFillForegroundColor(HSSFColor.YELLOW.index);
                csll.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);                
                csll.setAlignment(HSSFCellStyle.ALIGN_CENTER);               
                //csll.setWrapText(true);
                objHSSFCell.setCellStyle(csll);              
                columnCounter++;                
            }
        }
        
        
        if (rows != null && rows.length > 0) {
            /* Set Data into Sheet */
            for (int i = 0; i < rows.length; i++) {
                Vector objCellsData = (Vector) rows[i];                
                Row objHSSFRow = dataSheet.createRow(i+1);
                if (objCellsData != null && objCellsData.size() > 0) {
                    for (int j = 0; j < objCellsData.size(); j++) {
                        
                        /* Set Cell Data CSS */
                        
                        Cell objHSSFCell = objHSSFRow.createCell(j);                     
                        CellStyle csll = exportReadyWorkbook.createCellStyle();
                        Font objFont = exportReadyWorkbook.createFont();
                        objFont.setColor(IndexedColors.BLACK.index);
                        objFont.setBold(false);
                        objFont.setFontName("Calibri");
                        csll.setFont(objFont);                        
                        csll.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                        csll.setFillBackgroundColor(IndexedColors.WHITE.index);
                        csll.setFillForegroundColor(IndexedColors.WHITE.index);                        
                       
                        csll.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                        
                        //csll.setWrapText(true);
                        csll.setBorderBottom(CellStyle.BORDER_THIN);
                        csll.setBorderTop(CellStyle.BORDER_THIN);
                        
                        csll.setBottomBorderColor(HSSFColor.GREY_25_PERCENT.index);
                        csll.setTopBorderColor(HSSFColor.GREY_50_PERCENT.index);
                        
                        objHSSFCell.setCellStyle(csll);
                        Object cellData = objCellsData.get(j);                        
                        objHSSFCell.setCellValue((String) cellData);                        
                    }
                }
            }
            
            for(int i = 0; i < columnCount;i++)
            {
                if(i==2)
                {
                    dataSheet.setColumnWidth(i, 30*256);                    
                }
                else
                {
                    dataSheet.autoSizeColumn(i);
                }
            }           

            /* Write File */
            FileOutputStream objFileOutputStream = new FileOutputStream(objFile);
            exportReadyWorkbook.write(objFileOutputStream);
            objFileOutputStream.flush();
            objFileOutputStream.close();
            response = rows.length;
        }

        return response;
    }

}
