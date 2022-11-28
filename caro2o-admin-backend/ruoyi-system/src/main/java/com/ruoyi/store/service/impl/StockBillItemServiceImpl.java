package com.ruoyi.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.store.domain.StockBill;
import com.ruoyi.store.domain.StockBillItem;
import com.ruoyi.store.mapper.StockBillItemMapper;
import com.ruoyi.store.service.IStockBillItemService;
import org.springframework.stereotype.Service;

import java.io.Serializable;
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

    @Override
    public List<StockBillItem> getByBillId(String id) {
        LambdaQueryWrapper<StockBillItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StockBillItem::getBillId,id);
        List<StockBillItem> stockBillItems = baseMapper.selectList(wrapper);
        return stockBillItems;
    }

    @Override
    public void updateList(List<StockBillItem> itemFrom) {
        baseMapper.updateList(itemFrom);
    }

    @Override
    public void deleteBillId(String id) {
        baseMapper.deleteBillId(id);
    }

}
