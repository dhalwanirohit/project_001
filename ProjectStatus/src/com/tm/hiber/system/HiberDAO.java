/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm.hiber.system;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 *
 * @author user
 */
public class HiberDAO {

    private static HiberDAO instance;
    private SessionFactory sessionFactory;
    private static final Logger mLogger = Logger.getLogger(HiberDAO.class.getName());

    public static HiberDAO self() {
        if (instance == null) {
            instance = new HiberDAO();
        }
        return instance;
    }

    public HiberDAO() {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();            
        } catch (Throwable ex) {
            mLogger.log(Level.SEVERE, ex.getMessage());            
            System.exit(0);
        }
    }  

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}
