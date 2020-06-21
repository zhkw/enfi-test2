package com.cisdi.enfi.common.datamng.impl;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.cisdi.enfi.common.bean.ResultInfo;
import com.cisdi.enfi.common.dao.JDBCTemplateManager;
import com.cisdi.enfi.common.datamng.DAService;
import com.cisdi.enfi.common.utils.SpringFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

@Service("daService")
public class DAServiceImpl implements DAService {
    private Logger logger = LoggerFactory.getLogger(DAServiceImpl.class);
    private final String ARGS = "ARGS";
    private final String ARGTYPES = "ARGTYPES";

    public DAServiceImpl() {
    }

    private int counter(String str, char c) {
        int count = 0;

        for(int i = 0; i < str.length(); ++i) {
            if (str.charAt(i) == c) {
                ++count;
            }
        }

        return count;
    }

    private HashMap<String, Object[]> getArgMaps(List<Object> objs) {
        HashMap<String, Object[]> resultMap = new HashMap();
        Object[] args = new Object[objs.size()];
        Object[] argTypes = new Object[objs.size()];

        for(int i = 0; i < objs.size(); ++i) {
            Object obj = objs.get(i);
            if (obj == null) {
                argTypes[i] = 0;
                args[i] = obj;
            } else if (obj instanceof String) {
                argTypes[i] = -9;
                args[i] = obj;
            } else if (obj instanceof Integer) {
                argTypes[i] = 4;
                args[i] = obj;
            } else if (obj instanceof Double) {
                argTypes[i] = 8;
                args[i] = obj;
            } else if (obj instanceof Date) {
                argTypes[i] = 91;
                args[i] = obj;
            } else if (obj instanceof java.util.Date) {
                Timestamp ts = new Timestamp(((java.util.Date)obj).getTime());
                argTypes[i] = 93;
                args[i] = ts;
            } else {
                argTypes[i] = 1111;
                args[i] = obj;
            }

            resultMap.put("ARGS", args);
            resultMap.put("ARGTYPES", argTypes);
        }

        return resultMap;
    }

    private JdbcTemplate getJdbcTemplate(String dbId) {
        return JDBCTemplateManager.getInstance().getTemplate(dbId);
    }

    public ResultInfo queryBySql(String dbId, String sql, List<Object> list) {
        JdbcTemplate jdbcTemplate = this.getJdbcTemplate(dbId);
        ResultInfo rsInfo = new ResultInfo();
        if (jdbcTemplate == null) {
            rsInfo.setMessage("获取jdbcTemplate失败");
            rsInfo.setResult(false);
            return rsInfo;
        } else {
            int n = this.counter(sql, '?');
            if (list != null && list.size() != n || list == null && n > 0) {
                rsInfo.setMessage("sql参数个数不匹配");
                rsInfo.setResult(false);
                this.logger.debug("【平台日志】-sql参数个数不匹配!");
                return rsInfo;
            } else {
                new ArrayList();
                AtomikosDataSourceBean dataSource = this.getDataSource(dbId);
                this.logger.debug("【平台日志】-【查询前-连接池 " + dbId + " 连接总数：】" + dataSource.poolTotalSize() + "，【当前空闲数：】" + dataSource.poolAvailableSize());
                List reslist;
                if (list != null && list.size() > 0) {
                    HashMap<String, Object[]> params = this.getArgMaps(list);
                    this.logger.debug("【平台日志】-【查询】SQL：" + sql + "，Params：" + this.paramsFormater((Object[])params.get("ARGS")));
                    reslist = jdbcTemplate.queryForList(sql, (Object[])params.get("ARGS"));
                } else {
                    this.logger.debug("【平台日志】-【查询】SQL：" + sql);
                    reslist = jdbcTemplate.queryForList(sql);
                }

                this.logger.debug("【平台日志】-【查询后-连接池 " + dbId + " 连接总数：】" + dataSource.poolTotalSize() + "，【当前空闲数：】" + dataSource.poolAvailableSize());
                rsInfo.setObject(reslist);
                rsInfo.setMessage("查询成功");
                rsInfo.setResult(true);
                return rsInfo;
            }
        }
    }

