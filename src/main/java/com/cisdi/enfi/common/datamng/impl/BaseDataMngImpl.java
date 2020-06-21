package com.cisdi.enfi.common.datamng.impl;

import com.cisdi.enfi.common.data.Entity;
import com.cisdi.enfi.common.data.Root;
import com.cisdi.enfi.common.datamng.BaseDataMng;
import com.cisdi.enfi.common.utils.UuidUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Transactional
@Service("baseDataMng")
public class BaseDataMngImpl extends BaseEntityMngImpl implements BaseDataMng {
    private final String CREATETIME = "createTime";
    private final String UPDATETIME = "updateTime";

    public BaseDataMngImpl() {
    }

    public <T extends Root> T getObj(String id, String className) {
        return this.getObjByCondition((String)null, (String)className, "obj.id='" + id + "'");
    }

    public <T extends Root> T getObj(String dbId, String id, String className) {
        return this.getObjByCondition(dbId, className, "obj.id='" + id + "'");
    }

    public String saveObj(Root r) throws Exception {
        return this.saveObj((String)null, r);
    }

    public String saveObj(String dbId, Root r) throws Exception {
        if (r.getId() == null) {
            r.setId(UuidUtils.getUUID());
        }

        r.setAttribute("createTime", new Date());
        r.setAttribute("updateTime", new Date());
        return (String)this.getHibernateTemplate(dbId).save(r);
    }

    public void updateObj(Root r) throws Exception {
        this.updateObj((String)null, r);
    }

    public void updateObj(String dbId, Root r) throws Exception {
        r.setAttribute("updateTime", new Date());
        this.getHibernateTemplate(dbId).update(r);
    }

    public void deleteObj(String id, String className) throws Exception {
        this.deleteObj((String)null, id, (String)className);
    }

    public void deleteObj(String dbId, String id, String className) throws Exception {
        Set<String> set = new HashSet();
        this.deleteObj(dbId, id, className, set, true);
    }

    private void deleteObj(String dbId, String id, String className, Set<String> set, Boolean editConstrain) {
        Root r = this.getObj(dbId, id, className);
        if (r != null) {
            this.getHibernateTemplate(dbId).delete(r);
        }
    }

    public <T extends Root> List<T> getPagedObj(String dbId, String className, String condition, int firstRow, int pageSize) {
        Query q = null;
        if (condition != null && !condition.trim().equals("")) {
            q = this.getSession(dbId).createQuery("from " + className + " as obj where 1=1 and " + condition + " order by obj.createTime desc");
        } else {
            q = this.getSession(dbId).createQuery("from " + className + " as obj order by obj.createTime desc");
        }

        if (pageSize != -1) {
            q.setFirstResult(firstRow);
            q.setMaxResults(pageSize);
        }

        return q.list();
    }

    public <T extends Root> T getObjByCondition(String className, String condition) {
        return this.getObjByCondition((String)null, (String)className, condition);
    }

    public <T extends Root> List<T> getObjListByCondition(String dbId, String className, String condition) {
        String queryString = "";
        if (condition != null && !condition.trim().equals("")) {
            queryString = "from " + className + " as obj where 1=1 and " + condition + " order by obj.createTime desc";
        } else {
            queryString = "from " + className + " as obj " + " order by obj.createTime desc";
        }

        Query q = this.getSession(dbId).createQuery(queryString);
        return q.list();
    }

    public <T extends Entity> List<T> getPagedObjOrdered(String dbId, String className, String condition, int firstRow, int pageSize, String order) {
        Query q = null;
        if (condition != null && !condition.trim().equals("")) {
            if (order != null && !order.trim().equals("")) {
                q = this.getSession(dbId).createQuery("from " + className + " as obj where 1=1 and " + condition + " " + order);
            } else {
                q = this.getSession(dbId).createQuery("from " + className + " as obj where 1=1 and " + condition + " order by obj.createTime desc");
            }
        } else if (order != null && !order.trim().equals("")) {
            q = this.getSession(dbId).createQuery("from " + className + " as obj " + order);
        } else {
            q = this.getSession(dbId).createQuery("from " + className + " as obj order by obj.createTime desc");
        }

        if (pageSize != -1) {
            q.setFirstResult(firstRow);
            q.setMaxResults(pageSize);
        }

        return q.list();
    }

    public <T extends Root> T getObjByCondition(String dbId, String className, String condition) {
        String queryString = "";
        if (condition != null && !condition.trim().equals("")) {
            queryString = "from " + className + " as obj where 1=1 and (" + condition + ")" + " order by obj.createTime desc";
        } else {
            queryString = "from " + className + " as obj where 1=1 " + " order by obj.createTime desc";
        }

        Query q = this.getSession(dbId).createQuery(queryString);
        List<Root> list = q.list();
        //return list != null && list.size() > 0 ? (Root)list.get(0) : null;
        return null;
    }

