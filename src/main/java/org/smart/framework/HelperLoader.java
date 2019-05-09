package org.smart.framework;

import org.smart.framework.aop.AopHelper;
import org.smart.framework.dao.DatabaseHelper;
import org.smart.framework.ioc.BeanHelper;
import org.smart.framework.ioc.IocHelper;
import org.smart.framework.mvc.ActionHelper;
import org.smart.framework.mybatis.MybatisHelper;
import org.smart.framework.orm.EntityHelper;
import org.smart.framework.util.ClassUtil;

public final class HelperLoader {
    public static void init(){
        //定义需要加载的 Helper 类
        Class<?>[] classList = {
                DatabaseHelper.class,
                EntityHelper.class,
                ActionHelper.class,
                BeanHelper.class,
                MybatisHelper.class,
                AopHelper.class,
                IocHelper.class
        };
        if (!FrameworkConstant.MABATIS){
            classList[4] = null;
        }else{
            classList[0]=null;
        }
        //按照顺序加载类
        for(Class<?> cls : classList){
            if (cls != null){
                ClassUtil.loadClass(cls.getName());
            }
        }
    }
}
