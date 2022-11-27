package com.ruoyi.store.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.store.domain.StockBill;
import com.ruoyi.store.domain.StockBillItem;

import java.util.List;

/**
 * 出入库单据Service接口
 *
 * @author ruoyi
 * @date 2022-11-27
 */
public interface IStockBillItemService extends IService<StockBillItem> {

    /**
     * 查询出入库单据列表
     *
     * @param stockBill 出入库单据
     * @return 出入库单据集合
     */
    public List<StockBillItem> selectStockBillItemList(StockBillItem stockBillItem);

}
