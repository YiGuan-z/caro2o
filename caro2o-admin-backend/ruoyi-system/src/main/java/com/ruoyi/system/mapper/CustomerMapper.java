package com.ruoyi.system.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.Customer;

/**
 * 客户信息Mapper接口
 *
 * @author csj
 * @date 2022-11-27
 */
public interface CustomerMapper extends BaseMapper<Customer> {

  /**
   * 查询客户信息列表
   *
   * @param customer 客户信息
   * @return 客户信息集合
   */
  public List<Customer> selectCustomerList(Customer customer);
}
