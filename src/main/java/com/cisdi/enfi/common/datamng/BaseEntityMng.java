package com.cisdi.enfi.common.datamng;


import com.cisdi.enfi.common.data.Entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BaseEntityMng {
    Serializable saveObj(Entity var1) throws Exception;

    Serializable saveObj(String var1, Entity var2) throws Exception;

    void updateObj(Entity var1) throws Exception;

    void updateObj(String var1, Entity var2) throws Exception;

    void saveOrUpdateObj(Entity var1) throws Exception;

    void saveOrUpdateObj(String var1, Entity var2) throws Exception;

    void deleteObj(Entity var1) throws Exception;

    void deleteObj(String var1, Entity var2) throws Exception;

    <T extends Entity> List<T> getAllObj(String var1);

    <T extends Entity> List<T> getAllObj(String var1, String var2);

    <T extends Entity> List<T> getAllObj(Class<T> var1);

    <T extends Entity> List<T> getAllObj(String var1, Class<T> var2);

    Long getAllObjCount(String var1);

    Long getAllObjCount(String var1, String var2);

    <T extends Entity> Long getAllObjCount(Class<T> var1);

    <T extends Entity> Long getAllObjCount(String var1, Class<T> var2);

    <T extends Entity> List<T> queryhql(String var1, List<Object> var2) throws Exception;

    <T extends Entity> List<T> queryhql(String var1, String var2, List<Object> var3) throws Exception;

    <T extends Entity> List<T> querySql(String var1, List<Object> var2, Class<T> var3) throws Exception;

    <T extends Entity> List<T> querySql(String var1, String var2, List<Object> var3, Class<T> var4) throws Exception;

    List<Map<String, Object>> querySql(String var1, List<Object> var2) throws Exception;

    List<Map<String, Object>> querySql(String var1, String var2, List<Object> var3) throws Exception;

    List<Map<String, Object>> querySqlToLowerCase(String var1, List<Object> var2) throws Exception;

    List<Map<String, Object>> querySqlToLowerCase(String var1, String var2, List<Object> var3) throws Exception;

    Map<String, Object> querySqlSol(String var1, List<Object> var2) throws Exception;

    Map<String, Object> querySqlSol(String var1, String var2, List<Object> var3) throws Exception;

    int querySqlUD(String var1, List<Object> var2) throws Exception;

    int querySqlUD(String var1, String var2, List<Object> var3) throws Exception;

    <T extends Entity> List<T> getPagedObj(Class<T> var1, int var2, int var3);

    <T extends Entity> List<T> getPagedObj(String var1, Class<T> var2, int var3, int var4);

    <T extends Entity> List<T> getPagedObjByCondition(Class<T> var1, int var2, int var3, String var4) throws Exception;

    <T extends Entity> List<T> getPagedObjByCondition(String var1, Class<T> var2, int var3, int var4, String var5) throws Exception;

    <T extends Entity> Long getAllObjCountByCondition(Class<T> var1, String var2);

    <T extends Entity> Long getAllObjCountByCondition(String var1, Class<T> var2, String var3);

    Long getAllObjCountByCondition(String var1, String var2);

    Long getAllObjCountByCondition(String var1, String var2, String var3);

    <T extends Entity> T getSingleObjByHql(String var1, List<Object> var2) throws Exception;

    <T extends Entity> T getSingleObjByHql(String var1, String var2, List<Object> var3) throws Exception;

    <T extends Entity> List<T> getPagedObjOrdered(String var1, String var2, String var3, int var4, int var5, String var6);
}
