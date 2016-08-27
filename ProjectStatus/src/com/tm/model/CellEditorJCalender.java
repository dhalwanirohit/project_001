/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm.model;

import com.toedter.calendar.JDateChooser;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventObject;
import java.util.List;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author Rohit.Dhalwani (dhalwanirohit@gmail.com)
 */
public class CellEditorJCalender implements TableCellEditor {

    private final JDateChooser mDateChooser = new JDateChooser();
    private Date objDate;
    private final List<CellEditorListener> mListners = new ArrayList<CellEditorListener>();
    private final ChangeEvent ce = new ChangeEvent(this);

    public CellEditorJCalender() {
        
        JTextField jf = (JTextField) mDateChooser.getDateEditor().getUiComponent();
        jf.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                stopCellEditing();
            }
        });
    }
    
    
    
    

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value != null) {
            objDate = (Date) value;
            mDateChooser.setDate(objDate);
        }
        
        
        
        return mDateChooser;
    }

    @Override
    public Object getCellEditorValue() {
        return objDate;
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    @Override
    public boolean stopCellEditing() {
        objDate = mDateChooser.getDate();
        for (CellEditorListener lListners : mListners) {
            lListners.editingStopped(ce);
        }
        
        return true;
    }

    @Override
    public void cancelCellEditing() {
//        for (CellEditorListener lListners : mListners) {
//            lListners.editingCanceled(ce);
//        }
    }

    @Override
    public void addCellEditorListener(CellEditorListener l) {
        mListners.add(l);
    }

    @Override
    public void removeCellEditorListener(CellEditorListener l) {
        mListners.remove(l);
    }

}
