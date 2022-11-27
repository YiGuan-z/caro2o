package com.ruoyi.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.workflow.domain.Warehouse;

import java.util.List;

/**
 * 仓库信息Mapper接口
 *
 * @author Mi-Manchi
 * @date 2022-11-27
 */
public interface WarehouseMapper extends BaseMapper<Warehouse> {

  /**
   * 查询仓库信息列表
   *
   * @param warehouse 仓库信息
   * @return 仓库信息集合
   */
  public List<Warehouse> selectWarehouseList(Warehouse warehouse);
}
