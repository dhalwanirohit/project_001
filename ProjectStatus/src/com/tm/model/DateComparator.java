/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm.model;

import com.tm.main.SystemUI;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import org.apache.log4j.Logger;

/**
 *
 * @author Rohit.Dhalwani (dhalwanirohit@gmail.com)
 */
public class DateComparator implements Comparator<Object> {

    private final static Logger mLogger = Logger.getLogger(SystemUI.class.getName());

    @Override
    public int compare(Object o1, Object o2) {

        java.util.Date lDate = parseDate(o1);
        java.util.Date rDate = parseDate(o2);

        if (lDate == null && rDate == null) {
            return 0;
        }

        if (lDate != null && rDate == null) {

            return 1;
        }

        if (lDate == null && rDate != null) {

            return -1;
        }

        if (lDate != null && rDate != null) {

            return lDate.compareTo(rDate);
        }

        return 0;
    }

    private java.util.Date parseDate(Object o) {
        if (o != null) {
            String dateStr = (String) o;
            if (dateStr.trim().length() > 0) {
                try {
                    SimpleDateFormat sf = new SimpleDateFormat("dd.MMM.yyyy-EEE");
                    return sf.parse(String.valueOf(o));
                } catch (Exception e) {
                    mLogger.fatal("Error while parsing Date " + e.getMessage());
                }
            }
        }
        return null;
    }

}
