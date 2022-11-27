package com.ruoyi.workflow.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.workflow.domain.GoodsCategory;

/**
 * 物品分类信息Service接口
 *
 * @author Mi-Manchi
 * @date 2022-11-27
 */
public interface IGoodsCategoryService extends IService<GoodsCategory> {

    /**
     * 查询物品分类信息列表
     *
     * @param goodsCategory 物品分类信息
     * @return 物品分类信息集合
     */
    public List<GoodsCategory> selectGoodsCategoryList(GoodsCategory goodsCategory);

}