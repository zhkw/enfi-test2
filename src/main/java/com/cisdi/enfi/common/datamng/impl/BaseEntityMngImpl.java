package com.cisdi.enfi.common.datamng.impl;

import com.cisdi.enfi.common.dao.TemplateFactory;
import com.cisdi.enfi.common.data.Entity;
import com.cisdi.enfi.common.datamng.BaseEntityMng;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;

@Service("baseEntityMng")
@Transactional
public class BaseEntityMngImpl implements BaseEntityMng {
    public BaseEntityMngImpl() {
    }

    protected HibernateTemplate getHibernateTemplate(String dbId) {
        return TemplateFactory.getHibernateTemplate(dbId);
    }

    protected Session getSession(String dbId) {
        return this.getHibernateTemplate(dbId).getSessionFactory().getCurrentSession();
    }

    public Serializable saveObj(Entity r) throws Exception {
        return this.saveObj((String)null, r);
    }

    public Serializable saveObj(String dbId, Entity r) throws Exception {
        return this.getHibernateTemplate(dbId).save(r);
    }

    public void updateObj(Entity r) throws Exception {
        this.updateObj((String)null, r);
    }

    public void updateObj(String dbId, Entity r) throws Exception {
        this.getHibernateTemplate(dbId).update(r);
    }

    public void saveOrUpdateObj(Entity r) throws Exception {
        this.saveOrUpdateObj((String)null, r);
    }

    public void saveOrUpdateObj(String dbId, Entity r) throws Exception {
        this.getHibernateTemplate(dbId).saveOrUpdate(r);
    }

    public <T extends Entity> List<T> getAllObj(String className) {
        return this.getAllObj((String)null, (String)className);
    }

    public <T extends Entity> List<T> getAllObj(String dbId, String className) {
        Query q = this.getSession(dbId).createQuery("from " + className);
        return q.list();
    }

    public <T extends Entity> List<T> getAllObj(Class<T> classType) {
        return this.getAllObj(classType.getSimpleName());
    }

    public <T extends Entity> List<T> getAllObj(String dbId, Class<T> classType) {
        return this.getAllObj(dbId, classType.getSimpleName());
    }

    public Long getAllObjCount(String className) {
        return this.getAllObjCount((String)null, (String)className);
    }

    public Long getAllObjCount(String dbId, String className) {
        String sql = "select count(*) from " + className;
        Query query = this.getSession(dbId).createQuery(sql);
        List l = query.list();
        return (Long)l.get(0);
    }

    public <T extends Entity> Long getAllObjCount(Class<T> classType) {
        return this.getAllObjCount((String)null, (String)classType.getSimpleName());
    }

    public <T extends Entity> Long getAllObjCount(String dbId, Class<T> classType) {
        return this.getAllObjCount(dbId, classType.getSimpleName());
    }

    public <T extends Entity> List<T> queryhql(String hql, List<Object> param) throws Exception {
        return this.queryhql((String)null, hql, param);
    }

    public <T extends Entity> List<T> queryhql(String dbId, String hql, List<Object> param) throws Exception {
        Session session = this.getSession(dbId);
        Query hqlquery = session.createQuery(hql);
        if (param != null) {
            for(int i = 0; i < param.size(); ++i) {
                hqlquery.setParameter(i, param.get(i));
            }
        }

        return hqlquery.list();
    }

    public <T extends Entity> List<T> querySql(String sql, List<Object> param, Class<T> classType) throws Exception {
        return this.querySql((String)null, sql, param, classType);
    }

    public <T extends Entity> List<T> querySql(String dbId, String sql, List<Object> param, Class<T> classType) throws Exception {
        Session session = this.getSession(dbId);
        SQLQuery sqlquery = session.createSQLQuery(sql);
        if (param != null) {
            for(int i = 0; i < param.size(); ++i) {
                sqlquery.setParameter(i, param.get(i));
            }
        }

        List<T> list = null;
        list = sqlquery.setResultTransformer(Transformers.aliasToBean(classType)).list();
        return list;
    }

    public List<Map<String, Object>> querySql(String sql, List<Object> param) throws Exception {
        return this.querySql((String)null, (String)sql, (List)param);
    }

    public List<Map<String, Object>> querySql(String dbId, String sql, List<Object> param) throws Exception {
        Session session = this.getSession(dbId);
        Query sqlquery = session.createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        if (param != null) {
            for(int i = 0; i < param.size(); ++i) {
                sqlquery.setParameter(i, param.get(i));
            }
        }

        List<Map<String, Object>> list = sqlquery.list();
        return list;
    }

    public List<Map<String, Object>> querySqlToLowerCase(String sql, List<Object> param) throws Exception {
        return this.querySqlToLowerCase((String)null, sql, param);
    }

    public List<Map<String, Object>> querySqlToLowerCase(String dbId, String sql, List<Object> param) throws Exception {
        List<Map<String, Object>> list = this.querySql(dbId, sql, param);
        return this.toLowerCase(list);
    }

    public Map<String, Object> querySqlSol(String sql, List<Object> param) throws Exception {
        return this.querySqlSol((String)null, sql, param);
    }

