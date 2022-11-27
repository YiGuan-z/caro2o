package com.ruoyi.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.workflow.domain.GoodsCategory;

import java.util.List;

/**
 * 物品分类信息Mapper接口
 *
 * @author Mi-Manchi
 * @date 2022-11-27
 */
public interface GoodsCategoryMapper extends BaseMapper<GoodsCategory> {

  /**
   * 查询物品分类信息列表
   *
   * @param goodsCategory 物品分类信息
   * @return 物品分类信息集合
   */
  public List<GoodsCategory> selectGoodsCategoryList(GoodsCategory goodsCategory);
}
