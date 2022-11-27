package com.ruoyi.customer.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.customer.domain.CustomerVisit;

/**
 * 拜访信息Mapper接口
 *
 * @author xiaochen
 * @date 2022-11-27
 */
public interface CustomerVisitMapper extends BaseMapper<CustomerVisit> {

  /**
   * 查询拜访信息列表
   *
   * @param customerVisit 拜访信息
   * @return 拜访信息集合
   */
  public List<CustomerVisit> selectCustomerVisitList(CustomerVisit customerVisit);
}
