package com.cisdi.enfi.common.data;

import com.cisdi.enfi.common.utils.GeneralDictionary;

public class GeneralEntityUtils {
    /**
     * 反射entity
     *
     * @param className
     *            entity的名称
     * @return entity类型的Root
     */
    public static Root getRoot(String className) {
        Root root = null;
        int count = 0;
        while (root == null) {
            Class<?> objClass;
            try {
                objClass = Class.forName(GeneralDictionary.PO_BASEPATH[count] + className);
            } catch (ClassNotFoundException e) {
                count++;
                continue;
            }
            try {
                root = (Root) objClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return root;
    }

    /**
     * 反射entity，推荐使用这种方式
     *
     * @param clazz
     *            要反射的class
     * @return
     */
    public static Root getRoot(Class clazz) {
        Root root = null;
        try {
            root = (Root) clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return root;
    }

}

