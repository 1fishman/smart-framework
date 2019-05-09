package sample.controller;


import org.smart.framework.ioc.annotation.Inject;
import org.smart.framework.mvc.annotation.Action;
import org.smart.framework.mvc.annotation.Request;
import org.smart.framework.mvc.bean.View;
import org.smart.framework.orm.DataSet;
import sample.dao.SeckillDao;
import sample.entity.Seckill;

import java.util.List;

@Action
public class UserController {

    /*SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
    private void set() throws Exception {
        SqlSessionFactory sqlSession = sqlSessionFactoryBean.getObject();
        SqlSession sql =  sqlSession.openSession();


    }*/

    @Inject
    private SeckillDao seckillDao;

    @Request.Get(value = "/get")
    public View getUser(){
        System.out.println("1234123");
        return new View("detail.jsp").addModel("123","123");
    }


    @Request.Get(value = "/{seckillId}/detail")
    public View detail(Integer seckillId){

        if(seckillId==null){
            return new  View("redirect:/list");
        }
        Seckill seckill = DataSet.select(Seckill.class,"seckill_id="+seckillId);
        System.out.println(seckillId);
        if(seckill==null){
            return new  View("redirect:/list");
        }
        System.out.println("返回detail.jsp视图");
        return new View("detail.jsp").addModel("seckill",seckill);
    }

    @Request.Get(value = "/list")
    public View getList(){
        System.out.println("list");
        //List<Seckill> ls = DataSet.selectList(Seckill.class);
        List<Seckill> ls = seckillDao.queryAll(0,5);
        return new View("list.jsp").addModel("list",ls);
    }

}
