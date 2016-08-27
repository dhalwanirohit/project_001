/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tm.main;

import com.tm.hiber.service.util.UtilService;
import com.tm.model.CellDTO;
import com.tm.system.splash.SplashUI;
import com.tm.hiber.model.TaskMaster;
import com.tm.hiber.model.TaskOptions;
import com.tm.hiber.service.TMMasterDataService;
import com.tm.hiber.service.TMMasterDataServiceImpl;
import com.tm.hiber.service.TaskManagerService;
import com.tm.hiber.service.TaskManagerServiceImpl;
import com.tm.hiber.service.util.DatabaseUtil;
import com.tm.model.DateComparator;
import com.tm.model.NumberComparator;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author user
 */
public class SystemUI extends javax.swing.JFrame {

    private final static Logger mLogger = Logger.getLogger(SystemUI.class.getName());

    /**
     * Creates new form SystemUI
     */
    /* Cached Data */
    public static List<TaskMaster> mTaskMasterData = new ArrayList<TaskMaster>();
    public static Map<String, String> mStatusMasterData = new HashMap<String, String>();
    public static Map<String, String> mUserMasterData = new HashMap<String, String>();
    public static Map<String, String> mProjectMasterData = new HashMap<String, String>();
    public static Map<String, String> mPriorityMasterData = new HashMap<String, String>();
    public static Map<String, String> mTaskTypeMasterData = new HashMap<String, String>();
    public static Map<String, String> mRetrofitMasterData = new HashMap<String, String>();
    public static Map<String, String> mFrameworkMasterData = new HashMap<String, String>();
    public static Map<String, String> mReleasePhaseMasterData = new HashMap<String, String>();
    public static Map<String, String> mModuleMasterData = new HashMap<String, String>();
    public static Map<String, String> mTaskPlanMasterData = new HashMap<String, String>();


    /* Search Criteria */
    public List<Criterion> mSearchCriteria = new ArrayList<Criterion>();

    /* Services */
    final static TaskManagerService mSrvTaskManager = new TaskManagerServiceImpl();
    final static TMMasterDataService mSrvMasterDataService = new TMMasterDataServiceImpl();

    private CommentUI mCommentUI = null;
    
    private TableRowSorter objRowSorter = null;

    public SystemUI() {
        mLogger.info("SystemUI--Starts");
        initComponents();
        setExtendedState(MAXIMIZED_BOTH);

        setIconImage(UtilService.self().getIcon("/com/tm/system/icons32/Windows_32x32.png").getImage());

        /**
         * Setting Width of Column(s)
         */
        UtilService.self().setColumnSize(0, 40, mTableTasks);
        UtilService.self().setColumnSize(1, 100, mTableTasks);
        UtilService.self().setColumnSize(2, 300, mTableTasks);

        for (int i = 3; i < 9; i++) {
            UtilService.self().setColumnSize(i, 95, mTableTasks);
        }
        

        mTableTasks.setRowHeight(30);
        JTableHeader jh = mTableTasks.getTableHeader();
        TableCellRenderer tc = jh.getDefaultRenderer();
        DefaultTableCellRenderer dtc = (DefaultTableCellRenderer) tc;
        dtc.setHorizontalAlignment(SwingConstants.CENTER);
        jh.setFont(new Font("Calibri", Font.BOLD, 12));

        mTableTasks.setFont(new Font("Calibri", Font.PLAIN, 12));
        mTableTasks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DefaultTableCellRenderer cellRenderer = (DefaultTableCellRenderer) mTableTasks.getCellRenderer(0, 0);
        cellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        cellRenderer.setVerticalAlignment(SwingConstants.TOP);

        DefaultTableCellRenderer cellRenderer1 = (DefaultTableCellRenderer) mTableTasks.getCellRenderer(0, 1);
        cellRenderer1.setHorizontalAlignment(SwingConstants.CENTER);
        cellRenderer1.setVerticalAlignment(SwingConstants.TOP);
        
        /* Set Table Sorter */
        
        objRowSorter = new TableRowSorter(mTableTasks.getModel());       
        
        objRowSorter.setComparator(0, new NumberComparator());
        objRowSorter.setComparator(5, new DateComparator());
        objRowSorter.setComparator(7, new DateComparator());
        
        
        mTableTasks.setRowSorter(objRowSorter);
        


        /* Load Master Data */
        loadMasterData();

        this.getRootPane().setDefaultButton(btnListData);
        setTitle("Task Manager Ver 1.6.0");

        mLogger.info("SystemUI--End");
    }  

