package com.ruoyi.store.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.store.domain.StockBill;

/**
 * 出入库单据Mapper接口
 *
 * @author ruoyi
 * @date 2022-11-27
 */
public interface StockBillMapper extends BaseMapper<StockBill> {

  /**
   * 查询出入库单据列表
   *
   * @param stockBill 出入库单据
   * @return 出入库单据集合
   */
  public List<StockBill> selectStockBillList(StockBill stockBill);

  List<StockBill> selectForList(StockBill stockBill);

  Long selectForCount(StockBill stockBill);
}
