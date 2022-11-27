package com.ruoyi.store.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.store.mapper.GoodsStoreMapper;
import com.ruoyi.store.domain.GoodsStore;
import com.ruoyi.store.service.IGoodsStoreService;

/**
 * 物品库存Service业务层处理
 *
 * @author caseycheng
 * @date 2022-11-27
 */
@Service
public class GoodsStoreServiceImpl extends ServiceImpl<GoodsStoreMapper, GoodsStore> implements IGoodsStoreService {
	
	/**
	 * 查询物品库存列表
	 *
	 * @param goodsStore 物品库存
	 * @return 物品库存
	 */
	@Override
	public List<GoodsStore> selectGoodsStoreList(GoodsStore goodsStore) {
		return getBaseMapper().selectGoodsStoreList(goodsStore);
	}
	
	/**
	 * 查询所有分类
	 *
	 * @return
	 */
	@Override
	public Map<String, Map<String, Object>> selectAllStore() {
		List<GoodsStore> ret = baseMapper.selectAllStore();
		return null;
	}
}
