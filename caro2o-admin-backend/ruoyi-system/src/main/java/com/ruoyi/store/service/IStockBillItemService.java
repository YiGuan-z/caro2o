package com.ruoyi.store.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.store.domain.StockBill;
import com.ruoyi.store.domain.StockBillItem;

import java.io.Serializable;
import java.util.List;

/**
 * 出入库单据Service接口
 *
 * @author ruoyi
 * @date 2022-11-27
 */
public interface IStockBillItemService extends IService<StockBillItem> {

    List<StockBillItem> getByBillId(String id);
}
