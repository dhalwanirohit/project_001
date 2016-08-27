/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tm.model;

import java.util.Comparator;

/**
 *
 * @author Rohit.Dhalwani (dhalwanirohit@gmail.com)
 */
public class NumberComparator implements Comparator<Object>{

    @Override
    public int compare(Object o1, Object o2) {
        Integer lNum = parseInt(o1);
        Integer rNum = parseInt(o2);
         if (lNum == null && rNum == null) {
            return 0;
        }

        if (lNum != null && rNum == null) {

            return 1;
        }

        if (lNum == null && rNum != null) {

            return -1;
        }

        if (lNum != null && rNum != null) {

            return lNum.compareTo(rNum);
        }
        return 0;
    }
    
    private Integer parseInt(Object o)
    {
        if(o != null)
        {
            try
            {
                return Integer.parseInt((String)o);
            }
            catch(Exception e)
            {
                System.out.println("Exception while parsing integer " + e.getMessage());
            }
        }
        
        return null;
    }

}
