package com.cisdi.enfi.common.datamng;

import com.cisdi.enfi.common.data.Root;

import java.util.List;

public interface BaseDataMng extends BaseEntityMng {
    String saveObj(Root var1) throws Exception;

    String saveObj(String var1, Root var2) throws Exception;

    void saveOrUpdateObj(Root var1) throws Exception;

    void saveOrUpdateObj(String var1, Root var2) throws Exception;

    void updateObj(Root var1) throws Exception;

    void updateObj(String var1, Root var2) throws Exception;

    <T extends Root> T getObj(String var1, String var2);

    <T extends Root> T getObj(String var1, String var2, String var3);

    <T extends Root> T getObj(String var1, Class<T> var2);

    <T extends Root> T getObj(String var1, String var2, Class<T> var3);

    void deleteObj(String var1, String var2) throws Exception;

    void deleteObj(String var1, String var2, String var3) throws Exception;

    <T extends Root> void deleteObj(String var1, Class<T> var2) throws Exception;

    <T extends Root> void deleteObj(String var1, String var2, Class<T> var3) throws Exception;

    <T extends Root> T getObjByCondition(String var1, String var2);

    <T extends Root> T getObjByCondition(String var1, String var2, String var3);

    <T extends Root> T getObjByCondition(Class<T> var1, String var2);

    <T extends Root> T getObjByCondition(String var1, Class<T> var2, String var3);

    <T extends Root> List<T> getObjListByCondition(String var1, String var2);

    <T extends Root> List<T> getObjListByCondition(String var1, String var2, String var3);

    <T extends Root> List<T> getObjListByCondition(Class<T> var1, String var2);

    <T extends Root> List<T> getObjListByCondition(String var1, Class<T> var2, String var3);

    <T extends Root> List<T> getPagedObj(String var1, String var2, int var3, int var4);

    <T extends Root> List<T> getPagedObj(String var1, String var2, String var3, int var4, int var5);

    <T extends Root> List<T> getPagedObj(Class<T> var1, String var2, int var3, int var4);

    <T extends Root> List<T> getPagedObj(String var1, Class<T> var2, String var3, int var4, int var5);

    <T extends Root> List<T> getPagedObjOrdered(String var1, String var2, int var3, int var4, String var5);

    <T extends Root> List<T> getPagedObjOrdered(Class<T> var1, String var2, int var3, int var4, String var5);

    <T extends Root> List<T> getPagedObjOrdered(String var1, Class<T> var2, String var3, int var4, int var5, String var6);

    Long getAllObjCountByCondition(String var1, String var2);

    Long getAllObjCountByCondition(String var1, String var2, String var3);

    <T extends Root> T getObjByName(String var1, Class<T> var2);

    <T extends Root> T getObjByName(String var1, String var2, Class<T> var3);

    String getObjIdByName(String var1, Class<? extends Root> var2) throws Exception;

    String getObjIdByName(String var1, String var2, Class<? extends Root> var3) throws Exception;
}

