package com.cisdi.enfi.common.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class GeneralBeanOrMapUtils {
    /**
     * 将一个 Map 对象转化为一个 JavaBean
     *
     * @param <T>
     *
     * @param clazz
     *            要转化的类型
     * @param map
     *            包含属性值的 map
     * @return 转化出来的 JavaBean 对象
     * @throws IntrospectionException
     *             如果分析类属性失败
     * @throws IllegalAccessException
     *             如果实例化 JavaBean 失败
     * @throws InstantiationException
     *             如果实例化 JavaBean 失败
     * @throws InvocationTargetException
     *             如果调用属性的 setter 方法失败
     */
    public static <T> T convertMap2Bean(Class<T> clazz, Map map) throws IntrospectionException, IllegalAccessException,
            InstantiationException, InvocationTargetException {

        BeanInfo beanInfo = Introspector.getBeanInfo(clazz); // 获取类属性
        T obj = (T) clazz.newInstance(); // 创建 JavaBean 对象

        // 给 JavaBean 对象的属性赋值
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();

            if (map.containsKey(propertyName)) {
                // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
                Object value = map.get(propertyName);

                Object[] args = new Object[1];
                args[0] = value;

                descriptor.getWriteMethod().invoke(obj, args);
            }
        }
        return obj;
    }

    /**
     * 将Map转换为bean
     *
     * @param className
     *            要转换的类型名称（注意，需要加上包路径）
     * @param map
     *            包含属性值的 map
     * @return 转化出来的 JavaBean 对象
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws IntrospectionException
     */
    public static Object convertMap2Bean(String className, Map map) throws IllegalAccessException, InstantiationException,
            InvocationTargetException, ClassNotFoundException, IntrospectionException {
        Class clazz = Class.forName(className);
        Object obj = convertMap2Bean(clazz, map);
        return obj;
    }

    /**
     * 将一个 JavaBean 对象转化为一个 Map
     *
     * @param bean
     *            要转化的JavaBean 对象
     * @return 转化出来的 Map 对象
     * @throws IntrospectionException
     *             如果分析类属性失败
     * @throws IllegalAccessException
     *             如果实例化 JavaBean 失败
     * @throws InvocationTargetException
     *             如果调用属性的 setter 方法失败
     */
    public static Map convertBean2Map(Object bean) throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        Class clazz = bean.getClass();
        Map returnMap = new HashMap();
        BeanInfo beanInfo = Introspector.getBeanInfo(clazz);

        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean, new Object[0]);
                if (result != null) {
                    returnMap.put(propertyName, result);
                } else {
                    returnMap.put(propertyName, null);
                }
            }
        }
        return returnMap;
    }

    // public static void main(String[] args) {
    // MaterialCategoryVO vo = new MaterialCategoryVO();
    // vo.setId("123");
    // vo.setName("name");
    // vo.setCode("code");
    // vo.setEndTime(new Date());
    // vo.setPid("p123");
    // vo.setPname("pName");
    // vo.setPcode("pCode");
    //
    // Map<String, Object> map = new HashMap<String, Object>();
    // try {
    // map = convertBean2Map(vo);
    // System.out.println(map);
    //
    // MaterialCategoryVO vo1 = (MaterialCategoryVO)
    // convertMap2Bean(MaterialCategoryVO.class, map);
    // MaterialCategoryVO vo2 = (MaterialCategoryVO)
    // convertMap2Bean(GeneralDictionary.VO_BASEPATH + "MaterialCategoryVO",
    // map);
    // System.out.println(vo1);
    // System.out.println(vo2);
    // } catch (IllegalAccessException e) {
    // e.printStackTrace();
    // } catch (InvocationTargetException e) {
    // e.printStackTrace();
    // } catch (IntrospectionException e) {
    // e.printStackTrace();
    // } catch (InstantiationException e) {
    // e.printStackTrace();
    // } catch (ClassNotFoundException e) {
    // e.printStackTrace();
    // }
    // }
}
