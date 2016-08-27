/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tm.system.splash;

import com.toedter.calendar.JDateChooser;
import java.util.Date;

/**
 *
 * @author Rohit.Dhalwani (dhalwanirohit@gmail.com)
 */
public class TestClass {
    
    public static void main(String[] args)
    {
       // create 2 dates
   Date date = new Date(2011, 5, 21);
   Date date2 = new Date(2015, 1, 21);

   // tests if date 2 is before date and print
   boolean before = date2.before(date);
   System.out.println("Date 2 is before date: " + before);

   // tests if date is before date 2 and print
   before = date.before(date2);
   System.out.println("Date is before date 2: " + before);
    }

}
