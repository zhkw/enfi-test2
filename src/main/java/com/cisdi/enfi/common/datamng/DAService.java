package com.cisdi.enfi.common.datamng;

import com.cisdi.enfi.common.bean.ResultInfo;

import java.util.List;

public interface DAService {
    ResultInfo queryBySql(String var1, String var2, List<Object> var3);

    ResultInfo queryBySql2List(String var1, String var2, List<Object> var3);

    ResultInfo insertBySql(String var1, String var2, List<Object> var3);

    ResultInfo updateBySql(String var1, String var2, List<Object> var3);

    ResultInfo deleteBySql(String var1, String var2, List<Object> var3);
}
