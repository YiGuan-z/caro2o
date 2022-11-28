package com.ruoyi.store.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.store.mapper.StockBillMapper;
import com.ruoyi.store.domain.StockBill;
import com.ruoyi.store.service.IStockBillService;

/**
 * 出入库单据Service业务层处理
 *
 * @author ruoyi
 * @date 2022-11-27
 */
@Service
public class StockBillServiceImpl extends ServiceImpl<StockBillMapper, StockBill> implements IStockBillService {

    /**
     * 查询出入库单据列表
     *
     * @param stockBill 出入库单据
     * @return 出入库单据
     */
    @Override
    public List<StockBill> selectStockBillList(StockBill stockBill) {


        return getBaseMapper().selectStockBillList(stockBill);
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        LambdaUpdateWrapper<StockBill> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(StockBill::getStatus,-1);
        wrapper.in(StockBill::getId,list);
        int update = baseMapper.update(null, wrapper);
        return update>0;
    }

    @Override
    public boolean save(StockBill entity) {
        entity.setType(0);
        return super.save(entity);
    }

    @Override
    public boolean updateById(StockBill entity) {
        entity.setType(1);
        entity.setOperatorId(SecurityUtils.getUserId());
        entity.setOperateDate(new Date());
        return super.updateById(entity);
    }
}
