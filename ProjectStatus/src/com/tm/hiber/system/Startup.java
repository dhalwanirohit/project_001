/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm.hiber.system;

import com.tm.main.SystemUI;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

/**
 *
 * @author Rohit.Dhalwani (dhalwanirohit@gmail.com)
 */
public class Startup extends SwingWorker<Void, Integer> {

    int currentTask = 0;
    int totalTask = 4;
    private JProgressBar jb;
    private JFrame jf = null;
    
    public Startup(JProgressBar jb,JFrame jf)
    {
        this.jb = jb;
        this.jf = jf;
    }

    @Override
    protected Void doInBackground() throws Exception {
        
        setCurrentTask(1);
        publish(getCurrentProgress());        
        
        
        setCurrentTask(2);
        publish(getCurrentProgress());
        HiberDAO.self().getSessionFactory().openSession();
        
        
        
        setCurrentTask(3);
        publish(getCurrentProgress());
        HiberDAO.self().getSessionFactory().openSession();
        
         setCurrentTask(4);
        publish(getCurrentProgress());
        HiberDAO.self().getSessionFactory().openSession();
        
        
        return null;
    }

    @Override
    protected void process(List<Integer> chunks) {
        if(this.jb != null)
        {
            System.out.println("chunks-->" + chunks);
            jb.setValue(chunks.get(chunks.size() -1));
            System.out.println("jb-->" + jb.getValue());
           jb.validate();
        }
    }
    
    

    private void setCurrentTask(int currentTask) {
        this.currentTask = currentTask;
        setProgress(getCurrentProgress());        
    }

    private int getCurrentProgress() {
        if (currentTask == 0) {
            return 0;
        }
        return ((currentTask * 100) / totalTask);
    }

    @Override
    protected void done() {
        
        
        
         /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                    new SystemUI().setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        
        jf.dispose();
    }
    
    

}