    public void addSearchCriteria() {
        mLogger.info("addSearchCriteria--Starts");
        mSearchCriteria.clear();

        this.getRootPane().setDefaultButton(btnListData);

        if (sptxtRecordId.getText() != null && sptxtRecordId.getText().length() > 0) {
            mSearchCriteria.add(Restrictions.eq("taskId", new BigDecimal(sptxtRecordId.getText())));
        }

        if (sptxtReferenceNo.getText() != null && sptxtReferenceNo.getText().length() > 0) {
            mSearchCriteria.add(Restrictions.eq("taskReference", sptxtReferenceNo.getText()));
        }

        if (spcmbAssignee.getSelectedIndex() > 0) {
            mSearchCriteria.add(Restrictions.eq("assignee", ((CellDTO) spcmbAssignee.getSelectedItem()).getKey()));
        }

        if (spcmbStatus.getSelectedIndex() > 0) {
            mSearchCriteria.add(Restrictions.eq("status", ((CellDTO) spcmbStatus.getSelectedItem()).getKey()));
        }

        if (spcmbProject.getSelectedIndex() > 0) {
            mSearchCriteria.add(Restrictions.eq("projectName", ((CellDTO) spcmbProject.getSelectedItem()).getKey()));
        }

        if (spcmbPriority.getSelectedIndex() > 0) {
            mSearchCriteria.add(Restrictions.eq("priority", ((CellDTO) spcmbPriority.getSelectedItem()).getKey()));
        }

        /* Date Criteria */
        if (spdtAssignDateFrom.getDate() != null && spdtAssignDateTo.getDate() == null) {
            mSearchCriteria.add(Restrictions.eq("createDate", spdtAssignDateFrom.getDate()));
        }

        if (spdtAssignDateFrom.getDate() != null && spdtAssignDateTo.getDate() != null) {
            mSearchCriteria.add(Restrictions.between("createDate", spdtAssignDateFrom.getDate(), spdtAssignDateTo.getDate()));
        }

        if (spdtDeliveryDateFrom.getDate() != null && spdtDeliveryDateTo.getDate() == null) {
            mSearchCriteria.add(Restrictions.eq("eta", spdtDeliveryDateFrom.getDate()));
        }

        if (spdtDeliveryDateFrom.getDate() != null && spdtDeliveryDateTo.getDate() != null) {
            mSearchCriteria.add(Restrictions.between("eta", spdtDeliveryDateFrom.getDate(), spdtDeliveryDateTo.getDate()));
        }

        /* Date Criteria Ends */
        if (sptxtTitle.getText() != null && sptxtTitle.getText().length() > 0) {
            mSearchCriteria.add(Restrictions.ilike("title", sptxtTitle.getText(), MatchMode.ANYWHERE));
        }

        if (sptxtComment.getText() != null && sptxtComment.getText().length() > 0) {
            mSearchCriteria.add(Restrictions.ilike("commentText", sptxtComment.getText(), MatchMode.ANYWHERE));
        }

        if (sptxtModuleName.getText() != null && sptxtModuleName.getText().length() > 0) {
            mSearchCriteria.add(Restrictions.ilike("moduleName", sptxtModuleName.getText(), MatchMode.ANYWHERE));
        }

        if (spcmbTaskType.getSelectedIndex() > 0) {
            mSearchCriteria.add(Restrictions.eq("taskType", ((CellDTO) spcmbTaskType.getSelectedItem()).getKey()));
        }
        
        if(rdoPlanningStatus.isSelected())
        {
            mSearchCriteria.add(Restrictions.isNull("eta"));        
            mSearchCriteria.add(Restrictions.isNull("assignee"));
        }

        mLogger.info("addSearchCriteria--Ends");
    }

