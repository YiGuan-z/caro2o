package com.ruoyi.store.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.store.domain.Goods;

import java.util.List;

/**
 * 物品信息Service接口
 *
 * @author caseycehng
 * @date 2022-11-27
 */
public interface IGoodsService extends IService<Goods> {

    /**
     * 查询物品信息列表
     *
     * @param goods 物品信息
     * @return 物品信息集合
     */
    public List<Goods> selectGoodsList(Goods goods);
    Goods selectById(String id);

}
