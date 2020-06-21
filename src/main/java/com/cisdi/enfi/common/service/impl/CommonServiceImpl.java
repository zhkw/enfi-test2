package com.cisdi.enfi.common.service.impl;

import com.cisdi.enfi.common.bean.ResultInfo;
import com.cisdi.enfi.common.data.ClientData;
import com.cisdi.enfi.common.datamng.BaseDataMng;
import com.cisdi.enfi.common.datamng.DAService;
import com.cisdi.enfi.common.service.CommonService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommonServiceImpl extends BaseServiceImpl implements CommonService {

    @Resource
    DAService daService;
    @Resource
    BaseDataMng baseDataMng;

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ClientData getDropDownItemDisplayData(
            String dropDownName, String subCondition, String orderby)
            throws Exception {
        List<Object> params = new ArrayList<Object>();
        params.add(dropDownName);

        ResultInfo info = daService.queryBySql("CTP", "select * from CEIS_CONFIGSELEITEM where NAME = ? ", params);
        Map<String,Object> map = ((List<Map<String,Object>>)info.getObject()).get(0);
        String displayField = (String) map.get("displayfield");
        String resId = (String) map.get("resid");
        String condition = (String) map.get("condition");

        params.clear();
        params.add(resId);
        info = daService.queryBySql("CTP",
                "select * from CEIS_METACLASS where ID = ?", params);
        map = ((List<Map<String,Object>>)info.getObject()).get(0);
        String tableName = (String) map.get("tablename");

        StringBuffer sql = new StringBuffer();
        sql.append("select id," + displayField + " from " + tableName + " where 1=1 ");
        if (!(condition==null || condition.isEmpty())) {
            sql.append(" and " + condition);
        }

        if (subCondition != null && !subCondition.trim().isEmpty()) {
            String[] tokens = subCondition.split(",");
            for (String token : tokens) {
                sql.append(" and " + token);
            }
        }

        if (orderby != null && !orderby.isEmpty()) {
            sql.append(" order by " + orderby + " asc");
        }

        ClientData data = new ClientData();
        List<Map<String, Object>> dropDownList = this.getObjListBySql(sql.toString(), null);
        data.setObjList(dropDownList);
        HashMap<String, Object> other = new HashMap<String, Object>();
        other.put("displayField", displayField);
        data.setOtherValueMap(other);
        return data;
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public Number getCurrentMaxNumber(String tableName, String field,
                                      String condition) throws Exception {

        int maxNumber = 0;
        String sql = "";
        if (tableName == null || "".equals(tableName) || field == null
                || "".equals(field)) {
            return maxNumber;
        } else {
            sql = "select max(" + field + ") maxNumber from " + tableName
                    + " where 1=1";
            if (condition != null) {
                sql += " and " + condition;
            }
        }
        List<Object> objs = new ArrayList<Object>();
        // 这里不需要判断是否为空，执行sql以后必定有一条数据的，即使数据库中没有，那也是返回第0个元素是null，不会直接返回一个null
        Object maxNumberObj = this.getObjListBySql(sql, objs).get(0)
                .get("MAXNUMBER");
        String maxNumberString = "";
        if (maxNumberObj != null) {
            maxNumberString = maxNumberObj.toString();
        } else {
            maxNumberString = "0";
        }
        final String maxNumberStr = maxNumberString;
        Number number = new Number() {
            @Override
            public long longValue() {
                return Long.parseLong(maxNumberStr);
            }

            @Override
            public int intValue() {
                return Integer.parseInt(maxNumberStr);
            }

            @Override
            public float floatValue() {
                return Float.parseFloat(maxNumberStr);
            }

            @Override
            public double doubleValue() {
                return Double.parseDouble(maxNumberStr);
            }
        };
        return number;
    }
}

