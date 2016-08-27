/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm.hiber.service;

import com.tm.hiber.model.TaskComments;
import com.tm.hiber.model.TaskMaster;
import com.tm.hiber.model.TaskPlan;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

/**
 *
 * @author user
 */
public interface TaskManagerService {
    
    final int RESULT_SUCESSFUL = 0;
    final int RESULT_UNSUCESSFUL = -1;
    
    final int CLASS_TASK_MASTER = 1;
    final int CLASS_TASK_COMMENTS = 2;
    final int CLASS_TASK_MASTER_TEMPLATE = 3;
    
    /**
     * Save/Modify Task
     * @param objTaskMaster
     * @return 
     * RESULT_SUCESSFUL = 0;
     * RESULT_UNSUCESSFUL = -1;
     */
    int saveTask(TaskMaster objTaskMaster);
    
    /**
     * Fetch Task
     * @param taskId
     * @return Object of TaskMaster
     */
    TaskMaster getTask(BigDecimal taskId);
    
    /**
     * Fetch All Records
     * @return List of TaskMaster
     */
    List<TaskMaster> getAllTask();
    
    /**
     * Fetch All Records with given Criteria
     * @param listCriterions
     * @return 
     */
    List<TaskMaster> getAllTask(List<Criterion> listCriterions);
    
    /**
     * 
     * @param listCriterions
     * @param objListOrder
     * @return 
     */
    List<TaskMaster> getAllTask(List<Criterion> listCriterions,List<Order> objListOrder);
    
    /**
     * Prepare Map from List of Task
     * @return Map of TaskMaster
     */
    Map<BigDecimal,TaskMaster> getTaskMasterMap();
    
    /**
     * 
     * @param taskId
     * @param listOfTask
     * @return TaskMaster
     */
    TaskMaster findTask(BigDecimal taskId,List<TaskMaster> listOfTask);
    
    /**
     * Delete Record From Database
     * @param taskId
     * @return 
     */
    int deleteTask(BigDecimal taskId);    
    
    
    /**
     * Get all task comments
     * @param taskId
     * @return 
     */
    List<TaskComments> getTaskComments(BigDecimal taskId);
    
    /**
     * Save Task Comment
     * @param objComments
     * @return 
     */
    int saveTaskComment(TaskComments objComments);
    
    /**
     * New Sequence
     * @param tableName
     * @return 
     */
    BigDecimal getNextSeqence(int tableName);
    
    /**
     * Delete Task Comment
     * @param recordId
     * @return 
     */
    int deleteTaskComment(BigDecimal recordId);
    
    
    /**
     * Save Task Plan
     * @param objTaskPlans
     * @return 
     */
    int saveTaskPlan(List<TaskPlan> objTaskPlans);
    
    
    /**
     * 
     * @param refTaskId
     * @return 
     */
    List<TaskPlan> getTaskPlan(BigDecimal refTaskId);
    
}
