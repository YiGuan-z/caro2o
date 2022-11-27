package com.ruoyi.workflow.service.impl;

import java.util.List;
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
}