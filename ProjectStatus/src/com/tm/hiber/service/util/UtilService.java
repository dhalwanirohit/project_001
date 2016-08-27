/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm.hiber.service.util;

import com.tm.main.SystemUI;
import com.tm.model.CellDTO;
import com.tm.hiber.model.TaskComments;
import com.tm.hiber.model.TaskMaster;
import com.tm.hiber.model.TaskPlan;
import com.tm.model.TaskMasterExcelTemplate;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Rohit
 */
public class UtilService {

    private static UtilService objUtilService = null;

    public static UtilService self() {
        if (objUtilService == null) {
            objUtilService = new UtilService();
        }
        return objUtilService;
    }

    public void setCenter(JDialog jd) {
        Toolkit t = Toolkit.getDefaultToolkit();
        Dimension d = t.getScreenSize();
        jd.setLocation((d.width / 2) - (jd.getWidth() / 2), (d.height / 2) - (jd.getHeight() / 2));
    }

    public void setCenter(JFrame jf) {
        Toolkit t = Toolkit.getDefaultToolkit();
        Dimension d = t.getScreenSize();
        jf.setLocation((d.width / 2) - (jf.getWidth() / 2), (d.height / 2) - (jf.getHeight() / 2));
    }

    public void setSelected(JComboBox jcmb, String key) {
        for (int i = 0; i < jcmb.getItemCount(); i++) {
            CellDTO objCellDTO = (CellDTO) jcmb.getItemAt(i);
            if (objCellDTO != null && objCellDTO.getKey().equals(key)) {
                jcmb.setSelectedIndex(i);
            }
        }
    }

    public void populateDropDown(Map<String, String> objMap, JComboBox jcmb) {
        if (objMap != null && jcmb != null) {
            CellDTO dto = null;
            jcmb.removeAllItems();
            dto = new CellDTO();
            dto.setKey("-1");
            dto.setValue("[Select]");
            jcmb.addItem(dto);
            Set<String> keys = objMap.keySet();
            Iterator<String> keysItr = keys.iterator();
            while (keysItr.hasNext()) {
                String key = keysItr.next();
                dto = new CellDTO();
                dto.setKey(key);
                dto.setValue(objMap.get(key));
                jcmb.addItem(dto);
            }
        }
    }

    /**
     *
     * @param objTable
     * @param data
     * @param refClass
     * @return Count of Total Records
     */
    public int doRenderData(JTable objTable, List data, int refClass) {
        int response = 0;

        TableModel lTableModel = objTable.getModel();
        DefaultTableModel lDefaultTableModel = (DefaultTableModel) lTableModel;

        int totalRows = lDefaultTableModel.getRowCount();
        for (int rowCounter = 0; rowCounter < totalRows; rowCounter++) {
            lDefaultTableModel.removeRow(0);
        }

        if (data != null && data.size() > 0) {

            for (Object objTask : data) {
                switch (refClass) {
                    case 1:
                        lDefaultTableModel.addRow(convertToArray((TaskMaster) objTask));
                        break;
                    case 2:
                        lDefaultTableModel.addRow(convertToArray((TaskComments) objTask));
                        break;
                    case 3:
                        lDefaultTableModel.addRow(convertToArray((TaskMasterExcelTemplate) objTask));
                        break;
                    case 4:
                        lDefaultTableModel.addRow(convertToArray((TaskPlan) objTask));
                        break;
                }

            }

            response = data.size();

        }
        objTable.validate();

        return response;
    }

