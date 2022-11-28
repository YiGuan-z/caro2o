package com.ruoyi.workflow.service.impl;

import java.util.*;
import java.util.stream.Collectors;

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
		List<GoodsCategory> ret=new ArrayList<>(10);
		final List<GoodsCategory> categories = baseMapper.selectList(null);
		final Map<String,GoodsCategory> map = categories.stream()
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
	//删除分类时连同子分类一起删除
	//删除分类时如当前分类连同子分类下的商品还有库存则不可以删除
	@Override
	public boolean removeBatchByIds(Collection<?> list) {
		
		return super.removeBatchByIds(list);
	}
}