package com.ruoyi.store.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.store.domain.StockBill;
import com.ruoyi.store.domain.StockBillItem;

import java.util.List;

/**
 * 出入库单据Mapper接口
 *
 * @author ruoyi
 * @date 2022-11-27
 */
public interface StockBillItemMapper extends BaseMapper<StockBillItem> {

  /**
   * 查询出入库单据列表
   *
   * @param stockBill 出入库单据
   * @return 出入库单据集合
   */
  public List<StockBillItem> selectStockBillItemList(StockBillItem stockBill);
}
