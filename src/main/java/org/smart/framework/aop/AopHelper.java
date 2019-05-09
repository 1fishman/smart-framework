package org.smart.framework.aop;

import org.smart.framework.FrameworkConstant;
import org.smart.framework.InstanceFactory;
import org.smart.framework.aop.Proxy.Proxy;
import org.smart.framework.aop.Proxy.ProxyManager;
import org.smart.framework.aop.annotation.Aspect;
import org.smart.framework.aop.annotation.AspectOrder;
import org.smart.framework.core.ClassHelper;
import org.smart.framework.core.ClassScanner;
import org.smart.framework.core.fault.InitializationError;
import org.smart.framework.ioc.BeanHelper;
import org.smart.framework.mybatis.MybatisProxy;
import org.smart.framework.mybatis.annotation.SMapper;
import org.smart.framework.tx.TransactionProxy;
import org.smart.framework.tx.annotation.Service;
import org.smart.framework.tx.annotation.Transaction;
import org.smart.framework.util.ClassUtil;
import org.smart.framework.util.StringUtil;

import java.lang.annotation.Annotation;
import java.util.*;

public class AopHelper {

    /**
     * 获取ClassScanner
     */
    private static final ClassScanner classScanner = InstanceFactory.getClassScanner();

    static {
        try {
            // 创建 Proxy Map(用于　存放代理类　与　目标类列表的映射关系）
            Map<Class<?>, List<Class<?>>> proxyMap = createProxyMap();
            // 创建 Target Map ( 用于存放 目标类 与代理类列表 的映射关系)
            Map<Class<?>,List<Proxy>> targetMap = createTargetMap(proxyMap);
            // 遍历 TargetMap
            for (Map.Entry<Class<?>,List<Proxy>> targetEntry : targetMap.entrySet()){
                // 分别获取 map 中的key 与 value
                Class<?> targetClass = targetEntry.getKey();
                List<Proxy> proxyList = targetEntry.getValue();
                // 创建代理实例
                Object proxyInstance = ProxyManager.createProxy(targetClass,proxyList);
                // 用代理实例覆盖目标实例,并放入Bean 容器中
                BeanHelper.setBean(targetClass,proxyInstance);
            }
        } catch (Exception e) {
            throw new InitializationError("初始化 AopHelper 出错",e);
        }
    }

    private static Map<Class<?>, List<Class<?>>> createProxyMap() throws Exception {
        Map<Class<?>, List<Class<?>>> proxyMap = new LinkedHashMap<>();
        //添加相关代理
        //TODO 插件代理 先不实现
        //切面代理
        addAspectProxy(proxyMap);
        //事务代理
        addTransactionProxy(proxyMap);
        // mybatis 代理
        if (FrameworkConstant.MABATIS)
            addMybatisProxy(proxyMap);
        return proxyMap;
    }

    private static void addAspectProxy(Map<Class<?>,List<Class<?>>> proxyMap) throws Exception {
        //获取切面类(所有继承与BaseAspect的类)
        List<Class<?>> aspectProxyClassList = ClassHelper.getClassListBySuper(AspectProxy.class);
        //TODO 添加插件包下面的所有类 插件先不写
        //aspectProxyClassList.addAll(classScanner.getClassListBySuper(FrameworkConstant.PLUGIN_PACKAGE,AspectProxy.class));
        // 排序切面类
        sortAspectProxyClassList(aspectProxyClassList);
        // 遍历切面类
        for (Class<?> aspectProxyClass : aspectProxyClassList){
            // 判断 Aspect 注解是否存在
            Aspect aspect = aspectProxyClass.getAnnotation(Aspect.class);
            //创建目标类列表
            List<Class<?>> targetClassList = createTargetClassList(aspect);
            // 初始化 Proxy Map
            proxyMap.put(aspectProxyClass,targetClassList);
        }

    }

    private static void addMybatisProxy(Map<Class<?>,List<Class<?>>> proxyMap){
        // 使用MybatisProxy代理所有mapper
        List<Class<?>> mybatisClassList = ClassHelper.getclassListByAnnotation(SMapper.class);
        proxyMap.put(MybatisProxy.class,mybatisClassList);
    }


