package com.cisdi.enfi.common.service;

import com.cisdi.enfi.common.data.Root;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface BaseService {

    /**
     * 更新记录，更新null字段，等同于update(className, data, true)
     * @param className 类名
     * @param data 需要更新的数据
     * @throws Exception
     */
    public void update(String className, Map<String, Object> data) throws Exception;

    /**
     * 更新数据，可选择是否对null的字段进行更新
     * @param className 类名
     * @param data 需要更新的数据
     * @param updateNull 是否更新null或空字符串("")的数据
     * @throws Exception
     */
    public void update(String className, Map<String, Object> data, Boolean updateNull) throws Exception;

    /**
     * 根据sql更新数据
     * @param sql sql语句
     * @param objs 参数
     * @throws Exception
     */
    public void updateBySql(String sql, List<Object> objs) throws Exception;

    /**
     * 保存数据
     * @param className 类名
     * @param data 需要保存的数据
     * @return 返回新保存的数据的ID
     * @throws Exception
     */
    public String save(String className, Map<String, Object> data) throws Exception;

    /**
     * 指定ID保存数据，如果该ID已存在，则执行更新数据
     * @param className 类名
     * @param data 需要保存的数据
     * @return 返回指定的数据ID
     * @throws Exception
     */
    public String saveWithId(String className, Map<String, Object> data) throws Exception;

    /**
     * 根据sql保存数据
     * @param sql sql语句
     * @return
     * @throws Exception
     */
    public String saveBySql(String sql) throws Exception;

    /**
     * 删除数据
     * @param className 类名
     * @param id 数据的ID
     * @throws Exception
     */
    public void delete(String className, String id) throws Exception;

    /**
     * 根据sql删除数据
     * @param sql sql语句
     * @param objs 参数
     * @throws Exception
     */
    public void deleteBySql(String sql, List<Object> objs) throws Exception;

    /**
     * 根据ID查询数据
     * @param className 类名
     * @param id 要查找的数据的ID
     * @return 返回查询结果Root对象
     * @throws Exception
     */
    public Root getObjById(String className, String id) throws Exception;

    /**
     * 根据两个关联表的各自ID，查询关联表的数据（查询结果只有一条记录）
     * @param className 关联表的模型名称
     * @param firstField 第一个被关联表的域名
     * @param firstId 第一个被关联表的数据ID
     * @param secondField 第二个被关联表的域名
     * @param secondId 第二个被关联表的数据ID
     * @return root
     * @throws Exception
     */
    public Root getObjByRelId(
            String className, String firstField, String firstId,
            String secondField, String secondId) throws Exception;

    /**
     * 根据ID查询数据，在事务中执行
     * @param className 类名
     * @param id 要查找的数据的ID
     * @return 返回查询结果Root对象
     * @throws Exception
     */
    public Root getObjByIdRequired(String className, String id) throws Exception;

    /**
     * 按条件查询数据
     * @param className 类名
     * @param condition 筛选条件
     * @return 返回符合条件的对象集合
     * @throws Exception
     */
    public List<Root> getObjListByCondition(String className, String condition) throws Exception;

    /**
     * 按条件查询数据,在事务中执行
     * @param className 类名
     * @param condition 筛选条件
     * @return 返回符合条件的对象集合
     * @throws Exception
     */
    public List<Root> getObjListByConditionRequired(String className, String condition) throws Exception;

    /**
     * 按条件分页查询
     * @param className 类名
     * @param condition 筛选条件
     * @param firstRow 起始行数
     * @param pageSize 每页数据条数, -1表示不分页
     * @return 返回符合条件的对象集合
     * @throws Exception
     */
    public List<Root> getObjListByCondition(String className, String condition,
                                            int firstRow, int pageSize) throws Exception;

    /**
     * 按条件分页排序查询
     * @param className 类名
     * @param condition 筛选条件
     * @param firstRow 起始行数
     * @param pageSize 每页数据条数, -1表示不分页
     * @param orderby 排序顺序,例如 order by obj.name asc
     * @return 返回符合条件的对象集合
     * @throws Exception
     */
    public List<Root> getObjListByCondition(String className, String condition,
                                            int firstRow, int pageSize, String orderby) throws Exception;

    /**
     * 根据hql查询数据
     * @param hql hql语句
     * @param objs 参数
     * @return 返回符合条件的对象集合
     * @throws Exception
     */
    public List<Root> getObjListByHql(String hql, List<Object> objs) throws Exception;

    /**
     * 根据hql分页查询数据
     * @param hql hql语句
     * @param firstRow 起始行位置
     * @param pageSize 分页大小
     * @param objs 参数
     * @return 返回符合条件的对象集合
     * @throws Exception
     */
    public List<Root> getObjListByHql(String hql, int firstRow,
                                      int pageSize, List<Object> objs) throws Exception;

    /**
     * 根据HQL查询 in (:objs) 关键字固定位objs
     * @param hql 查询条件
     * @param objs 列表
     * @return
     * @throws Exception
     */
    public List<Root> getObjListByHqlIN(String hql, List<String> objs) throws Exception;

    /**
     * 根据sql查询数据
     * @param sql sql语句
     * @param objs 参数
     * @return 返回符合条件的对象集合
     * @throws Exception
     */
    public List<Map<String, Object>> getObjListBySql(String sql, List<Object> objs) throws Exception;

    /**
     * 根据SQL查询关键字in objs
     * @param sql sql语句
     * @param objs 列表
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getObjListBySqlIN(String sql, List<String> objs) throws Exception;

    /**
     * 查询某张表中某一列符合某种条件下当前最大的数字
     * @param tableName 表名
     * @param field 列名(注意：这一列必中的数据须是可以被转换成数字的)
     * @param condition 查询过滤条件（例如：id='123'）
     * @return 符合条件的最大的maxNumber，Number类型，具体是Integer还是Double等等，根据不同的业务逻辑，分别调运intValue
     *         ()或doubleValue()等等
     * @throws Exception
     */
    public Number getCurrentMaxNumber(String tableName, String field,
                                      String condition) throws Exception;

    /**
     * 根据Sql进行分页查询
     *
     * @param sql
     * 		查询SQL
     * @param page
     * 		当前页（1为第一页）
     * @param pageSize
     * 		页大小
     * @param params
     * @return
     */
    public List<Map<String, Object>> getObjListBySqlPaged(String sql, int page, int pageSize,
                                                          List<Object> params) throws Exception;

    /** 根据sql查询结果数
     *
     * @param sql
     * @param params
     * @return
     * @throws BaseException
     */
    public long getTotalCountBySql(String sql, List<Object> params) throws Exception;

    /**
     * 存储过程调用
     * @param sql  调用存储过程语句
     * @param inParamIndexs 传入参数索引
     * @param inParams 传入参数值 和输入参数索引一一对应
     * @param outResultIndex 输出参数索引
     * @param outResultTypes 输出参数type类型 和输出参数索引一一对应
     * @return 输出参数的集合 按照输出参数索引从小到大存入
     * @throws SQLException
     */
    public List<Object> prepareCall(String sql,
                                    List<Integer> inParamIndexs,List<Object> inParams,
                                    List<Integer> outResultIndex, List<Integer> outResultTypes) throws SQLException;
}

