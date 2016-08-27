/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm.main;

import com.tm.hiber.service.util.UtilService;
import com.tm.model.CellDTO;
import com.tm.hiber.model.TaskComments;
import com.tm.hiber.model.TaskMaster;
import com.tm.hiber.service.TaskManagerService;
import com.tm.hiber.service.TaskManagerServiceImpl;
import com.tm.model.CellRendererTextArea;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author user
 */
public class CommentUI extends javax.swing.JFrame {

    private SystemUI mParentFrame = null;
    private List<String> mProjects = null;
    Map<String, List<TaskMaster>> mMapTaskMaster = new HashMap<String, List<TaskMaster>>();
    List<TaskComments> mTaskCommentList = new ArrayList<TaskComments>();
    private String mSelectedTaskId = "";
    private final static Logger mLogger = Logger.getLogger(CommentUI.class.getName());

    /* Services */
    private final TaskManagerService mSrvTaskManagerService = new TaskManagerServiceImpl();

    /**
     * Creates new form CommentUI
     *
     * @param lParentFrame
     */
    public CommentUI(SystemUI lParentFrame) {
        mLogger.log(Level.INFO, "CommentUI--Starts");
        initComponents();
        setIconImage(UtilService.self().getIcon("/com/tm/system/icons32/Windows_32x32.png").getImage());
        this.mParentFrame = lParentFrame;
        UtilService.self().setCenter(this);
        setTitle("Comment..");
        JTableHeader jh = tableCommentTable.getTableHeader();
        TableCellRenderer tc = jh.getDefaultRenderer();
        DefaultTableCellRenderer dtc = (DefaultTableCellRenderer) tc;
        dtc.setHorizontalAlignment(SwingConstants.CENTER);
        jh.setFont(new Font("Calibri", Font.BOLD, 12));
        tableCommentTable.setFont(new Font("Calibri", Font.PLAIN, 12));
        renderTask(jTree2);
        UtilService.self().setColumnSize(0, 450, tableCommentTable);
        mLogger.log(Level.INFO, "CommentUI--Ends");
        //tableCommentTable.getColumnModel().getColumn(0).setCellRenderer(new CellRendererTextArea());
        //tableCommentTable.setRowHeight(50);
        
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b); //To change body of generated methods, choose Tools | Templates.
        renderTask(jTree2);
    }
    
    
            

    public List<TaskComments> getComments(BigDecimal taskId) {
        mLogger.log(Level.INFO, "getComments--Starts");
        if (mTaskCommentList != null) {
            mTaskCommentList.clear();
        }
        mTaskCommentList = mSrvTaskManagerService.getTaskComments(taskId);
        mLogger.log(Level.INFO, "getComments--Ends");
        return mTaskCommentList;
    }

    private void renderTask(JTree objJTree) {
        mLogger.log(Level.INFO, "renderTask--Starts");
        if (SystemUI.mProjectMasterData != null && SystemUI.mProjectMasterData.size() > 0) {
            Set<String> keys = SystemUI.mProjectMasterData.keySet();
            Iterator<String> keysItr = keys.iterator();
            mProjects = new ArrayList<String>();

            while (keysItr.hasNext()) {
                String s = keysItr.next();
                List<Criterion> cList = new ArrayList<Criterion>();
                cList.add(Restrictions.eq("projectName", s));
                mMapTaskMaster.put(s, new TaskManagerServiceImpl().getAllTask(cList));
                mProjects.add(SystemUI.mProjectMasterData.get(s));
            }
        }

        if (objJTree != null) {

            DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Task List");

            DefaultMutableTreeNode branchNode = null;

            Set<String> keys = SystemUI.mProjectMasterData.keySet();
            Iterator<String> keysItr = keys.iterator();

            DefaultMutableTreeNode leafNode = null;
            while (keysItr.hasNext()) {

                String projectId = keysItr.next();
                String projectName = SystemUI.mProjectMasterData.get(projectId);
                branchNode = new DefaultMutableTreeNode(new CellDTO(projectId, projectName));

                List<TaskMaster> listTaskMaster = mMapTaskMaster.get(projectId);
                for (TaskMaster t : listTaskMaster) {
                    if (t.getStatus() != null && !(t.getStatus().equals("-1"))) {
                        String leafText = t.getTaskReference() == null ? "":t.getTaskReference();
                        leafNode = new DefaultMutableTreeNode(new CellDTO(t.getTaskId().toString(), leafText + " # " + t.getTitle()));
                        branchNode.add(leafNode);
                    }
                }
                rootNode.add(branchNode);
            }

            objJTree.removeAll();
            DefaultTreeModel dtm = new DefaultTreeModel(rootNode);
            objJTree.setModel(dtm);
        }

        mLogger.log(Level.INFO, "renderTask--Ends");
    }

    public void updateTaskList() {
        mLogger.log(Level.INFO, "updateTaskList--Starts");
        /* Render records on Table */

        UtilService.self().doRenderData(tableCommentTable, mTaskCommentList, TaskManagerService.CLASS_TASK_COMMENTS);
        mLogger.log(Level.INFO, "updateTaskList--Ends");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jSplitPane2 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree2 = new javax.swing.JTree();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableCommentTable = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/tm/system/icons16/Windows_16x16.png"))); // NOI18N
        jButton1.setToolTipText("Switch Window");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/tm/system/icons16/refresh-icon_16x16.png"))); // NOI18N
        jButton2.setToolTipText("Refresh");
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/tm/system/icons16/Close_16x16.png"))); // NOI18N
        jButton3.setToolTipText("System Exit");
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);

        jSplitPane2.setDividerLocation(300);
        jSplitPane2.setDividerSize(2);

        jTree2.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jTree2.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTree2ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jTree2);

        jSplitPane2.setLeftComponent(jScrollPane1);

        jSplitPane1.setDividerLocation(400);
        jSplitPane1.setDividerSize(2);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jScrollPane2.setAutoscrolls(true);

        tableCommentTable.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        tableCommentTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Comment", ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableCommentTable.setShowHorizontalLines(false);
        tableCommentTable.setShowVerticalLines(false);
        tableCommentTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableCommentTableMouseClicked(evt);
            }
        });
        tableCommentTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tableCommentTableKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(tableCommentTable);

        jSplitPane1.setTopComponent(jScrollPane2);

        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jTextArea1.setForeground(new java.awt.Color(0, 0, 153));
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextArea1KeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(jTextArea1);

        jSplitPane1.setRightComponent(jScrollPane3);

        jSplitPane2.setRightComponent(jSplitPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 768, Short.MAX_VALUE)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (mParentFrame != null) {
            mParentFrame.setVisible(true);
            setVisible(false);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTree2ValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_jTree2ValueChanged
        if (jTree2.getLastSelectedPathComponent() != null) {
            mSelectedTaskId = "";
            DefaultMutableTreeNode df = (DefaultMutableTreeNode) jTree2.getLastSelectedPathComponent();
            if (df != null && df.isLeaf() && df.getLevel() == 2) {
                CellDTO dto = (CellDTO) df.getUserObject();
                mSelectedTaskId = dto.getKey();
                if (dto.getKey() != null && mSelectedTaskId != null && mSelectedTaskId.length() > 0) {
                    mTaskCommentList = getComments(new BigDecimal(mSelectedTaskId));
                    updateTaskList();
                }
            }
        }

    }//GEN-LAST:event_jTree2ValueChanged

    private void tableCommentTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableCommentTableKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            int[] selectedRowIndex = tableCommentTable.getSelectedRows();
            if (selectedRowIndex != null && selectedRowIndex.length > 0) {
                for (int i = 0; i < selectedRowIndex.length; i++) {
                    TaskComments tc = mTaskCommentList.get(selectedRowIndex[i]);
                    mSrvTaskManagerService.deleteTaskComment(tc.getRecordId());
                }
                mTaskCommentList = getComments(new BigDecimal(mSelectedTaskId));
                updateTaskList();
            }
        }
    }//GEN-LAST:event_tableCommentTableKeyPressed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
