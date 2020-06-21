package com.cisdi.enfi.common.dao;

import com.cisdi.enfi.common.utils.SpringFactory;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.util.HashMap;

public class HibernateTemplateManger {
    private static Logger logger = LoggerFactory.getLogger(HibernateTemplateManger.class);
    private String defaultDB;
    private static HashMap<String, HibernateTemplate> templates = new HashMap();
    private static HibernateTemplateManger instance = null;

    public HibernateTemplateManger() {
    }

    public static HibernateTemplateManger getInstance() {
        if (instance == null) {
            instance = new HibernateTemplateManger();
        }

        return instance;
    }

    protected HibernateTemplate getHibernateTemplateById(String dbId) {
        if (dbId == null || "".equals(dbId.trim())) {
            dbId = this.defaultDB;
        }

        if (dbId == null) {
            logger.error("【平台日志】-defaultDB为空，无法获取HibernateTemplate对象。");
            return null;
        } else {
            if (templates.get(dbId) == null) {
                this.cacheHibernateTemplate(dbId);
            }

            return (HibernateTemplate)templates.get(dbId);
        }
    }

    private SessionFactory getSessionFactoryById(String dbId) {
        return (SessionFactory) SpringFactory.getObject(dbId);
    }

    private synchronized void cacheHibernateTemplate(String dbId) {
        SessionFactory sessionFactory = this.getSessionFactoryById(dbId);
        HibernateTemplate ht = new HibernateTemplate();
        ht.setSessionFactory(sessionFactory);
        templates.put(dbId, ht);
    }

    public String getDefaultDB() {
        return this.defaultDB;
    }

    public void setDefaultDB(String defaultDB) {
        this.defaultDB = defaultDB;
    }
}