    public ResultInfo queryBySql2List(String dbId, String sql, List<Object> list) {
        ResultInfo rsInfo = this.queryBySql(dbId, sql, list);
        List<LinkedHashMap<String, Object>> reslist = (List)rsInfo.getObject();
        List<ArrayList<Object>> list1 = new ArrayList();
        Iterator i$ = reslist.iterator();

        while(i$.hasNext()) {
            LinkedHashMap<String, Object> temp = (LinkedHashMap)i$.next();
            ArrayList<Object> lt = new ArrayList();
            Iterator i$1 = temp.values().iterator();

            while(i$1.hasNext()) {
                Object o = i$1.next();
                lt.add(o);
            }

            list1.add(lt);
        }

        rsInfo.setObject(list1);
        rsInfo.setMessage("查询成功");
        rsInfo.setResult(true);
        return rsInfo;
    }

    public ResultInfo insertBySql(String dbId, String sql, List<Object> list) {
        return this.excuteUpdate(dbId, sql, list);
    }

    public ResultInfo updateBySql(String dbId, String sql, List<Object> list) {
        return this.excuteUpdate(dbId, sql, list);
    }

    public ResultInfo deleteBySql(String dbId, String sql, List<Object> list) {
        return this.excuteUpdate(dbId, sql, list);
    }

    private ResultInfo excuteUpdate(String dbId, String sql, List<Object> list) {
        ResultInfo rsInfo = new ResultInfo();
        JdbcTemplate jdbcTemplate = this.getJdbcTemplate(dbId);
        if (jdbcTemplate == null) {
            rsInfo.setMessage("获取jdbcTemplate失败");
            rsInfo.setResult(false);
            return rsInfo;
        } else {
            int n = this.counter(sql, '?');
            if (list != null && list.size() != n || list == null && n > 0) {
                rsInfo.setMessage("sql参数个数不匹配");
                rsInfo.setResult(false);
                this.logger.debug("【平台日志】-sql参数个数不匹配!");
                return rsInfo;
            } else {
                AtomikosDataSourceBean dataSource = this.getDataSource(dbId);
                this.logger.debug("【平台日志】-【更新前-连接池 " + dbId + " 连接总数：】" + dataSource.poolTotalSize() + "，【当前空闲数：】" + dataSource.poolAvailableSize());
                if (list != null && list.size() > 0) {
                    HashMap<String, Object[]> params = this.getArgMaps(list);
                    this.logger.debug("【平台日志】-【更新】SQL：" + sql + "，Params：" + this.paramsFormater((Object[])params.get("ARGS")));
                    jdbcTemplate.update(sql, (Object[])params.get("ARGS"));
                } else {
                    this.logger.debug("【平台日志】-【更新】SQL：" + sql);
                    jdbcTemplate.update(sql);
                }

                this.logger.debug("【平台日志】-【更新后-连接池 " + dbId + " 连接总数：】" + dataSource.poolTotalSize() + "，【当前空闲数：】" + dataSource.poolAvailableSize());
                rsInfo.setMessage("操作成功");
                rsInfo.setResult(true);
                return rsInfo;
            }
        }
    }

    private AtomikosDataSourceBean getDataSource(String dbId) {
        String dataSourceName = dbId + "_DataSource";
        return (AtomikosDataSourceBean) SpringFactory.getObject(dataSourceName);
    }

    private String paramsFormater(Object[] objs) {
        StringBuffer result = new StringBuffer();
        String separator = "; ";

        for(int i = 0; i < objs.length; ++i) {
            Object obj = objs[i];
            if (obj == null) {
                result.append("NULL" + separator);
            } else if (obj instanceof String) {
                result.append((String)obj + separator);
            } else if (obj instanceof Integer) {
                result.append((Integer)obj + separator);
            } else if (obj instanceof Double) {
                result.append((Double)obj + separator);
            } else if (obj instanceof Date) {
                result.append((Date)obj + separator);
            } else if (obj instanceof java.util.Date) {
                Timestamp ts = new Timestamp(((java.util.Date)obj).getTime());
                result.append(ts + separator);
            } else {
                result.append(obj + separator);
            }
        }

        return result.toString();
    }
}

