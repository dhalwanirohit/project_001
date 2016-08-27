/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tm.model;

/**
 *
 * @author Rohit.Dhalwani (dhalwanirohit@gmail.com)
 */
public class CellDTO {

    private String key;
    private String value;

    public CellDTO()
    {
        
    }
    
    public CellDTO(String key) {
        this.key = key;
    }

    public CellDTO(String key, String value) {
        this.key = key;
        this.value = value;
    }   
    
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }   
    
}
