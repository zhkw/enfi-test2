package com.cisdi.enfi.common.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;

public class TemplateFactory {
    public TemplateFactory() {
    }

    public static HibernateTemplate getDefaultHibernateTemplate() {
        return HibernateTemplateManger.getInstance().getHibernateTemplateById((String)null);
    }

    public static HibernateTemplate getHibernateTemplate(String dbId) {
        return HibernateTemplateManger.getInstance().getHibernateTemplateById(dbId);
    }

    public static JdbcTemplate getJDBCTemplate(String dbId) {
        return JDBCTemplateManager.getInstance().getTemplate(dbId);
    }
}
