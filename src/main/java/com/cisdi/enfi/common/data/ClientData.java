package com.cisdi.enfi.common.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientData {

    private String warning = null;
    // modelname
    private HashMap< String, Object > otherValueMap =
            new HashMap< String, Object >();
    private List<Map< String, Object >> objList =
            new ArrayList< Map< String, Object >>();

    public ClientData() {

    }

    public ClientData(Root root) {
        Map< String, Object > map = root.getHashAttributes();
        List< Map< String, Object >> list =
                new ArrayList< Map< String, Object >>();
        list.add(map);
        this.setObjList(list);
    }

    public ClientData(List< Root > roots) {
        List< Map< String, Object >> list =
                new ArrayList< Map< String, Object >>();
        for (Root root : roots) {
            Map< String, Object > map = root.getHashAttributes();
            list.add(map);
        }
        this.setObjList(list);
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }

    public HashMap< String, Object > getOtherValueMap() {
        return otherValueMap;
    }

    public void setOtherValueMap(HashMap< String, Object > otherValueMap) {
        this.otherValueMap = otherValueMap;
    }

    public List< Map< String, Object >> getObjList() {
        return objList;
    }

    public void setObjList(List< Map< String, Object >> objList) {
        this.objList = objList;
    }
}

