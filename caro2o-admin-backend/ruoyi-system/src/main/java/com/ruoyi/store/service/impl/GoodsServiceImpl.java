package com.ruoyi.store.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.Assert;
import com.ruoyi.store.domain.Goods;
import com.ruoyi.store.mapper.GoodsMapper;
import com.ruoyi.store.service.IGoodsService;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 物品信息Service业务层处理
 *
 * @author caseycehng
 * @date 2022-11-27
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    /**
     * 查询物品信息列表
     *
     * @param goods 物品信息
     * @return 物品信息
     */
    @Override
    public List<Goods> selectGoodsList(Goods goods) {
        return getBaseMapper().selectGoodsList(goods);
    }
    
    @Override
    public Goods selectById(String id) {
        return baseMapper.selectByGoodsId(id);
    }
	@Override
	public boolean selectCategoryGoods(Collection<?> list) {
		return baseMapper.selectCategoryGoods(list)==0;
	}
	
	@Override
	public Goods getById(Serializable id) {
		return baseMapper.selectList(new LambdaQueryWrapper<Goods>().eq(Goods::getId,id)).get(0);
	}
	
	@Override
    public boolean save(Goods entity) {
        Assert.assertTrue(entity,(e)-> e.
                getCategoryId()!=null &&
                e.getGoodsCover()!=null &&
                e.getGoodsName()!=null,
                "必填字段为空");
        return super.save(entity);
    }
}
