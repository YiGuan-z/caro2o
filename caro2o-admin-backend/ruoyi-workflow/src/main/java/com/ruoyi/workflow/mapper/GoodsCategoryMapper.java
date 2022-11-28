package com.ruoyi.workflow.mapper;

import java.io.Serializable;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.workflow.domain.GoodsCategory;

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
	
	List<GoodsCategory> selectByIdFormTree(Long id);
	
	GoodsCategory selectByBusiPath(String busiPath);
	
	GoodsCategory selectByParentId(Long parentId);
}