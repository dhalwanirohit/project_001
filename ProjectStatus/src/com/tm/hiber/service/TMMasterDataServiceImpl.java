/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm.hiber.service;

import com.tm.hiber.system.HiberDAO;
import com.tm.hiber.model.TaskOptions;
import com.tm.hiber.service.util.QueryConstants;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Level;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.apache.log4j.Logger;

/**
 *
 * @author Rohit.Dhalwani (dhalwanirohit@gmail.com)
 */
public class TMMasterDataServiceImpl implements TMMasterDataService {

    private final static Logger mLogger = Logger.getLogger(TMMasterDataServiceImpl.class.getName());
    
    @Override
    public List<TaskOptions> getAllOptions() {
        List<TaskOptions> response = new ArrayList<TaskOptions>();
        mLogger.log(Level.INFO,"getAllOptions -- Starts");
        Session objSession = HiberDAO.self().getSessionFactory().openSession();
        if (objSession != null) {
            Transaction objTransaction = objSession.beginTransaction();
            objTransaction.begin();
            Query objQuery = objSession.createQuery(QueryConstants.GET_OPTIONS_QUERY);
            response = objQuery.list();
            objTransaction.commit();
            objSession.close();
        }
        mLogger.log(Level.INFO,"getAllOptions -- End");
        return response;
    }
    

}
