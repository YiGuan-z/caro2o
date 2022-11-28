package com.ruoyi.store.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.store.domain.Warehouse;
import com.ruoyi.store.mapper.WarehouseMapper;
import com.ruoyi.store.service.IWarehouseService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 仓库信息Service业务层处理
 *
 * @author Mi-Manchi
 * @date 2022-11-27
 */
@Service
public class WarehouseServiceImpl extends ServiceImpl<WarehouseMapper, Warehouse> implements IWarehouseService {

    /**
     * 查询仓库信息列表
     *
     * @param warehouse 仓库信息
     * @return 仓库信息
     */
    @Override
    public List<Warehouse> selectWarehouseList(Warehouse warehouse) {
        return getBaseMapper().selectWarehouseList(warehouse);
    }
}