    public List<TaskMaster> getCurrentTaskMasterData() {
        mLogger.info("getCurrentTaskMasterData--Starts");

        /* Update Task List */
        if (mSearchCriteria.size() > 0) {
            mTaskMasterData = mSrvTaskManager.getAllTask(mSearchCriteria);
        } else {
            mTaskMasterData = mSrvTaskManager.getAllTask();
        }

        /* Return */
        mLogger.info("getCurrentTaskMasterData--Starts");
        return mTaskMasterData;
    }

    public void updateTaskList() {
        mLogger.info("updateTaskList--Starts");

        /* Render records on Table */
        int recordCount = UtilService.self().doRenderData(mTableTasks, getCurrentTaskMasterData(), TaskManagerService.CLASS_TASK_MASTER);

        /* Update Record Count on UI */
        lblRecordCounterValue.setText("(" + recordCount + ")");

        mLogger.info("updateTaskList--Ends");
    }

    private void loadMasterData() {
        mLogger.info("loadMasterData--Starts");

        List<TaskOptions> objAllOptions = mSrvMasterDataService.getAllOptions();

        if (objAllOptions != null) {

            for (TaskOptions objTaskOptions : objAllOptions) {

                if (objTaskOptions.getTag().equalsIgnoreCase("User")) {
                    mUserMasterData.put(objTaskOptions.getKeyText(), objTaskOptions.getValueText());
                }

                if (objTaskOptions.getTag().equalsIgnoreCase("Status")) {
                    mStatusMasterData.put(objTaskOptions.getKeyText(), objTaskOptions.getValueText());
                }

                if (objTaskOptions.getTag().equalsIgnoreCase("Project")) {
                    mProjectMasterData.put(objTaskOptions.getKeyText(), objTaskOptions.getValueText());
                }

                if (objTaskOptions.getTag().equalsIgnoreCase("Priority")) {
                    mPriorityMasterData.put(objTaskOptions.getKeyText(), objTaskOptions.getValueText());
                }

                if (objTaskOptions.getTag().equalsIgnoreCase("TaskType")) {
                    mTaskTypeMasterData.put(objTaskOptions.getKeyText(), objTaskOptions.getValueText());
                }

                if (objTaskOptions.getTag().equalsIgnoreCase("Retrofit")) {
                    mRetrofitMasterData.put(objTaskOptions.getKeyText(), objTaskOptions.getValueText());
                }

                if (objTaskOptions.getTag().equalsIgnoreCase("Framework")) {
                    mFrameworkMasterData.put(objTaskOptions.getKeyText(), objTaskOptions.getValueText());
                }

                if (objTaskOptions.getTag().equalsIgnoreCase("Release_Phase")) {
                    mReleasePhaseMasterData.put(objTaskOptions.getKeyText(), objTaskOptions.getValueText());
                }

                if (objTaskOptions.getTag().equalsIgnoreCase("Module")) {
                    mModuleMasterData.put(objTaskOptions.getKeyText(), objTaskOptions.getValueText());
                }

                if (objTaskOptions.getTag().equalsIgnoreCase("TaskPlan")) {
                    mTaskPlanMasterData.put(objTaskOptions.getKeyText(), objTaskOptions.getValueText());
                }

            }

            /* Populate Dropdown Box from Cached Data */
            UtilService.self().populateDropDown(mUserMasterData, spcmbAssignee);
            UtilService.self().populateDropDown(mStatusMasterData, spcmbStatus);
            UtilService.self().populateDropDown(mProjectMasterData, spcmbProject);
            UtilService.self().populateDropDown(mPriorityMasterData, spcmbPriority);
            UtilService.self().populateDropDown(mTaskTypeMasterData, spcmbTaskType);

        }
        mLogger.info("loadMasterData--Ends");
    }

