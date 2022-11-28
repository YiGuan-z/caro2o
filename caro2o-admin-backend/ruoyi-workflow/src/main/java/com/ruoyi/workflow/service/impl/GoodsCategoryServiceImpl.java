package com.ruoyi.workflow.service.impl;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.ruoyi.common.utils.Assert;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.workflow.mapper.GoodsCategoryMapper;
import com.ruoyi.workflow.domain.GoodsCategory;
import com.ruoyi.workflow.service.IGoodsCategoryService;

/**
 * 物品分类信息Service业务层处理
 *
 * @author Mi-Manchi
 * @date 2022-11-27
 */
@Service
public class GoodsCategoryServiceImpl extends ServiceImpl<GoodsCategoryMapper, GoodsCategory> implements IGoodsCategoryService {
	
	/**
	 * 查询物品分类信息列表
	 *
	 * @param goodsCategory 物品分类信息
	 * @return 物品分类信息
	 */
	@Override
	public List<GoodsCategory> selectGoodsCategoryList(GoodsCategory goodsCategory) {
		return getBaseMapper().selectGoodsCategoryList(goodsCategory);
	}
	
	@Override
	public List<GoodsCategory> getTreeData() {
		final List<GoodsCategory> categories = baseMapper.selectList(null);
		return buildTreeData(categories);
	}
	
	public List<GoodsCategory> buildTreeData(List<GoodsCategory> data) {
		List<GoodsCategory> ret = new ArrayList<>(10);
		final Map<String, GoodsCategory> map = data.stream()
				.collect(Collectors.toMap(GoodsCategory::getBusiPath, goods -> goods));
		final AtomicInteger length = new AtomicInteger(Integer.MAX_VALUE);
		//查找最小值
		final Optional<GoodsCategory> root = map.values().stream().min(Comparator.comparingInt(prev -> prev.getBusiPath().length()));
		map.forEach((key, node) -> {
			length.set(key.split(":").length);
			if (length.get() != 1) {
				final String parentKey = key.substring(0, key.length() - 2);
				final GoodsCategory goodsCategory = map.get(parentKey);
				goodsCategory.getChildren().add(node);
			}
		});
		ret.add(root.orElse(null));
		return ret;
	}
	
	//通过id获取它的子结构
	@Override
	public GoodsCategory getById(Serializable id) {
		//获取它与它的子集，直接从数据库中查询，再经过树结构处理
		final List<GoodsCategory> categories = baseMapper.selectByIdFormTree((Long) id);
		//如果没有子集，就直接返回
		if (categories.size()==1)return categories.get(0);
		//有子集就找出父
		final GoodsCategory busiPath = baseMapper.selectByParentId(categories.get(0).getParentId());
		categories.add(busiPath);
		final List<GoodsCategory> list = buildTreeData(categories);
		Assert.assertTrue(list, (item) -> item.size() != 0, "id不正确，请检查id");
		return list.get(0).getChildren().get(0);
	}
	
	
	//删除分类时连同子分类一起删除
	//删除分类时如当前分类连同子分类下的商品还有库存则不可以删除
	@Override
	public boolean removeBatchByIds(Collection<?> list) {
		list.forEach(id -> {
		
		});
		return true;
	}
}