package com.ruoyi.store.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.store.domain.GoodsStore;

/**
 * 物品库存Service接口
 *
 * @author caseycheng
 * @date 2022-11-27
 */
public interface IGoodsStoreService extends IService<GoodsStore> {

    /**
     * 查询物品库存列表
     *
     * @param goodsStore 物品库存
     * @return 物品库存集合
     */
    public List<GoodsStore> selectGoodsStoreList(GoodsStore goodsStore);
}