//        if (mSelectedTaskId != null && mSelectedTaskId.length() > 0) {
//            mTaskCommentList = getComments(new BigDecimal(mSelectedTaskId));
        renderTask(jTree2);
        updateTaskList();
        jTree2.validate();
        tableCommentTable.clearSelection();
        // }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        System.exit(0);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTextArea1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextArea1KeyPressed
        if (evt != null && evt.isControlDown() &&  evt.getKeyCode() == KeyEvent.VK_ENTER && mSelectedTaskId != null && mSelectedTaskId.length() > 0) {
            if (jTextArea1.getText() != null && jTextArea1.getText().trim().length() > 0) {
                TaskComments tc = new TaskComments();
                tc.setRecordId(mSrvTaskManagerService.getNextSeqence(TaskManagerService.CLASS_TASK_COMMENTS));
                tc.setCommentText(jTextArea1.getText());
                tc.setCommentDate(new Date());
                tc.setRefTaskId(new BigDecimal(mSelectedTaskId));
                int response = mSrvTaskManagerService.saveTaskComment(tc);
                if (response == TaskManagerService.RESULT_SUCESSFUL) {
                    mTaskCommentList = getComments(new BigDecimal(mSelectedTaskId));
                    updateTaskList();
                    jTextArea1.setText("");                    
                }
            }

        } else if (evt != null && evt.getKeyCode() == KeyEvent.VK_UP) {
            if (mTaskCommentList != null && mTaskCommentList.size() > 0) {
                jTextArea1.setText(mTaskCommentList.get(mTaskCommentList.size() - 1).getCommentText());
            }
        }
    }//GEN-LAST:event_jTextArea1KeyPressed

    private void tableCommentTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableCommentTableMouseClicked
        if (evt != null && tableCommentTable.getRowCount() > 0 && tableCommentTable.getSelectedRow() > -1) {
            jTextArea1.setText((String) tableCommentTable.getValueAt(tableCommentTable.getSelectedRow(), 0));
        }
    }//GEN-LAST:event_tableCommentTableMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTree jTree2;
    private javax.swing.JTable tableCommentTable;
    // End of variables declaration//GEN-END:variables
}
