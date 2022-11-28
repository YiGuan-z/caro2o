package com.ruoyi.store.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.store.domain.GoodsCategory;
import com.ruoyi.store.mapper.GoodsCategoryMapper;
import com.ruoyi.store.service.IGoodsCategoryService;
import com.ruoyi.store.service.IGoodsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 物品分类信息Service业务层处理
 *
 * @author Mi-Manchi
 * @date 2022-11-27
 */
@Service
public class GoodsCategoryServiceImpl extends ServiceImpl<GoodsCategoryMapper, GoodsCategory> implements IGoodsCategoryService {
	@Autowired
	private IGoodsService iGoodsService;
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
		List<GoodsCategory> ret=new ArrayList<>(10);
		final Map<String,GoodsCategory> map = data.stream()
				.collect(Collectors.toMap(GoodsCategory::getBusiPath, goods -> goods));
		map.forEach((key,node)->{
			final int nodeLenght = key.split(":").length;
			if (nodeLenght==1){
				ret.add(node);
			}else {
				final String parentKey = key.substring(0, key.length() - 2);
				final GoodsCategory goodsCategory = map.get(parentKey);
				goodsCategory.getChildren().add(node);
			}
		});
		return ret;
	}
	
	//通过id获取它的子结构
	@Override
	public GoodsCategory getById(Serializable id) {
		//获取它与它的子集，直接从数据库中查询，再经过树结构处理
		final List<GoodsCategory> categories = baseMapper.selectByIdFormTree((Long) id);
		//如果没有子集，就直接返回
		if (categories.size() == 1) return categories.get(0);
		final GoodsCategory rootNode = categories.get(0);
		final Map<String, GoodsCategory> map = categories.stream().collect(Collectors.toMap(GoodsCategory::getBusiPath, goods -> goods));
		
		categories.stream().skip(1).forEach(node-> {
			String key=node.getBusiPath().substring(0,node.getBusiPath().length()-2);
			map.get(key).getChildren().add(node);
		});
		return rootNode;
	}
	
	
	//删除分类时连同子分类一起删除
	//删除分类时如当前分类连同子分类下的商品还有库存则不可以删除
	@Override
	public boolean removeBatchByIds(Collection<?> list) {
		boolean ok=!iGoodsService.selectCategoryGoods(list);
		if (ok) throw new ServiceException("该商品还有库存");
		boolean ret=false;
		list.forEach((key)->{
			final GoodsCategory category = getById((String) key);
			for (GoodsCategory temp=category;category.getChildren().size()!=0;temp=category.getChildren().remove(0)){
				baseMapper.deleteById(temp.getId());
			}
		});
		return true;
	}
}