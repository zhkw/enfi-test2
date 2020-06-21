package com.cisdi.enfi.common.service.impl;

import com.cisdi.enfi.common.dao.TemplateFactory;
import com.cisdi.enfi.common.data.GeneralEntityUtils;
import com.cisdi.enfi.common.data.Root;
import com.cisdi.enfi.common.datamng.BaseDataMng;
import com.cisdi.enfi.common.datamng.BaseEntityMng;
import com.cisdi.enfi.common.datamng.DAService;
import com.cisdi.enfi.common.service.BaseService;
import com.cisdi.enfi.common.utils.DBRouterUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@Service
@Transactional
public class BaseServiceImpl implements BaseService {


    @Resource
    protected BaseDataMng baseDataMng;
    @Resource
    protected BaseEntityMng baseEntityMng;
    @Resource
    protected DAService daService;
    protected Connection conn;


    protected static HibernateTemplate hibernateTemplate;

    public HibernateTemplate getHibernateTemplate() {
        return hibernateTemplate;
    }

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    static {
        if (hibernateTemplate == null) {
            hibernateTemplate = TemplateFactory
                    .getHibernateTemplate(DBRouterUtils.getDBId());
        }
    }

    public void update(String className, Map<String, Object> data)
            throws Exception {
        this.update(className, data, true);
    }

    public void update(String className, Map<String, Object> data,
                       Boolean updateNull) throws Exception {
        // 通过Id得到要更新的原始数据
        String dbId = DBRouterUtils.getDBId();
        String id = data.get("id").toString();
        Root root = this.getObjById(className, id);
        Map<String, Object> oldMap = root.getHashAttributes();

        // 如果oldMap和data指向的是相同的引用，就不执行下面两个迭代过程
        if (oldMap != data) {

            if (updateNull == null || !updateNull) {
                // 去掉所有的value是null或空字符串的键值对
                Iterator<String> dataIt = data.keySet().iterator();
                while (dataIt.hasNext()) {
                    String key = dataIt.next();
                    if (data.get(key) == null || "".equals(data.get(key))) {
                        dataIt.remove();
                    }
                }
            }

            // 去掉oldMap中要被跟新的键值对
            Iterator<String> it = oldMap.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                if (data.containsKey(key)) {
                    it.remove();
                }
            }

        }
        // 构造新的data和root对象
        oldMap.putAll(data);
        //oldMap.put("updater", ((EnvironmentVariable)request.getSession().getAttribute("EV")).getUserName());
        root.setHashAttributes((HashMap<String, Object>) oldMap);

