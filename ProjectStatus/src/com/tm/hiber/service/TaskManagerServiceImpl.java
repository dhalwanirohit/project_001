/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm.hiber.service;

import com.tm.hiber.system.HiberDAO;
import com.tm.hiber.model.TaskComments;
import com.tm.hiber.model.TaskMaster;
import com.tm.hiber.model.TaskPlan;
import com.tm.hiber.service.util.QueryConstants;
import com.tm.hiber.service.util.UtilService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Criterion;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Rohit.Dhalwani (dhalwanirohit@gmail.com)
 */
public class TaskManagerServiceImpl implements TaskManagerService {

    private final static Logger mLogger = Logger.getLogger(TaskManagerServiceImpl.class.getName());

    @Override
    public int saveTask(TaskMaster objTaskMaster) {
        mLogger.log(Level.INFO, "saveTask--Starts");
        int response = RESULT_UNSUCESSFUL;
        try {
            Session objSession = HiberDAO.self().getSessionFactory().openSession();
            if (objSession != null) {
                Transaction objTransaction = objSession.beginTransaction();
                objTransaction.begin();
                objSession.saveOrUpdate(objTaskMaster);
                objSession.flush();
                objTransaction.commit();
                objSession.close();
                response = RESULT_SUCESSFUL;
            }
        } catch (Exception e) {
            mLogger.log(Level.FATAL, e.getMessage());
        }
        mLogger.log(Level.INFO, "saveTask--End");
        return response;
    }

    @Override
    public TaskMaster getTask(BigDecimal taskId) {
        mLogger.log(Level.INFO, "getTask--Starts");
        TaskMaster response = null;
        Session objSession = HiberDAO.self().getSessionFactory().openSession();
        if (objSession != null) {
            Transaction objTransaction = objSession.beginTransaction();
            objTransaction.begin();
            response = (TaskMaster) objSession.get(TaskMaster.class, taskId);
            objTransaction.commit();
        }
        mLogger.log(Level.INFO, "getTask--End");
        return response;
    }

    @Override
    public List<TaskMaster> getAllTask() {
        mLogger.log(Level.INFO, "getAllTask--Starts");
        List<TaskMaster> response = new ArrayList<TaskMaster>();
        Session objSession = HiberDAO.self().getSessionFactory().openSession();
        if (objSession != null) {
            Transaction objTransaction = objSession.beginTransaction();
            objTransaction.begin();
            Query objQuery = objSession.createQuery(QueryConstants.GET_ALL_TASK);
            response = objQuery.list();
            objTransaction.commit();
        }
        mLogger.log(Level.INFO, "getAllTask--End");
        return response;
    }

    @Override
    public List<TaskMaster> getAllTask(List<Criterion> listCriterions) {
        List<TaskMaster> response = new ArrayList<TaskMaster>();
        mLogger.log(Level.INFO, "getAllTask--Starts");
        Session objSession = HiberDAO.self().getSessionFactory().openSession();
        if (objSession != null) {
            Transaction objTransaction = objSession.beginTransaction();
            objTransaction.begin();
            Criteria objCriteria = objSession.createCriteria(TaskMaster.class);
            if (listCriterions != null && listCriterions.size() > 0) {
                for (Criterion objCriterion : listCriterions) {
                    objCriteria.add(objCriterion);
                }
            }
            response = objCriteria.list();
            objTransaction.commit();
        }
        mLogger.log(Level.INFO, "getAllTask--End");
        return response;
    }
    
    @Override
    public List<TaskMaster> getAllTask(List<Criterion> listCriterions,List<Order> objListOrder) {
        List<TaskMaster> response = new ArrayList<TaskMaster>();
        mLogger.log(Level.INFO, "getAllTask--Starts");
        Session objSession = HiberDAO.self().getSessionFactory().openSession();
        if (objSession != null) {
            Transaction objTransaction = objSession.beginTransaction();
            objTransaction.begin();
            Criteria objCriteria = objSession.createCriteria(TaskMaster.class);
            if (listCriterions != null && listCriterions.size() > 0) {
                for (Criterion objCriterion : listCriterions) {
                    objCriteria.add(objCriterion);
                }
            }
            if (objListOrder != null && objListOrder.size() > 0) {
                for (Order objOrder : objListOrder) {
                    objCriteria.addOrder(objOrder);
                }
            }
            
            response = objCriteria.list();
            objTransaction.commit();
        }
        mLogger.log(Level.INFO, "getAllTask--End");
        return response;
    }

    @Override
    public Map<BigDecimal, TaskMaster> getTaskMasterMap() {
        mLogger.log(Level.INFO, "getTaskMasterMap--Starts");
        Map<BigDecimal, TaskMaster> response = new HashMap();
        List listTaskMaster = getAllTask();
        if (listTaskMaster != null) {
            for (Object listTaskMaster1 : listTaskMaster) {
                TaskMaster tm = (TaskMaster) listTaskMaster1;
                response.put(tm.getTaskId(), tm);
            }
        }
        mLogger.log(Level.INFO, "getTaskMasterMap--End");
        return response;
    }

    @Override
    public TaskMaster findTask(BigDecimal taskId, List<TaskMaster> listOfTask) {
        mLogger.log(Level.INFO, "findTask--Starts");
        TaskMaster response = null;
        if (listOfTask != null && listOfTask.size() > 0) {
            for (TaskMaster objTaskMaster : listOfTask) {
                if (objTaskMaster != null && (objTaskMaster.getTaskId().compareTo(taskId) == 0)) {
                    response = objTaskMaster;
                }
            }
        }
        mLogger.log(Level.INFO, "findTask--Ends");
        return response;
    }

