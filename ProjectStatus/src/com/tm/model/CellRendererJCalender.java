/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm.model;

import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Rohit.Dhalwani (dhalwanirohit@gmail.com)
 */
public class CellRendererJCalender implements TableCellRenderer {

    JLabel lblTextOnTable = new JLabel();

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if(value != null)
        {
            lblTextOnTable.setText(new SimpleDateFormat("dd.MM.yyyy").format((Date) value));
        }else
        {
            lblTextOnTable.setText("Awaiting Date");
        }
        return lblTextOnTable;
    }

}
