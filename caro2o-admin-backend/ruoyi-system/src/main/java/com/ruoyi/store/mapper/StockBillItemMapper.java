package com.ruoyi.store.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.store.domain.StockBill;
import com.ruoyi.store.domain.StockBillItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 出入库单据Mapper接口
 *
 * @author ruoyi
 * @date 2022-11-27
 */
public interface StockBillItemMapper extends BaseMapper<StockBillItem> {


    void updateList(@Param("itemFrom") List<StockBillItem> itemFrom);

    void deleteBillId(String id);
}