        clearSession();
        if (dbId == null) {
            baseDataMng.updateObj(root);
        } else {
            baseDataMng.updateObj(dbId, root);
        }
        flushSession();
    }

    public void updateBySql(String sql, List<Object> objs) throws Exception {
        String dbId = DBRouterUtils.getDBId();
        daService.updateBySql(dbId, sql, objs);
    }

    public String save(String className, Map<String, Object> data)
            throws Exception {
        String dbId = DBRouterUtils.getDBId();
        Root root = GeneralEntityUtils.getRoot(className);
        root.setHashAttributes((HashMap<String, Object>) data);
        root.setId(UUID.randomUUID().toString());

        //root.setAttribute("creater", ((EnvironmentVariable)request.getSession().getAttribute("EV")).getUserName());
        //root.setAttribute("updater", ((EnvironmentVariable)request.getSession().getAttribute("EV")).getUserName());

        if (dbId == null) {
            baseDataMng.saveObj(root);
        } else {
            baseDataMng.saveObj(dbId, root);
        }
        flushSession();
        return root.getId();
    }

    public String saveWithId(String className, Map<String, Object> data)
            throws Exception {
        String dbid = null;//DBRouterUtils.getDBId();
        Root root = GeneralEntityUtils.getRoot(className);
        root.setHashAttributes((HashMap<String, Object>) data);
        root.setId((String) data.get("id"));
        //root.setAttribute("creater", ((EnvironmentVariable)request.getSession().getAttribute("EV")).getUserName());
        //root.setAttribute("updater", ((EnvironmentVariable)request.getSession().getAttribute("EV")).getUserName());
        clearSession();
        Root obj;
        if (dbid == null) {
            obj = baseDataMng.getObj(root.getId(), className);
            if (obj == null) {
                baseDataMng.saveOrUpdateObj(dbid, root);
            } else {
                obj.setHashAttributes(root.getHashAttributes());
                baseDataMng.updateObj(obj);
            }
        } else {
            obj = baseDataMng.getObj(dbid, root.getId(), className);
            if (obj == null) {
                baseDataMng.saveOrUpdateObj(dbid, root);
            } else {
                obj.setHashAttributes(root.getHashAttributes());
                baseDataMng.updateObj(dbid, obj);
            }
        }


        String rootId = root.getId();
        return rootId;
    }

    public String saveBySql(String sql) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    public void delete(String className, String id) throws Exception {
        String dbId = DBRouterUtils.getDBId();
        if (dbId == null) {
            baseDataMng.deleteObj(id, className);
        } else {
            baseDataMng.deleteObj(dbId, id, className);
        }
    }

    public void deleteBySql(String sql, List<Object> objs) throws Exception {
        String dbid = DBRouterUtils.getDBId();
        daService.deleteBySql(dbid, sql, objs);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public Root getObjById(String className, String id) throws Exception {
        Root root = null;
        String dbid = DBRouterUtils.getDBId();
        if (dbid == null) {
            root = baseDataMng.getObj(id, className);
        } else {
            root = baseDataMng.getObj(dbid, id, className);
        }

        return root;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public Root getObjByRelId(String className, String firstField,
                              String firstId, String secondField, String secondId)
            throws Exception {
        List<Root> roots = null;
        roots = this.getObjListByCondition(className, "obj." + firstField
                + "='" + firstId + "' and obj." + secondField + "='" + secondId
                + "'");
        if (roots != null && roots.size() > 0) {
            return roots.get(0);
        }
        return null;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Root getObjByIdRequired(String className, String id)
            throws Exception {
        Root root = null;
        String dbid = DBRouterUtils.getDBId();
        if (dbid == null) {
            root = baseDataMng.getObj(id, className);
        } else {
            root = baseDataMng.getObj(dbid, id, className);
        }

        return root;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<Root> getObjListByCondition(String className, String condition)
            throws Exception {
        List<Root> list = new ArrayList<Root>();
        String dbid = DBRouterUtils.getDBId();
        if (dbid == null) {
            list = baseDataMng.getObjListByCondition(className, condition);
        } else {
            list = baseDataMng
                    .getObjListByCondition(dbid, className, condition);
        }
        return list;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<Root> getObjListByConditionRequired(String className,
                                                    String condition) throws Exception {
        List<Root> list = new ArrayList<Root>();
        String dbid = DBRouterUtils.getDBId();
        if (dbid == null) {
            list = baseDataMng.getObjListByCondition(className, condition);
        } else {
            list = baseDataMng
                    .getObjListByCondition(dbid, className, condition);
        }
        return list;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<Root> getObjListByCondition(String className, String condition,
                                            int firstRow, int pageSize) throws Exception {
        List<Root> list = new ArrayList<Root>();
        String dbid = DBRouterUtils.getDBId();
        if (dbid == null) {
            list = baseDataMng.getPagedObj(className, condition, firstRow,
                    pageSize);
        } else {
            list = baseDataMng.getPagedObj(dbid, className, condition,
                    firstRow, pageSize);
        }
        return list;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<Root> getObjListByCondition(String className, String condition,
                                            int firstRow, int pageSize, String orderby) throws Exception {
        List<Root> list = new ArrayList<Root>();
        String dbid = DBRouterUtils.getDBId();
        if (dbid == null) {
            list = baseDataMng.getPagedObjOrdered(className, condition,
                    firstRow, pageSize, orderby);
        } else {
            list = baseDataMng.getPagedObjOrdered(dbid, className, condition,
                    firstRow, pageSize, orderby);
        }
        return list;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<Root> getObjListByHql(String hql, List<Object> objs)
            throws Exception {
        return baseDataMng.queryhql(hql, objs);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<Root> getObjListByHql(String hql, int firstRow, int pageSize,
                                      List<Object> objs) throws Exception {
        // TODO Auto-generated method stub
        return this.getObjListByHql(hql, objs);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<Root> getObjListByHqlIN(String hql, List<String> objs)
            throws Exception {
        Query q = hibernateTemplate.getSessionFactory().getCurrentSession().createQuery(hql);
        q.setParameterList("objs", objs);
        return q.list();
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<Map<String, Object>> getObjListBySql(String sql,
                                                     List<Object> objs) throws Exception {
        return baseDataMng.querySql(DBRouterUtils.getDBId(), sql, objs);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<Map<String, Object>> getObjListBySqlIN(String sql,
                                                       List<String> objs) throws Exception {
        List<String> objArray = new ArrayList<String>();
        for (String s : objs) {
            objArray.add("'" + s + "'");
        }
        return this.getObjListBySql(sql + objArray.toString().replace("[", "(").replace("]", ")"), null);
    }

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

    public void clearSession() {
        hibernateTemplate.getSessionFactory().getCurrentSession().clear();
    }

    public void flushSession() {
        hibernateTemplate.getSessionFactory().getCurrentSession().flush();
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<Map<String, Object>> getObjListBySqlPaged(String sql, int page,
                                                          int pageSize, List<Object> params) throws Exception {
        //不分页
        List<Map<String, Object>> result = null;
        if (page == 0 && pageSize == -1) {
            return getObjListBySql(sql, params);
        }

        //分页
        String head = "select * from (select tt.*, rownum as rn from (";
        String end = ") tt where rownum <= ?) tb where tb.rn >= ?";
        String pagedSql = head + sql + end;

        //起始行
        int startRow = page * pageSize;
        int endRow = (page - 1) * pageSize + 1;

        List<Object> pas = new ArrayList<Object>();;
        if (params != null && !"".equals(params)) {
            pas.addAll(params);
        }
        pas.add(startRow);
        pas.add(endRow);

        result = getObjListBySql(pagedSql, pas);

        //当前页没有数据时，返回上一页的数据
        if (result != null && result.size() == 0 && page > 1) {
            //复制值
            List<Object> pas_1 = new ArrayList<Object>();
            if (params != null && !"".equals(params)) {
                pas_1.addAll(params);
            }
            startRow = (page - 1) * pageSize;
            endRow = (page - 2) * pageSize + 1;
            pas_1.add(startRow);
            pas_1.add(endRow);
            result = getObjListBySql(pagedSql, pas_1);
        }

        return result;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public long getTotalCountBySql(String sql, List<Object> params)
            throws Exception {
        Map<String, Object> result =null;
        String countKey = "count(*)";
        List<Map<String,Object>> rlist=this.getObjListBySql(sql,params);
        if(rlist.size()>0){
            result =rlist.get(0);
        }
        if (result != null) {
            countKey = result.keySet().iterator().next();
            return Long.parseLong(result.get(countKey).toString());
        }

        return 0;
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<Object> prepareCall(String sql,
                                    List<Integer> inParamIndexs, List<Object> inParams,
                                    List<Integer> outResultIndex, List<Integer> outResultTypes) throws SQLException {
        List<Object> list = new ArrayList<Object>();
        Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
        session.doWork(new Work(){
            @Override
            public void execute(Connection connection) throws SQLException{
                conn = connection;
            }
        });
        CallableStatement cstmt = conn.prepareCall(sql);
        for (int i = 0; inParamIndexs!=null && i < inParamIndexs.size(); i++){
            cstmt.setObject(inParamIndexs.get(i), inParams.get(i));
        }
        for (int i = 0;outResultIndex!=null && i < outResultIndex.size(); i++){
            cstmt.registerOutParameter(outResultIndex.get(i), outResultTypes.get(i));
        }
        cstmt.execute();
        for (int i = 0;outResultIndex!=null && i < outResultIndex.size(); i++){
            list.add(cstmt.getObject(outResultIndex.get(i)));
        }
        return list;
    }

}