    private void clearCriteria() {
        mLogger.info("clearCriteria--Starts");
        spcmbAssignee.setSelectedIndex(0);
        spcmbPriority.setSelectedIndex(0);
        spcmbProject.setSelectedIndex(0);
        spcmbStatus.setSelectedIndex(0);
        spdtAssignDateFrom.setDate(null);
        spdtAssignDateTo.setDate(null);
        spdtDeliveryDateFrom.setDate(null);
        spdtDeliveryDateTo.setDate(null);
        sptxtRecordId.setText("");
        sptxtReferenceNo.setText("");
        sptxtTitle.setText("");
        sptxtModuleName.setText("");
        spcmbTaskType.setSelectedIndex(0);
        sptxtComment.setText("");
        mLogger.info("clearCriteria--Ends");
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
        jLabel22 = new javax.swing.JLabel();
        jTextField16 = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        jProgressBar3 = new javax.swing.JProgressBar();
        jLabel24 = new javax.swing.JLabel();
        jTextField18 = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jComboBox8 = new javax.swing.JComboBox();
        jLabel26 = new javax.swing.JLabel();
        jTextField19 = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jTextField20 = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jComboBox9 = new javax.swing.JComboBox();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea4 = new javax.swing.JTextArea();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        jTextField21 = new javax.swing.JTextField();
        jCheckBox4 = new javax.swing.JCheckBox();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        sptxtRecordId = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        spcmbStatus = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        spcmbAssignee = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        spdtAssignDateFrom = new com.toedter.calendar.JDateChooser();
        spdtDeliveryDateFrom = new com.toedter.calendar.JDateChooser();
        jLabel10 = new javax.swing.JLabel();
        spcmbProject = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        spcmbPriority = new javax.swing.JComboBox();
        sptxtReferenceNo = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        sptxtModuleName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        sptxtTitle = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        sptxtComment = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        spcmbTaskType = new javax.swing.JComboBox();
        spdtAssignDateTo = new com.toedter.calendar.JDateChooser();
        spdtDeliveryDateTo = new com.toedter.calendar.JDateChooser();
        jPanel7 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jLabel35 = new javax.swing.JLabel();
        lblRecordCounterValue = new javax.swing.JLabel();
        btnListData = new javax.swing.JButton();
        btnExportData = new javax.swing.JButton();
        rdoPlanningStatus = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        mTableTasks = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        jButton13 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel22.setText("Record No:");

        jLabel23.setText("Bug Description:");

        jProgressBar3.setToolTipText("");
        jProgressBar3.setValue(40);
        jProgressBar3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel24.setText("Reference No:");

        jLabel25.setText("Project:");

        jComboBox8.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel26.setText("Report Date:");

        jLabel27.setText("Delivery Date:");

        jLabel28.setText("Assigned To:");

        jComboBox9.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jTextArea4.setColumns(20);
        jTextArea4.setRows(5);
        jScrollPane4.setViewportView(jTextArea4);

        jButton8.setText("Reset");

        jButton9.setText("Save");

        jLabel29.setText("Unknown:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel22)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel28)
                                    .addComponent(jLabel24)
                                    .addComponent(jLabel25))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField18, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                                    .addComponent(jComboBox9, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jComboBox8, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel23)
                                .addComponent(jLabel26))
                            .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addComponent(jLabel27))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField17)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField19)
                            .addComponent(jTextField20)
                            .addComponent(jTextField21, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jProgressBar3, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel22)
                        .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel23))
                    .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel24)
                            .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel26))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel28)
                            .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel27))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25)
                            .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel29)
                            .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jButton8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jProgressBar3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Filters", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 0, 12))); // NOI18N

        jLabel5.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel5.setText("ID:");

        sptxtRecordId.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel6.setText("Status:");

        spcmbStatus.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        spcmbStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select", "Open", "Close" }));

        jLabel7.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel7.setText("User:");

        spcmbAssignee.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        spcmbAssignee.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel8.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel8.setText("Create Date:");

        jLabel9.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel9.setText("Delivery Date:");

        jLabel10.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel10.setText("Project:");

        spcmbProject.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        spcmbProject.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel11.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel11.setText("Priority:");

        spcmbPriority.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        spcmbPriority.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        sptxtReferenceNo.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        jLabel12.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel12.setText("Reference No:");

        jLabel13.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel13.setText("Comment:");

        sptxtModuleName.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel2.setText("Title:");

        sptxtTitle.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel3.setText("Module Name:");

        sptxtComment.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        jLabel14.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel14.setText("Task Type:");

        spcmbTaskType.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        spcmbTaskType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select", "Open", "Close" }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(spcmbAssignee, 0, 120, Short.MAX_VALUE)
                    .addComponent(sptxtRecordId)
                    .addComponent(spcmbPriority, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sptxtReferenceNo, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10)
                    .addComponent(jLabel6)
                    .addComponent(jLabel9)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(spdtDeliveryDateFrom, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                            .addComponent(spdtAssignDateFrom, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(spdtDeliveryDateTo, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                            .addComponent(spdtAssignDateTo, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(spcmbProject, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(spcmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(sptxtComment, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(sptxtTitle, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(spcmbTaskType, 0, 158, Short.MAX_VALUE)
                    .addComponent(sptxtModuleName))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {spdtAssignDateFrom, spdtAssignDateTo, spdtDeliveryDateFrom, spdtDeliveryDateTo});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(sptxtModuleName, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(sptxtTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(sptxtComment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(spcmbTaskType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(spdtAssignDateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(spdtAssignDateTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel9)
                            .addComponent(spdtDeliveryDateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(spdtDeliveryDateTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(spcmbProject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(spcmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(sptxtRecordId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel5))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                    .addComponent(sptxtReferenceNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel7)
                            .addComponent(spcmbAssignee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(spcmbPriority, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        jButton4.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jButton4.setMnemonic('L');
        jButton4.setText("Clear Criteria");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel35.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel35.setText("Record Count:");

        lblRecordCounterValue.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        lblRecordCounterValue.setText("(0)");

        btnListData.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        btnListData.setMnemonic('W');
        btnListData.setText("List Down");
        btnListData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnListDataActionPerformed(evt);
            }
        });

        btnExportData.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        btnExportData.setMnemonic('W');
        btnExportData.setText("Export Data");
        btnExportData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportDataActionPerformed(evt);
            }
        });

        rdoPlanningStatus.setText("Unplanned/Unassiged");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel35)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblRecordCounterValue)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(rdoPlanningStatus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnExportData)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnListData)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addContainerGap())
        );

        jPanel7Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnExportData, btnListData, jButton4});

        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblRecordCounterValue)
                            .addComponent(jLabel35)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton4)
                            .addComponent(btnListData)
                            .addComponent(btnExportData)
                            .addComponent(rdoPlanningStatus))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        mTableTasks.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Task Reference", "Task", "Priority", "Status", "Created On", "Project", "Delivery Date", "Assignee", "Notes"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        mTableTasks.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mTableTasksMouseClicked(evt);
            }
        });
        mTableTasks.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                mTableTasksKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(mTableTasks);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/tm/system/icons16/Document_16x16.png"))); // NOI18N
        jButton13.setToolTipText("New");
        jButton13.setFocusable(false);
        jButton13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton13.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton13);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/tm/system/icons16/Edit_16x16.png"))); // NOI18N
        jButton1.setToolTipText("Edit/Modify");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/tm/system/icons16/Database_16x16.png"))); // NOI18N
        jButton14.setToolTipText("Import/Export");
        jButton14.setFocusable(false);
        jButton14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton14.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton14);

        jButton16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/tm/system/icons16/Line_Graph_16x16.png"))); // NOI18N
        jButton16.setToolTipText("Graph");
        jButton16.setFocusable(false);
        jButton16.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton16.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton16);

        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/tm/system/icons16/Windows_16x16.png"))); // NOI18N
        jButton12.setToolTipText("Comment Window");
        jButton12.setFocusable(false);
        jButton12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton12.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton12);

        jMenu1.setText("System");

        jMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/tm/system/icons16/Database_16x16.png"))); // NOI18N
        jMenu2.setText("Data");

        jMenuItem2.setText("Import");
        jMenu2.add(jMenuItem2);

        jMenuItem3.setText("Export");
        jMenu2.add(jMenuItem3);

        jMenu1.add(jMenu2);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/tm/system/icons16/Close_16x16.png"))); // NOI18N
        jMenuItem1.setText("Exit");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 852, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mTableTasksKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mTableTasksKeyPressed

        String strTaskId = (String) mTableTasks.getValueAt(mTableTasks.getSelectedRow(), 0);

        if (strTaskId != null && strTaskId.length() > 0) {
            BigDecimal taskId = new BigDecimal(strTaskId);
            if (evt.getKeyCode() == KeyEvent.VK_F2) {
                TaskUI objRecord = new TaskUI(this);
                objRecord.setTaskId(taskId);
                objRecord.setVisible(true);
                objRecord.requestFocus();
            } else if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
                int userAnswer = JOptionPane.showConfirmDialog(null, "Do you want to delete selected record?", "Delete Record", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (userAnswer == JOptionPane.YES_OPTION) {
                    int response = mSrvTaskManager.deleteTask(taskId);
                    if (response == TaskManagerService.RESULT_SUCESSFUL) {
                        JOptionPane.showMessageDialog(null, "Deleted");
                        DefaultTableModel df = (DefaultTableModel) mTableTasks.getModel();
                        df.removeRow(mTableTasks.convertRowIndexToModel(mTableTasks.getSelectedRow()));
                        updateTaskList();

                    } else {
                        JOptionPane.showMessageDialog(null, "Unable to Delete");
                    }
                }

            }
        }
    }//GEN-LAST:event_mTableTasksKeyPressed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        if (mCommentUI == null) {
            mCommentUI = new CommentUI(this);
        }
        setVisible(false);
        mCommentUI.setVisible(true);
    }//GEN-LAST:event_jButton12ActionPerformed

    private void mTableTasksMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mTableTasksMouseClicked
        if (evt != null && evt.getClickCount() == 2 && mTableTasks.getSelectedRow() > -1) {
            String strTaskId = (String) mTableTasks.getValueAt(mTableTasks.getSelectedRow(), 0);
            if (strTaskId != null && strTaskId.length() > 0) {
                BigDecimal taskId = new BigDecimal(strTaskId);
                TaskUI objRecord = new TaskUI(this);
                objRecord.setTaskId(taskId);
                objRecord.setVisible(true);
                objRecord.requestFocus();
            }
        }
    }//GEN-LAST:event_mTableTasksMouseClicked

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        TaskUI r = new TaskUI(this);
        r.setVisible(true);
        r.requestFocus();
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                    new DBOperationUI().setVisible(true);
                } catch (ClassNotFoundException e) {
                    mLogger.log(Level.FATAL, e.getMessage());
                } catch (InstantiationException e) {
                    mLogger.log(Level.FATAL, e.getMessage());
                } catch (IllegalAccessException e) {
                    mLogger.log(Level.FATAL, e.getMessage());
                } catch (UnsupportedLookAndFeelException e) {
                    mLogger.log(Level.FATAL, e.getMessage());
                }
            }
        });
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (mTableTasks != null && mTableTasks.getSelectedRow() > -1) {
            String strTaskId = (String) mTableTasks.getValueAt(mTableTasks.getSelectedRow(), 0);
            if (strTaskId != null && strTaskId.length() > 0) {
                BigDecimal taskId = new BigDecimal(strTaskId);
                TaskUI objRecord = new TaskUI(this);
                objRecord.setTaskId(taskId);
                objRecord.setVisible(true);
                objRecord.requestFocus();
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        int userAnswer = JOptionPane.showConfirmDialog(null, "Exit from Task Manager?", "System Exit", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (userAnswer == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void btnListDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnListDataActionPerformed
        addSearchCriteria();       
        updateTaskList();
    }//GEN-LAST:event_btnListDataActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        clearCriteria();
        mSearchCriteria.clear();
        updateTaskList();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        GraphUI objGraphUI = new GraphUI();
        objGraphUI.setVisible(true);
    }//GEN-LAST:event_jButton16ActionPerformed

    private void btnExportDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportDataActionPerformed
        try {

            if (mTaskMasterData.size() <= 0) {
                JOptionPane.showMessageDialog(null, "No Data to Export", "Export Data", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            File selectedFile = null;

            JFileChooser objFileChooser = new JFileChooser();
            objFileChooser.setSelectedFile(new File(DatabaseUtil.self().getExportWorkbookName() + ".xls"));
            int userSelection = objFileChooser.showSaveDialog(null);

            if (userSelection == JFileChooser.CANCEL_OPTION) {
                return;
            }

            if (objFileChooser.getSelectedFile() != null) {
                selectedFile = objFileChooser.getSelectedFile();
                if (selectedFile.exists()) {
                    selectedFile.delete();
                }
            }

            if (selectedFile != null) {
                int exportedRecords = DatabaseUtil.self().exportData(selectedFile, mTableTasks);
                JOptionPane.showMessageDialog(null, exportedRecords + " Rows Exported", "Export Data", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Unable to Export Data");
            mLogger.fatal("Exception while exporting Data-->" + e.getMessage());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Unable to Export Data");
            mLogger.fatal("Exception while exporting Data-->" + e.getMessage());
        }
    }//GEN-LAST:event_btnExportDataActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SystemUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SystemUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SystemUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SystemUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        SplashUI objSplashUI = new SplashUI();

        try {
            java.awt.EventQueue.invokeAndWait(objSplashUI);
        } catch (InterruptedException ex) {
            mLogger.log(Level.FATAL, ex.getMessage());
        } catch (InvocationTargetException ex) {
            mLogger.log(Level.FATAL, ex.getMessage());
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                    new SystemUI().setVisible(true);
                } catch (ClassNotFoundException e) {
                    mLogger.log(Level.FATAL, e.getMessage());
                } catch (InstantiationException e) {
                    mLogger.log(Level.FATAL, e.getMessage());
                } catch (IllegalAccessException e) {
                    mLogger.log(Level.FATAL, e.getMessage());
                } catch (UnsupportedLookAndFeelException e) {
                    mLogger.log(Level.FATAL, e.getMessage());
                }
            }
        });

        objSplashUI.dispose();

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExportData;
    private javax.swing.JButton btnListData;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JComboBox jComboBox8;
    private javax.swing.JComboBox jComboBox9;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JProgressBar jProgressBar3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTextArea jTextArea4;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField21;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblRecordCounterValue;
    public static javax.swing.JTable mTableTasks;
    private javax.swing.JCheckBox rdoPlanningStatus;
    private javax.swing.JComboBox spcmbAssignee;
    private javax.swing.JComboBox spcmbPriority;
    private javax.swing.JComboBox spcmbProject;
    private javax.swing.JComboBox spcmbStatus;
    private javax.swing.JComboBox spcmbTaskType;
    private com.toedter.calendar.JDateChooser spdtAssignDateFrom;
    private com.toedter.calendar.JDateChooser spdtAssignDateTo;
    private com.toedter.calendar.JDateChooser spdtDeliveryDateFrom;
    private com.toedter.calendar.JDateChooser spdtDeliveryDateTo;
    private javax.swing.JTextField sptxtComment;
    private javax.swing.JTextField sptxtModuleName;
    private javax.swing.JTextField sptxtRecordId;
    private javax.swing.JTextField sptxtReferenceNo;
    private javax.swing.JTextField sptxtTitle;
    // End of variables declaration//GEN-END:variables
}
