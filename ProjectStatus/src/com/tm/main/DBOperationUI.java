/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm.main;

import com.tm.hiber.model.TaskMaster;
import com.tm.hiber.service.TMDataOperationService;
import com.tm.hiber.service.TMDataOperationServiceImpl;
import com.tm.hiber.service.TaskManagerService;
import com.tm.hiber.service.TaskManagerServiceImpl;
import com.tm.hiber.service.util.UtilService;
import com.tm.model.TaskMasterExcelTemplate;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
public class DBOperationUI extends javax.swing.JFrame implements Runnable{

    private final TMDataOperationService mSrvDataService = new TMDataOperationServiceImpl();
    private final TaskManagerService mSrvTaskManagerService = new TaskManagerServiceImpl();
    private List<TaskMasterExcelTemplate> mTaskMasterList = new ArrayList<TaskMasterExcelTemplate>();
    private File selectedFile = null;
    private final static Logger mLogger = Logger.getLogger(DBOperationUI.class.getName());

    /**
     * Creates new form DBOperationUI
     */
    public DBOperationUI() {
        mLogger.log(Level.INFO, "DBOperationUI--Starts");
        initComponents();
        UtilService.self().setCenter(this);

        setIconImage(UtilService.self().getIcon("/com/tm/system/icons16/Wizard_16x16.png").getImage());
        KeyboardFocusManager mgr = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        mgr.addKeyEventDispatcher(new KeyEventDispatcher() {

            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    dispose();
                }

                return false;
            }
        });

        JTableHeader jh = jTable1.getTableHeader();
        TableCellRenderer tc = jh.getDefaultRenderer();
        DefaultTableCellRenderer dtc = (DefaultTableCellRenderer) tc;
        dtc.setHorizontalAlignment(SwingConstants.CENTER);
        jh.setFont(new Font("Calibri", Font.BOLD, 12));

        jTable1.setFont(new Font("Calibri", Font.PLAIN, 12));

        UtilService.self().setColumnSize(0, 100, jTable1);
        UtilService.self().setColumnSize(2, 100, jTable1);
        UtilService.self().setColumnSize(3, 100, jTable1);

        setTitle("Data Operation - Excel File");

        mLogger.log(Level.INFO, "DBOperationUI--Ends");
    }

    private void updateTable() {
        mLogger.log(Level.INFO, "updateTable--Starts");
        UtilService.self().doRenderData(jTable1, mTaskMasterList, TaskManagerService.CLASS_TASK_MASTER_TEMPLATE);
        mLogger.log(Level.INFO, "updateTable--Ends");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        btnBrowesFile = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtFilePath = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnImportData = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTabbedPane1.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        jPanel1.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        btnBrowesFile.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        btnBrowesFile.setText("Browse");
        btnBrowesFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowesFileActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel1.setText("Select File:");

        txtFilePath.setEditable(false);
        txtFilePath.setBackground(new java.awt.Color(204, 204, 204));
        txtFilePath.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        txtFilePath.setFocusable(false);

        jTable1.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Task Reference", "Title", "Description", "Project", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setToolTipText("");
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTable1KeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        btnImportData.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        btnImportData.setText("Import Data");
        btnImportData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportDataActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jButton1.setText("Save Data");
        jButton1.setToolTipText("Save Imported Data to Database");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jButton2.setText("Reset Data");
        jButton2.setToolTipText("Reset Data");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jButton3.setText("Close");
        jButton3.setToolTipText("Save Imported Data to Database");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 689, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtFilePath)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBrowesFile)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnImportData))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnBrowesFile, btnImportData});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton1, jButton2, jButton3});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBrowesFile)
                    .addComponent(jLabel1)
                    .addComponent(txtFilePath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnImportData))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Import Data", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBrowesFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowesFileActionPerformed
        JFileChooser jf = new JFileChooser("C:");
        jf.addChoosableFileFilter(new FileNameExtensionFilter("Excel", "xls"));
        jf.showDialog(null, "Select File");
        if (jf.getSelectedFile() != null && jf.getSelectedFile().canRead()) {
            selectedFile = jf.getSelectedFile();
            txtFilePath.setText(jf.getSelectedFile().getPath());
        }
    }//GEN-LAST:event_btnBrowesFileActionPerformed

    private void btnImportDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportDataActionPerformed
        btnImportData.setEnabled(false);
        if (selectedFile != null) {
            String fileType = selectedFile.getPath();
            fileType = fileType.substring(fileType.lastIndexOf("."), fileType.length());
            if (fileType.endsWith("xls")) {
                mTaskMasterList.clear();
                mTaskMasterList = mSrvDataService.prepareTaskMasterFromExcel(selectedFile);
                updateTable();
                if (mTaskMasterList.size() > 0) {
                    JOptionPane.showMessageDialog(null, "Press 'Save' button to Save Data", "Message", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "File Should be .xls Type", "Message", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(null, "Please Select File", "Message", JOptionPane.ERROR_MESSAGE);
        }
        btnImportData.setEnabled(true);
    }//GEN-LAST:event_btnImportDataActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        saveTask();
        //updateTable();

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        txtFilePath.setText("");
        mTaskMasterList.clear();
        updateTable();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTable1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            int selectedRowIndex = jTable1.getSelectedRow();
            if (selectedRowIndex > -1) {
                mTaskMasterList.remove(selectedRowIndex);
                updateTable();
            }
        }
    }//GEN-LAST:event_jTable1KeyPressed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBrowesFile;
    private javax.swing.JButton btnImportData;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField txtFilePath;
    // End of variables declaration//GEN-END:variables

    private void saveTask() {    
       EventQueue.invokeLater(this);
    }

    @Override
    public void run() {
        for (TaskMasterExcelTemplate objExcelTemplate : mTaskMasterList) {
            TaskMaster newRecord = new TaskMaster();
            newRecord.setTaskId(mSrvTaskManagerService.getNextSeqence(TaskManagerService.CLASS_TASK_MASTER));
            newRecord.setTitle(objExcelTemplate.getTitle());
            newRecord.setDescription(objExcelTemplate.getDescription());
            newRecord.setTaskReference(objExcelTemplate.getTaskReference());
            newRecord.setProjectName(objExcelTemplate.getProjectName());
            newRecord.setCreateDate(objExcelTemplate.getCreateDate());
            newRecord.setAuditLastupdateby(objExcelTemplate.getAuditLastupdateby());
            newRecord.setAuditLastupdateon(objExcelTemplate.getAuditLastupdateon());
            newRecord.setPriority(objExcelTemplate.getPriority());
            newRecord.setStatus("0");
            int response = mSrvTaskManagerService.saveTask(newRecord);
            if (response == TaskManagerService.RESULT_SUCESSFUL) {
                objExcelTemplate.setStatus("Uploaded");
            } else {
                objExcelTemplate.setStatus("Not Uploaded");
            }
        }
        
        updateTable();
    }
}
