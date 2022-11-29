package com.ruoyi.store.mapper;



import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.store.domain.GoodsCategory;

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
  	List<GoodsCategory> selectAllData();
	
	List<GoodsCategory> selectByIdFormTree(Long id);
	
	GoodsCategory selectByBusiPath(String busiPath);
	
	GoodsCategory selectByParentId(Long parentId);
	
	List<GoodsCategory> selectParentByPath(String path);

	boolean save(GoodsCategory entity);

	List<GoodsCategory> selectChildrenByParentId(Object o);

	List<GoodsCategory> selectChild(Long id);

	void deleteForId(Long id);

	boolean updateForId(GoodsCategory entity);
}