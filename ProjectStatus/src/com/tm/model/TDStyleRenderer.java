/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm.model;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Rohit.Dhalwani (dhalwanirohit@gmail.com)
 */
public class TDStyleRenderer extends JLabel implements TableCellRenderer {

    public TDStyleRenderer() {
        setVerticalAlignment(SwingConstants.TOP);
    }

    public TDStyleRenderer(boolean horizontalCenter) {
        if (horizontalCenter) {
            setHorizontalAlignment(SwingConstants.CENTER);
        }
        setVerticalAlignment(SwingConstants.TOP);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText(value.toString());
        return this;
    }

}
