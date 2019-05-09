package org.smart.framework.orm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart.framework.core.ClassHelper;
import org.smart.framework.orm.annotation.Column;
import org.smart.framework.orm.annotation.Entity;
import org.smart.framework.orm.annotation.Table;
import org.smart.framework.util.ArrayUtil;
import org.smart.framework.util.MapUtil;
import org.smart.framework.util.StringUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 初始化 Entity 结构
 */
public class EntityHelper {

    private static final Logger logger = LoggerFactory.getLogger(EntityHelper.class);
    /**
     * 实体类 ==> 表名
     */
    private static final Map<Class<?>,String> entityClassTableNameMap = new HashMap<>();

    /**
     * 实体类 => (字段名 => 列名)
     */
    private static final Map<Class<?>,Map<String,String>> entityClassFieldMapMap = new HashMap<>();

    static {
        List<Class<?>> entityClassList = ClassHelper.getclassListByAnnotation(Entity.class);
        for (Class<?> entityClass : entityClassList){
            initEntityNameMap(entityClass);
            initEntityFieldMapMap(entityClass);
        }
    }

    /**
     * 向map中加入此实例类名和表名的映射
     * @param entityClass
     */
    private static void initEntityNameMap(Class<?> entityClass){
        //判断该实体类上是否存在Table注解
        String tableName;
        if(entityClass.isAnnotationPresent(Table.class)){
            //若存在,则使用该注解中定义的表名
            tableName = entityClass.getAnnotation(Table.class).value();
        }else {
            //如果没有注解,则用本身的类名
            tableName = StringUtil.camelhumpToUnderline(entityClass.getSimpleName());
        }
        logger.debug("实体到表名的映射："+tableName+"   "+entityClass.getSimpleName());
        entityClassTableNameMap.put(entityClass,tableName);
    }

    private static void initEntityFieldMapMap(Class<?> entityClass){
        //获取并遍历实体类中的所有字段(不包括父类中的字段)
        Field[] fields = entityClass.getDeclaredFields();
        if(ArrayUtil.isNotEmpty(fields)){
            //创建一个fieldMap(用于存放列名与字段名的映射关系)
            Map<String,String> fieldMap = new HashMap<>();
            for(Field field : fields){
                String fieldName = field.getName();
                String columnName;
                if(field.isAnnotationPresent(Column.class)){
                    //如果存在注解,使用注解中定义的列名
                    columnName = field.getAnnotation(Column.class).value();
                }else{
                    //如果不存在,则将字段名转换为下划线风格
                    columnName = StringUtil.camelhumpToUnderline(fieldName);
                }
                fieldMap.put(fieldName,columnName);
            }
            entityClassFieldMapMap.put(entityClass,fieldMap);

        }

    }

    /**
     * 获取entity对应的表名
     * @param entityClass
     * @return
     */
    public static String getTableName(Class<?> entityClass){
        return entityClassTableNameMap.get(entityClass);
    }

    /**
     * 获取字段和列名的映射
     * @param entityClass
     * @return
     */
    public static Map<String,String> getFieldMap(Class<?> entityClass){
        return entityClassFieldMapMap.get(entityClass);
    }

    public static Map<String,String> getColumnMap(Class<?> entityClass){
        return MapUtil.invert(getFieldMap(entityClass));
    }

    public static String getColumnName(Class<?> entityClass,String field){
        String columnName = getFieldMap(entityClass).get(field);
        return StringUtil.isNotEmpty(columnName)?columnName:field;
    }

}
