/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm.main;

import com.tm.hiber.service.util.UtilService;
import com.tm.model.CellDTO;
import com.tm.hiber.model.TaskMaster;
import com.tm.hiber.model.TaskPlan;
import com.tm.hiber.model.TaskPlanId;
import com.tm.hiber.service.TMMasterDataService;
import com.tm.hiber.service.TMMasterDataServiceImpl;
import com.tm.hiber.service.TaskManagerService;
import com.tm.hiber.service.TaskManagerServiceImpl;
import com.tm.hiber.service.util.MyOwnFocusTraversalPolicy;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
public class TaskUI extends javax.swing.JFrame {

    /**
     * Creates new form Record
     */
    private BigDecimal mTaskId;
    private TaskMaster mTaskMaster;
    private SystemUI parentFrame;
    private List<TaskPlan> mListTaskPlan = new ArrayList<TaskPlan>();

    private final TaskManagerService mSrvTaskManagerService = new TaskManagerServiceImpl();
    private final TMMasterDataService mSrvTMMasterDataService = new TMMasterDataServiceImpl();
    private final static Logger mLogger = Logger.getLogger(TaskUI.class.getName());

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        renderData();
    }

    public void setTaskId(BigDecimal pTaskId) {
        this.mTaskId = pTaskId;
    }

    public BigDecimal getTaskId() {
        return mTaskId;
    }

    private void renderData() {
        mLogger.log(Level.INFO, "renderData--Starts");
        if (mTaskId != null) {

            /**
             * For Existing Record Find task and Render on Screen
             */
            mTaskMaster = mSrvTaskManagerService.findTask(mTaskId, parentFrame.getCurrentTaskMasterData());

            if (mTaskMaster != null) {
                txtRecordNo.setText(mTaskId.toString());
                txtReferenceNo.setText(mTaskMaster.getTaskReference());
                txtTitle.setText(mTaskMaster.getTitle());
                txtDescription.setText(mTaskMaster.getDescription());
                txtComments.setText(mTaskMaster.getCommentText());
                UtilService.self().setSelected(cmbAssignee, mTaskMaster.getAssignee());
                UtilService.self().setSelected(cmbPriority, mTaskMaster.getPriority());
                UtilService.self().setSelected(cmbProject, mTaskMaster.getProjectName());
                UtilService.self().setSelected(cmbStatus, mTaskMaster.getStatus());
                dtDeliveryDate.setDate(mTaskMaster.getEta());
                dtReportDate.setDate(mTaskMaster.getCreateDate());
                UtilService.self().setSelected(cmbModule, mTaskMaster.getModuleName());
                txtCodeRevision.setText(mTaskMaster.getCodeRevision());
                UtilService.self().setSelected(cmbRetrofitRequired, mTaskMaster.getRetrofitRequired());
                UtilService.self().setSelected(cmbFramework, mTaskMaster.getFrameworkVersion());
                UtilService.self().setSelected(cmbReleasePhase, mTaskMaster.getReleasePhase());
                txtReleaseNote.setText(mTaskMaster.getReleaseNotes());
            }

        } else {

            /* For New Record */
            mTaskId = mSrvTaskManagerService.getNextSeqence(TaskManagerService.CLASS_TASK_MASTER);
            txtRecordNo.setText(mTaskId.toString());
        }

        renderTaskPlan();

       // renderComments();
        mLogger.log(Level.INFO, "renderData--Ends");
    }

    public TaskUI(SystemUI lParentFrame) {
        mLogger.log(Level.INFO, "TaskUI--Starts");
        initComponents();
        setIconImage(UtilService.self().getIcon("/com/tm/system/icons16/Document_16x16.png").getImage());
        this.parentFrame = lParentFrame;
        setTitle("Task");
        UtilService.self().setCenter(this);
        setDefaultCloseOperation(HIDE_ON_CLOSE);

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

        //tableTaskPlanning.getColumnModel().getColumn(2).setCellEditor(new CellEditorJCalender());
        //tableTaskPlanning.getColumnModel().getColumn(2).setCellRenderer(new CellRendererJCalender());
        tableTaskPlanning.setRowHeight(20);
        UtilService.self().setColumnSize(0, 30, tableTaskPlanning);
        UtilService.self().setColumnSize(1, 100, tableTaskPlanning);
        UtilService.self().setColumnSize(2, 100, tableTaskPlanning);
        JTableHeader jh = tableTaskPlanning.getTableHeader();
        TableCellRenderer tc = jh.getDefaultRenderer();
        DefaultTableCellRenderer dtc = (DefaultTableCellRenderer) tc;
        dtc.setHorizontalAlignment(SwingConstants.CENTER);
        jh.setFont(new Font("Calibri", Font.BOLD, 12));

        tableTaskPlanning.setFont(new Font("Calibri", Font.PLAIN, 12));

        DefaultTableCellRenderer cellRenderer1 = (DefaultTableCellRenderer) tableTaskPlanning.getCellRenderer(0, 0);
        cellRenderer1.setHorizontalAlignment(SwingConstants.CENTER);
        cellRenderer1.setVerticalAlignment(SwingConstants.TOP);

        UtilService.self().populateDropDown(SystemUI.mUserMasterData, cmbAssignee);
        UtilService.self().populateDropDown(SystemUI.mProjectMasterData, cmbProject);
        UtilService.self().populateDropDown(SystemUI.mStatusMasterData, cmbStatus);
        UtilService.self().populateDropDown(SystemUI.mPriorityMasterData, cmbPriority);
        UtilService.self().populateDropDown(SystemUI.mRetrofitMasterData, cmbRetrofitRequired);
        UtilService.self().populateDropDown(SystemUI.mFrameworkMasterData, cmbFramework);
        UtilService.self().populateDropDown(SystemUI.mReleasePhaseMasterData, cmbReleasePhase);
        UtilService.self().populateDropDown(SystemUI.mModuleMasterData, cmbModule);

        setFocusTraversalPolicy();
        
        mLogger.log(Level.INFO, "TaskUI--Ends");
    }

    private void renderTaskPlan() {

        if (mTaskId != null) {

            /* Load Existing Plan (if any) */
            mListTaskPlan = mSrvTaskManagerService.getTaskPlan(mTaskId);

            /* Load New if Existing Task Not Available */
            if (mListTaskPlan == null || mListTaskPlan.size() <= 0) {

                /* Get Task Level from Cached Master Data and Create new Plan */
                Set<String> setTaskPlanKey = SystemUI.mTaskPlanMasterData.keySet();
                Iterator<String> itrTaskPlanItr = setTaskPlanKey.iterator();
                int taskCounter = 0;
                while (itrTaskPlanItr.hasNext()) {
                    String next = itrTaskPlanItr.next();
                    ++taskCounter;
                    mListTaskPlan.add(new TaskPlan(new TaskPlanId(new BigDecimal(taskCounter), mTaskId), SystemUI.mTaskPlanMasterData.get(String.valueOf(taskCounter))));
                }
            }

            UtilService.self().doRenderData(tableTaskPlanning, mListTaskPlan, 4);
        }

    }

