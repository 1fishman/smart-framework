package org.smart.framework.tx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart.framework.aop.Proxy.Proxy;
import org.smart.framework.aop.Proxy.ProxyChain;
import org.smart.framework.dao.DatabaseHelper;
import org.smart.framework.tx.annotation.Transaction;

import java.lang.reflect.Method;

public class TransactionProxy implements Proxy {

    private static final Logger logger = LoggerFactory.getLogger(TransactionProxy.class);

    /**
     * 定义一个线程局部变量,用于保存当前线程是否进行了事务处理,默认为false(未处理)
     */
    private static final ThreadLocal<Boolean> flagContainer = new ThreadLocal<Boolean>(){
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };



    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result;
        boolean flag = flagContainer.get();
        Method method = proxyChain.getTargetMethod();
        //若当前线程未进行事务处理,并且在目标方法上定义了Transaction注解,则说明该方法需要进行事务处理
        if(!flag && method.isAnnotationPresent(Transaction.class)){
            //设置当前线程已经进行事务处理
            flagContainer.set(true);
            try{
                //开启事务
                DatabaseHelper.beginTransaction();
                logger.debug("[Smart] begin transaction");
                //执行目标方法
                result = proxyChain.doProxyChain();
                //提交事务
                DatabaseHelper.commitTransaction();
                logger.debug("[Smart] commit transaction");
            }catch (Exception e){
                DatabaseHelper.rollbackTransaction();
                logger.debug("[Smart] rollback transaction");
                throw e;
            }finally {
                //移除线程的局部变量
                flagContainer.remove();
            }
        }else{
            result = proxyChain.doProxyChain();
        }
        return result;
    }
}
