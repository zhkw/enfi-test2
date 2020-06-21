package com.cisdi.enfi.common.utils;

public class GeneralDictionary {

    // 各模块entity路径
    public static String[] PO_BASEPATH = { "com.cisdi.enfi.ms.entity.", "com.cisdi.enfi.pbs.entity."};

    /*
     * ******************************************************************
     * VO基本包路径
     */
    public static String MS_VO_BASEPATH = "com.cisdi.enfi.ms.vo.";
    public static String PBS_VO_BASEPATH = "com.cisdi.enfi.pbs.vo.";
    /*
     * ******************************************************************
     * 属性类型：主属性、组织属性
     */
    public static String ATTR_TYPE_MAINATTR = "M";
    public static String ATTR_TYPE_ORGATTR = "O";

    /**
     * ****************************************************************** 组织关键字
     */
    public static String OU_KEY = "OUID";
    /*
     * ******************************************************************
     * 属性组类型：公用属性组、自定义属性组
     */
    public static String ATTRGRP_TYPE_PUB = "P";
    public static String ATTRGRP_TYPE_CUS = "C";

    /**
     * 物料编码规则生成方式：顺序生成、函数生成
     */
    public static String SEQUENCE_GEN = "SX";
    public static String FUNCTION_GEN = "HS";
    /*
     * ******************************************************************
     * 模块代码
     */
    public static String MODELCODE_MM_MNG = "MM-MNG";
    public static String MODELCODE_MM_MC = "MM-MC";
    public static String MODELCODE_MM_MA = "MM-MA";
    public static String MODELCODE_MM_MU = "MM-MU";

    /*
     * *****************************************************************
     * 资源操作方式
     */

    public static String MODEL_OPR_QUERY="query";         //查询
    public static String MODEL_OPR_UPDATE="update";       //更新
    public static String MODEL_OPR_DELETE="delete";       //删除
    public static String MODEL_OPR_ADD="add";             //添加

    public static String TASK_FORM = "form";
}