    private static void addTransactionProxy(Map<Class<?>,List<Class<?>>> proxyMap){
        //使用 TransactionProxy 代理所有Service 类
        List<Class<?>> serviceClassList = ClassHelper.getclassListByAnnotation(Service.class);
        proxyMap.put(TransactionProxy.class,serviceClassList);
    }



    /**
     * 排序代理类列表,如果有AspectOrder注解按照注解顺序来,如果没有则按照字母顺序
     * @param proxyClassList
     */
    private static void sortAspectProxyClassList(List<Class<?>> proxyClassList){
        // 排序代理类列表
        Collections.sort(proxyClassList, new Comparator<Class<?>>() {
            @Override
            public int compare(Class<?> aspect1, Class<?> aspect2) {
                if(aspect1.isAnnotationPresent(AspectOrder.class) || aspect2.isAnnotationPresent(AspectOrder.class)){
                    // 若有 Order 注解, 则优先比较 (序号越小 越靠前)
                    if(aspect1.isAnnotationPresent(AspectOrder.class)){
                        return getOrderValue(aspect1) - getOrderValue(aspect2);
                    }else {
                        return getOrderValue(aspect2) - getOrderValue(aspect1);
                    }
                }else {
                    //若没有 Order注解,则比较类名
                    return aspect1.hashCode() - aspect2.hashCode();
                }
            }
            private int getOrderValue(Class<?> aspect) {
                return aspect.getAnnotation(AspectOrder.class) != null ? aspect.getAnnotation(AspectOrder.class).value() : 0;
            }
        });
    }



    /**
     * 获取需要代理的类列表
     * @param aspect
     * @return
     * @throws Exception
     */
    private static List<Class<?>> createTargetClassList(Aspect aspect) throws Exception{
        List<Class<?>> targetClassList = new ArrayList<>();
        // 获取 Aspect 注解的相关属性
        String pkg = aspect.pkg();
        String cls = aspect.cls();
        Class<? extends Annotation> annotation = aspect.annotation();
        //如果包名不为空,则需要进一步判断类名是否为空
        if(StringUtil.isNotEmpty(pkg)){
            if (StringUtil.isNotEmpty(cls)){
                //如果类名不为空,则仅仅添加该类
                targetClassList.add(ClassUtil.loadClass(pkg+"."+cls,false));
            }else{
                    //如果注解不为空并且不是Aspect注解,则添加指定包名下带有该注解的所有类
                if(annotation != null && !annotation.equals(Aspect.class)){
                    targetClassList.addAll(classScanner.getClassListByAnnotation(pkg,annotation));
                }else{
                    //添加该包名下的所有类
                    targetClassList.addAll(classScanner.getClassList(pkg));
                }
            }
        }else{
            // 若注解不为空,并且不是Aspect注解,则添加应用包名下带有该注解的所有类
            if(annotation != null && !annotation.equals(Aspect.class)){
                targetClassList.addAll(ClassHelper.getclassListByAnnotation(annotation));
            }
        }
        return targetClassList;
    }

    private static Map<Class<?>,List<Proxy>> createTargetMap(Map<Class<?>, List<Class<?>>> proxyMap) throws Exception{
        Map<Class<?>,List<Proxy>> targetMap = new HashMap<>();
        //遍历 Proxy Map
        for (Map.Entry<Class<?>,List<Class<?>>> proxyEntry : proxyMap.entrySet()){
            //分别获取 map中的key 和value
            Class<?> proxyClass = proxyEntry.getKey();
            List<Class<?>> targetClassList = proxyEntry.getValue();
            //遍历目标列表
            for (Class<?> targetClass : targetClassList){
                //创建代理类(切面类) 实例
                Proxy baseAspect = (Proxy) proxyClass.newInstance();
                //初始化 Target Map
                if(targetMap.containsKey(targetClass)){
                    targetMap.get(targetClass).add(baseAspect);
                }else{
                    List<Proxy> baseAspectList = new ArrayList<>();
                    baseAspectList.add(baseAspect);
                    targetMap.put(targetClass,baseAspectList);
                }
            }
        }
        return targetMap;
    }


}
