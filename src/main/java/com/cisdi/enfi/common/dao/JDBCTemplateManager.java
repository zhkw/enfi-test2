package com.cisdi.enfi.common.dao;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.cisdi.enfi.common.utils.SpringFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;

public class JDBCTemplateManager {
    private static Logger logger = LoggerFactory.getLogger(JDBCTemplateManager.class);
    private HashMap<String, JdbcTemplate> jdbcTemplates = new HashMap();
    private static JDBCTemplateManager instance = null;

    private JDBCTemplateManager() {
    }

    public static JDBCTemplateManager getInstance() {
        if (instance == null) {
            instance = new JDBCTemplateManager();
        }

        return instance;
    }

    public JdbcTemplate getTemplate(String name) {
        JdbcTemplate jt = (JdbcTemplate)this.jdbcTemplates.get(name);
        if (jt == null) {
            jt = this.createTemplate(name);
        }

        logger.debug("【平台日志】-得到JdbcTemplate：" + name);
        return jt;
    }

    private synchronized JdbcTemplate createTemplate(String name) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        String dataSourceName = name + "_DataSource";
        AtomikosDataSourceBean dataSource = (AtomikosDataSourceBean) SpringFactory.getObject(dataSourceName);
        jdbcTemplate.setDataSource(dataSource);
        this.jdbcTemplates.put(name, jdbcTemplate);
        return jdbcTemplate;
    }
}

