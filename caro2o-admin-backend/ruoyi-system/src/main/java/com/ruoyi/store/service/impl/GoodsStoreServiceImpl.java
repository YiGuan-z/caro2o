package com.ruoyi.store.service.impl;

import java.util.List;
import java.util.Map;

import com.ruoyi.store.service.IGoodsCategoryService;
import com.ruoyi.store.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
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
	@Autowired
	private IGoodsService goodsService;
	@Autowired
	private IGoodsCategoryService iGoodsCategoryService;
	
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
	
	
}