    public Map<String, Object> querySqlSol(String dbId, String sql, List<Object> param) throws Exception {
        Session session = this.getSession(dbId);
        Query sqlquery = session.createSQLQuery(sql);
        if (param != null) {
            for(int i = 0; i < param.size(); ++i) {
                sqlquery.setParameter(i, param.get(i));
            }
        }

        return (Map)sqlquery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).uniqueResult();
    }

    public int querySqlUD(String sql, List<Object> param) throws Exception {
        return this.querySqlUD((String)null, sql, param);
    }

    public int querySqlUD(String dbId, String sql, List<Object> param) throws Exception {
        Session session = this.getSession(dbId);
        SQLQuery sqlquery = session.createSQLQuery(sql);
        if (param != null) {
            for(int i = 0; i < param.size(); ++i) {
                sqlquery.setParameter(i, param.get(i));
            }
        }

        return sqlquery.executeUpdate();
    }

    protected List<Map<String, Object>> toLowerCase(List<Map<String, Object>> list) {
        List<Map<String, Object>> results = new ArrayList();
        Iterator i$ = list.iterator();

        while(i$.hasNext()) {
            Map<String, Object> map = (Map)i$.next();
            Map<String, Object> _map = new HashMap();
            Iterator i$1 = map.keySet().iterator();

            while(i$1.hasNext()) {
                String key = (String)i$1.next();
                _map.put(key.toString().toLowerCase(), map.get(key));
            }

            results.add(_map);
        }

        return results;
    }

    public void deleteObj(Entity r) throws Exception {
        this.deleteObj((String)null, r);
    }

    public void deleteObj(String dbId, Entity r) throws Exception {
        this.getHibernateTemplate(dbId).delete(r);
    }

    public <T extends Entity> List<T> getPagedObj(Class<T> classType, int firstRow, int pageSize) {
        return this.getPagedObj((String)null, classType, firstRow, pageSize);
    }

    public <T extends Entity> List<T> getPagedObj(String dbId, Class<T> classType, int firstRow, int pageSize) {
        Query q = null;
        q = this.getSession(dbId).createQuery("from " + classType.getSimpleName());
        if (pageSize != -1) {
            q.setFirstResult(firstRow);
            q.setMaxResults(pageSize);
        }

        return q.list();
    }

    public Long getAllObjCountByCondition(String className, String condition) {
        return this.getAllObjCountByCondition((String)null, (String)className, condition);
    }

    public Long getAllObjCountByCondition(String dbId, String className, String condition) {
        String conditionStr = condition == null ? "" : " as obj where 1=1 and " + condition;
        String queryString = "select count(*) from " + className + conditionStr;
        Query query = this.getSession(dbId).createQuery(queryString);
        List l = query.list();
        long r = (Long)l.get(0);
        return r;
    }

    public <T extends Entity> Long getAllObjCountByCondition(Class<T> classType, String condition) {
        return this.getAllObjCountByCondition(classType.getSimpleName(), condition);
    }

    public <T extends Entity> Long getAllObjCountByCondition(String dbId, Class<T> classType, String condition) {
        return this.getAllObjCountByCondition(dbId, classType.getSimpleName(), condition);
    }

    public <T extends Entity> T getSingleObjByHql(String hql, List<Object> param) throws Exception {
        return this.getSingleObjByHql((String)null, hql, param);
    }

    public <T extends Entity> T getSingleObjByHql(String dbId, String hql, List<Object> param) throws Exception {
//        List<T> list = this.queryhql(dbId, hql, param);
//        return list != null && list.size() > 0 ? (Entity)list.get(0) : null;
        return null;
    }

    public <T extends Entity> List<T> getPagedObjByCondition(Class<T> classType, int firstRow, int pageSize, String condition) throws Exception {
        return this.getPagedObjByCondition((String)null, classType, firstRow, pageSize, condition);
    }

    public <T extends Entity> List<T> getPagedObjByCondition(String dbId, Class<T> classType, int firstRow, int pageSize, String condition) throws Exception {
        Query q = null;
        if (condition != null && !condition.trim().equals("")) {
            q = this.getSession(dbId).createQuery("from " + classType.getSimpleName() + " as obj where 1=1 and " + this.parseCondition(condition));
        } else {
            q = this.getSession(dbId).createQuery("from " + classType.getSimpleName());
        }

        if (pageSize != -1) {
            q.setFirstResult(firstRow);
            q.setMaxResults(pageSize);
        }

        return q.list();
    }

    public <T extends Entity> List<T> getPagedObjOrdered(String dbId, String className, String condition, int firstRow, int pageSize, String order) {
        Query q = null;
        if (condition != null && !condition.trim().equals("")) {
            if (order != null && !order.trim().equals("")) {
                q = this.getSession(dbId).createQuery("from " + className + " as obj where 1=1 and " + condition + " " + order);
            } else {
                q = this.getSession(dbId).createQuery("from " + className + " as obj where 1=1 and " + condition);
            }
        } else if (order != null && !order.trim().equals("")) {
            q = this.getSession(dbId).createQuery("from " + className + " as obj " + order);
        } else {
            q = this.getSession(dbId).createQuery("from " + className + " as obj");
        }

        if (pageSize != -1) {
            q.setFirstResult(firstRow);
            q.setMaxResults(pageSize);
        }

        return q.list();
    }

    protected String parseCondition(String condition) {
        if (condition == null) {
            return null;
        } else {
            return condition.trim().startsWith("and ") ? condition.replaceFirst("\\s*and\\s", " ") : condition;
        }
    }
}