//    private void renderComments()
//    {
//        if (mTaskId != null) {
//            List<TaskComments> response = mSrvTaskManagerService.getTaskComments(mTaskId);
//            String txtCommentAsHTMLText = UtilService.self().getCommentsAsHTMLText(response);
//            jEditorPane1.setText(txtCommentAsHTMLText);
//        }
//    }
    /**
     *
     */
    public void loadData() {
        mLogger.log(Level.INFO, "loadData--Starts");
        TaskMaster tm = mSrvTaskManagerService.findTask(mTaskId, parentFrame.getCurrentTaskMasterData());
        txtTitle.setText(tm.getTitle());
        mLogger.log(Level.INFO, "loadData--Ends");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtRecordNo = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtTitle = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtReferenceNo = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        cmbProject = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        cmbAssignee = new javax.swing.JComboBox();
        dtDeliveryDate = new com.toedter.calendar.JDateChooser();
        dtReportDate = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        cmbPriority = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDescription = new javax.swing.JTextArea();
        jLabel16 = new javax.swing.JLabel();
        cmbStatus = new javax.swing.JComboBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtComments = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        cmbModule = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableTaskPlanning = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtCodeRevision = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cmbFramework = new javax.swing.JComboBox();
        cmbRetrofitRequired = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        cmbReleasePhase = new javax.swing.JComboBox();
        jLabel14 = new javax.swing.JLabel();
        txtReleaseNote = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                onWindowClosingEvent(evt);
            }
        });

        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel4.setForeground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        jLabel1.setText("Record No:");

        txtRecordNo.setEditable(false);
        txtRecordNo.setBackground(new java.awt.Color(204, 204, 255));
        txtRecordNo.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        txtRecordNo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        txtRecordNo.setFocusable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true), "Task Information", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 0, 12), new java.awt.Color(0, 0, 255))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel2.setText("Title:");

        txtTitle.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        jLabel10.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel10.setText("Reference No:");

        txtReferenceNo.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        jLabel11.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel11.setText("Project:");

        cmbProject.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        cmbProject.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel12.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel12.setText("Create Date:");

        jLabel13.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel13.setText("Delivery Date:");

        jLabel15.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel15.setText("Assignee:");

        cmbAssignee.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        cmbAssignee.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel3.setText("Priority:");

        cmbPriority.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        cmbPriority.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel4.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel4.setText("Description:");

        txtDescription.setColumns(20);
        txtDescription.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        txtDescription.setLineWrap(true);
        txtDescription.setRows(5);
        jScrollPane1.setViewportView(txtDescription);

        jLabel16.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel16.setText("Status:");

        cmbStatus.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        txtComments.setColumns(20);
        txtComments.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        txtComments.setLineWrap(true);
        txtComments.setRows(5);
        txtComments.setWrapStyleWord(true);
        jScrollPane2.setViewportView(txtComments);

        jLabel5.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel5.setText("Notes:");

        jLabel17.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel17.setText("Module:");

        cmbModule.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        cmbModule.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(cmbPriority, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel12)
                                .addComponent(jLabel13)
                                .addComponent(jLabel15)
                                .addComponent(jLabel17))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(dtDeliveryDate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                                .addComponent(dtReportDate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cmbAssignee, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cmbModule, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cmbProject, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbStatus, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtReferenceNo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
                    .addComponent(txtTitle)
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cmbAssignee, cmbPriority, cmbProject, cmbStatus, dtDeliveryDate, dtReportDate, txtReferenceNo});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel16)
                            .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(txtTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(3, 3, 3)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel10)
                            .addComponent(txtReferenceNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                    .addComponent(jLabel11)
                                    .addComponent(cmbProject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                    .addComponent(jLabel3)
                                    .addComponent(cmbPriority, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                    .addComponent(jLabel12)
                                    .addComponent(dtReportDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                    .addComponent(jLabel13)
                                    .addComponent(dtDeliveryDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                    .addComponent(jLabel15)
                                    .addComponent(cmbAssignee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(75, 75, 75)
                                .addComponent(jLabel5)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(cmbModule, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jScrollPane1, jScrollPane2});

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true), "Task Planning", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 0, 12), new java.awt.Color(0, 0, 255))); // NOI18N

        tableTaskPlanning.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Sr#", "Level", "Planned Date", "Analyst"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableTaskPlanning.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableTaskPlanningMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tableTaskPlanning);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jButton3.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jButton3.setText("Reset");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jButton2.setText("Close");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true), "Task Reference", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 0, 12), new java.awt.Color(0, 0, 255))); // NOI18N

        jLabel6.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel6.setText("Code Revision:");

        txtCodeRevision.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel7.setText("Retrofit Required:");

        jLabel8.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel8.setText("Framework:");

        cmbFramework.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        cmbFramework.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbRetrofitRequired.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        cmbRetrofitRequired.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel9.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel9.setText("Release Phase:");

        cmbReleasePhase.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        cmbReleasePhase.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel14.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel14.setText("Release Note:");

        txtReleaseNote.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCodeRevision)
                    .addComponent(cmbRetrofitRequired, 0, 136, Short.MAX_VALUE)
                    .addComponent(cmbFramework, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbReleasePhase, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtReleaseNote))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtCodeRevision, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cmbRetrofitRequired, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(cmbFramework, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(cmbReleasePhase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtReleaseNote, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton1.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jButton1.setText("Save");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtRecordNo, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel4Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton1, jButton2, jButton3});

        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtRecordNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton3)
                        .addComponent(jButton2))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addContainerGap())))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int response = saveTask();
        if (response == TaskManagerService.RESULT_SUCESSFUL) {
            parentFrame.updateTaskList();
            dispose();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void onWindowClosingEvent(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_onWindowClosingEvent
        parentFrame.updateTaskList();
    }//GEN-LAST:event_onWindowClosingEvent

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        resetData();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void tableTaskPlanningMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableTaskPlanningMouseClicked
        if (evt.getClickCount() == 2) {
            if (tableTaskPlanning.getSelectedColumn() == 2) {
                CalendarUI c = new CalendarUI(null, true);
                c.setVisible(true);
                Date selectedDate = c.getSelectedDate();
                if (selectedDate != null) {
                    String date = new SimpleDateFormat("dd.MM.yyyy").format(selectedDate);
                    tableTaskPlanning.setValueAt(date, tableTaskPlanning.getSelectedRow(), 2);
                }

            }
        }
    }//GEN-LAST:event_tableTaskPlanningMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbAssignee;
    private javax.swing.JComboBox cmbFramework;
    private javax.swing.JComboBox cmbModule;
    private javax.swing.JComboBox cmbPriority;
    private javax.swing.JComboBox cmbProject;
    private javax.swing.JComboBox cmbReleasePhase;
    private javax.swing.JComboBox cmbRetrofitRequired;
    private javax.swing.JComboBox cmbStatus;
    private com.toedter.calendar.JDateChooser dtDeliveryDate;
    private com.toedter.calendar.JDateChooser dtReportDate;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tableTaskPlanning;
    private javax.swing.JTextField txtCodeRevision;
    private javax.swing.JTextArea txtComments;
    private javax.swing.JTextArea txtDescription;
    private javax.swing.JTextField txtRecordNo;
    private javax.swing.JTextField txtReferenceNo;
    private javax.swing.JTextField txtReleaseNote;
    private javax.swing.JTextField txtTitle;
    // End of variables declaration//GEN-END:variables

    private int saveTask() {

        int response = TaskManagerService.RESULT_UNSUCESSFUL;

        if (mTaskMaster == null) {
            /* For New Record */
            mTaskMaster = new TaskMaster();
        }

        mTaskMaster.setTaskId(new BigDecimal(txtRecordNo.getText()));
        mTaskMaster.setTaskReference(txtReferenceNo.getText());
        mTaskMaster.setTitle(txtTitle.getText());
        mTaskMaster.setDescription(txtDescription.getText());
        mTaskMaster.setCommentText(txtComments.getText());
        mTaskMaster.setCreateDate(dtReportDate.getDate());
        mTaskMaster.setEta(dtDeliveryDate.getDate());
        mTaskMaster.setAssignee(((CellDTO) cmbAssignee.getSelectedItem()).getKey());
        mTaskMaster.setPriority(((CellDTO) cmbPriority.getSelectedItem()).getKey());
        mTaskMaster.setProjectName(((CellDTO) cmbProject.getSelectedItem()).getKey());
        mTaskMaster.setStatus(((CellDTO) cmbStatus.getSelectedItem()).getKey());
        mTaskMaster.setModuleName(((CellDTO) cmbModule.getSelectedItem()).getKey());
        mTaskMaster.setCodeRevision(txtCodeRevision.getText());
        mTaskMaster.setRetrofitRequired(((CellDTO) cmbRetrofitRequired.getSelectedItem()).getKey());
        mTaskMaster.setFrameworkVersion(((CellDTO) cmbFramework.getSelectedItem()).getKey());
        mTaskMaster.setReleasePhase(((CellDTO) cmbReleasePhase.getSelectedItem()).getKey());
        mTaskMaster.setReleaseNotes(txtReleaseNote.getText());
        mTaskMaster.setAuditLastupdateby(UtilService.self().getSystemUser());
        mTaskMaster.setAuditLastupdateon(new Date());

        response = mSrvTaskManagerService.saveTask(mTaskMaster);

        if (response == TaskManagerService.RESULT_SUCESSFUL) {

            setTaskPlanValues();

            response = mSrvTaskManagerService.saveTaskPlan(mListTaskPlan);
        }

        if (response == TaskManagerService.RESULT_SUCESSFUL) {
            JOptionPane.showMessageDialog(null, "Saved", "Message", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Unable to Save", "Message", JOptionPane.ERROR_MESSAGE);
        }

        return response;
    }

    private void resetData() {
        txtReferenceNo.setText("");
        txtTitle.setText("");
        txtDescription.setText("");
        txtComments.setText("");
        dtReportDate.setDate(null);
        dtDeliveryDate.setDate(null);
        cmbAssignee.setSelectedIndex(0);
        cmbPriority.setSelectedIndex(0);
        cmbProject.setSelectedIndex(0);
        cmbStatus.setSelectedIndex(0);
    }

    private void setTaskPlanValues() {
        if (mListTaskPlan != null && mListTaskPlan.size() > 0) {
            for (int i = 0; i < mListTaskPlan.size(); i++) {
                Date lPlanDate = null;
                String lAnalyst = "";
                Object cellValue;

                cellValue = tableTaskPlanning.getValueAt(i, 2);
                if (cellValue != null) {

                    try {
                        lPlanDate = new SimpleDateFormat("dd.MM.yyyy").parse((String) cellValue);
                    } catch (ParseException ex) {
                        mLogger.log(Level.FATAL, "Date Parsing Error");
                    }
                }

                cellValue = tableTaskPlanning.getValueAt(i, 3);
                if (cellValue != null) {
                    lAnalyst = (String) cellValue;
                }

                if (lPlanDate != null) {
                    mListTaskPlan.get(i).setPlannedDate(lPlanDate);
                }

                if (lAnalyst.trim().length() > 0) {
                    mListTaskPlan.get(i).setAnalyst(lAnalyst);
                }

            }
        }
    }

    private void setFocusTraversalPolicy() {
        Vector myComponents = new Vector();
        myComponents.add(cmbStatus);
        myComponents.add(txtReferenceNo);
        myComponents.add(cmbProject);
        myComponents.add(cmbPriority);
        myComponents.add(dtReportDate);
        myComponents.add(dtDeliveryDate);
        myComponents.add(cmbAssignee);
        myComponents.add(cmbModule);
        myComponents.add(txtCodeRevision);
        myComponents.add(cmbRetrofitRequired);
        myComponents.add(cmbFramework);
        myComponents.add(cmbReleasePhase);
        myComponents.add(txtReleaseNote);
        myComponents.add(jButton1);        
        FocusTraversalPolicy myFTP = new MyOwnFocusTraversalPolicy(myComponents);
        setFocusTraversalPolicy(myFTP);
    }

}
