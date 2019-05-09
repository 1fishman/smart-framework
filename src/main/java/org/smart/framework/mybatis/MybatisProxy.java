package org.smart.framework.mybatis;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart.framework.aop.Proxy.Proxy;
import org.smart.framework.aop.Proxy.ProxyChain;
import org.smart.framework.mvc.fault.MybatisException;
import org.smart.framework.tx.annotation.Transaction;

import java.lang.reflect.Method;

public class MybatisProxy implements Proxy {

    private static final Logger log = LoggerFactory.getLogger(MybatisProxy.class);

    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Method targetMethod = proxyChain.getTargetMethod();
        Class<?> targetClass = proxyChain.getTargetClass();
        // 如果是事务,需要有异常回滚
        if (targetMethod.isAnnotationPresent(Transaction.class)){
            SqlSessionFactory sqlSessionFactory = MybatisHelper.getSqlSessionFactory();
            SqlSession sqlSession = sqlSessionFactory.openSession();
            try {
                Object targetMapper = sqlSession.getMapper(targetClass);
                Object result = targetMethod.invoke(targetMapper,proxyChain.getMethodParams());
                return result;
            }catch (Exception e){
                sqlSession.rollback();
                sqlSession.close();
                throw new MybatisException(e);
            }
        }else{
            // 非事务类型直接提交
            try{
                SqlSessionFactory sqlSessionFactory = MybatisHelper.getSqlSessionFactory();
                SqlSession sqlSession = sqlSessionFactory.openSession(false);
                Object targetMapper = sqlSession.getMapper(targetClass);
                Object result = targetMethod.invoke(targetMapper,proxyChain.getMethodParams());
                sqlSession.commit();
                sqlSession.close();
                return result;
            }catch (Exception e){
                throw new MybatisException(e);
            }

        }
    }
}
