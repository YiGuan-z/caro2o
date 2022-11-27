package com.ruoyi.store.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.store.domain.GoodsStore;

/**
 * 物品库存Mapper接口
 *
 * @author caseycheng
 * @date 2022-11-27
 */
public interface GoodsStoreMapper extends BaseMapper<GoodsStore> {

  /**
   * 查询物品库存列表
   *
   * @param goodsStore 物品库存
   * @return 物品库存集合
   */
  public List<GoodsStore> selectGoodsStoreList(GoodsStore goodsStore);
	
	List<GoodsStore> selectAllStore();
}
