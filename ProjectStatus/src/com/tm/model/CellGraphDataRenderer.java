/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm.model;

import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Rohit.Dhalwani (dhalwanirohit@gmail.com)
 */
public class CellGraphDataRenderer extends DefaultTableCellRenderer {

    Map<String, Color> mMapColors = new HashMap<String, Color>();

    public CellGraphDataRenderer(Map<String, Color> lMapColors) {
        mMapColors = lMapColors;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        //Cells are by default rendered as a JLabel.
        JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (l.getText().equalsIgnoreCase("BUSY")) {
            l.setBackground(findColor((String) table.getValueAt(row, 0)));
            l.setForeground(findColor((String) table.getValueAt(row, 0)));

        }else if(l.getText().equalsIgnoreCase("BLANK"))
        {
            l.setBackground(Color.white);
            l.setForeground(Color.white);
        }
        else if(l.getText().equalsIgnoreCase("FREE")){
            l.setBackground(Color.white);
            l.setForeground(Color.white);
        }
        else
        {
            l.setBackground(Color.white);
            l.setForeground(Color.black);
        }
        return l;
    }

    private Color findColor(String userName) {
        return mMapColors.get(userName);
    }

}