    @Override
    public int deleteTask(BigDecimal taskId) {
        mLogger.log(Level.INFO, "deleteTask--Starts");
        int response = RESULT_UNSUCESSFUL;
        Session objSession = HiberDAO.self().getSessionFactory().openSession();
        if (objSession != null) {
            Transaction objTransaction = objSession.beginTransaction();
            objTransaction.begin();
            Query objQuery = objSession.createQuery(QueryConstants.DELETE_TASK);
            objQuery.setParameter("taskId", taskId);
            int res = objQuery.executeUpdate();
            objSession.flush();
            objTransaction.commit();
            if (res > 0) {
                response = RESULT_SUCESSFUL;
            }
        }
        mLogger.log(Level.INFO, "deleteTask--End");
        return response;
    }

    @Override
    public List<TaskComments> getTaskComments(BigDecimal taskId) {
        mLogger.log(Level.INFO, "getTaskComments--Starts");
        List<TaskComments> response = new ArrayList<TaskComments>();
        Session objSession = HiberDAO.self().getSessionFactory().openSession();
        if (objSession != null) {
            Transaction objTransaction = objSession.beginTransaction();
            objTransaction.begin();
            Query objQuery = objSession.createQuery(QueryConstants.GET_COMMENTS_BY_TASKID);
            objQuery.setParameter("refTaskId", taskId);
            response = objQuery.list();
            objTransaction.commit();
        }
        mLogger.log(Level.INFO, "getTaskComments--End");
        return response;
    }

    @Override
    public int saveTaskComment(TaskComments objComments) {
        mLogger.log(Level.INFO, "saveTaskComment--Starts");
        int response = RESULT_UNSUCESSFUL;
        Session objSession = HiberDAO.self().getSessionFactory().openSession();
        if (objSession != null) {
            Transaction objTransaction = objSession.beginTransaction();
            objTransaction.begin();
            objSession.saveOrUpdate(objComments);
            objSession.flush();
            objTransaction.commit();
            response = RESULT_SUCESSFUL;
        }
        mLogger.log(Level.INFO, "saveTaskComment--End");
        return response;
    }

    @Override
    public BigDecimal getNextSeqence(int tableName) {
        mLogger.log(Level.INFO, "getNextSeqence--Starts");
        BigDecimal response = null;
        Session objSession = HiberDAO.self().getSessionFactory().openSession();
        if (objSession != null) {
            Transaction objTransaction = objSession.beginTransaction();
            objTransaction.begin();
            Query query = null;
            switch (tableName) {
                case TaskManagerService.CLASS_TASK_COMMENTS:
                    query = objSession.createSQLQuery(QueryConstants.GET_NEXTSEQ_OF_TASKCOMMENTS);
                    break;
                case TaskManagerService.CLASS_TASK_MASTER:
                    query = objSession.createSQLQuery(QueryConstants.GET_NEXTSEQ_OF_MASTEROPTIONS);
                    break;
            }
            if (query != null) {
                response = (BigDecimal) query.uniqueResult();
            }
            objTransaction.commit();
        }
        mLogger.log(Level.INFO, "getNextSeqence--End");
        return response;
    }

    @Override
    public int deleteTaskComment(BigDecimal recordId) {
        mLogger.log(Level.INFO, "deleteTaskComment--Starts");
        int response = RESULT_UNSUCESSFUL;
        Session objSession = HiberDAO.self().getSessionFactory().openSession();
        if (objSession != null) {
            Transaction objTransaction = objSession.beginTransaction();
            objTransaction.begin();
            Query objQuery = objSession.createQuery(QueryConstants.DELETE_COMMENTS_BY_COMMENT_ID);
            objQuery.setParameter("recordId", recordId);
            int res = objQuery.executeUpdate();
            objSession.flush();
            objTransaction.commit();
            if (res > 0) {
                response = RESULT_SUCESSFUL;
            }
        }
        mLogger.log(Level.INFO, "deleteTaskComment--End");
        return response;
    }

    @Override
    public int saveTaskPlan(List<TaskPlan> objTaskPlans) {
        mLogger.log(Level.INFO, "saveTaskPlan--Starts");
        int response = RESULT_UNSUCESSFUL;
        Session objSession = HiberDAO.self().getSessionFactory().openSession();
        if (objSession != null) {
            Transaction objTransaction = objSession.beginTransaction();
            objTransaction.begin();
            if (objTaskPlans != null && objTaskPlans.size() > 0) {
                for (TaskPlan objTaskPlan : objTaskPlans) {
                    objTaskPlan.setAuditLastupdateby(UtilService.self().getSystemUser());
                    objTaskPlan.setAuditLastupdateon(new Date());
                    objSession.saveOrUpdate(objTaskPlan);                    
                }
                objSession.flush();
            }            
            response = RESULT_SUCESSFUL;
            objTransaction.commit();
        }
        mLogger.log(Level.INFO, "saveTaskPlan--End");
        return response;
    }

    @Override
    public List<TaskPlan> getTaskPlan(BigDecimal refTaskId) {
        List<TaskPlan> response = new ArrayList<TaskPlan>();
        mLogger.log(Level.INFO, "getTaskPlan--Starts");
        Session objSession = HiberDAO.self().getSessionFactory().openSession();
        if (objSession != null) {
            Transaction objTransaction = objSession.beginTransaction();
            objTransaction.begin();
            Criteria objCriteria = objSession.createCriteria(TaskPlan.class);
            Criterion objCondition = Restrictions.eq("id.refTaskId",refTaskId);            
            objCriteria.add(objCondition);            
            response = objCriteria.list();
            objTransaction.commit();
        }
        mLogger.log(Level.INFO, "getTaskPlan--End");
        return response;
    }

}
