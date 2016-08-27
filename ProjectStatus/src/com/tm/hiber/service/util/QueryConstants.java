/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tm.hiber.service.util;

/**
 *
 * @author Rohit.Dhalwani (dhalwanirohit@gmail.com)
 */
public class QueryConstants {
    
    /* Task Master Queries */
    
    public static final String GET_ALL_TASK = "from TaskMaster t order by t.taskId";
    public static final String DELETE_TASK = "delete TaskMaster t where t.taskId = :taskId";

    /* Master Data Queries */
    
    public static final String GET_OPTIONS_QUERY = "from TaskOptions";
    public static final String GET_NEXTSEQ_OF_MASTEROPTIONS = "select TASKSEQ.nextval as num from dual";
    
    /* Task Comments Queries */
    
    public static final String GET_COMMENTS_BY_TASKID = "from TaskComments t where t.refTaskId = :refTaskId order by t.commentDate,t.recordId";
    public static final String GET_NEXTSEQ_OF_TASKCOMMENTS = "select COMMENTSEQ.nextval as num from dual";
    public static final String DELETE_COMMENTS_BY_COMMENT_ID = "delete TaskComments t where t.recordId = :recordId";
}
