package com.ruoyi.store.service.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.store.domain.StockBillItem;
import com.ruoyi.store.service.IStockBillItemService;
import com.ruoyi.system.service.ISysUserService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.store.mapper.StockBillMapper;
import com.ruoyi.store.domain.StockBill;
import com.ruoyi.store.service.IStockBillService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 出入库单据Service业务层处理
 *
 * @author ruoyi
 * @date 2022-11-27
 */
@Service
public class StockBillServiceImpl extends ServiceImpl<StockBillMapper, StockBill> implements IStockBillService {
    @Autowired
    private IStockBillItemService stockBillItemService;
    /**
     * 查询出入库单据列表
     *
     * @param stockBill 出入库单据
     * @return 出入库单据
     */
    @Override
    public List<StockBill> selectStockBillList(StockBill stockBill) {
        Long count = getBaseMapper().selectForCount(stockBill);
        if(count==0){
            return Collections.emptyList();
        }
        List<StockBill> stockBills = getBaseMapper().selectForList(stockBill);
        stockBills.stream().forEach(node -> {
            node.setPrice(node.getPrice().multiply(new BigDecimal(node.getAmounts())));
        });
        return stockBills;
    }


    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        LambdaUpdateWrapper<StockBill> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(StockBill::getStatus, -1).set(StockBill::getOperatorId,SecurityUtils.getUserId());
        wrapper.in(StockBill::getId, list);
        int update = baseMapper.update(null, wrapper);
        return update > 0;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean save(StockBill entity) {
        entity.setType(0);
        entity.setOperatorId(SecurityUtils.getUserId());
        //存储子元素
        final List<StockBillItem> items = entity.getItemFrom();
        if (items.size()!=0){
            items.forEach(item->stockBillItemService.save(item));
        }
        return super.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateById(StockBill entity) {
        entity.setType(1);
        entity.setOperatorId(SecurityUtils.getUserId());
        entity.setOperateDate(new Date());
        List<StockBillItem> byBillId = stockBillItemService.getByBillId(entity.getId());
        //把数据库查到的值转换成map，key是
        Map<String, StockBillItem> map = byBillId.stream().collect(Collectors.toMap(StockBillItem::getId, r -> r));
        entity.getItemFrom().forEach(node->{
            StockBillItem item = map.get(node.getId());
            BigDecimal newPrice = node.getPrice();
            if(newPrice.doubleValue()<0){
                throw new ServiceException("钱不能为负数");
            }
            Integer amounts = item.getAmounts();
            Integer newAmounts = node.getAmounts();
            int i = amounts - newAmounts;
            if (i<0){
                throw new ServiceException("库存不够!!!");
            }
            node.setAmounts(i);
        });

        stockBillItemService.deleteBillId(entity.getId());
        stockBillItemService.updateList(entity.getItemFrom());
        return super.updateById(entity);
    }
}
