package sample.dao;

import org.apache.ibatis.annotations.Param;
import org.smart.framework.ioc.annotation.Bean;
import org.smart.framework.mybatis.annotation.SMapper;
import sample.entity.Seckill;

import java.util.Date;
import java.util.List;
import java.util.Map;


@SMapper
public interface SeckillDao {

    /**
     * 减库存
     * @param seckillId
     * @param killTime
     * @return 影响行数为1 表示更新数据行数
     */
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

    Seckill queryById(long seckillId);

    /**
     * 根据偏移量和限制查询商品
     * @param offset
     * @param limit
     * @return
     */

    List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);


    void killByProcedure(Map<String, Object> paramMap);

}
