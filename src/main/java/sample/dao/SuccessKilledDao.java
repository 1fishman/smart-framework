package sample.dao;

import org.apache.ibatis.annotations.Param;
import org.smart.framework.mybatis.annotation.SMapper;
import sample.entity.SuccessKilled;
@SMapper
public interface SuccessKilledDao {

    /**
     * 插入购买明细，可过滤重复
     * @param seckillId
     * @param userPhone
     * @return 插入结果行数
     */
    int insertSuccessKilled(long seckillId, @Param("userPhone") long userPhone);

    /**
     * 根据Id查询SuccessKilled并携带秒杀产品对象实体
     * @param seckillId
     * @return
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);
}