    public Object[] convertToArray(TaskMaster objTaskMaster) {
        List<String> response = new ArrayList<String>();
        response.add(objTaskMaster.getTaskId().toString());
        response.add(objTaskMaster.getTaskReference());
        response.add(objTaskMaster.getTitle());
        response.add(SystemUI.mPriorityMasterData.get(objTaskMaster.getPriority()));
        response.add(SystemUI.mStatusMasterData.get(objTaskMaster.getStatus()));
        response.add(objTaskMaster.getCreateDate() == null ? "" : new SimpleDateFormat("dd.MMM.yyyy-EEE").format(objTaskMaster.getCreateDate()));
        response.add(SystemUI.mProjectMasterData.get(objTaskMaster.getProjectName()));
        response.add(objTaskMaster.getEta() == null ? "" : new SimpleDateFormat("dd.MMM.yyyy-EEE").format(objTaskMaster.getEta()));
        response.add(SystemUI.mUserMasterData.get(objTaskMaster.getAssignee()));
        response.add(objTaskMaster.getCommentText());
        return response.toArray();
    }

    public Object[] convertToArray(TaskMasterExcelTemplate objTaskMaster) {
        List<String> response = new ArrayList<String>();
        response.add(objTaskMaster.getTaskReference());
        response.add(objTaskMaster.getTitle());
        response.add(objTaskMaster.getDescription());
        response.add(objTaskMaster.getProjectName());
        if (objTaskMaster.getStatus() != null && objTaskMaster.getStatus().length() > 0) {
            response.add(objTaskMaster.getStatus());
        } else {
            response.add("-----");
        }
        return response.toArray();
    }

    public Object[] convertToArray(TaskComments objTaskComments) {
        List<String> response = new ArrayList<String>();
        response.add(objTaskComments.getCommentText());
        response.add(objTaskComments.getCommentDate() == null ? "" : new SimpleDateFormat("dd.MMM.yyyy-EEE").format(objTaskComments.getCommentDate()));
        return response.toArray();
    }

    public Object[] convertToArray(TaskPlan objTaskPlan) {
        Object[] obj = new Object[4];
        obj[0] = objTaskPlan.getId().getRecordId();
        obj[1] = objTaskPlan.getAnalysisLevel();
        obj[2] = objTaskPlan.getPlannedDate() == null ? "" : new SimpleDateFormat("dd.MM.yyyy").format(objTaskPlan.getPlannedDate());
        obj[3] = objTaskPlan.getAnalyst();
        return obj;
    }

    public void setColumnSize(int index, int size, JTable jtable) {
        jtable.getColumnModel().getColumn(index).setMaxWidth(size);
        jtable.getColumnModel().getColumn(index).setPreferredWidth(size);
        jtable.getColumnModel().getColumn(index).setWidth(size);
    }

    public String getSystemUser() {
        return System.getProperty("user.name");
    }

    public List<java.awt.Color> getRandomColor(int noOfRequiredSet) {
        List<java.awt.Color> response = new ArrayList<Color>();
        while (response.size() <= noOfRequiredSet) {
            int red = randInt(50, 255);
            int green = randInt(50, 255);
            int blue = randInt(50, 255);
            java.awt.Color objColor = new java.awt.Color(red, green, blue);
            if (!response.contains(objColor)) {
                response.add(objColor);
            }
        }
        return response;
    }

    public int randInt(int min, int max) {
        // Usually this can be a field rather than a method variable
        Random rand = new Random();
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    public ImageIcon getIcon(String imageName) {
        return new ImageIcon(getClass().getResource(imageName));
    }

    public String getCommentsAsHTMLText(List<TaskComments> listOfComments) {
        StringBuilder sb = new StringBuilder("<HTML><STYLE>td{font-family:calibri;font-size:12}</STYLE><BODY><TABLE>");
        for (TaskComments objTaskComments : listOfComments) {
            sb.append("<TR><TD>");
            sb.append(objTaskComments.getCommentText());
            sb.append("</TD></TR>");
        }
        sb.append("</TABLE></HTML>");
        return sb.toString();
    }

    public Date getDateByDay(int day) {
        Calendar cal = Calendar.getInstance();        
        cal.set(Calendar.DAY_OF_WEEK, day);        
        return cal.getTime();
    }

}
