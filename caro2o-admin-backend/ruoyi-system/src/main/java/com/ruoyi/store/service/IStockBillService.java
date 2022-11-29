package com.ruoyi.store.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.store.domain.StockBill;

/**
 * 出入库单据Service接口
 *
 * @author ruoyi
 * @date 2022-11-27
 */
public interface IStockBillService extends IService<StockBill> {

    /**
     * 查询出入库单据列表
     *
     * @param stockBill 出入库单据
     * @return 出入库单据集合
     */
    public List<StockBill> selectStockBillList(StockBill stockBill);

    void updateStatusById(Long id);
}
