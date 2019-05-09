package org.smart.framework;

import org.apache.ibatis.session.SqlSessionFactory;
import org.smart.framework.core.ClassScanner;
import org.smart.framework.core.ConfigHelper;
import org.smart.framework.core.impl.DefaultClassScanner;
import org.smart.framework.dao.DataAccessor;
import org.smart.framework.dao.impl.DefaultDataAccessor;
import org.smart.framework.ds.DataSourceFactory;
import org.smart.framework.ds.impl.DefaultDataSourceFactory;
import org.smart.framework.mvc.HandlerExceptionResolver;
import org.smart.framework.mvc.HandlerInvoker;
import org.smart.framework.mvc.HandlerMapping;
import org.smart.framework.mvc.ViewResolver;
import org.smart.framework.mvc.impl.DefaultHandlerExceptionResolver;
import org.smart.framework.mvc.impl.DefaultHandlerInvoker;
import org.smart.framework.mvc.impl.DefaultHandlerMapping;
import org.smart.framework.mvc.impl.DefaultViewResolver;
import org.smart.framework.util.ObjectUtil;
import org.smart.framework.util.StringUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 实例工厂
 */

public class InstanceFactory {
    /**
     * 用于缓存对应实例
     */
    private static final Map<String,Object> cache = new ConcurrentHashMap<>();

    /**
     * ClassScanner
     */

    private static final String ClASS_SCANNER = "smart.framework.custom.class_scanner";

    /**
     * DataSourceFactory
     */
    private static final String DS_FACTORY = "smart.framework.custom.ds_factory";

    /**
     * DataAccessor
     */
    private static final String DATA_ACCESSOR = "smart.framework.custom.data_accessor";

    private static final String HANDLER_MAPPING = "smart.framework.custom.handler_mapping";

    private static final String HANDLER_INVOKER = "smart.framework.custom.handler_invoker";

    private static final String HANDLER_EXCEPTION_RESOLVER = " smart.framework.custom.handler_exception_resolver";

    private static final String VIEW_RESOLVER = "smart.framework.custom.view_resolver";

    /**
     * 获取ClassScanner
     * @return
     */
    public static ClassScanner getClassScanner(){
        return getInstance(ClASS_SCANNER, DefaultClassScanner.class);
    }

    /**
     * 获取DataSourceFactory
     * @return
     */
    public static DataSourceFactory getDataSourceFactory(){
        return getInstance(DS_FACTORY, DefaultDataSourceFactory.class);
    }
    /**
     * 获取DataAccessor
     * @return
     */
    public static DataAccessor getDataAccessor(){
        return getInstance(DATA_ACCESSOR, DefaultDataAccessor.class);
    }

    public static HandlerMapping getHandlerMapping(){
        return getInstance(HANDLER_MAPPING, DefaultHandlerMapping.class);
    }

    public static ViewResolver getViewResolver(){
        return getInstance(VIEW_RESOLVER, DefaultViewResolver.class);
    }

    public static HandlerInvoker getHandlerInvoker(){
        return getInstance(HANDLER_INVOKER, DefaultHandlerInvoker.class);
    }

    public static HandlerExceptionResolver getHandlerExceptionResolver(){
        return getInstance(HANDLER_EXCEPTION_RESOLVER, DefaultHandlerExceptionResolver.class);
    }



    @SuppressWarnings("unchecked")
    public static <T> T getInstance(String cacheKey,Class<T> defaultImpliClass){
        //若缓存中存在对应的实例,则返回该实例
        if(cache.containsKey(cacheKey)){
            return (T) cache.get(cacheKey);
        }

        //从配置文件中获取相应的接口实现类配置
        String implClassName = ConfigHelper.getString(cacheKey);
        if(StringUtil.isEmpty(implClassName)){
            implClassName = defaultImpliClass.getName();
        }
        //通过反射创建该实现类的对应实例
        T instance = ObjectUtil.newInstance(implClassName);
        if(instance!=null){
            cache.put(cacheKey,instance);
        }

        return instance;

    }




}
