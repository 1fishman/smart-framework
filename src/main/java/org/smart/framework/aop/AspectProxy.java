package org.smart.framework.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart.framework.aop.Proxy.Proxy;
import org.smart.framework.aop.Proxy.ProxyChain;

import java.lang.reflect.Method;

public abstract class AspectProxy implements Proxy {

    private static final Logger logger = LoggerFactory.getLogger(AspectProxy.class);


    // 执行代理
    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result = null;

        // 获取目标类
        Class<?> cls = proxyChain.getTargetClass();
        // 获取目标方法
        Method method = proxyChain.getTargetMethod();
        // 获取目标参数
        Object[] params = proxyChain.getMethodParams();
        begin();
        try {
            if(intercept(cls,method,params)){
                before(cls,method,params);
                result = proxyChain.doProxyChain();
                after(cls,method,params,result);
            }else{
                result = proxyChain.doProxyChain();
            }
        }catch (Exception e){
            logger.error("AOP异常",e);
            error(cls,method,params,e);
            throw e;
        }finally {
            end();
        }
        return result;
    }

    public void begin(){};
    public boolean intercept(Class<?> cls,Method method,Object[] params)throws Throwable{
        return true;
    }

    public void before(Class<?> cls,Method method,Object[] params) throws Throwable{

    }
    public void after(Class<?> cls, Method method, Object[] params, Object result) throws Throwable {
    }

    public void error(Class<?> cls, Method method, Object[] params, Throwable e) {
    }

    public void end() {
    }
}
