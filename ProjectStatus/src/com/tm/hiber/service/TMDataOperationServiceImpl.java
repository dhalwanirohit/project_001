/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm.hiber.service;

import com.tm.hiber.service.util.UtilService;
import com.tm.model.TaskMasterExcelTemplate;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Rohit.Dhalwani (dhalwanirohit@gmail.com)
 */
public class TMDataOperationServiceImpl implements TMDataOperationService {

    private final List<TaskMasterExcelTemplate> mTaskMasterList = new ArrayList<TaskMasterExcelTemplate>();
    private static final Logger mLogger = Logger.getLogger(TMDataOperationServiceImpl.class.getName());

    @Override
    public List<TaskMasterExcelTemplate> prepareTaskMasterFromExcel(File excelFile) {
        mLogger.log(Level.INFO, "prepareTaskMasterFromExcel--Starts");
        if (excelFile != null && excelFile.exists()) {
            InputStream fis = null;
            try {
                mTaskMasterList.clear();
                fis = new FileInputStream(excelFile);
                HSSFWorkbook objWorkBook = new HSSFWorkbook(fis);
                HSSFSheet objSheet = objWorkBook.getSheetAt(0);
                Iterator<Row> rowItr = objSheet.iterator();
                TaskMasterExcelTemplate objTaskMaster;
                while (rowItr.hasNext()) {
                    Row row = rowItr.next();
                    objTaskMaster = new TaskMasterExcelTemplate();
                    Iterator<Cell> cellItr = row.cellIterator();
                    while (cellItr.hasNext()) {

                        Cell objCell = cellItr.next();
                        String cellValue = getCellData(objCell);
                        switch (objCell.getColumnIndex()) {

                            case 0:
                                objTaskMaster.setTaskReference(cellValue);
                                break;                                
                            case 2:
                                Date createDate = parseDate(cellValue);
                                if (createDate != null) {
                                    objTaskMaster.setCreateDate(createDate);
                                }
                                break;
                            case 6:
                                objTaskMaster.setTitle(cellValue);
                                break;
                            case 7:
                                objTaskMaster.setDescription(cellValue);
                                break;
                            case 8:
                                objTaskMaster.setPriority(cellValue);
                                break;
                            case 18:
                                objTaskMaster.setProjectName(cellValue);
                                break;
                        }

                    }
                    
                    objTaskMaster.setAuditLastupdateon(new Date());
                    objTaskMaster.setAuditLastupdateby(UtilService.self().getSystemUser());
                    
                    mTaskMasterList.add(objTaskMaster);
                }

            } catch (Exception ex) {
                mLogger.log(Level.FATAL, ex.getMessage());
            } finally {
                try {
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException ex) {
                    mLogger.log(Level.FATAL, ex.getMessage());
                }
            }

        }
        mLogger.log(Level.INFO, "prepareTaskMasterFromExcel--Ends");
        return mTaskMasterList;
    }

    private String getCellData(Cell objCell) {
        mLogger.log(Level.INFO, "getCellData--Starts");
        String response;
        switch (objCell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                Double d = objCell.getNumericCellValue();
                response = String.valueOf(d.intValue());
                break;
            case Cell.CELL_TYPE_STRING:
                response = objCell.getStringCellValue();
                break;
            default:
                response = objCell.getStringCellValue();
                break;
        }
        mLogger.log(Level.INFO, "getCellData--Ends");
        return response;
    }

    private Date parseDate(String dateStr) {
        Date response = null;
        try {
            if(dateStr != null)
            {
                String[] arrDateTime = dateStr.split(" ");
                if(arrDateTime != null && arrDateTime.length > 0)
                {
                    SimpleDateFormat objFormater = new SimpleDateFormat("MM/dd/yyyy");
                    response = objFormater.parse(arrDateTime[0]);    
                    System.out.println("ParsedDate-->" + response);
                }
            }
        } catch (Exception e) {
            mLogger.fatal("UnableToParseDate-->" + e.getMessage());
        }
        return response;
    }

}
