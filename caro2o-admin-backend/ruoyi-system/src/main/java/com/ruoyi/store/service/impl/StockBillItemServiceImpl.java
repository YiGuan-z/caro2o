package com.ruoyi.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.store.domain.StockBill;
import com.ruoyi.store.domain.StockBillItem;
import com.ruoyi.store.mapper.StockBillItemMapper;
import com.ruoyi.store.service.IStockBillItemService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * 出入库单据Service业务层处理
 *
 * @author ruoyi
 * @date 2022-11-27
 */
@Service
public class StockBillItemServiceImpl extends ServiceImpl<StockBillItemMapper, StockBillItem> implements IStockBillItemService {

    /**
     * 查询出入库单据列表
     *
     * @param stockBill 出入库单据
     * @return 出入库单据
     */
    @Override
    public List<StockBillItem> selectStockBillItemList(StockBillItem stockBillItem) {
        return getBaseMapper().selectStockBillItemList(stockBillItem);
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        LambdaUpdateWrapper<StockBillItem> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(StockBillItem::getStatus,-1);
        wrapper.in(StockBillItem::getId,list);
        int update = baseMapper.update(null, wrapper);
        return update>0;
    }
}
