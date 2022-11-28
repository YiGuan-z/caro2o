package com.ruoyi.store.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.store.domain.Goods;

import java.util.Collection;
import java.util.List;

/**
 * 物品信息Mapper接口
 *
 * @author caseycehng
 * @date 2022-11-27
 */
public interface GoodsMapper extends BaseMapper<Goods> {
	
	/**
	 * 查询物品信息列表
	 *
	 * @param goods 物品信息
	 * @return 物品信息集合
	 */
	public List<Goods> selectGoodsList(Goods goods);
	
	Goods selectByGoodsId(String id);
	
	/**
	 * 查询该分类id下商品数
	 * @param list
	 * @return
	 */
	Integer selectCategoryGoods(Collection<?> list);
}
