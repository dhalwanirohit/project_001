/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm.hiber.service;

import com.tm.model.TaskMasterExcelTemplate;
import java.io.File;
import java.util.List;

/**
 *
 * @author user
 */
public interface TMDataOperationService {
    
    List<TaskMasterExcelTemplate> prepareTaskMasterFromExcel(File excelFile);  
    
    
}