    public <T extends Root> List<T> getObjListByCondition(String className, String condition) {
        return this.getObjListByCondition((String)null, (String)className, condition);
    }

    public <T extends Root> List<T> getPagedObj(String className, String condition, int firstRow, int pageSize) {
        return this.getPagedObj((String)null, (String)className, condition, firstRow, pageSize);
    }

    public <T extends Root> List<T> getPagedObjOrdered(String className, String condition, int firstRow, int pageSize, String order) {
        return this.getPagedObjOrdered((String)null, (String)className, condition, firstRow, pageSize, order);
    }

    public Long getAllObjCount(String className) {
        return this.getAllObjCount((String)null, className);
    }

    public Long getAllObjCount(String dbId, String className) {
        return this.getAllObjCountByCondition(dbId, className, (String)null);
    }

    public <T extends Root> T getObjByName(String name, Class<T> classType) {
        Session session = this.getSession((String)null);
        List<Root> list = session.createCriteria(classType).add(Restrictions.eq("name", name)).list();
//        return list != null && list.size() > 0 ? (Root)list.get(0) : null;
        return null;
    }


    public <T extends Root> T getObjByName(String dbId, String name, Class<T> classType) {
        Session session = this.getSession(dbId);
        List<Root> list = session.createCriteria(classType).add(Restrictions.eq("name", name)).list();
//        return list != null && list.size() > 0 ? (Root)list.get(0) : null;
        return null;
    }

    public void saveOrUpdateObj(Root r) throws Exception {
        this.saveOrUpdateObj((String)null, r);
    }

    public void saveOrUpdateObj(String dbId, Root r) throws Exception {
        if (r.getId() == null) {
            r.setId(UuidUtils.getUUID());
            r.setAttribute("createTime", new Date());
        }

        r.setAttribute("updateTime", new Date());
        this.getHibernateTemplate(dbId).saveOrUpdate(r);
    }

    public String getObjIdByName(String objName, Class<? extends Root> className) throws Exception {
        return this.getObjIdByName((String)null, objName, className);
    }

    public String getObjIdByName(String dbId, String objName, Class<? extends Root> className) throws Exception {
        Session session = this.getSession(dbId);
        List<Root> list = session.createCriteria(className).add(Restrictions.eq("name", objName)).list();
        return list != null && list.size() > 0 ? ((Root)list.get(0)).getAttribute("id").toString() : null;
    }

    public <T extends Root> T getObj(String id, Class<T> classType) {
        return this.getObj(id, classType.getSimpleName());
    }

    public <T extends Root> T getObj(String dbId, String id, Class<T> classType) {
        return this.getObj(dbId, id, classType.getSimpleName());
    }

    public <T extends Root> void deleteObj(String id, Class<T> classType) throws Exception {
        this.deleteObj(id, classType.getSimpleName());
    }

    public <T extends Root> void deleteObj(String dbId, String id, Class<T> classType) throws Exception {
        this.deleteObj(dbId, id, classType.getSimpleName());
    }

    public <T extends Root> T getObjByCondition(Class<T> classType, String condition) {
        return this.getObjByCondition(classType.getSimpleName(), condition);
    }

    public <T extends Root> T getObjByCondition(String dbId, Class<T> classType, String condition) {
        return this.getObjByCondition(dbId, classType.getSimpleName(), condition);
    }

    public <T extends Root> List<T> getObjListByCondition(Class<T> classType, String condition) {
        return this.getObjListByCondition(classType.getSimpleName(), condition);
    }

    public <T extends Root> List<T> getObjListByCondition(String dbId, Class<T> classType, String condition) {
        return this.getObjListByCondition(dbId, classType.getSimpleName(), condition);
    }

    public <T extends Root> List<T> getPagedObj(Class<T> classType, String condition, int firstRow, int pageSize) {
        return this.getPagedObj(classType.getSimpleName(), condition, firstRow, pageSize);
    }

    public <T extends Root> List<T> getPagedObj(String dbId, Class<T> classType, String condition, int firstRow, int pageSize) {
        return this.getPagedObj(dbId, classType.getSimpleName(), condition, firstRow, pageSize);
    }

    public <T extends Root> List<T> getPagedObjOrdered(Class<T> classType, String condition, int firstRow, int pageSize, String order) {
        return this.getPagedObjOrdered(classType.getSimpleName(), condition, firstRow, pageSize, order);
    }

    public <T extends Root> List<T> getPagedObjOrdered(String dbId, Class<T> classType, String condition, int firstRow, int pageSize, String order) {
        return this.getPagedObjOrdered(dbId, classType.getSimpleName(), condition, firstRow, pageSize, order);
    }
}

